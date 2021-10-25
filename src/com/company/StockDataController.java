package com.company;

//asking for the stock data
public class StockDataController {
    private FileController reader;
    private API_Controller apiController;

    public StockDataController(String fileDir) {
        reader = new FileController(fileDir);
        apiController = new API_Controller();
    }

    public Stock getStockData(String symbol) {
        Stock s = reader.readFile(symbol);
        if (s != null) {
            apiController.addMissingPriceData(s);
            return s;
        } else {
            System.out.println("Stock " + symbol + " is not available");
            return null;
        }
    }
}
