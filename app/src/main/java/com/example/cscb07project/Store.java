package com.example.cscb07project;

import android.provider.ContactsContract;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class  Store {
    String name;
    String ownerName;
    ArrayList<Product> products;
    ArrayList<Order> incomingOrders; //[Pintao] changed the element type from Product to Order
    ArrayList<Order> outgoingOrders; //[Pintao] changed the element type from Product to Order
    Database database = Database.getInstance();

    public Store (String name, String ownerName) {
        this.name = name;
        this.ownerName = ownerName;
        products = new ArrayList<Product>();
        incomingOrders = new ArrayList<Order>();
        outgoingOrders = new ArrayList<Order>();
    }

    public Store(){
        products = new ArrayList<Product>();
        incomingOrders = new ArrayList<Order>();
        outgoingOrders = new ArrayList<Order>();
    }

    public void receiveOrder(Order order) {
        incomingOrders.add(order);
    }

    public void fulfillOrder(Order order) {
        incomingOrders.remove(order);
        outgoingOrders.add(order);
        Customer customer = database.findCustomer(order.getCustomerName());
        customer.moveToCompleteOrders(order);
    }

    //=== Getters === Remove getters later if not needed
    public String getName() {
        return name;
    }

    public String getOwnerName() {
        return ownerName;
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

    public void setOwner(String ownerName) {
        this.ownerName = ownerName;
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

    @NonNull
    @Override
    public String toString() {
        Database database = Database.getInstance();
        return  database.findStoreOwner(ownerName).toString() + " " + name;
    }
}
