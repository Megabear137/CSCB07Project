package com.example.cscb07project;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CustomerUsageEntryScreen extends AppCompatActivity {
    String username;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_usage_entry_screen);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        TextView welcome = findViewById(R.id.customerUsageWelcomeView);
        welcome.setText("Welcome " + username + "!");
    }

    public void viewStores(View view){
        Intent intent = new Intent(this, ViewStores.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }


    public void viewOrders(View view){
        Intent intent = new Intent(this, ViewOrders.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }



}