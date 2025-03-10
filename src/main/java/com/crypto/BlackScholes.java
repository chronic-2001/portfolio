package com.crypto;

public class BlackScholes {
  private static final double RISK_FREE_RATE = 0.02;

  public static double calculateCallPrice(double S, double K, double t, double sigma) {
    if (t <= 0)
      return Math.max(S - K, 0);
    double d1 = d1(S, K, t, sigma);
    double d2 = d2(d1, sigma, t);
    return S * N(d1) - K * Math.exp(-RISK_FREE_RATE * t) * N(d2);
  }

  public static double calculatePutPrice(double S, double K, double t, double sigma) {
    if (t <= 0)
      return Math.max(K - S, 0);
    double d1 = d1(S, K, t, sigma);
    double d2 = d2(d1, sigma, t);
    return K * Math.exp(-RISK_FREE_RATE * t) * N(-d2) - S * N(-d1);
  }

  private static double d1(double S, double K, double t, double sigma) {
    return (Math.log(S / K) + (RISK_FREE_RATE + sigma * sigma / 2) * t) / (sigma * Math.sqrt(t));
  }

  private static double d2(double d1, double sigma, double t) {
    return d1 - sigma * Math.sqrt(t);
  }

  // Cumulative distribution function for standard normal distribution
  private static double N(double x) {
    if (x > 6.0)
      return 1.0;
    if (x < -6.0)
      return 0.0;
    return 0.5 * (1.0 + erf(x / Math.sqrt(2.0)));
  }

  // Error function approximation (accurate to ~1e-7), see
  // https://en.wikipedia.org/wiki/Error_function#Approximation_with_elementary_functions
  private static double erf(double z) {
    double t = 1.0 / (1.0 + 0.5 * Math.abs(z));
    double ans = 1 - t * Math.exp(-z * z - 1.26551223 +
        t * (1.00002368 + t * (0.37409196 + t * (0.09678418 +
            t * (-0.18628806 + t * (0.27886807 + t * (-1.13520398 +
                t * (1.48851587 + t * (-0.82215223 + t * 0.17087277)))))))));
    return z >= 0 ? ans : -ans;
  }
}