package com.example.cscb07project;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;

public class Order {
    String customerName;
    String storeName;
    HashMap<Product, Integer> products;
    boolean isFulfilled;
    long time;

    public Order (String customerName, String storeName, HashMap<Product, Integer> products) {
        this.products = products;
        this.customerName = customerName;
        this.storeName = storeName;
        isFulfilled = false;
        time = System.currentTimeMillis();
    }

    public Order (String customerName, String storeName) {
        this.products = new HashMap<>();
        this.customerName = customerName;
        this.storeName = storeName;
        isFulfilled = false;
    }

    public Order(){

    }

    @Override
    public int hashCode() {
        return customerName.hashCode()/2+storeName.hashCode()/2-((int)time)%1000000000;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Order other = (Order) obj;
        if (!customerName.equals(other.customerName))
            return false;
        if(!storeName.equals(other.storeName))
            return false;
        if (time!=other.time)
            return false;
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addProductToOrder(Product product){
        if(products.containsKey(product)){
            int old = products.get(product);
            products.replace(product, old + 1);
        }
        else{
            products.put(product, 1);
        }
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
