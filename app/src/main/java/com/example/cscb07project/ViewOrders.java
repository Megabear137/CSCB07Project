package com.example.cscb07project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

public class ViewOrders extends AppCompatActivity {

    Customer customer;

    TextView title;
    Button leftButton;
    Button rightButton;

    TextView pageNumber;
    Button next;
    Button previous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_orders);

        Database database = Database.getInstance();

        int page = 1;


        title = findViewById(R.id.viewOrdersTitles);
        leftButton = findViewById(R.id.viewOrdersLeftButton);
        rightButton = findViewById(R.id.viewOrdersRightButton);
        pageNumber = findViewById(R.id.viewOrdersPageNumber);
        next = findViewById(R.id.viewOrdersNextPageButton);
        previous = findViewById(R.id.viewOrdersPreviousPageButton);

        title.setText(R.string.shopping_cart);
        leftButton.setText(R.string.pending_orders);
        rightButton.setText(R.string.completed_orders);


    }


}