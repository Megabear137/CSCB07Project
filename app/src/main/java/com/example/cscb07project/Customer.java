package com.example.cscb07project;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;

public class Customer extends User{
    HashMap<Product, Integer> cart;
    ArrayList<Order> pendingOrders;
    ArrayList<Order> completedOrders;

    public Customer (String username, String password) {
        this.username = username;
        this.password = password;
        isStoreOwner = false;
        cart = new HashMap<Product, Integer>();
        pendingOrders = new ArrayList<Order>();
        completedOrders = new ArrayList<Order>();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addProductToCart(Product product) {
        if (cart.containsKey(product)) {
            int quantity = cart.get(product);
            cart.replace(product, quantity+1);
        }
        else {
            cart.put(product, 1);
        }
    }

    /* Assumption: Whenever a customer makes an order in a store,
    all products in his/her cart from the specified store will be cleared and pended. */
    public void makeOrder(Store store) {
        HashMap<Product, Integer> productsInOrder = new HashMap<Product, Integer>();
        for (Product product: cart.keySet()) {
            if (product.getBrand().equals(store.getName())) {
                productsInOrder.put(product, cart.get(product));
                cart.remove(product);
            }
        }
        Order order = new Order(this, store, productsInOrder);
        pendingOrders.add(order);
        store.receiveOrder(order);
    }

    public void moveToCompleteOrders(Order order) {
        pendingOrders.remove(order);
        completedOrders.add(order);
    }

    public HashMap<Product, Integer> getCart() {
        return cart;
    }

    public void setCart(HashMap<Product, Integer> cart) {
        this.cart = cart;
    }

    public ArrayList<Order> getPendingOrders() {
        return pendingOrders;
    }

    public void setPendingOrders(ArrayList<Order> pendingOrders) {
        this.pendingOrders = pendingOrders;
    }

    public ArrayList<Order> getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(ArrayList<Order> completedOrders) {
        this.completedOrders = completedOrders;
    }
}



