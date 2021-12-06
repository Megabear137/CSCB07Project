package com.example.cscb07project;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Customer extends User{
    ArrayList<Order> cart;
    ArrayList<Order> pendingOrders;
    ArrayList<Order> completedOrders;

    public Customer (String username) {
        pendingOrders = new ArrayList<Order>();
        completedOrders = new ArrayList<Order>();

        this.username = username;
        isStoreOwner = false;
        cart = new ArrayList<>();
        pendingOrders = new ArrayList<Order>();
        completedOrders = new ArrayList<Order>();
    }

    public Customer () {
        cart = new ArrayList<>();
        pendingOrders = new ArrayList<Order>();
        completedOrders = new ArrayList<Order>();

        this.username = "";
        this.isStoreOwner = false;
    }



    /*@RequiresApi(api = Build.VERSION_CODES.N)
    public void addProductToCart(Product product) {
        if (cart.containsKey(product)) {
            int quantity = cart.get(product);
            cart.replace(product, quantity+1);
        }
        else {
            cart.put(product, 1);
        }
    }

    public void makeOrder(Store store) {
        HashMap<Product, Integer> productsInOrder = new HashMap<Product, Integer>();
        for (Product product: cart.keySet()) {
            if (product.getBrand().equals(store.getName())) {
                productsInOrder.put(product, cart.get(product));
                cart.remove(product);
            }
        }
        Order order = new Order(getUsername(), store.getName(), productsInOrder);
        pendingOrders.add(order);
        store.receiveOrder(order);
    }

    public void moveToCompleteOrders(Order order) {
        pendingOrders.remove(order);
        completedOrders.add(order);
    }

     */


    public ArrayList<Order> getCart() {
        return cart;
    }

    public ArrayList<Order> getPendingOrders() {
        return pendingOrders;
    }

    public ArrayList<Order> getCompletedOrders() {
        return completedOrders;
    }

    public void setUsername(String username){
        this.username = username;
    }


    public void setCompletedOrders(ArrayList<Order> completedOrders) {
        this.completedOrders = completedOrders;
    }

    public void setPendingOrders(ArrayList<Order> pendingOrders) {
        this.pendingOrders = pendingOrders;
    }


    @NonNull
    @Override
    public String toString() {
        return username + " " + " " + isStoreOwner ;
    }

    public void setCart(ArrayList<Order> cart) {
        this.cart = cart;
    }

    public void setIsStoreOwner(boolean isStoreOwner){
        this.isStoreOwner = isStoreOwner;
    }

    public boolean getIsStoreOwner() { return isStoreOwner;}
}



