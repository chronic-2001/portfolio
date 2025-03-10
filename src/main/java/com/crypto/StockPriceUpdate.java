package com.crypto;

import java.util.Map;

public class StockPriceUpdate {

  private final int updateNumber;
  private final Map<String, Double> prices;

  public StockPriceUpdate(int updateNumber, Map<String, Double> prices) {
    this.updateNumber = updateNumber;
    this.prices = prices;
  }

  public int getUpdateNumber() {
    return updateNumber;
  }

  public Map<String, Double> getPrices() {
    return prices;
  }
}