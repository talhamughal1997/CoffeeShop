package com.example.coffeeshop.Models;

import java.io.Serializable;

public class MenuItemModel implements Serializable{

    MenuItemModel() {
    }

    String name, taste, description, imageUrl;

    public MenuItemModel(String name, String taste, String description, String imageUrl) {
        this.name = name;
        this.taste = taste;
        this.description = description;
        this.imageUrl = imageUrl;
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


}
