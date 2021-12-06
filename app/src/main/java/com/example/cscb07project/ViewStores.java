package com.example.cscb07project;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class ViewStores extends AppCompatActivity {

    int page;
    int maxPage;
    Button previous;
    Button next;
    TextView pageNumber;
    Customer customer;

    ArrayList<TextView> storeNames;
    ArrayList<Store> stores;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stores);

        page = 1;
        maxPage = (int) Math.ceil(Database.storeCount / 4.0);

        customer = (Customer)Database.user;
        stores = Database.stores;

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

        for(int i = 0; i < 4; i++){
            storeNames.get(i).setVisibility(View.GONE);
        }

        int storesOnPage = Math.min(4, Database.storeCount);

        for(int i = 0; i < storesOnPage; i++){
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
        maxPage = (int) Math.ceil(stores.size() / 4.0);
        page++;
        pageNumber.setText(page + "");
        if(page > 1) previous.setVisibility(View.VISIBLE);
        if(page == maxPage) next.setVisibility(View.GONE);

        updateStores();
    }

    public void previousPage(View view){
        maxPage = (int) Math.ceil(stores.size() / 4.0);

        page--;
        pageNumber.setText(page + "");
        if(page == 1) previous.setVisibility(View.GONE);
        if(page < maxPage) next.setVisibility(View.VISIBLE);

        updateStores();
    }

    public void updateStores(){

        for(int i = 0; i < 4; i++){
            storeNames.get(i).setText("");
            storeNames.get(i).setVisibility(View.GONE);
        }

        int storesOnPage = Math.min(4, stores.size() - 4 * (page - 1));

        for(int i = 0; i < storesOnPage; i++){
            storeNames.get(i).setText(stores.get(4 * (page - 1) + i).getName());
            storeNames.get(i).setVisibility(View.VISIBLE);
        }
    }

    public void viewStore(View view){
        Intent intent = new Intent(this, ViewStoreActivity.class);
        if(view.getId() == R.id.viewStoresStoreName){
            intent.putExtra("store", stores.get(4 * (page - 1)).getName());
        }
        else if(view.getId() == R.id.viewStoresStoreName1){
            intent.putExtra("store", stores.get(4 * (page - 1) + 1).getName());
        }
        else if(view.getId() == R.id.viewStoresStoreName2){
            intent.putExtra("store", stores.get(4 * (page - 1) + 2).getName());
        }
        else if(view.getId() == R.id.viewStoresStoreName3){
            intent.putExtra("store", stores.get(4 * (page - 1) + 3).getName());
        }
        startActivity(intent);
    }

    public void Search(View view){
        String search = ((TextView)findViewById(R.id.viewStoresSearchBar)).getText().toString();

        if(search.equals("")){
            stores = Database.stores;
        }
        else{
            ArrayList<Store> searchStores = new ArrayList<>();
            for(Store store: Database.stores){
                if(store.getName().toLowerCase(Locale.ROOT).contains(search.toLowerCase(Locale.ROOT))){
                    searchStores.add(store);
                }
            }
            stores = searchStores;
        }

        if(stores.size() == 0) Toast.makeText(getApplicationContext(), "No Stores Found", Toast.LENGTH_SHORT).show();
        if(stores.size() == 1) Toast.makeText(getApplicationContext(), "1 Store Found", Toast.LENGTH_SHORT).show();
        else Toast.makeText(getApplicationContext(), stores.size() + " Stores Found", Toast.LENGTH_SHORT).show();

        page = 1;
        pageNumber.setText(page + "");
        previous.setVisibility(View.GONE);
        maxPage = (int) Math.ceil(stores.size() / 4.0);
        if(maxPage == 1) next.setVisibility(View.GONE);
        else next.setVisibility(View.VISIBLE);

        updateStores();
     }
}