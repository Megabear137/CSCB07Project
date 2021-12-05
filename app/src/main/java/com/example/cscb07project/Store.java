package com.example.cscb07project;

import android.provider.ContactsContract;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class  Store {
    String name;
    String ownerName;
    ArrayList<Product> products;
    ArrayList<Order> incomingOrders;
    ArrayList<Order> outgoingOrders;

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

   /* public void receiveOrder(Order order) {
        incomingOrders.add(order);
    }

    public void fulfillOrder(Order order) {
        incomingOrders.remove(order);
        outgoingOrders.add(order);
        Customer customer = database.findCustomer(order.getCustomerName());
        customer.moveToCompleteOrders(order);
    }
    */

    //Search product by name in the store and return it. Return null if no matching product found.
    public Product findProduct(String productName) {
        for (Product product: products) {
            if (product.getName().equals(productName))
                return product;
        }
        return null;
    }

    public boolean productExists(String productName) {
        if (findProduct(productName) != null)
            return true;
        return false;

    }

    //Edits the fields of a product in the store given a Product, its new name, new brand,
    // and new price.
    // Return 1 if successful, -1 otherwise.

    public int editProductInfo(Product product, String newName, String newBrand, double newPrice) {
        if(product.getName() == null){
            return -1;
        }
        Product editedProduct = findProduct(product.getName());
        if (product != null) {
            product.setName(newName);
            product.setBrand(newBrand);
            product.setPrice(newPrice);
            return 1;
        }
        return -1;

    }

    public boolean isIncomingOrdersEmpty() {
        return incomingOrders == null || incomingOrders.isEmpty();
    }
    public boolean isOutgoingOrdersEmpty() {
        return outgoingOrders == null || outgoingOrders.isEmpty();
    }



    @Override
    public int hashCode() {
        return (name.hashCode()+ownerName.hashCode())/2;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Store other = (Store) obj;
        if (!name.equals(other.name))
            return false;
        if(!ownerName.equals(other.ownerName))
            return false;
        return true;
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
        StoreOwner storeOwner = (StoreOwner)Database.user;
        return  storeOwner.toString() + " " + name;
    }
}
