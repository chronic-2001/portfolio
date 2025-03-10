package com.crypto;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PortfolioManager {
  private final List<Position> positions = new ArrayList<>();
  private final Map<String, Stock> stocks = new HashMap<>();
  private final Map<String, Option> options = new HashMap<>();

  private final Map<String, Double> prices = new ConcurrentHashMap<>();

  public void loadPositions() {
    try (InputStream is = getClass().getClassLoader().getResourceAsStream("positions.csv");
        BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

      // Skip header
      br.readLine();

      String line;
      while ((line = br.readLine()) != null) {
        String[] parts = line.split(",");
        if (parts.length != 2)
          continue;

        String ticker = parts[0].trim();
        double quantity = Double.parseDouble(parts[1].trim());
        positions.add(new Position(ticker, quantity));
      }

    } catch (Exception e) {
      throw new RuntimeException("Failed to load positions", e);
    }
  }

  public void loadSecurities() throws Exception {
    try (Connection conn = DatabaseManager.getConnection();
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM security")) {

      while (rs.next()) {
        String ticker = rs.getString("ticker");
        SecurityType type = SecurityType.valueOf(rs.getString("type"));

        if (type == SecurityType.STOCK) {
          Stock stock = new Stock(
              ticker,
              rs.getDouble("mu"),
              rs.getDouble("sigma"),
              rs.getDouble("initial_price"));
          stocks.put(ticker, stock);
          prices.put(ticker, stock.getInitialPrice());
        } else if (type == SecurityType.CALL) {
          options.put(ticker, new CallOption(
              ticker,
              rs.getDouble("strike"),
              LocalDate.parse(rs.getString("maturity")),
              rs.getString("underlying_ticker")));
        } else if (type == SecurityType.PUT) {
          options.put(ticker, new PutOption(
              ticker,
              rs.getDouble("strike"),
              LocalDate.parse(rs.getString("maturity")),
              rs.getString("underlying_ticker")));
        }
      }
    }
  }

  public void calculateOptions() {
    for (Option option : options.values()) {
      double stockPrice = getPrice(option.getUnderlyingTicker());
      double sigma = stocks.get(option.getUnderlyingTicker()).getSigma();
      double optionPrice = option.calculatePrice(stockPrice, sigma);
      updatePrice(option.getTicker(), optionPrice);
    }
  }

  public void printPortfolio() {
    System.out.println("\n## Portfolio");
    System.out.printf("%-20s %10s %15s %15s%n", "symbol", "price", "qty", "value");

    double nav = 0.0;
    for (Position position : positions) {
      double price = getPrice(position.getTicker());
      double value = price * position.getQuantity();
      nav += value;

      System.out.printf("%-20s %10.2f %15s %15s%n",
          position.getTicker(),
          price,
          formatNumber(position.getQuantity()),
          formatNumber(value));
    }

    System.out.printf("%n#Total portfolio %46s%n%n", formatNumber(nav));
  }

  private String formatNumber(double number) {
    return String.format("%,.2f", number);
  }

  public Map<String, Stock> getStocks() {
    return stocks;
  }

  public void updatePrice(String ticker, double price) {
    prices.put(ticker, price);
  }

  public double getPrice(String ticker) {
    return prices.getOrDefault(ticker, 0.0);
  }
}