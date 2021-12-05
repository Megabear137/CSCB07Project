package com.example.cscb07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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

    TextView orderInfo;
    TextView customerInfo;
    TextView costInfo;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_owner_view_incoming);
        orderInfo = (TextView) findViewById(R.id.incomingOrderInfo);
        customerInfo = (TextView) findViewById(R.id.customerNameText);
        costInfo = (TextView) findViewById(R.id.incomingTotalCost);
        spinner = (Spinner) findViewById(R.id.incomingOrderSpinner);
        spinner.setOnItemSelectedListener(this);
        updateSpinner();
    }

    public void displayMessage(CharSequence text, int duration) {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void updateSpinner() {
        Store userStore = Database.store;
        if (!userStore.isIncomingOrdersEmpty()) {
            ArrayList<Order> orders = userStore.getIncomingOrders();
            ArrayList<String> allOrders = new ArrayList<>();
            for (Order order : orders) {
                allOrders.add(Integer.toString(order.getId()));
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, allOrders);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
        }
        else {
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item,getResources().
                    getStringArray(R.array.defaultOrderSpinnerValues));
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
            resetAllTextViews();
        }

    }

    public void resetAllTextViews() {
        orderInfo.setText(getString(R.string.incomingOrderInfo));
        customerInfo.setText("");
        costInfo.setText("");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
        Database database = new Database();
        String orderID = parent.getItemAtPosition(position).toString();
        if (!orderID.equals("You have no orders.")){
            Store userStore = Database.store;
            int intOrderID = Integer.parseInt(orderID);
            Order order = database.findIncomingOrder(intOrderID);
            HashMap<String, Integer> products =  order.getProducts();
            if (products != null) {
                String totalCostMessage;
                StringBuilder allInfo = new StringBuilder();
                double totalCost = 0.0;
                double subtotal;
                double currentPrice;
                String customerName = "Customer name: " + order.getCustomerName();
                for (Map.Entry<String, Integer> m : products.entrySet()) {
                    Product current = userStore.findProduct((String) m.getKey());
                    subtotal = current.getPrice() * m.getValue();
                    subtotal = Math.round(subtotal * 100.0) / 100.0;
                    currentPrice = current.getPrice();
                    currentPrice = Math.round(currentPrice * 100.0) / 100.0;
                    totalCost += subtotal;
                    allInfo.append("Name:       ");
                    allInfo.append(current.getName());
                    allInfo.append("\n\n");
                    allInfo.append("Brand:       ");
                    allInfo.append(current.getBrand());
                    allInfo.append("\n\n");
                    allInfo.append("Price:        $");
                    allInfo.append(currentPrice);
                    allInfo.append("\n\n");
                    allInfo.append("Quantity:   ");
                    allInfo.append(m.getValue());
                    allInfo.append("\n\n");
                    allInfo.append("Subtotal:   $");
                    allInfo.append(subtotal);
                    allInfo.append("\n\n");
                    allInfo.append("------------------------------------\n\n");
                }
                totalCost = Math.round(totalCost * 100.0) / 100.0;
                totalCostMessage = "Total cost: $" + totalCost;
                orderInfo.setText(allInfo.toString());
                orderInfo.setMovementMethod(new ScrollingMovementMethod());
                orderInfo.scrollTo(0,0);
                customerInfo.setText(customerName);
                costInfo.setText(totalCostMessage);
            }

        }


    }


    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        int duration = Toast.LENGTH_SHORT;
        CharSequence text = "Select an order to view";
        displayMessage(text,duration);

    }

    public void markCompleted(View view) {
        String orderID = (String)spinner.getSelectedItem();
        if (orderID != null && !orderID.isEmpty() && !orderID.equals("You have no orders.")){
            Database database = new Database();
            database.storeCompleteOrder(Integer.parseInt(orderID), this);
        }
        else {
            CharSequence text = "You have no incoming orders";
            int duration = Toast.LENGTH_SHORT;
            displayMessage(text,duration);
        }

    }

    public void validateComplete(int result){
        int duration = Toast.LENGTH_SHORT;
        CharSequence text = "Failed to fulfill order";
        if (result == 1) {
            text = "Successfully fulfilled order!";
            updateSpinner();
        }
        displayMessage(text,duration);
    }


}