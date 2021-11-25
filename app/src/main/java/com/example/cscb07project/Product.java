package com.example.cscb07project;

public class Product {
    String name;
    String brand;
    double price;

    public Product (String brand, String name, double price) {
        this.price = price;
        this.brand = brand;
        this.name = name;
    }

    //=== Getters === Remove getters later if not needed
    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public double getPrice() {
        return price;
    }

    //=== Setters === remove setters later if not needed
    public void setName(String name) {
        this.name = name;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
