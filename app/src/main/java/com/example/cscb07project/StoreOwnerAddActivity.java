package com.example.cscb07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StoreOwnerAddActivity extends AppCompatActivity {
    EditText editName;
    EditText editBrand;
    EditText editPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_owner_add);
        editName = findViewById(R.id.productNameText);
        editBrand = findViewById(R.id.productBrandText);
        editPrice = findViewById(R.id.productPriceText);
    }

    public void displayMessage(CharSequence text, int duration) {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void addToStore (View view) {
        Product product = new Product();
        String productName = editName.getText().toString();
        String brandName = editBrand.getText().toString();
        String price = editPrice.getText().toString();
        StoreOwner user;
        Database database = new Database();
        Store userStore = Database.store;
        int duration = Toast.LENGTH_SHORT;
        CharSequence text;
        Pattern p = Pattern.
                compile("[a-zA-Z0-9]+[a-zA-Z0-9\\s]*");
        Matcher nameMatcher = p.matcher(productName);
        Matcher brandMatcher = p.matcher(brandName);
        if (!nameMatcher.matches()){
            text = "Name must be alphanumeric";
            displayMessage(text,duration);
        } else if (!brandMatcher.matches()){
            text = "Brand name must be alphanumeric";
            displayMessage(text,duration);
        }
        else if (price.isEmpty()) {
            text = "Price cannot be empty!";
            displayMessage(text,duration);
        }

        Product other = database.findProduct(productName);
        if (!productName.isEmpty() && !price.isEmpty() && !brandName.isEmpty() &&
                nameMatcher.matches()&& brandMatcher.matches()) {
            try {
                double doublePrice = Double.parseDouble(price);
                doublePrice = Math.round(doublePrice *100.0)/ 100.0;
                product.setPrice(doublePrice);
                product.setBrand(editBrand.getText().toString());
                product.setName(editName.getText().toString());
                if (doublePrice <= 0) {
                    text = "Price must be positive!";
                    displayMessage(text,duration);
                } else if (userStore.productExists(productName)
                        || product.equals(other)) {
                    text = "Product already exists. Try editing from previous menu.";
                    displayMessage(text,duration);
                } else {
                    try {
                        doublePrice = Double.parseDouble(price);
                        doublePrice = Math.round(doublePrice *100.0)/ 100.0;
                        product.setPrice(doublePrice);
                        user = (StoreOwner) Database.user;
                        database.addProductToStore(user.getStoreName(), product);
                        text = "Successfully added!";
                        displayMessage(text,duration);
                    } catch (NumberFormatException e) {
                        text = "Price must be numeric!";
                        displayMessage(text,duration);
                        e.printStackTrace();
                    }
                }

            }
            catch (NumberFormatException e) {
                text = "Price must be numeric!";
                displayMessage(text,duration);
                e.printStackTrace();
            }

        }

    }

}