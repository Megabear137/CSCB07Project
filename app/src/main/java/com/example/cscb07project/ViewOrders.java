package com.example.cscb07project;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

        Database database = new Database();


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

        customer = (Customer)Database.user;

        orderSpinner = findViewById(R.id.viewOrdersSpinner);
        orderSpinner.setOnItemSelectedListener(this);

        ArrayList<Order> orders = new ArrayList<>();
        ArrayList<String> spinnerOrders = new ArrayList<>();

        if(current.equals("Shopping Cart")){
            orders = customer.cart;
            findViewById(R.id.viewOrdersGoToStore).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.viewOrdersGoToStore)).setText("Go to Store");
            ((TextView)findViewById(R.id.viewOrdersChooseOrder)).setText("Choose an Order");
            ((TextView)findViewById(R.id.viewOrdersMakeOrder)).setText("Make Order");
            ((TextView)findViewById(R.id.viewOrdersMakeOrder)).setVisibility(View.VISIBLE);
        }
        else if(current.equals("Pending Orders")){
            findViewById(R.id.viewOrdersGoToStore).setVisibility(View.VISIBLE);
            orders = customer.pendingOrders;
            ((TextView)findViewById(R.id.viewOrdersGoToStore)).setText("Go to Store");
            ((TextView)findViewById(R.id.viewOrdersChooseOrder)).setText("View a Pending Order");
            ((TextView)findViewById(R.id.viewOrdersMakeOrder)).setVisibility(View.GONE);
        }
        else if(current.equals("Completed Orders")){
            orders = customer.completedOrders;
            findViewById(R.id.viewOrdersGoToStore).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.viewOrdersGoToStore)).setText("Delete Order");
            ((TextView)findViewById(R.id.viewOrdersChooseOrder)).setText("View a Past Order");
            ((TextView)findViewById(R.id.viewOrdersMakeOrder)).setText("Delete All Orders");
            ((TextView)findViewById(R.id.viewOrdersMakeOrder)).setVisibility(View.VISIBLE);
        }

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

        Intent intent = getIntent();
        if(intent.hasExtra("store")){
            int position = 0;
            for(Order order: orders){
                if(order.getStoreName().equals(intent.getStringExtra("store"))) break;
                position++;
            }
            orderSpinner.setSelection(position);
        }

        updateProductSpinner();
    }

    public void updateProductSpinner(){

        productSpinner = findViewById(R.id.viewOrdersProductsSpinner);
        productSpinner.setOnItemSelectedListener(this);

        ArrayList<String> spinnerProducts = new ArrayList<>();

        String orderName = orderSpinner.getSelectedItem().toString();
        if(orderName.equals("No Orders Found")){
            spinnerProducts.add("Please Choose an Order");
            findViewById(R.id.viewOrdersEditQuantity).setVisibility(View.GONE);
            findViewById(R.id.viewOrdersQuantity).setVisibility(View.GONE);
            findViewById(R.id.viewOrdersChange).setVisibility(View.GONE);
        }
        else{

            if(current.equals("Shopping Cart")){
                findViewById(R.id.viewOrdersEditQuantity).setVisibility(View.VISIBLE);
                findViewById(R.id.viewOrdersQuantity).setVisibility(View.VISIBLE);
                findViewById(R.id.viewOrdersChange).setVisibility(View.VISIBLE);
            }
            else{
                findViewById(R.id.viewOrdersEditQuantity).setVisibility(View.GONE);
                findViewById(R.id.viewOrdersQuantity).setVisibility(View.GONE);
                findViewById(R.id.viewOrdersChange).setVisibility(View.GONE);
            }


            String orderIDString = orderName.substring(orderName.lastIndexOf(" ")).trim();
            int orderID = Integer.parseInt(orderIDString + "");
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

        updateTotals();

    }

    void updateTotals(){
        TextView productTotalView = findViewById(R.id.viewOrdersProductPrice);
        TextView subtotalView = findViewById(R.id.viewOrdersSubtotalPrice);
        TextView grandTotalView = findViewById(R.id.viewOrdersGrandTotalPrice);

        if(!orderSpinner.getSelectedItem().toString().equals("No Orders Found")){
            String productName = productSpinner.getSelectedItem().toString();
            productName = productName.substring(0, productName.lastIndexOf("x")).trim();

            String orderIDString = orderSpinner.getSelectedItem().toString();
            orderIDString = orderIDString.substring(orderIDString.lastIndexOf(" ")).trim();
            int orderID = Integer.parseInt(orderIDString);

            ArrayList<Order> orders = new ArrayList<>();
            Order order = null;

            if(current.equals("Shopping Cart")) orders = customer.cart;
            else if(current.equals("Pending Orders")) orders = customer.pendingOrders;
            else orders = customer.completedOrders;

            for(Order o: orders){
                if(o.id == orderID){
                    order = o;
                    break;
                }
            }

            Database d1 = new Database();
            Product product = d1.findProductInStore(productName, order.getStoreName());

            while(product == null){
                product = d1.findProductInStore(productName, order.getStoreName());
            }

            double productTotal = product.getPrice();
            double subtotal = productTotal * order.products.get(productName);
            double grandTotal = order.calculateTotal();

            java.text.DecimalFormat df =new java.text.DecimalFormat("#.00");
            String s1 = df.format(productTotal);
            String s2 = df.format(subtotal);
            String s3 = df.format(grandTotal);

            productTotalView.setText(s1);
            subtotalView.setText(s2);
            grandTotalView.setText(s3);
        }
        else{
            productTotalView.setText("");
            subtotalView.setText("");
            grandTotalView.setText("");
        }



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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void changeAmount(View view){

        Database d1 = new Database();

        if (((TextView) findViewById(R.id.viewOrdersQuantity)).getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter an amount first.", Toast.LENGTH_SHORT).show();
        } else {
            int quantity = Integer.parseInt(((TextView) findViewById(R.id.viewOrdersQuantity)).getText().toString());

            String productName = productSpinner.getSelectedItem().toString();
            productName = productName.substring(0, productName.lastIndexOf("x")).trim();

            String orderIDString = orderSpinner.getSelectedItem().toString();
            orderIDString = orderIDString.substring(orderIDString.lastIndexOf(" ")).trim();
            int orderID = Integer.parseInt(orderIDString);

            Log.i("bruh", orderID + " " + productName);

            for (Order order : customer.cart) {
                if (order.id == orderID) {
                    for (String name : order.products.keySet()) {
                        if (name.equals(productName)) {
                            order.products.replace(name, quantity);
                            if (order.products.get(name) == 0)
                                d1.deleteProductFromCart(name, order.getStoreName());
                            d1.updateDatabase();
                            updateOrderSpinner();
                        }
                    }
                }
            }
        }
    }


    public void makeOrder(View view) {

        if(current.equals("Shopping Cart")){
            if(!orderSpinner.getSelectedItem().toString().equals("No Orders Found")){
                String orderIDString = orderSpinner.getSelectedItem().toString();
                orderIDString = orderIDString.substring(orderIDString.lastIndexOf(" ")).trim();
                int orderID = Integer.parseInt(orderIDString);

                for(Order order: customer.cart){
                    if(order.id == orderID){
                        Database database = new Database();
                        database.makeOrder(order.getStoreName());
                    }
                }

                updateOrderSpinner();
                Toast.makeText(getApplicationContext(), "Order has been successfully made.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Please choose an order first.", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Database database = new Database();
            database.customerDeleteFullHistory();
            Toast.makeText(getApplicationContext(), "Order history has been cleared.", Toast.LENGTH_SHORT).show();
            updateOrderSpinner();
        }


    }

    public void goToStore(View view){

        if(!orderSpinner.getSelectedItem().toString().equals("No Orders Found")) {
            if (current.equals("Shopping Cart") || current.equals("Pending Orders")) {


                String storeName = "";

                String orderIDString = orderSpinner.getSelectedItem().toString();
                orderIDString = orderIDString.substring(orderIDString.lastIndexOf(" ")).trim();
                int orderID = Integer.parseInt(orderIDString);

                ArrayList<Order> orders;

                if (current.equals("Shopping Cart")) orders = customer.cart;
                else orders = customer.pendingOrders;

                for (Order order : orders) {
                    if (order.id == orderID) {
                        storeName = order.getStoreName();
                    }
                }

                Intent intent = new Intent(this, ViewStoreActivity.class);
                intent.putExtra("store", storeName);
                startActivity(intent);
            }
            else {

                String orderIDString = orderSpinner.getSelectedItem().toString();
                orderIDString = orderIDString.substring(orderIDString.lastIndexOf(" ")).trim();
                int orderID = Integer.parseInt(orderIDString);

                Order orderToRemove = null;

                for (Order order : customer.completedOrders) {
                    if (order.id == orderID) {
                        orderToRemove = order;
                    }
                }

                Database d1 = new Database();
                if (d1.customerDeleteHistory(orderToRemove) == 1) {
                    Toast.makeText(getApplicationContext(), "Order successfully deleted from history.", Toast.LENGTH_SHORT).show();
                    updateOrderSpinner();
                } else {
                    Toast.makeText(getApplicationContext(), "Order could not be deleted. Please navigate away from page then return and try again.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "Please choose an order first.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if(((TextView) adapterView.getChildAt(0)) != null){
            ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
            ((TextView) adapterView.getChildAt(0)).setTextSize(20);
        }
        if(((TextView) adapterView.getChildAt(1)) != null){
            ((TextView) adapterView.getChildAt(1)).setTextColor(Color.WHITE);
            ((TextView) adapterView.getChildAt(1)).setTextSize(20);
        }

        updateProductSpinner();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}