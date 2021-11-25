package com.example.cscb07project;

public abstract class User {
    String username;
    String password;
    boolean isStoreOwner;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isStoreOwner() {
        return isStoreOwner;
    }
}
