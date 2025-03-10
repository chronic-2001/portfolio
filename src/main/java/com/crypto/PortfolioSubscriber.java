package com.crypto;

import com.google.common.eventbus.Subscribe;

public class PortfolioSubscriber {
  private final PortfolioManager portfolioManager;

  public PortfolioSubscriber(PortfolioManager portfolioManager) {
    this.portfolioManager = portfolioManager;
  }

  @Subscribe
  public void handlePriceUpdate(StockPriceUpdate event) {
    System.out.printf("%n## %d Market Data Update%n",
        event.getUpdateNumber());

    event.getPrices().forEach((symbol, price) -> System.out.printf("%s change to %.2f%n", symbol, price));
    portfolioManager.calculateOptions();
    portfolioManager.printPortfolio();
  }
}