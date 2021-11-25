package com.example.cscb07project;

import java.util.ArrayList;

public class Store {
    String name;
    StoreOwner owner;
    ArrayList<Product> products;
    ArrayList<Order> incomingOrders; //[Pintao] changed the element type from Product to Order
    ArrayList<Order> outgoingOrders; //[Pintao] changed the element type from Product to Order

    public Store (String name, StoreOwner owner, ArrayList<Product> products,
                  ArrayList<Order> incomingOrders, ArrayList<Order> outgoingOrders ) {
        this.name = name;
        this.owner = owner;
        this.products = products;
        this.incomingOrders = incomingOrders;
        this.outgoingOrders = outgoingOrders;
    }

    public void receiveOrder(Order order) {
        incomingOrders.add(order);
    }

    public void fulfillOrder(Order order) {
        incomingOrders.remove(order);
        outgoingOrders.add(order);
        order.getCustomer().moveToCompleteOrders(order);
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

    public ArrayList<Order> getIncomingOrders() {
        return incomingOrders;
    }

    public ArrayList<Order> getOutgoingOrders() {
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

    public void setIncomingOrders(ArrayList<Order> incomingOrders) {
        this.incomingOrders = incomingOrders;
    }

    public void setOutgoingOrders(ArrayList<Order> outgoingOrders) {
        this.outgoingOrders = outgoingOrders;
    }
}
