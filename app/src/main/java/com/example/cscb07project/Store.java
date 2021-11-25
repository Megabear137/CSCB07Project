package com.example.cscb07project;

import java.util.ArrayList;

public class Store {
    String name;
    StoreOwner owner;
    ArrayList<Product> products;
    ArrayList<Product> incomingOrders;
    ArrayList<Product> outgoingOrders;

    public Store (String name, StoreOwner owner, ArrayList<Product> products,
                  ArrayList<Product> incomingOrders, ArrayList<Product> outgoingOrders ) {
        this.name = name;
        this.owner = owner;
        this.products = products;
        this.incomingOrders = incomingOrders;
        this.outgoingOrders = outgoingOrders;

    }

    //=== Getters === Remove getters later if not needed
    public String getName() {
        return name;
    }

    public StoreOwner getOwner() {
        return owner;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public ArrayList<Product> getIncomingOrders() {
        return incomingOrders;
    }

    public ArrayList<Product> getOutgoingOrders() {
        return outgoingOrders;
    }

    //=== Setters === Remove setters later if not needed
    public void setName(String name) {
        this.name = name;
    }

    public void setOwner(StoreOwner owner) {
        this.owner = owner;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public void setIncomingOrders(ArrayList<Product> incomingOrders) {
        this.incomingOrders = incomingOrders;
    }

    public void setOutgoingOrders(ArrayList<Product> outgoingOrders) {
        this.outgoingOrders = outgoingOrders;
    }
}
