package com.example.coffeeshop.Models;

public class CoffeeModel {

    String name, taste, description, image;

    public CoffeeModel(String name, String taste, String description, String image) {
        this.name = name;
        this.taste = taste;
        this.description = description;
        this.image = image;
    }


    CoffeeModel() {
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

    public String getImage() {
        return image;
    }
}
