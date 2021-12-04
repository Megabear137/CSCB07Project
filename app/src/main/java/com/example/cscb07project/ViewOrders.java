package com.example.cscb07project;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewOrders extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Customer customer;

    TextView title;
    Button leftButton;
    Button rightButton;

    Spinner orderSpinner;
    Spinner productSpinner;

    String current;
    int cartSize;
    int pendingOrdersSize;
    int completedOrdersSize;

    int currentQuantity;
    float productPrice;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

        Database database = new Database();

        Intent intent = getIntent();
        String customerName = intent.getStringExtra("username");
        customer = (Customer)database.user;

        if(customer.cart == null) cartSize = 0;
        else cartSize = customer.cart.size();

        if(customer.pendingOrders == null) pendingOrdersSize = 0;
        else pendingOrdersSize = customer.pendingOrders.size();

        if(customer.completedOrders == null) completedOrdersSize = 0;
        else completedOrdersSize = customer.completedOrders.size();

        current = "Shopping Cart";

        title = findViewById(R.id.viewOrdersTitles);
        leftButton = findViewById(R.id.viewOrdersLeftButton);
        rightButton = findViewById(R.id.viewOrdersRightButton);

        title.setText(R.string.shopping_cart);
        leftButton.setText(R.string.pending_orders);
        rightButton.setText(R.string.completed_orders);

        updateOrderSpinner();
    }

    public void updateOrderSpinner(){
        orderSpinner = findViewById(R.id.viewOrdersSpinner);
        orderSpinner.setOnItemSelectedListener(this);

        ArrayList<Order> orders = new ArrayList<>();
        ArrayList<String> spinnerOrders = new ArrayList<>();

        if(current.equals("Shopping Cart")) orders = customer.cart;
        else if(current.equals("Pending Orders")) orders = customer.pendingOrders;
        else if(current.equals("Completed Orders")) orders = customer.completedOrders;

        for (Order order: orders) {
            spinnerOrders.add(order.toString());
        }

        if(spinnerOrders.size() == 0){
            spinnerOrders.add("No Orders Found");
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, spinnerOrders);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderSpinner.setAdapter(dataAdapter);

        updateProductSpinner();
    }

    public void updateProductSpinner(){

        productSpinner = findViewById(R.id.viewOrdersProductsSpinner);
        productSpinner.setOnItemSelectedListener(this);

        ArrayList<String> spinnerProducts = new ArrayList<>();

        String orderName = orderSpinner.getSelectedItem().toString();
        if(orderName.equals("No Orders Found")){
            spinnerProducts.add("Please Choose an Order");
        }
        else{
            int orderID = Integer.parseInt(orderName.charAt(orderName.length() - 1) + "");
            ArrayList<Order> orders = new ArrayList<>();

            if(current.equals("Shopping Cart")) orders = customer.cart;
            else if(current.equals("Pending Orders")) orders = customer.pendingOrders;
            else if(current.equals("Completed Orders")) orders = customer.completedOrders;

            for(Order order: orders){
                if(order.id == orderID){
                    for(String productName: order.products.keySet()){
                        spinnerProducts.add(productName + " x" + order.products.get(productName));
                    }
                }
            }

        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, spinnerProducts);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productSpinner.setAdapter(dataAdapter);

    }

    public void Back(View view){
        Intent intent = new Intent(this, CustomerUsageEntryScreen.class);
        intent.putExtra("username", customer.getUsername());
        startActivity(intent);
    }

    public void rightButton(View view){
        if(current.equals("Shopping Cart")){
            title.setText(rightButton.getText());
            current = (String) rightButton.getText();
            rightButton.setText("Shopping Cart");
        }
        else if(current.equals("Completed Orders")){
            title.setText(rightButton.getText());
            current = (String) rightButton.getText();
            rightButton.setText("Completed Orders");
        }
        else if(current.equals("Pending Orders")){
            title.setText(rightButton.getText());
            current = (String) rightButton.getText();
            rightButton.setText("Pending Orders");
        }
        updateOrderSpinner();
    }

    public void leftButton(View view){
        if(current.equals("Shopping Cart")){
            title.setText(leftButton.getText());
            current = (String) leftButton.getText();
            leftButton.setText("Shopping Cart");
        }
        else if(current.equals("Completed Orders")){
            title.setText(leftButton.getText());
            current = (String) leftButton.getText();
            leftButton.setText("Completed Orders");
        }
        else if(current.equals("Pending Orders")){
            title.setText(leftButton.getText());
            current = (String) leftButton.getText();
            leftButton.setText("Pending Orders");
        }
        updateOrderSpinner();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}