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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class StoreOwnerViewFulfilledActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_owner_view_fulfilled);
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
        ArrayList<Order> orders = userStore.getIncomingOrders();
        ArrayList<String> allOrders= new ArrayList<String>();
        for (Order order: orders) {
           allOrders.add(Integer.toString(order.getID())) //============== Implement getID, and id fields
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, allOrders);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        //====
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
        Database database = new Database();
        Intent i = getIntent();
        String username = i.getStringExtra("username");
        String orderName = parent.getItemAtPosition(position).toString();
        StoreOwner user = (StoreOwner) Database.user;
        Store userStore = Database.store;

        TextView orderInfo = (TextView) findViewById(R.id.fulfilledOrderInfo);
        orderInfo.setText()








        int duration = Toast.LENGTH_SHORT;
        CharSequence text = "Selected: " + orderName;
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





}