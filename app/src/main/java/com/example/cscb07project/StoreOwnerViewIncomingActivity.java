package com.example.cscb07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StoreOwnerViewIncomingActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_owner_view_incoming);
        updateSpinner();
    }

    public void updateSpinner() {
        Intent i = getIntent();
        String username = i.getStringExtra("username");
        Spinner spinner = (Spinner) findViewById(R.id.incomingOrderSpinner);
        spinner.setOnItemSelectedListener(this);
        StoreOwner user = (StoreOwner) Database.user;
        Store userStore = Database.store;
        ArrayList<Order> orders = userStore.getIncomingOrders();
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
        Order order = database.findIncomingOrder(intOrderID);
        TextView orderInfo = (TextView) findViewById(R.id.incomingOrderInfo);
        TextView priceInfo = (TextView) findViewById(R.id.priceText);
        TextView quantityInfo = (TextView) findViewById(R.id.quantityText);
        TextView customerInfo = (TextView) findViewById(R.id.customerNameText);
        TextView costInfo = (TextView) findViewById(R.id.incomingTotalCost);
        HashMap<String, Integer> products =  order.getProducts();
        if (products != null) {
            String allProductNames = "Product:\n";
            String allPrices = "Price:\n";
            String allQuantities ="Quantity:\n";
            String totalCostMessage;
            Double totalCost = 0.0;
            String customerName = "Customer name: " +order.getCustomerName();
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


    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        int duration = Toast.LENGTH_SHORT;
        CharSequence text = "Select an order to view";
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }

    public void markCompleted(View view) {

        Spinner spinner = (Spinner) findViewById(R.id.incomingOrderSpinner);
        String orderID = (String)spinner.getSelectedItem();

        Database database = new Database();
        database.storeCompleteOrder(Integer.parseInt(orderID), this);

    }

    public void validateComplete(int result){

        Store userStore = Database.store;
        Spinner spinner = (Spinner) findViewById(R.id.incomingOrderSpinner);
        String orderID = (String)spinner.getSelectedItem();

        int duration = Toast.LENGTH_SHORT;
        CharSequence text = "Failed to fulfill order";
        Context context = getApplicationContext();
        if (userStore.getIncomingOrders() == null || userStore.getIncomingOrders().isEmpty()){
            text = "You have no incoming orders";
        }

        else if (result == 1) {
            text = "Successfully fulfilled order!";
            updateSpinner();
        }

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}