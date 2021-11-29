package com.example.cscb07project;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class StoreOwner extends User{
    String storeName;
    ArrayList<String> stock;
    ArrayList<Order> orders;

    public StoreOwner (String username, String password) {
        this.username = username;
        this.password = password;
        isStoreOwner = true;
        this.storeName = "";
    }

    public StoreOwner () {
        this.username = "";
        this.password = "";
        isStoreOwner = true;
        this.storeName = "";
        stock = new ArrayList<String>();
        orders = new ArrayList<Order>();
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setUsername(String username){
        this.username = username;
    }
    public ArrayList<String> getStock() {
        return stock;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setIsStoreOwner(){
        this.isStoreOwner = true;
    }
    public void setStock(ArrayList<String> stock) {
        this.stock = stock;
    }

    public String getUsername() {
        return username;
    }
    public ArrayList<Order> getOrders() {
        return orders;
    }

    public String getPassword() {
        return password;
    }

    public String getStoreName() {
        return storeName;
    }

    public boolean getIsStoreOwner() {
        return isStoreOwner;
    }

    @NonNull
    @Override
    public String toString() {
        return username + " " + storeName + " "  + isStoreOwner;
    }
    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }
}
