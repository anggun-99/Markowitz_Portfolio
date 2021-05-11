package com.company;

import java.util.*;

public class Stock {

    private String symbol;
    private ArrayList<StockPrice> stocks;

    public Stock(String symbol) {
        this.symbol = symbol;
        this.stocks = new ArrayList<>();
    }

    public String getSymbol() {
        return symbol;
    }

    public void addNewPrice(Date date, double price) {
        //adding new stock prices from the class StockPrice
        stocks.add(new StockPrice(date,price));
    }

    public void sortStocks() {
        Collections.sort(stocks, new Comparator<StockPrice>() {
            @Override
            public int compare(StockPrice o1, StockPrice o2) {
                return o1.compareTo(o2);
            }
        });
    }

    public StockPrice getStockPriceAt(int pos){
        if (pos >= this.stocks.size())
            return null;
        return this.stocks.get(pos);
    }

    public int getStockPriceSize(){
        return stocks.size();
    }

}
