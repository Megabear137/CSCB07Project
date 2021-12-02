package com.example.cscb07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class StoreOwnerEditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        String username = i.getStringExtra("username");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_owner_edit);
        Spinner spinner = (Spinner) findViewById(R.id.productSpinner);
        spinner.setOnItemSelectedListener(this);
        Database database = Database.getInstance();
        StoreOwner user = database.findStoreOwner(username);
        Store userStore = database.findStore(user.getStoreName());
        ArrayList<Product> products = userStore.getProducts();
        ArrayList<String> allProducts = new ArrayList<String>;
        for (Product product: products) {
            allProducts.add(product.getName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, allProducts);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
        String product = parent.getItemAtPosition(position).toString();
        int duration = Toast.LENGTH_SHORT;
        CharSequence text = "Selected: " + product;
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        int duration = Toast.LENGTH_SHORT;
        CharSequence text = "Select a product to edit";
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, duration);

    }
}