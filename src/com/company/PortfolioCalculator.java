package com.company;

public class PortfolioCalculator {
    private DataController controller;

    public PortfolioCalculator(DataController controller) {
        this.controller = controller;
    }

    public double calculateRenditeForStock(String symbol) {
        Stock s = controller.getDataForSymbol(symbol);

        double sum = 0;
        for (int i = 1; i < s.getStockPriceSize(); ++i){
            System.out.println("Date: " + s.getStockPriceAt(i).getDate().toString() + "\t Price : " +
                    s.getStockPriceAt(i).getPrice().toString());
            sum += (s.getStockPriceAt(i).getPrice() - s.getStockPriceAt(i-1).getPrice())/s.getStockPriceAt(i-1).getPrice();
        }
        return Math.pow((1+sum/(s.getStockPriceSize()-1)),250) - 1;
    }

    public double calculateRiskForStocks(String symbol) {
        Stock s = controller.getDataForSymbol(symbol);

        double sum = 0;
        for(int i =1; i< s.getStockPriceSize(); i++){
            sum += ((s.getStockPriceAt(i).getPrice() - s.getStockPriceAt(i-1).getPrice())/s.getStockPriceAt(i-1).getPrice()-calculateRenditeForStock(symbol)/250)^2
        }
        return Math.sqrt(sum/(s.getStockPriceSize()-1));
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
