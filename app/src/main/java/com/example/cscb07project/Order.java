package com.example.cscb07project;

import java.util.ArrayList;
import java.util.HashMap;

public class Order {
    Customer customer;
    Store store;
    HashMap<Product, Integer> products;
    boolean isFulfilled;

    public Order (Customer customer, Store store, HashMap<Product, Integer> products) {
        this.products = products;
        this.customer = customer;
        this.store = store;
        isFulfilled = false;


    }

    //=== Setters === Remove Setters later if not needed
    public void setProducts(HashMap<Product, Integer> products) {
        this.products = products;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setFulfilled(boolean fulfilled) {
        isFulfilled = fulfilled;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    // === Getters === Remove getters later if not needed
    public HashMap<Product, Integer> getProducts() {
        return products;
    }

    public Customer getCustomer() {
        return customer;
    }

    public boolean isFulfilled() {
        return isFulfilled;
    }

    public Store getStore() {
        return store;
    }
}
