package com.example.cscb07project;

public class MyPresenter implements Contract.Presenter {
    private Contract.Model model;
    private Contract.View view;
    Database database = Database.getInstance();

    public MyPresenter(Contract.Model model, Contract.View view) {
        this.model = model;
        this.view = view;
    }

    //Will be removed once password reader is implemented in the model
    private boolean checkPassword(String password) {
        return true;
    }

    //Checks the validity of login input
    public boolean checkLogin() {
        String username = view.getUsername();
        if (username.equals("")) {
            view.displayMessage("username cannot be empty");
            return false;
        } else if (model.userExists(username)) {
            if (checkPassword(view.getPassword())) {
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
        } else if (model.userExists(username)) {
            view.displayMessage("username already exists");
            return false;
        } else {
            view.displayMessage("success");
            return true;
        }
    }

    public boolean checkCustomer() { return database.isCustomer(view.getUsername()); }
    public boolean addCustomer(String username, String password) { return database.addCustomer(username, password); }
    public boolean addStoreOwner(String username, String password) { return database.addStoreOwner(username, password); }
}