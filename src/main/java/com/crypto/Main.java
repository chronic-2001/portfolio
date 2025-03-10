package com.crypto;

import com.google.common.eventbus.EventBus;

public class Main {
  public static void main(String[] args) {
    EventBus eventBus = new EventBus();
    PortfolioManager portfolioManager = new PortfolioManager();
    PortfolioSubscriber subscriber = new PortfolioSubscriber(portfolioManager);

    eventBus.register(subscriber);

    try {
      portfolioManager.loadPositions();
      portfolioManager.loadSecurities();

      MarketDataProvider dataProvider = new MarketDataProvider(eventBus, portfolioManager);
      new Thread(dataProvider).start();

      Thread.sleep(30_000);
      dataProvider.stop();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
