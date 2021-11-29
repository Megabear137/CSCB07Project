package com.example.cscb07project;

public interface Contract {
    public interface Model {
        public boolean userExists(String username);
    }

    public interface View {
        public void displayMessage(String message);
        public String getUsername();
        public String getPassword();
    }

    public interface Presenter {
        public boolean checkLogin();
        public boolean checkSignup();
        public boolean checkCustomer();
    }
}

