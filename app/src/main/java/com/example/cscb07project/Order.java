package com.example.cscb07project;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;

public class Order {
    String customerName;
    String storeName;
    HashMap<String, Integer> products;
    boolean isFulfilled;
    int id;

    public Order (String customerName, String storeName, HashMap<String, Integer> products, int id) {
        this.products = products;
        this.customerName = customerName;
        this.storeName = storeName;
        isFulfilled = false;
        this.id = id;
    }

    public Order (String customerName, String storeName, int id) {
        this.id = id;
        this.products = new HashMap<>();
        this.customerName = customerName;
        this.storeName = storeName;
        isFulfilled = false;
    }

    public double calculateTotal() {
        double total = 0;
        Database database = new Database();
        for(String productName: products.keySet()) {
            double price = database.findProductInStore(productName, storeName).getPrice();
            total += price * products.get(productName);
        }

        return total;
    }

    public Order(){

    }

    @Override
    public int hashCode() {
        return id;
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
        if (id != other.id)
            return false;
        return true;
    }


   /* @RequiresApi(api = Build.VERSION_CODES.N)
    public void addProductToOrder(Product product){
        if(products.containsKey(product)){
            int old = products.get(product);
            products.replace(product, old + 1);
        }
        else{
            products.put(product, 1);
        }
    }
    */


    public void setProducts(HashMap<String, Integer> products) {
        this.products = products;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setIsFulfilled(boolean fulfilled) {
        isFulfilled = fulfilled;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setId(int id) {
        this.id = id;
    }


    public HashMap<String, Integer> getProducts() {
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

    public int getId() {
        return id;
    }

    @NonNull
    @Override
    public String toString() {
        return storeName + ": Order ID: " + id;
    }
}
