package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        DataController data = new DataController("data/");
        Stock s = data.getDataForSymbol("AMZN");
        s.sortStocks();

        for (int i = 0; i < s.getStockPriceSize(); ++i){
            System.out.println("Date: " + s.getStockPriceAt(i).getDate().toString() + "\t Price : " +
                    s.getStockPriceAt(i).getPrice().toString());
        }

        String symbol1 = "CAT";
        String symbol2 = "AAPL";

        PortfolioCalculator portfolioCalculator = new PortfolioCalculator(data);
        PortfolioCalculator portfolioCalculator1 = new PortfolioCalculator(data);

       System.out.print("jährliche erwartete Rendite (" + symbol1 + "): " + portfolioCalculator.calculateRenditeForStock(symbol1)
       + "\t jährliches Risiko (" + symbol1 + "): " + portfolioCalculator.calculateRiskForStocks(symbol1)
       + "\njährliche Rendite (" + symbol2 + "): " + portfolioCalculator1.calculateRenditeForStock(symbol2)
       + "\t jährliches Risiko (" + symbol2 + "): " + portfolioCalculator1.calculateRiskForStocks(symbol2)
       + "\nKovarianz von beiden Aktien: " + portfolioCalculator.calculateCovarianceOfStocks(symbol1, symbol2)
       + "\nKorrelation von beiden Aktien: " + portfolioCalculator.calculateCorrelationOfStocks(symbol1,symbol2)
       + "\ndie zu erwartenden Rendite des Portfolios: [");

       double[] portRendite = portfolioCalculator.calculateRenditeForPortfolio(symbol1,symbol2);

       for (int i = 0; i < portRendite.length; ++i){
           if (i != 0)
                System.out.print("," + portRendite[i]);
           else
               System.out.print(portRendite[i]);
       }

       double[] portRisiken = portfolioCalculator.calculateRiskForPortfolio(symbol1, symbol2);

       System.out.print("]" + "\ndie Risiken des Portfolios: [");
       for (int i = 0; i < portRisiken.length; ++i){
           if(i != 0 )
               System.out.print("," + portRisiken[i] );
           else
               System.out.print(portRisiken[i] );
       }
       System.out.print("]");

    }
}
