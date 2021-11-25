package com.example.cscb07project;

import java.util.ArrayList;

public class StoreOwner extends User{
    Store store;

    public StoreOwner (String username, String password) {
        this.username = username;
        this.password = password;
        isStoreOwner = true;
        store = null;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Store getStore() {
        return store;
    }
}
