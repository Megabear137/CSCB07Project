package com.example.cscb07project;

import java.util.ArrayList;

public class StoreOwner extends  User{
    ArrayList<String> stock;
    ArrayList<String> orders;

    public StoreOwner (String username, String password) {
        this.username = username;
        this.password = password;
        isStoreOwner = true;
    }








}
