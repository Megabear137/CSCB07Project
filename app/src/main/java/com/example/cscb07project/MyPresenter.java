package com.example.cscb07project;

public class MyPresenter implements Contract.Presenter {
    private Contract.Model model;
    private Contract.View view;

    public MyPresenter(Contract.Model model, Contract.View view) {
        this.model = model;
        this.view = view;
    }

    Database database = Database.getInstance();

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
            if (checkPassword(view.getPassword()) == true) {
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
            /*Will be replaced once proper adding to firebase is implemented in model
            Check if customer or storeowner
            then add using addCustomer or addStoreOwner

            addToFirebase(username, view.getPassword());
            view.displayMessage("success");
            return true;
             */
        }
    }

    public boolean checkCustomer() {
        return isCustomer(view.getUsername());
    }
}