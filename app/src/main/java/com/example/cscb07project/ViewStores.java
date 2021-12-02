package com.example.cscb07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewStores extends AppCompatActivity {

    int page;
    int maxPage;
    Button previous;
    Button next;
    TextView pageNumber;
    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stores);

        page = 1;
        maxPage = (int) Math.ceil(Database.storeCount / 5.0);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        customer = (Customer)Database.user;

        pageNumber = findViewById(R.id.viewStoresPageNumber);
        pageNumber.setText(page + "");

        previous = findViewById(R.id.viewStorePreviousPageButton);
        previous.setVisibility(View.GONE);

        next = findViewById(R.id.viewStoreNextPageButton);
        if(maxPage == 1) next.setVisibility(View.GONE);
    }

    public void Back(View view){
        Intent intent = new Intent(this, CustomerUsageEntryScreen.class);
        intent.putExtra("username", customer.getUsername());
        startActivity(intent);
    }

    void nextPage(View view){
        page++;
        pageNumber.setText(page + "");
        if(page > 1) previous.setVisibility(View.VISIBLE);
        if(page == maxPage) next.setVisibility(View.GONE);
    }

    void previousPage(View view){
        page--;
        pageNumber.setText(page + "");
        if(page == 1) previous.setVisibility(View.GONE);
        if(page < maxPage) next.setVisibility(View.VISIBLE);
    }
}