package com.company;

public class PortfolioCalculator {
    private DataController controller;

    public PortfolioCalculator(DataController controller){
        this.controller = controller;
    }

    public double calculateRenditeForStock(String symbol) {
        Stock s = controller.getDataForSymbol(symbol);

        double sum = 0;
        for (int i = 0; i < s.getStockPriceSize(); ++i){
            System.out.println("Date: " + s.getStockPriceAt(i).getDate().toString() + "\t Price : " +
                    s.getStockPriceAt(i).getPrice().toString());
            sum += s.getStockPriceAt(i).getPrice();
        }
        return sum;
    }

    public double calculateRiskForStocks() {
        return 0;
    }

    public double calculateCorrelationOfStocks() {
        return 0;
    }

    public double calculateCovarianceOfStocks() {
        return 0;
    }

    public double calculateRenditeForPortfolio() {
        return 0;
    }

    public double calculateRiskForPortfolio() {
        return 0;
    }
}
