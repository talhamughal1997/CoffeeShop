package com.example.coffeeshop.Models;

public class UserCartModel {
    private String name;
    int qty;
    long price;

    public UserCartModel() {
    }

    public UserCartModel(String name, int qty, long price) {
        this.name = name;
        this.qty = qty;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getQty() {
        return qty;
    }

    public long getPrice() {
        return price;
    }
}
