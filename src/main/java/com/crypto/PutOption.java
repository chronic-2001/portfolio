package com.crypto;

import java.time.LocalDate;

public class PutOption extends Option {

  public PutOption(String ticker, double strike, LocalDate maturity, String underlyingTicker) {
    super(ticker, strike, maturity, underlyingTicker);
  }

  @Override
  public SecurityType getType() {
    return SecurityType.PUT;
  }

  @Override
  public double calculatePrice(double stockPrice, double sigma) {
    return BlackScholes.calculatePutPrice(stockPrice, getStrike(), calculateTimeToMaturity(getMaturity()), sigma);
  }

}
