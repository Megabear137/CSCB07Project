package com.example.cscb07project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        CharSequence text = "Store name already exists!";
        Toast toast = Toast.makeText(context, text, duration);

        EditText editText = (EditText) findViewById(R.id.storeNameText);
        TextView textView = findViewById(R.id.registerStoreFeedback);
        String storeName = editText.getText().toString();


        int result = database.addStore(storeName,username);

        Intent nextIntent = new Intent(this, StoreOwnerHomeActivity.class);
        Intent customerIntent = new Intent(this, CustomerUsageEntryScreen.class);
        Intent previousIntent = new Intent(this, SignupActivity.class);
        if (result == 1) {
            nextIntent.putExtra("username",username);
            startActivity(nextIntent);
        }
        else if (result == -1)
            toast.show();
        else if (result == -2)
            startActivity(nextIntent);
        else if (result == -3)
            startActivity(customerIntent);
        else
            startActivity(previousIntent);


    }


}


