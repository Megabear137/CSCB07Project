package com.example.cscb07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StoreOwnerEditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText productText;
    EditText brandText;
    EditText priceText;
    Spinner productSpinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_owner_edit);
        productText = findViewById(R.id.editProductName);
        brandText = findViewById(R.id.editBrandName);
        priceText = findViewById(R.id.editPrice);
        productSpinner = findViewById(R.id.productSpinner);
        updateSpinner();


    }

    public void updateSpinner() {
        productSpinner.setOnItemSelectedListener(this);
        Store userStore = Database.store;
        ArrayList<Product> products = userStore.getProducts();
        ArrayList<String> allProducts = new ArrayList<>();
        for (Product product: products) {
            allProducts.add(product.getName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, allProducts);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productSpinner.setAdapter(dataAdapter);
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
        String productName = parent.getItemAtPosition(position).toString();
        Store userStore = Database.store;
        Product product = userStore.findProduct(productName);
        productText.setText(productName);
        brandText.setText(product.getBrand());
        priceText.setText(String.format(Locale.getDefault(),"%.2f",product.getPrice()));
        int duration = Toast.LENGTH_SHORT;
        CharSequence text = "Selected: " + productName;
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        if(((TextView) parent.getChildAt(0)) != null){
            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            ((TextView) parent.getChildAt(0)).setTextSize(20);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        int duration = Toast.LENGTH_SHORT;
        CharSequence text = "Select a product to edit";
        displayMessage(text,duration);
    }

    public void displayMessage(CharSequence text, int duration) {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


    public void editProduct(View view) {
        String productName = (String) productSpinner.getSelectedItem();
        Product editedProduct = new Product();
        String newProductName = productText.getText().toString();
        String newBrandName =  brandText.getText().toString();
        String newPrice = priceText.getText().toString();

        Database database = new Database();
        Store userStore = Database.store;
        Product oldProduct = userStore.findProduct(productName);
        int duration = Toast.LENGTH_SHORT;
        CharSequence text;

        Pattern p = Pattern.compile("\\w+((\\s|-|')?\\w)*");
        Matcher nameMatcher = p.matcher(newProductName);
        Matcher brandMatcher = p.matcher(newBrandName);

        if(userStore.products.isEmpty()){
            text = "You have no items to edit!";
            displayMessage (text,duration);
        }
        else if (!nameMatcher.matches()){
            text = "Name must be alphanumeric";
            displayMessage (text,duration);
        } else if (!brandMatcher.matches()){
            text = "Brand name must be alphanumeric";
            displayMessage (text,duration);
        }
        else if (newPrice.isEmpty()){
            text = "Price cannot be empty!";
            displayMessage (text,duration);
        }

        if (!newProductName.isEmpty() && !newBrandName.isEmpty() && !newPrice.isEmpty() &&
                  nameMatcher.matches() && brandMatcher.matches()) {
            try
            {
                double doublePrice = Double.parseDouble(newPrice);
                doublePrice = Math.round(doublePrice *100.0)/ 100.0;
                editedProduct.setPrice(doublePrice);
                editedProduct.setBrand(newBrandName);
                editedProduct.setName(newProductName);
                if (doublePrice <= 0) {
                    text = "Price must be positive!";
                    displayMessage (text,duration);
                }
                else if (userStore.productExists(newProductName)
                        && !newProductName.equals(productName)){
                    text = "Product with this name already exists.";
                    displayMessage (text,duration);
                }
                else {
                    text = "Successfully changed!";
                    displayMessage (text,duration);
                    database.editProduct(oldProduct, userStore.getName(), newProductName, newBrandName, doublePrice);
                    updateSpinner();
                }

            }
            catch(NumberFormatException e)
            {
                text = "Price must be numeric!";
                displayMessage (text,duration);
                e.printStackTrace();
            }
        }


    }



}