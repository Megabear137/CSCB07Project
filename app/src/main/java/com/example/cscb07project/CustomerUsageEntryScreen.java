package com.example.cscb07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CustomerUsageEntryScreen extends AppCompatActivity {
    User customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_usage_entry_screen);

        Intent intent = getIntent();
        String username = intent.getStringExtra("Username");

        Database database = Database.getInstance();
        customer = database.findCustomer(username);

        TextView welcome = findViewById(R.id.customerUsageWelcomeView);
        welcome.setText("Welcome " + username + "!");

    }

    public void viewStores(View view){
        Intent intent = new Intent(this, ViewStores.class);
        intent.putExtra("Username", customer.getUsername());
        startActivity(intent);
    }


    public void viewOrders(View view){
        Intent intent = new Intent(this, ViewOrders.class);
        intent.putExtra("Username", customer.getUsername());
        startActivity(intent);
    }



}