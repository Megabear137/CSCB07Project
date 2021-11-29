package com.example.cscb07project;

public class MyPresenter implements Contract.Presenter{
    private Contract.Model model;
    private Contract.View view;

    public MyPresenter(Contract.Model model, Contract.View view){
        this.model = model;
        this.view = view;
    }

    public void checkUsername() {
        String username = view.getUsername();
        if(username.equals(""))
            view.displayMessage("Username cannot be empty");
        else if(model.userExists(username))
            view.displayMessage("user found");
        else
            view.displayMessage("user not found");
    }

}
