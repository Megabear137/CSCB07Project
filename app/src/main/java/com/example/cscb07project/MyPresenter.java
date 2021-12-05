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

        if(username.equals("")) view.displayMessage("Please Enter a Username");
        else if(password.equals("")) view.displayMessage("Please Enter a Password");
        else database.matchPass(username, password, this);
    }


    public void checkSignup(String value) {
        String username = view.getUsername();
        if (username.equals("")) {
            view.displayMessage("Username Cannot Be Empty");
        }
        else{
            if(value.equals("I am a customer.")) database.addCustomer(view.getUsername(), view.getPassword(), this);
            else database.addStoreOwner(view.getUsername(), view.getPassword(), this);
        }
    }

    public void validateLogin(User user){
        if(user.isStoreOwner) database.initializeStore(((StoreOwner)user).getStoreName());
        else database.initializeStores();
        view.validateLogin(user);
    }

    public void invalidateLogin(int result){
        if(result == 0) view.displayMessage("Incorrect Password");
        else view.displayMessage("User Not Found");
    }

    public void validateSignup(User user){
        if(user.isStoreOwner) database.initializeStore(((StoreOwner)user).getStoreName());
        else database.initializeStores();
        view.displayMessage("Success");
        view.validateSignup(user);
    }
    public void invalidateSignup(){
        view.displayMessage("Username Taken");
    }

}