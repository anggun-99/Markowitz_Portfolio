package com.company;

import java.util.*;

//alle infos für ein stock
public class Stock {

    private String symbol;
    private ArrayList<StockPrice> priceList;

    public Stock(String symbol) {
        this.symbol = symbol;
        this.priceList = new ArrayList<>();
    }

    public String getSymbol() {
        return symbol;
    }

    public void addNewPrice(Date date, double price) {
        //adding new stock prices from the class StockPrice
        priceList.add(new StockPrice(date, price));
    }

    public void sortStocks() {
        Collections.sort(priceList, new Comparator<StockPrice>() {
            @Override
            public int compare(StockPrice o1, StockPrice o2) {
                return o1.compareTo(o2);
            }
        });
    }

    public StockPrice getStockPriceAt(int pos) {
        if (pos >= this.priceList.size())
            return null;
        return this.priceList.get(pos);
    }

    public int getStockPriceSize() {
        return priceList.size();
    }

    Date getLatestDate() {
        return this.priceList.get(getStockPriceSize() - 1).getDate();
    }

}
