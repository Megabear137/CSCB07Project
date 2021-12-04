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
        TextView priceInfo = (TextView) findViewById(R.id.fulfilledPriceText);
        TextView quantityInfo = (TextView) findViewById(R.id.fulfilledQuantityText);
        TextView customerInfo = (TextView) findViewById(R.id.fulfilledCustomerName);
        TextView costInfo = (TextView) findViewById(R.id.fulfilledTotalCost);
        if (order != null) {
            HashMap<String, Integer> products =  order.getProducts();
            if (products != null) {
                String allProductNames = "Product:\n";
                String allPrices = "Price:\n";
                String allQuantities ="Quantity:\n";
                String customerName = "Customer name: " +order.getCustomerName();
                String totalCostMessage;
                Double totalCost = 0.0;
                for(Map.Entry<String,Integer> m: products.entrySet()) {
                    Product current = userStore.findProduct((String) m.getKey());
                    allProductNames += m.getKey()  + "\n";
                    allPrices += current.getPrice()+ "\n";
                    allQuantities += m.getValue() + "\n";
                    totalCost += current.getPrice() * m.getValue();
                }
                totalCost = Math.round(totalCost *100.0)/ 100.0;
                totalCostMessage = "Total cost: $" + Double.toString(totalCost);
                orderInfo.setText(allProductNames);
                priceInfo.setText(allPrices);
                quantityInfo.setText(allQuantities);
                customerInfo.setText(customerName);
                costInfo.setText(totalCostMessage);
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
        CharSequence text = "You have no outgoing orders";
        int duration = Toast.LENGTH_SHORT;
        Context context = getApplicationContext();
        if (userStore.getOutgoingOrders()!= null && !userStore.getOutgoingOrders().isEmpty()) {
            Order order = database.findOutgoingOrder(Integer.parseInt(orderID));
            if (database.storeDeleteHistory(order) == 1) {
                text = "Successfully removed order!";
                updateSpinner();
            }
        }

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }











}