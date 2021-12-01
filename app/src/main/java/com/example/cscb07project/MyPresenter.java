package com.example.cscb07project;

public class MyPresenter implements Contract.Presenter {
    private Contract.View view;
    Database database = Database.getInstance();

    public MyPresenter(Contract.View view) {
        this.view = view;
    }

    //Checks the validity of login input
    public boolean checkLogin() {
        String username = view.getUsername();
        if (username.equals("")) {
            view.displayMessage("username cannot be empty");
            return false;
        } else if (database.userExists(username)) {
            if (database.matchPass(username, view.getPassword()) == true) {
                view.displayMessage("user found");
                return true;
            } else {
                view.displayMessage("incorrect password");
                return false;
            }
        } else {
            view.displayMessage("user not found");
            return false;
        }
    }

    //Checks the validity of signup input
    public boolean checkSignup() {
        String username = view.getUsername();
        if (username.equals("")) {
            view.displayMessage("username cannot be empty");
            return false;
        } else if (database.userExists(username)) {
            view.displayMessage("username already exists");
            return false;
        } else {
            view.displayMessage("success");
            return true;
        }
    }

    public boolean checkCustomer() { return database.isCustomer(view.getUsername()); }
    public boolean addCustomer() { return database.addCustomer(view.getUsername(), view.getPassword()); }
    public boolean addStoreOwner() { return database.addStoreOwner(view.getUsername(), view.getPassword()); }
}