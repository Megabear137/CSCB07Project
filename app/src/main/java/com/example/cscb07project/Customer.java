package com.example.cscb07project;

import java.util.ArrayList;

public class Customer extends User{
    ArrayList<Product> cart;
    ArrayList<Order> pendingOrders;
    ArrayList<Order> completedOrders;

    public Customer (String username, String password) {
        cart = new ArrayList<Product>();
        pendingOrders = new ArrayList<Order>();
        completedOrders = new ArrayList<Order>();

        this.username = username;
        this.password = password;
        isStoreOwner = false;
    }

    public void addProductToCart(Product product) {
        cart.add(product);
    }

    /* Assumption: Whenever a customer makes an order in a store,
    all products in his/her cart from the specified store will be cleared and pended. */
    public void makeOrder(Store store) {
        ArrayList<Product> productsInOrder = new ArrayList<Product>();
        for (Product product: cart) {
            if (product.getBrand() == store.getName()) {
                productsInOrder.add(product);
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


    public ArrayList<Product> getCart() {
        return cart;
    }

    public ArrayList<Order> getPendingOrders() {
        return pendingOrders;
    }

    public ArrayList<Order> getCompletedOrders() {
        return completedOrders;
    }
}



