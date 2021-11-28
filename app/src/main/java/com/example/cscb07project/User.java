package com.example.cscb07project;

public abstract class User {
    String username;
    String password;
    boolean isStoreOwner;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isStoreOwner() {
        return isStoreOwner;
    }

    public void setStoreOwner(boolean storeOwner) {
        isStoreOwner = storeOwner;
    }
}
