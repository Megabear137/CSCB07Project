package com.example.cscb07project;

import java.util.ArrayList;

public class MyModel implements Contract.Model{
    ArrayList<String> usernames;

    public MyModel(){
        usernames = new ArrayList<String>();
        usernames.add("Zubair");
    }

    public boolean userExists(String username) {
        return usernames.contains(username);
    }

}
