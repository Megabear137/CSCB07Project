package com.example.cscb07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void moveToLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void signupButton(View view) {
        Spinner spinner = (Spinner)findViewById(R.id.spinner2);
        String value = String.valueOf(spinner.getSelectedItem());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        if (value == "I am a customer.") {
            Customer cust = new Customer(username from import, password from import);
            myRef.child("customers")
            Intent intent = new Intent(this, CustomerUsage.class);
            startActivity(intent);
        }
        if (value == "I am a store owner.") {
            Code for adding store owner to the firebase
            Intent intent = new Intent(this, StoreOwnerUsage.class);
            startActivity(intent);
        }
    }
}