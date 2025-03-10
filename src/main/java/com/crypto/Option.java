package com.crypto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public abstract class Option extends Security {
  private final double strike;
  private final LocalDate maturity;
  private final String underlyingTicker;

  public Option(String ticker, double strike, LocalDate maturity, String underlyingTicker) {
    super(ticker);
    this.strike = strike;
    this.maturity = maturity;
    this.underlyingTicker = underlyingTicker;
  }

  public double getStrike() {
    return strike;
  }

  public LocalDate getMaturity() {
    return maturity;
  }

  public String getUnderlyingTicker() {
    return underlyingTicker;
  }

  protected double calculateTimeToMaturity(LocalDate maturity) {
    LocalDate now = LocalDate.now();
    long days = ChronoUnit.DAYS.between(now, maturity);
    return Math.max(days / 365.0, 0.0);
  }

  public abstract double calculatePrice(double stockPrice, double sigma);
}
