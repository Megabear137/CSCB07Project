package com.example.cscb07project;

import androidx.annotation.NonNull;

public class Product {
    String name;
    String brand;
    double price;

    public Product (String brand, String name, double price) {
        this.price = price;
        this.brand = brand;
        this.name = name;
    }

    public Product(){

    }

    @Override
    public int hashCode() {
        return name.hashCode()/2 + brand.hashCode()/2;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Product other = (Product) obj;
        if (!name.equals(other.name))
            return false;
        if(!brand.equals(other.brand))
            return false;
        return true;
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
