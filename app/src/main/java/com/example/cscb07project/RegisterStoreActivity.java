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


    public void registerStore (View view) {
        //==== read from database to check if unique
        //==== write to database if unique else feedback message
        EditText editText = (EditText) findViewById(R.id.storeNameText);

       /* String storeName = editText.getText().toString();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Stores");
        ref.child("0").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("demo", "Error getting data", task.getException());
                }
                else {
                    Log.i("demo", task.getResult().getValue().toString());
                    String [] tokens = task.getResult().getValue().toString().split(", ");
                    int storeNameLen = tokens[tokens.length- 1].length();
                    String dataBaseStoreName = tokens[tokens.length- 1].substring(5, storeNameLen-1);
                    if(storeName.equals(dataBaseStoreName))
                        Log.i("demo","A");

                    Log.i("demo",storeName);
                }
            }
        });
    */
        //=== Successfully registered
        Intent intent = new Intent(this, StoreOwnerHomeActivity.class);
        startActivity(intent);







    }











}