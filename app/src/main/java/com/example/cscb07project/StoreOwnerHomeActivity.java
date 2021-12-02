package com.example.cscb07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class StoreOwnerHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_owner_home);
    }

    public void navigateToAddProducts (View view) {
        Intent i = getIntent();
        String username = i.getStringExtra("username");
        Intent intent = new Intent(this, StoreOwnerAddActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void navigateToEditProducts (View view) {
        Intent i = getIntent();
        String username = i.getStringExtra("username");
        Intent intent = new Intent(this, StoreOwnerEditActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void navigateToViewIncoming (View view) {
        Intent i = getIntent();
        String username = i.getStringExtra("username");
        Intent intent = new Intent(this, StoreOwnerViewIncomingActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
    public void navigateToViewFulfilled (View view) {
        Intent i = getIntent();
        String username = i.getStringExtra("username");
        Intent intent = new Intent(this, StoreOwnerViewFulfilledActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void logout (View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }








}