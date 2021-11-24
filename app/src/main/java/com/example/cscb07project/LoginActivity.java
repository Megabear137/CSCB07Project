package com.example.cscb07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void moveToSignup(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void loginButton(View view) {
        /*
        boolean value = true is the username is that of a store owner, false otherwise;
        if (value == false) {
            Intent intent = new Intent(this, CustomerUsage.class);
            startActivity(intent);
        }
        if (value == true) {
            Intent intent = new Intent(this, StoreOwnerUsage.class);
            startActivity(intent);
        }
        */

    }
}