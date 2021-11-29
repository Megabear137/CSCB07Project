package com.example.cscb07project;

import java.util.ArrayList;
import java.util.HashMap;

public class Order {
    String customerName;
    String storeName;
    HashMap<Product, Integer> products;
    boolean isFulfilled;

    public Order (String customerName, String storeName, HashMap<Product, Integer> products) {
        this.products = products;
        this.customerName = customerName;
        this.storeName = storeName;
        isFulfilled = false;
    }

    public Order(){

    }

    //=== Setters === Remove Setters later if not needed
    public void setProducts(HashMap<Product, Integer> products) {
        this.products = products;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setFulfilled(boolean fulfilled) {
        isFulfilled = fulfilled;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    // === Getters === Remove getters later if not needed
    public HashMap<Product, Integer> getProducts() {
        return products;
    }

    public String getCustomerName() {
        return customerName;
    }

    public boolean getIsFulfilled() {
        return isFulfilled;
    }

    public String getStoreName() {
        return storeName;
    }
}
