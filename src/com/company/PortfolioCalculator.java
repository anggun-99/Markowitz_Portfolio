package com.company;

public class PortfolioCalculator {
    private DataController controller;

    public PortfolioCalculator(DataController controller) {
        this.controller = controller;
    }

    //yearly expected return of a share
    public double calculateRenditeForStock(String symbol) {
        Stock s = controller.getDataForSymbol(symbol);

        double sum = 0;
        for (int i = 1; i < s.getStockPriceSize(); ++i){
            sum += (s.getStockPriceAt(i).getPrice() - s.getStockPriceAt(i-1).getPrice())/s.getStockPriceAt(i-1).getPrice();
        }
        return Math.pow((1+sum/(s.getStockPriceSize()-1)),250) - 1;
    }

    //yearly risk
    public double calculateRiskForStocks(String symbol) {
        Stock s = controller.getDataForSymbol(symbol);

        double sum = 0;
        for(int i =1; i< s.getStockPriceSize(); ++i){
            sum += Math.pow(((s.getStockPriceAt(i).getPrice() - s.getStockPriceAt(i-1).getPrice())/s.getStockPriceAt(i-1).getPrice()-calculateRenditeForStock(symbol)/250),2);
        }
        return Math.sqrt(sum/(s.getStockPriceSize()-1)) * Math.sqrt(250);
    }

    public double calculateCovarianceOfStocks(String symbol1, String symbol2) {
        Stock s1 = controller.getDataForSymbol(symbol1);
        Stock s2 = controller.getDataForSymbol(symbol2);

        double sum = 0;
        for(int i = 1; i< s1.getStockPriceSize(); ++i ) {
            sum+= ((s1.getStockPriceAt(i).getPrice() - s1.getStockPriceAt(i-1).getPrice())/s1.getStockPriceAt(i-1).getPrice()-calculateRenditeForStock(symbol1)/250)*
                    ((s2.getStockPriceAt(i).getPrice()-s2.getStockPriceAt(i-1).getPrice())/s2.getStockPriceAt(i-1).getPrice()-calculateRenditeForStock(symbol2)/250);
        }
        return sum/(s1.getStockPriceSize()-1) * 250;
    }

    public double calculateCorrelationOfStocks(String symbol1, String symbol2) {

        double correlation = calculateCovarianceOfStocks(symbol1, symbol2)/(calculateRiskForStocks(symbol1)*calculateRiskForStocks(symbol2));
        return correlation;
    }

    public double calculateRenditeForPortfolio(String symbol1, String symbol2) {
        double[] renditePortfolio = new double[11];

        for(int w = 0; w<renditePortfolio.length;w++) {
            renditePortfolio[w] = (Double.valueOf(w)/10)* calculateRenditeForStock(symbol1) + (1-(Double.valueOf(w)/10))* calculateRenditeForStock(symbol2);
        }
        return renditePortfolio[5];
    }

    public double calculateRiskForPortfolio(String symbol1, String symbol2) {
        double[] risikoPortfolio = new double[11];

        for(int w = 0; w< risikoPortfolio.length; w++) {
            risikoPortfolio[w] = Math.sqrt((Double.valueOf(w)/10)*(Double.valueOf(w)/10)*calculateRiskForStocks(symbol1)*calculateRiskForStocks(symbol1)
            + (1-(Double.valueOf(w)/10))*(1-(Double.valueOf(w)/10))*calculateRiskForStocks(symbol2)
            + 2*(Double.valueOf(w)/10)*(1-(Double.valueOf(w)/10))*calculateCovarianceOfStocks(symbol1, symbol2));
        }
        return risikoPortfolio[5];
    }
}
