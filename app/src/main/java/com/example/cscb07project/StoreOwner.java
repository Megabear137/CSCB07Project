package com.example.cscb07project;

import java.util.ArrayList;

public class StoreOwner extends User{
    ArrayList<String> stock;
    ArrayList<Order> orders;

    public StoreOwner (String username, String password) {
        this.username = username;
        this.password = password;
        isStoreOwner = true;
        stock = new ArrayList<String>();
        orders = new ArrayList<Order>();
    }


    public ArrayList<String> getStock() {
        return stock;
    }

    public void setStock(ArrayList<String> stock) {
        this.stock = stock;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }
}
