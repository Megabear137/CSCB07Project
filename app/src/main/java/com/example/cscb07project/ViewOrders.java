package com.example.cscb07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ViewOrders extends AppCompatActivity {

    Customer customer;

    TextView title;
    Button leftButton;
    Button rightButton;

    TextView pageNumber;
    Button next;
    Button previous;

    ArrayList<FloatingActionButton> confirmButtons;
    ArrayList<FloatingActionButton> deleteButtons;
    ArrayList<TextView> storeNames;
    ArrayList<TextView> productNames;
    ArrayList<TextView> orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

        Database database = Database.getInstance();

        Intent intent = getIntent();
        String customerName = intent.getStringExtra("username");
        customer = (Customer)database.user;

        int cartSize;
        if(customer.cart == null) cartSize = 0;
        else cartSize = customer.cart.size();

        int page = 1;
        int maxPage = (int) Math.ceil(cartSize / 5.0);

        title = findViewById(R.id.viewOrdersTitles);
        leftButton = findViewById(R.id.viewOrdersLeftButton);
        rightButton = findViewById(R.id.viewOrdersRightButton);
        pageNumber = findViewById(R.id.viewOrdersPageNumber);
        next = findViewById(R.id.viewOrdersNextPageButton);
        previous = findViewById(R.id.viewOrdersPreviousPageButton);

        title.setText(R.string.shopping_cart);
        leftButton.setText(R.string.pending_orders);
        rightButton.setText(R.string.completed_orders);

        confirmButtons = new ArrayList<>();
        deleteButtons = new ArrayList<>();
        storeNames = new ArrayList<>();
        productNames = new ArrayList<>();
        orders = new ArrayList<>();

        confirmButtons.add(findViewById(R.id.viewOrdersConfirm0));
        confirmButtons.add(findViewById(R.id.viewOrdersConfirm1));
        confirmButtons.add(findViewById(R.id.viewOrdersConfirm2));
        confirmButtons.add(findViewById(R.id.viewOrdersConfirm3));
        confirmButtons.add(findViewById(R.id.viewOrdersConfirm4));

        deleteButtons.add(findViewById(R.id.orderX0));
        deleteButtons.add(findViewById(R.id.orderX1));
        deleteButtons.add(findViewById(R.id.orderX2));
        deleteButtons.add(findViewById(R.id.orderX3));
        deleteButtons.add(findViewById(R.id.orderX4));

        storeNames.add(findViewById(R.id.orderStoreName0));
        storeNames.add(findViewById(R.id.orderStoreName1));
        storeNames.add(findViewById(R.id.orderStoreName2));
        storeNames.add(findViewById(R.id.orderStoreName3));
        storeNames.add(findViewById(R.id.orderStoreName4));

        productNames.add(findViewById(R.id.orderProducts0));
        productNames.add(findViewById(R.id.orderProducts1));
        productNames.add(findViewById(R.id.orderProducts2));
        productNames.add(findViewById(R.id.orderProducts3));
        productNames.add(findViewById(R.id.orderProducts4));

        orders.add(findViewById(R.id.orderBackground0));
        orders.add(findViewById(R.id.orderBackground1));
        orders.add(findViewById(R.id.orderBackground2));
        orders.add(findViewById(R.id.orderBackground3));
        orders.add(findViewById(R.id.orderBackground4));


        for(int i = 0; i < 5; i++){
            orders.get(i).setVisibility(View.GONE);
            productNames.get(i).setVisibility(View.GONE);
            storeNames.get(i).setVisibility(View.GONE);
            confirmButtons.get(i).setVisibility(View.GONE);
            deleteButtons.get(i).setVisibility(View.GONE);
        }



        int ordersOnPage = Math.min(cartSize, 5);


        for(int i = 0; i < ordersOnPage; i++){
            Order order = customer.getCart().get(i);
            orders.get(i).setVisibility(View.VISIBLE);

            productNames.get(i).setVisibility(View.VISIBLE);
            productNames.get(i).setText("Products: " + order.toString());

            storeNames.get(i).setVisibility(View.VISIBLE);
            storeNames.get(i).setText("Store: " + order.getStoreName());

            confirmButtons.get(i).setVisibility(View.VISIBLE);
            deleteButtons.get(i).setVisibility(View.VISIBLE);
        }


    }

    public void Back(View view){
        Intent intent = new Intent(this, CustomerUsageEntryScreen.class);
        intent.putExtra("username", customer.getUsername());
        startActivity(intent);
    }

    public void showCart(View view){
        int ordersOnPage;
        for(Order order: customer.cart){

        }
    }


}