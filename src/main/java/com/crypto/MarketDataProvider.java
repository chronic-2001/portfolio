package com.crypto;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.eventbus.EventBus;

public class MarketDataProvider implements Runnable {
    private final PortfolioManager portfolioManager;
    private final EventBus eventBus;
    private final ScheduledExecutorService scheduler;
    private final AtomicInteger updateCounter = new AtomicInteger(0);

    private volatile boolean running = true;

    public MarketDataProvider(EventBus eventBus, PortfolioManager portfolioManager) {
        this.eventBus = eventBus;
        this.portfolioManager = portfolioManager;
        Map<String, Stock> stocks = portfolioManager.getStocks();
        this.scheduler = Executors.newScheduledThreadPool(stocks.size());

        // print initial prices
        int updateNum = updateCounter.incrementAndGet();
        // Use a tree map to get stocks ordered
        Map<String, Double> prices = new TreeMap<>();
        for (String ticker : stocks.keySet()) {
            prices.put(ticker, portfolioManager.getPrice(ticker));
        }
        eventBus.post(new StockPriceUpdate(updateNum, prices));

    }

    @Override
    public void run() {
        for (Stock stock : portfolioManager.getStocks().values()) {
            scheduleStockUpdate(stock);
        }
    }

    private void scheduleStockUpdate(Stock stock) {
        double deltaTSeconds = 0.5 + ThreadLocalRandom.current().nextDouble() * 1.5; // 0.5–2 seconds
        long deltaTMillis = (long) (deltaTSeconds * 1000);

        scheduler.schedule(() -> {
            if (running) {
                updateStockPrice(stock, deltaTSeconds);
                scheduleStockUpdate(stock); // Reschedule with new deltaT
            }
        }, deltaTMillis, TimeUnit.MILLISECONDS);
    }

    private void updateStockPrice(Stock stock, double deltaTSeconds) {
        double currentPrice = portfolioManager.getPrice(stock.getTicker());
        double mu = stock.getMu();
        double sigma = stock.getSigma();

        double epsilon = ThreadLocalRandom.current().nextGaussian();
        double deltaS = currentPrice * (mu * (deltaTSeconds / 7257600) +
                sigma * epsilon * Math.sqrt(deltaTSeconds / 7257600));

        double newPrice = Math.max(currentPrice + deltaS, 0.0);
        portfolioManager.updatePrice(stock.getTicker(), newPrice);
        int updateNum = updateCounter.incrementAndGet();
        eventBus.post(new StockPriceUpdate(
                updateNum,
                Collections.singletonMap(stock.getTicker(), newPrice)));
    }

    public void stop() {
        running = false;
        scheduler.shutdownNow();
    }
}