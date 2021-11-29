package com.example.cscb07project;

public interface Contract {
    interface Model{
        boolean userExists(String username);
    }

    interface View{
        String getUsername();
        void displayMessage(String message);
    }

    interface Presenter{
        void checkUsername();
    }
}

