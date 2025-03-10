package com.crypto;

public abstract class Security {
  private final String ticker;

  public Security(String ticker) {
    this.ticker = ticker;
  }

  public String getTicker() {
    return ticker;
  }

  public abstract SecurityType getType();
}