package com.crypto;

import java.time.LocalDate;

public class CallOption extends Option {

  public CallOption(String ticker, double strike, LocalDate maturity, String underlyingTicker) {
    super(ticker, strike, maturity, underlyingTicker);
  }

  @Override
  public SecurityType getType() {
    return SecurityType.CALL;
  }

  @Override
  public double calculatePrice(double stockPrice, double sigma) {
    return BlackScholes.calculateCallPrice(stockPrice, getStrike(), calculateTimeToMaturity(getMaturity()), sigma);
  }

}
