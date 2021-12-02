package com.example.cscb07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class StoreOwnerAddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_owner_add);
    }

    public void addToStore (View view) {
        Intent i = getIntent();
        String username = i.getStringExtra("username");
        EditText editName = (EditText) findViewById(R.id.productNameText);
        EditText editBrand = (EditText) findViewById(R.id.productBrandText);
        EditText editPrice = (EditText) findViewById(R.id.productPriceText);
        Product product = new Product();
        String productName = editName.getText().toString();
        String brandName = editName.getText().toString();
        String price = editPrice.getText().toString();
        StoreOwner user = (StoreOwner) Database.user;
        Database database = new Database();
        Store userStore = Database.store;

        // ==================== Toast
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        CharSequence text = "Successfully added!";
        Toast toast = Toast.makeText(context, text, duration);
        // ================

        //=== May need to do better type checking for price
        if (productName.isEmpty()) {
            text = "Name cannot be empty!";
            toast = Toast.makeText(context, text, duration);
            toast.show();
        } else if (brandName.isEmpty()) {
            text = "Brand cannot be empty!";
            toast = Toast.makeText(context, text, duration);
            toast.show();
        } else if (price.isEmpty()) {
            text = "Price cannot be empty!";
            toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        //=== Remains to deal with uniqueness
        Product other = database.findProduct(productName);


        if (!productName.isEmpty() && !price.isEmpty() && !brandName.isEmpty()) {
            try {
                Double doublePrice = Double.parseDouble(price);
                product.setPrice(doublePrice);
                product.setBrand(editBrand.getText().toString());
                product.setName(editName.getText().toString());
                if (doublePrice <= 0) {
                    text = "Price must be positive!";
                    toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else if (database.productExists(userStore.getName(), productName)
                        || product.equals(other)) {
                    text = "Product already exists. Try editing from previous menu.";
                    toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {
                    text = "Successfully added!";
                    toast = Toast.makeText(context, text, duration);
                    toast.show();
                    database.addProductToStore(user.getStoreName(), product);
                }
                try {
                    doublePrice = Double.parseDouble(price);
                    product.setPrice(doublePrice);
                    user = (StoreOwner) Database.user;
                    database.addProductToStore(user.getStoreName(), product);

                } catch (NumberFormatException e) {
                    text = "Price must be numeric!";
                    toast = Toast.makeText(context, text, duration);
                    toast.show();
                    e.printStackTrace();
                }
            }catch (Exception e){

            }


        }


    }

}