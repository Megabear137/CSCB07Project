package com.example.cscb07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Database database = Database.getInstance();
        //NEVER EVER EVER EVER MAKE ANY CHANGES TO THE DATABASE INSIDE OF THE MAIN ACTIVITY UNLESS
        //YOU LIKE WIPING OUR DATA >:(
    }

    public void navigateToLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void navigateToSignup(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void navigateToCustomer(View view) {
        Intent intent = new Intent(this, CustomerUsageEntryScreen.class);
        intent.putExtra("USERNAME", "Zubair");
        startActivity(intent);
    }
}