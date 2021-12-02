package com.example.cscb07project;

import android.util.Log;

public class MyPresenter implements Contract.Presenter {
    private Contract.View view;
    private Contract.Model database;

    public MyPresenter(Contract.View view,  Contract.Model database) {

        this.view = view;
        this.database = database;
    }

    //Checks the validity of login input
    public void checkLogin() {
        String username = view.getUsername();
        String password = view.getPassword();
        database.matchPass(username, password);
    }


    public void checkSignup(String value) {
        String username = view.getUsername();
        if (username.equals("")) {
            view.displayMessage("username cannot be empty");
        }
        else{
            Log.i("demo", value);
            if(value.equals("I am a customer.")) addCustomer();
            else addStoreOwner();
        }
    }

    public boolean checkCustomer() { return database.isCustomer(); }
    public void addCustomer() {

        database.addCustomer(view.getUsername(), view.getPassword());
    }
    public void addStoreOwner() { database.addStoreOwner(view.getUsername(), view.getPassword()); }
}