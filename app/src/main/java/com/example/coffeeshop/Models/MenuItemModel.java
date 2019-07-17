package com.example.coffeeshop.Models;

import java.io.Serializable;

public class MenuItemModel implements Serializable{

    MenuItemModel() {
    }

    String name, taste, description, imageUrl;
    long price;

    public MenuItemModel(String name, String taste, String description, String imageUrl, long price) {
        this.name = name;
        this.taste = taste;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getTaste() {
        return taste;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public long getPrice() {
        return price;
    }
}
