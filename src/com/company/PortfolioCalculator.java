package com.company;

import java.util.Date;

public class PortfolioCalculator implements Runnable {
    private DataController controller;
    private double[] yearlyReturnPort;
    private double[] yearlyRiskPort;
private String symbol1;
private String symbol2;


    public PortfolioCalculator(DataController controller) {
        this.controller = controller;
    }

    public PortfolioCalculator(DataController controller, String symbol1, String symbol2) {
        this.controller = controller;
        this.symbol1 = symbol1;
        this.symbol2 = symbol2;
    }

    public void calculate(String symbol1, String symbol2){
        double sym1Return = calculateRenditeForStock(symbol1);
        double sym1Risk = calculateRiskForStocks(symbol1);

        double sym2Return = calculateRenditeForStock(symbol2);
        double sym2Risk = calculateRiskForStocks(symbol2);

        calculateRenditeForPortfolio(sym1Return, sym2Return);
        calculateRiskForPortfolio(sym1Risk, sym2Risk, symbol1, symbol2);

    }

    //yearly expected return of a share
    public double calculateRenditeForStock(String symbol) {
        Stock s = controller.getDataForSymbol(symbol);

        double sum = 0;
        for (int i = 1; i < s.getStockPriceSize(); ++i){
            if (i==800)
                i = i;
            sum += (s.getStockPriceAt(i).getPrice() - s.getStockPriceAt(i-1).getPrice()) /
                    s.getStockPriceAt(i-1).getPrice();
        }
        return Math.pow((1+sum/(s.getStockPriceSize()-1)),250) - 1;
    }

    //yearly risk
    public double calculateRiskForStocks(String symbol) {
        Stock s = controller.getDataForSymbol(symbol);

        double sum = 0;
        for(int i =1; i< s.getStockPriceSize(); ++i){
            sum += Math.pow(((s.getStockPriceAt(i).getPrice() - s.getStockPriceAt(i-1).getPrice()) /
                    s.getStockPriceAt(i-1).getPrice() - calculateRenditeForStock(symbol) / 250),2);
        }
        return Math.sqrt(sum/(s.getStockPriceSize()-1)) * Math.sqrt(250);
    }

    public double calculateCovarianceOfStocks(String symbol1, String symbol2) {
        Stock s1 = controller.getDataForSymbol(symbol1);
        Stock s2 = controller.getDataForSymbol(symbol2);

        Date startS1 = s1.getStockPriceAt(0).getDate();
        Date startS2 = s2.getStockPriceAt(0).getDate();
        int s1Offset = 0;
        int s2Offset = 0;

        if (startS1.compareTo(startS2) < 0){
            //s1 starts before
            while(startS1.compareTo(startS2) < 0 && s1Offset < s1.getStockPriceSize()){
                s1Offset++;
                startS1 = s1.getStockPriceAt(s1Offset).getDate();
            }
        } else if (startS2.compareTo(startS1) < 0){
            //s2 starts before
            while(startS2.compareTo(startS1) < 0 && s2Offset < s2.getStockPriceSize()) {
                s2Offset++;
                startS2 = s2.getStockPriceAt(s2Offset).getDate();
            }
        }

        double sum = 0;
        for(int i = 1; i < Math.min(s1.getStockPriceSize(), s2.getStockPriceSize()) - Math.max(s1Offset,s2Offset); ++i ) {
            sum+= ((s1.getStockPriceAt(i + s1Offset).getPrice() - s1.getStockPriceAt(i-1 + s1Offset).getPrice()) /
                        s1.getStockPriceAt(i-1 + s1Offset).getPrice() - calculateRenditeForStock(symbol1)/250) *
                    ((s2.getStockPriceAt(i + s2Offset).getPrice() - s2.getStockPriceAt(i-1 + s2Offset).getPrice()) /
                            s2.getStockPriceAt(i-1 + s2Offset).getPrice() - calculateRenditeForStock(symbol2)/250);
        }
        return sum / (Math.min(s1.getStockPriceSize(), s2.getStockPriceSize()) - 1) * 250;
    }

    public double calculateCorrelationOfStocks(String symbol1, String symbol2) {

        double correlation = calculateCovarianceOfStocks(symbol1, symbol2) /
                (calculateRiskForStocks(symbol1)*calculateRiskForStocks(symbol2));
        return correlation;
    }

    public double[] calculateRenditeForPortfolio(String symbol1, String symbol2) {
        double[] renditePortfolio = new double[11];

        for(int w = 0; w<renditePortfolio.length;w++) {
            renditePortfolio[w] = ((double) w /10)* calculateRenditeForStock(symbol1) + (1-((double) w /10))
                    * calculateRenditeForStock(symbol2);
        }
        return renditePortfolio;
    }

    void calculateRenditeForPortfolio(double sym1, double sym2) {
        yearlyReturnPort = new double[11];

        for(int w = 0; w < yearlyReturnPort.length; w++) {
            yearlyReturnPort[w] = ((double) w /10)* sym1 + (1-((double) w /10))
                    * sym2;
        }
    }

    public double[] calculateRiskForPortfolio(String symbol1, String symbol2) {
        double[] risikoPortfolio = new double[11];

        for(int w = 0; w< risikoPortfolio.length; w++) {
            risikoPortfolio[w] = Math.sqrt((Double.valueOf(w)/10)*(Double.valueOf(w)/10)*calculateRiskForStocks(symbol1)
                    * calculateRiskForStocks(symbol1)
            + (1-(Double.valueOf(w)/10))*(1-(Double.valueOf(w)/10))*calculateRiskForStocks(symbol2)
            + 2*(Double.valueOf(w)/10)*(1-(Double.valueOf(w)/10))*calculateCovarianceOfStocks(symbol1, symbol2));
        }
        return risikoPortfolio;
    }

    void calculateRiskForPortfolio(double sym1, double sym2, String symbol1, String symbol2) {
        yearlyRiskPort = new double[11];

        for(int w = 0; w< yearlyRiskPort.length; w++) {
            yearlyRiskPort[w] = Math.sqrt((Double.valueOf(w)/10)*(Double.valueOf(w)/10)*sym1
                    * sym1
                    + (1-(Double.valueOf(w)/10))*(1-(Double.valueOf(w)/10))* sym2
                    + 2*(Double.valueOf(w)/10)*(1-(Double.valueOf(w)/10))*calculateCovarianceOfStocks(symbol1, symbol2));
        }
    }

    public double[] getYearlyReturnPort() {
        return yearlyReturnPort;
    }

    public double[] getYearlyRiskPort() {
        return yearlyRiskPort;
    }

    @Override
    public void run() {
        calculate(symbol1, symbol2);
    }
}
