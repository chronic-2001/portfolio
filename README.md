# Real-Time Portfolio Valuation System

A Java system that calculates real-time portfolio values including stocks and European options.

## Notes

I'm using double for simplicity in this mock system, but it’s important to note that floating-point arithmetic can introduce rounding errors. For example, values like 0.1 cannot be represented exactly in binary, which could lead to inaccuracies over many calculations. In a real-world financial system, we’d use BigDecimal for monetary values and critical calculations (e.g., Black-Scholes, NAV) to ensure precision and avoid cumulative rounding errors.

## Features

- Real-time stock price simulation using Geometric Brownian Motion
- European option pricing using Black-Scholes model
- Embedded H2 database for security definitions
- CSV position file parsing
- Real-time portfolio NAV calculation
- Formatted console output

## Technologies

- Java 8
- H2 Database
- Guava
- Gradle

## Getting Started

### Prerequisites

- JDK 8
- Gradle 7+

### Build and run

```bash
./gradlew clean build
./gradlew run
```
