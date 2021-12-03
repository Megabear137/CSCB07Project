package com.example.cscb07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StoreOwnerViewFulfilledActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_owner_view_fulfilled);
        updateSpinner();
    }



    public void updateSpinner() {
        Intent i = getIntent();
        String username = i.getStringExtra("username");
        Spinner spinner = (Spinner) findViewById(R.id.fulfilledOrders);
        spinner.setOnItemSelectedListener(this);
        Database database = Database.getInstance();
        StoreOwner user = (StoreOwner) Database.user;
        Store userStore = Database.store;
        ArrayList<Order> orders = userStore.getOutgoingOrders();
        ArrayList<String> allOrders= new ArrayList<String>();
        for (Order order: orders) {
            allOrders.add(Integer.toString(order.getId()));
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, allOrders);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
        Database database = new Database();
        Intent i = getIntent();
        String username = i.getStringExtra("username");
        String orderID = parent.getItemAtPosition(position).toString();
        StoreOwner user = (StoreOwner) Database.user;
        Store userStore = Database.store;
        int intOrderID = Integer.parseInt(orderID);
        Order order = database.findOutgoingOrder(intOrderID);
        TextView orderInfo = (TextView) findViewById(R.id.fulfilledOrderInfo);
        if (order != null) {
            HashMap<String, Integer> products =  order.getProducts();
            if (products != null) {
                String allProductsInfo = "";
                for(Map.Entry<String,Integer> m: products.entrySet()) {
                    Product current = userStore.findProduct((String) m.getKey());
                    allProductsInfo += "Name: " +  m.getKey() + "\n" + " Price: " +  current.getPrice() + "\n" + " Quantity: " +  m.getValue() + "\n";
                    allProductsInfo += "--------------------------\n";
                }
                String allOrderInfo = order.getCustomerName() + "\n" + allProductsInfo;
                orderInfo.setText(allOrderInfo);
            }

            int duration = Toast.LENGTH_SHORT;
            CharSequence text = "Selected: " + orderID;
            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }


    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        int duration = Toast.LENGTH_SHORT;
        CharSequence text = "Select an order to view";
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }

    public void deleteFulfilledOrder(View view) {
        Database database = new Database();
        StoreOwner user = (StoreOwner) Database.user;
        Store userStore = Database.store;
        Intent i = getIntent();
        String username = user.getUsername();
        Spinner spinner = (Spinner) findViewById(R.id.fulfilledOrders);
        String orderID = (String) spinner.getSelectedItem();
        Order order = database.findOutgoingOrder(Integer.parseInt(orderID));

        //==
        int duration = Toast.LENGTH_SHORT;
        CharSequence text = "Failed to remove order";
        Context context = getApplicationContext();
        if (userStore.outgoingOrders == null || userStore.outgoingOrders.isEmpty()) {
            text = "You have no outgoing orders";
        } else if (database.storeDeleteHistory(order) == 1) {
            text = "Successfully removed order!";
            updateSpinner();
        }
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }











}