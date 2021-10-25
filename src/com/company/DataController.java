package com.company;

import java.util.HashMap;

//saving the stock data
public class DataController {
    //HashMap : a list of keys and values.
    private HashMap<String, Stock> stocks;
    private StockDataController controller;

    public DataController(String path) {
        this.stocks = new HashMap<>();
        this.controller = new StockDataController(path);
    }

    public Stock getDataForSymbol(String symbol) {
        if (stocks.containsKey(symbol))
            return stocks.get(symbol);
        else {
            Stock s = controller.getStockData(symbol);
            stocks.put(symbol, s);
            return s;
        }
    }
}
