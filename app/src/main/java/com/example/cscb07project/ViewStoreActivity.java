package com.example.cscb07project;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewStoreActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Store store;
    Customer customer;

    boolean canOrder = true;
    String storeName;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_store);

        storeName = getIntent().getStringExtra("store");
        Database database = new Database();
        store = database.findStore(storeName);
        customer = (Customer) Database.user;
        TextView view = findViewById(R.id.viewStoreStoreName);
        view.setText(storeName);
        updateProductsSpinner();
        updateCartSpinner();
        updateTotal();
    }

    public void updateProductsSpinner() {
        Spinner spinner = (Spinner)findViewById(R.id.productsSpinner);
        spinner.setOnItemSelectedListener(this);
        ArrayList<Product> products = store.getProducts();
        ArrayList<String> productsInSpinner = new ArrayList<>();
        for (Product product: products) {
            productsInSpinner.add(product.toString());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, productsInSpinner);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    public void updateCartSpinner() {
        customer = (Customer) Database.user;
        Spinner spinner = (Spinner)findViewById(R.id.cartSpinner);
        spinner.setOnItemSelectedListener(this);
        Order orderInCart = null;
        for (Order order: customer.cart) {
            if (order.getStoreName().equals(store.getName()))
                orderInCart = order;
        }
        if (orderInCart == null) {
            ArrayList<String> emptySpinner = new ArrayList<String>();
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, emptySpinner);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
            return;
        }

        Database database = new Database();
        ArrayList<String> productsInSpinner = new ArrayList<String>();
        for(String productName: orderInCart.products.keySet()){
            Product product = database.findProductInStore(productName, storeName);
            String orderInfo = product.toString() + "    *" + orderInCart.products.get(productName);
            productsInSpinner.add(orderInfo);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, productsInSpinner);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    public void Back(View view){
        Intent intent = new Intent(this, ViewStores.class);
        startActivity(intent);
    }

    public void updateTotal() {
        Order orderInCart = null;
        for (Order order: customer.cart) {
            if (order.getStoreName().equals(store.getName())) {
                orderInCart = order;
            }
        }
        TextView totalTextView = findViewById(R.id.total);
        if(orderInCart == null) {
            totalTextView.setText("$0.00");
            findViewById(R.id.viewStoreEditOrder).setVisibility(View.GONE);
        }
        else{
            java.text.DecimalFormat df =new java.text.DecimalFormat("#.00");
            String s = df.format(orderInCart.calculateTotal());
            String total = "$"+s;
            totalTextView.setText(total);
            findViewById(R.id.viewStoreEditOrder).setVisibility(View.VISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addAmount(View view) {
        Spinner spinner = (Spinner) findViewById(R.id.productsSpinner);
        String productInfo = (String) spinner.getSelectedItem();
        int index = productInfo.indexOf(";");
        String productName = productInfo.substring(0,index);
        EditText editText = (EditText) findViewById(R.id.amountAdded);
        if(!editText.getText().toString().equals("")){
            int quantity = Integer.parseInt(editText.getText().toString());
            Database database = new Database();
            if(canOrder){
                canOrder = false;
                database.addProductToCart(store.getName(), productName, quantity, this);
            }
            else{
                Toast.makeText(getApplicationContext(), "Failed to place order. Please wait a moment and then try again.", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "Please enter an amount first.", Toast.LENGTH_SHORT).show();
        }


    }

    public void setCanOrder(){
        canOrder = true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void remove(View view) {
        Spinner spinner = (Spinner) findViewById(R.id.cartSpinner);
        String productInfo = (String) spinner.getSelectedItem();
        if(productInfo == null){
            Toast.makeText(getApplicationContext(), "There is nothing to remove.", Toast.LENGTH_SHORT).show();
        }
        else{
            int index = productInfo.indexOf(";");
            String productName = productInfo.substring(0,index);
            Database database = new Database();
            database.deleteProductFromCart(productName, store.getName());
            updateCartSpinner();
            updateTotal();
        }
    }

    public void makeOrder(View view) {
        Database database = new Database();
        database.makeOrder(store.getName());
        updateCartSpinner();
        updateTotal();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapter,View view,int position,long id) {
        Toast.makeText(getApplicationContext(), "Item Selected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        Toast.makeText(getApplicationContext(), "Please select an item", Toast.LENGTH_SHORT).show();
    }

    public void editOrder(View view){
        Intent intent = new Intent(this, ViewOrders.class);
        intent.putExtra("store", storeName);
        startActivity(intent);
    }

}