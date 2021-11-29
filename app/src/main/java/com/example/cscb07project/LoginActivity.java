package com.example.cscb07project;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements Contract.View{

    private Contract.Presenter presenter; // This class will contain the presenter that will validate the login process

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        presenter = new MyPresenter(new MyModel(), this);
    }

    public void moveToSignup(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    // Will display a message if there is any error with the login (user not found, wrong password, etc.)
    public void displayMessage(String message){
        TextView textView = (TextView) findViewById(R.id.loginText);
        textView.setText(message);
    }

    //Will get the username from the username text box
    public String getUsername(){
        EditText editText = findViewById(R.id.loginUsername);
        return editText.getText().toString();
    }

    //Will get the password from the password text box
    public String getPassword(){
        EditText editText = findViewById(R.id.loginPassword);
        return editText.getText().toString();
    }

    public void loginButton(View view) {

        /*if (presenter.checkLogin() == true) {
            if (presenter.checkCustomer() == true) {
                //Intent intent = new Intent(this, CustomerUsage.class);
                //startActivity(intent);
            }
            else {
                //Intent intent = new Intent(this, StoreOwnerUsage.class);
                //startActivity(intent);
            }
        }

         */

        Intent intent = new Intent(this, StoreOwnerHomeActivity.class);
        startActivity(intent);



    }


}