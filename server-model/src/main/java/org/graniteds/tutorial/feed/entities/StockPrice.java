package org.graniteds.tutorial.feed.entities;

import java.io.Serializable;
import java.math.BigDecimal;

public class StockPrice implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private BigDecimal price;

    public StockPrice() {
    }

    public StockPrice(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
