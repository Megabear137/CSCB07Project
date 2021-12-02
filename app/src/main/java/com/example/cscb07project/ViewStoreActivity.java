package com.example.cscb07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ViewStoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_store);

        String storeName = getIntent().getStringExtra("store");
        TextView view = findViewById(R.id.viewStoreStoreName);
        view.setText(storeName);

    }

    public void Back(View view){
        Intent intent = new Intent(this, ViewStores.class);
        startActivity(intent);
    }
}