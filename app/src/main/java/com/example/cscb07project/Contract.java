package com.example.cscb07project;

public interface Contract {
    interface Model {
        void updateDatabase();
        boolean userExists(String username);
        boolean matchPass(String username, String password);
        boolean storeExists(String storeName);
        boolean isCustomer(String username);
        boolean isStoreOwner(String username);
        Customer findCustomer(String username);
        StoreOwner findStoreOwner(String username);
        Store findStore(String storeName);
        boolean addCustomer(String username, String password);
        boolean addStoreOwner(String username, String password);
        int addStore(String storeName, String ownerName);
        int addProductToStore(String storeName, Product product);
        int addProductToCart(Customer customer, Store store, Product product,int quantity);
        int deleteProductFromCart(Customer customer, Product product);
        int makeOrder(Customer customer, Store store);
        int fulfillOrder(Order order);
    }

    interface View {
        void displayMessage(String message);
        String getUsername();
        String getPassword();
    }

    interface Presenter {
        boolean checkLogin();
        boolean checkSignup();
        boolean checkCustomer();
        boolean addCustomer();
        boolean addStoreOwner();
    }
}

