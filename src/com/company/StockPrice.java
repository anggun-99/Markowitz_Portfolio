package com.company;

import java.util.Date;

public class StockPrice implements Comparable<StockPrice> {
    private Date date;
    private Double price;

    public StockPrice(Date date, Double price) {
        this.date = date;
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public Double getPrice() {
        return price;
    }

    @Override
    public int compareTo(StockPrice o) {
        return getDate().compareTo(o.getDate());
    }
}
