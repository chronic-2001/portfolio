package com.crypto;

public class Position {
  private final String ticker;
  private final double quantity;

  public Position(String ticker, double quantity) {
    this.ticker = ticker;
    this.quantity = quantity;
  }

  public String getTicker() {
    return ticker;
  }

  public double getQuantity() {
    return quantity;
  }
}
