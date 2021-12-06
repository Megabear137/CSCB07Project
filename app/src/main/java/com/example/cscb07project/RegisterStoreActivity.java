package com.example.cscb07project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterStoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_store);

    }

    public void displayMessage(CharSequence text, int duration) {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void registerStore(View view) {
        //==== read from database to check if unique
        //==== write to database if unique else feedback message
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        EditText editText = findViewById(R.id.storeNameText);
        String storeName = editText.getText().toString();
        Pattern p = Pattern.compile("\\w+((\\s|-|')?\\w)*");
        Matcher nameMatcher = p.matcher(storeName);
        if (!nameMatcher.matches()) {
            int duration = Toast.LENGTH_SHORT;
            CharSequence text = "Store name must be alphanumeric!";
            displayMessage(text,duration);
        }
        else {
            Database database = new Database();
            database.addStore(storeName, username, this);
        }

    }

    void verifyRegisterStore(int result){

        Intent nextIntent = new Intent(this, StoreOwnerHomeActivity.class);
        if (result == 1) {
            nextIntent.putExtra("username",Database.user.getUsername());
            startActivity(nextIntent);
        }
        else {
            int duration = Toast.LENGTH_SHORT;
            CharSequence text = "Store name already exists!";
            displayMessage(text, duration);
        }

    }


}


