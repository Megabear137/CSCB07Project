package com.example.cscb07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class StoreOwnerHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_owner_home);
        Intent i = getIntent();
        TextView welcomeText = (TextView) findViewById(R.id.ownerWelcomeText);
        String welcomeMessage = "Welcome back, \n" + i.getStringExtra("username");
        welcomeText.setText(welcomeMessage);
    }

    public void navigateToAddProducts (View view) {
        Intent intent = new Intent(this, StoreOwnerAddActivity.class);
        startActivity(intent);
    }

    public void navigateToEditProducts (View view) {
        Intent intent = new Intent(this, StoreOwnerEditActivity.class);
        startActivity(intent);
    }

    public void navigateToViewIncoming (View view) {
        Intent intent = new Intent(this, StoreOwnerViewIncomingActivity.class);
        startActivity(intent);
    }
    public void navigateToViewFulfilled (View view) {
        Intent intent = new Intent(this, StoreOwnerViewFulfilledActivity.class);
        startActivity(intent);
    }

    public void logout (View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }








}