package com.example.coffeeshop.Models;

public class MenuModel {
    private String id, name, taste, imageUrl;

    public MenuModel(String id, String name, String taste, String imageUrl) {
        this.id = id;
        this.name = name;
        this.taste = taste;
        this.imageUrl = imageUrl;
    }

    public MenuModel() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTaste() {
        return taste;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    ;


}
