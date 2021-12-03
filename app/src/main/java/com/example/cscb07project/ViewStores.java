package com.example.cscb07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewStores extends AppCompatActivity {

    int page;
    int maxPage;
    Button previous;
    Button next;
    TextView pageNumber;
    Customer customer;

    ArrayList<TextView> storeNames;
    ArrayList<TextView> stores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stores);

        page = 1;
        maxPage = (int) Math.ceil(Database.storeCount / 5.0);

        Intent intent = getIntent();
        customer = (Customer)Database.user;

        pageNumber = findViewById(R.id.viewStoresPageNumber);
        pageNumber.setText(page + "");

        previous = findViewById(R.id.viewStoresPreviousPageButton);
        previous.setVisibility(View.GONE);

        next = findViewById(R.id.viewStoresNextPageButton);
        if(maxPage == 1) next.setVisibility(View.GONE);

        storeNames = new ArrayList<>();

        storeNames.add(findViewById(R.id.viewStoresStoreName));
        storeNames.add(findViewById(R.id.viewStoresStoreName1));
        storeNames.add(findViewById(R.id.viewStoresStoreName2));
        storeNames.add(findViewById(R.id.viewStoresStoreName3));

        stores = new ArrayList<>();

        stores.add(findViewById(R.id.viewStoresStore));
        stores.add(findViewById(R.id.viewStoresStore1));
        stores.add(findViewById(R.id.viewStoresStore2));
        stores.add(findViewById(R.id.viewStoresStore2));

        for(int i = 0; i < 4; i++){
            stores.get(i).setVisibility(View.GONE);
            storeNames.get(i).setVisibility(View.GONE);
        }

        Log.i("count", Database.storeCount + "");
        int storesOnPage = Math.min(4, Database.storeCount);

        for(int i = 0; i < storesOnPage; i++){
            stores.get(i).setVisibility(View.VISIBLE);
            storeNames.get(i).setVisibility(View.VISIBLE);
            storeNames.get(i).setText(Database.stores.get(i).getName());
        }
    }

    public void Back(View view){
        Intent intent = new Intent(this, CustomerUsageEntryScreen.class);
        intent.putExtra("username", customer.getUsername());
        startActivity(intent);
    }

    public void nextPage(View view){
        page++;
        pageNumber.setText(page + "");
        if(page > 1) previous.setVisibility(View.VISIBLE);
        if(page == maxPage) next.setVisibility(View.GONE);

        int storesOnPage = Math.min(4, Database.storeCount - 4 * (page - 1));

        for(int i = 0; i < storesOnPage; i++){
            storeNames.get(i).setText(Database.stores.get(4 * (page - 1) + i).getName());
            storeNames.get(i).setVisibility(View.VISIBLE);
        }
    }

    public void previousPage(View view){
        page--;
        pageNumber.setText(page + "");
        if(page == 1) previous.setVisibility(View.GONE);
        if(page < maxPage) next.setVisibility(View.VISIBLE);
    }

    public void viewStore(View view){
        Intent intent = new Intent(this, ViewStoreActivity.class);
        if(view.getId() == R.id.viewStoresStoreName){
            intent.putExtra("store", Database.stores.get(4 * (page - 1)).getName());
        }
        else if(view.getId() == R.id.viewStoresStoreName1){
            intent.putExtra("store", Database.stores.get(4 * (page - 1) + 1).getName());
        }
        else if(view.getId() == R.id.viewStoresStoreName2){
            intent.putExtra("store", Database.stores.get(4 * (page - 1) + 2).getName());
        }
        else if(view.getId() == R.id.viewStoresStoreName3){
            intent.putExtra("store", Database.stores.get(4 * (page - 1) + 3).getName());
        }
        startActivity(intent);
    }
}