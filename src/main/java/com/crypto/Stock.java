package com.crypto;

public class Stock extends Security {
  private final double mu;
  private final double sigma;
  private final double initialPrice;

  public Stock(String ticker, double mu, double sigma, double initialPrice) {
    super(ticker);
    this.mu = mu;
    this.sigma = sigma;
    this.initialPrice = initialPrice;
  }

  public double getMu() {
    return mu;
  }

  public double getSigma() {
    return sigma;
  }

  public double getInitialPrice() {
    return initialPrice;
  }

  @Override
  public SecurityType getType() {
    return SecurityType.STOCK;
  }

}
