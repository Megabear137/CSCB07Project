package com.example.cscb07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class StoreOwnerAddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_owner_add);
    }

    public void addToStore (View view) throws NumberFormatException{
        Intent i = getIntent();
        String username = i.getStringExtra("username");
        EditText editName = (EditText) findViewById(R.id.productNameText);
        EditText editBrand = (EditText) findViewById(R.id.productBrandText);
        EditText editPrice= (EditText) findViewById(R.id.productPriceText);
        Product product = new Product();
        product.setBrand(editBrand.getText().toString());
        product.setName(editName.getText().toString());
        String price = editPrice.getText().toString();
        //=== May need to do better type checking for price
        try
        {
            Double doublePrice = Double.parseDouble(price);
            product.setPrice(doublePrice);
            Database database = Database.getInstance();
            StoreOwner user = database.findStoreOwner(username);
            database.addProductToStore(user.getStoreName(), product);

        }
        catch(NumberFormatException e)
        {
            throw new NumberFormatException("Not Double");
        }


    }




}