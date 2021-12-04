package com.example.cscb07project;

public interface Contract {
    interface Model {
        void updateDatabase();
        void matchPass(String username, String password, Contract.Presenter presenter);
        boolean storeExists(String storeName);
        boolean productExists(String productName);
        boolean isCustomer();
        boolean isStoreOwner();
        Store findStore(String storeName);
        Product findProduct(String productName);
        Product findProductInStore(String productName, String storeName);
        void addCustomer(String username, String password, Contract.Presenter presenter);
        void addStoreOwner(String username, String password, Contract.Presenter presenter);
        void addStore(String storeName, String ownerName, RegisterStoreActivity rsa);
        int addProductToStore(String storeName, Product product);
        void addProductToCart(String storeName, String productName, int quantity, ViewStoreActivity vsa);
        int deleteProductFromCart(String productName, String storeName);
        int makeOrder(String storeName);
    }

    interface View {
        void displayMessage(String message);
        String getUsername();
        String getPassword();
        void validateLogin(User user);
        void validateSignup(User user);
    }

    interface Presenter {
        void checkLogin();
        void checkSignup(String value);
        void validateLogin(User user);
        void invalidateLogin(int result);
        void validateSignup(User user);
        void invalidateSignup();
    }
}

