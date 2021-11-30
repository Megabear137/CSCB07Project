package com.example.cscb07project;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public abstract class User {
    String username;
    String password;
    boolean isStoreOwner;

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (!username.equals(other.username))
            return false;
        return true;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean getIsStoreOwner() {
        return isStoreOwner;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isStoreOwner() {
        return isStoreOwner;
    }

    public void setStoreOwner(boolean storeOwner) {
        isStoreOwner = storeOwner;
    }
}
