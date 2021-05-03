package com.company;

import java.util.HashMap;

public class StockDataController {
    private String fileDir;
    private FileController reader;

    public StockDataController(String fileDir){
        reader = new FileController(fileDir);
    }

    public Stock getStockData(String symbol) {
        Stock s = reader.readFile(symbol);
        if (s != null)
            return s;
        else
            //todo
            return null;
    }
}
