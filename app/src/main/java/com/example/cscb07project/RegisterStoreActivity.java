package com.example.cscb07project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterStoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_store);

    }


    public void registerStore(View view) {
        //==== read from database to check if unique
        //==== write to database if unique else feedback message
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        Database database = Database.getInstance();

        EditText editText = (EditText) findViewById(R.id.storeNameText);
        TextView textView = findViewById(R.id.registerStoreFeedback);
        String storeName = editText.getText().toString();

        int result = database.addStore(storeName, username);

        Intent nextIntent = new Intent(this, StoreOwnerHomeActivity.class);
        Intent customerIntent = new Intent(this, CustomerUsageEntryScreen.class);
        Intent previousIntent = new Intent(this, SignupActivity.class);
        if (result == 1) {
            startActivity(nextIntent);
        }
        else if (result == -1)
            textView.setText("This store name is already taken. Enter a different store name.");
        else if (result == -2)
            startActivity(nextIntent);
        else if (result == -3)
            startActivity(customerIntent);
        else
            startActivity(previousIntent);


    }


}


