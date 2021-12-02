package com.example.cscb07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class StoreOwnerEditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_owner_edit);
        updateSpinner();


    }

    public void updateSpinner() {
        //===
        Intent i = getIntent();
        String username = i.getStringExtra("username");
        Spinner spinner = (Spinner) findViewById(R.id.productSpinner);
        spinner.setOnItemSelectedListener(this);
        Database database = Database.getInstance();
        StoreOwner user = (StoreOwner) Database.user;
        Store userStore = Database.store;
        ArrayList<Product> products = userStore.getProducts();
        ArrayList<String> allProducts = new ArrayList<String>();
        for (Product product: products) {
            allProducts.add(product.getName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, allProducts);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        //====
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
        Database database = new Database();
        Intent i = getIntent();
        String username = i.getStringExtra("username");
        String productName = parent.getItemAtPosition(position).toString();
        StoreOwner user = (StoreOwner) Database.user;
        Store userStore = Database.store;
        Product product = database.findProductInStore(productName);
        EditText productText = (EditText) findViewById(R.id.editProductName);
        EditText brandText = (EditText) findViewById(R.id.editBrandName);
        EditText priceText = (EditText) findViewById(R.id.editPrice);
        productText.setText(productName);
        brandText.setText(product.getBrand());
        priceText.setText(Double.toString(product.getPrice()));
        int duration = Toast.LENGTH_SHORT;
        CharSequence text = "Selected: " + productName;
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

    public void editProduct(View view) {
        Intent i = getIntent();
        String username = i.getStringExtra("username");

        EditText productText = (EditText) findViewById(R.id.editProductName);
        EditText brandText = (EditText) findViewById(R.id.editBrandName);
        EditText priceText = (EditText) findViewById(R.id.editPrice);
        Spinner spinner = (Spinner) findViewById(R.id.productSpinner);
        String productName = (String) spinner.getSelectedItem();

        Product editedProduct = new Product();
        String newProductName = productText.getText().toString();
        String newBrandName =  brandText.getText().toString();
        String newPrice = priceText.getText().toString();

        Database database = Database.getInstance();
        StoreOwner user = (StoreOwner) Database.user;
        Store userStore = Database.store;
        Product oldProduct = userStore.findProduct(productName);

        // ==================== Toast
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        CharSequence text = "Successfully added!";
        Toast toast = Toast.makeText(context, text, duration);
        // ================

        if(userStore.products.isEmpty()){
            text = "You have no items to edit!";
            toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        //=== May need to do better type checking for price
        if (newProductName.isEmpty()){
            text = "Name cannot be empty!";
            toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else if (newBrandName.isEmpty()){
            text = "Brand cannot be empty!";
            toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else if (newPrice.isEmpty()){
            text = "Price cannot be empty!";
            toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        Product other = userStore.findProduct(productName);

        if (!newProductName.isEmpty() && !newBrandName.isEmpty() && !newPrice.isEmpty() &&
                !userStore.products.isEmpty()) {
            try
            {
                Double doublePrice = Double.parseDouble(newPrice);
                editedProduct.setPrice(doublePrice);
                editedProduct.setBrand(newBrandName);
                editedProduct.setName(newProductName);
                if (doublePrice <= 0) {
                    text = "Price must be positive!";
                    toast = Toast.makeText(context,text,duration);
                    toast.show();
                }
                else if (database.productExists(userStore.getName(), newProductName)
                        && !newProductName.equals(productName)){
                    text = "Product with this name already exists.";
                    toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else {
                    text = "Successfully changed!";
                    toast = Toast.makeText(context, text, duration);
                    toast.show();
                    database.editProduct(oldProduct, userStore.getName(), newProductName, newBrandName, doublePrice);
                    updateSpinner();
                }

            }
            catch(NumberFormatException e)
            {
                text = "Price must be numeric!";
                toast = Toast.makeText(context, text, duration);
                toast.show();
                e.printStackTrace();
            }
        }





















    }



}