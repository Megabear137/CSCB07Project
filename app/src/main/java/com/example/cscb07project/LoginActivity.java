package com.example.cscb07project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
        TextView textView = (TextView) findViewById(R.id.textView5);
        textView.setText(message);
    }

    //Will get the username from the username text box
    public String getUsername(){
        EditText editText = findViewById(R.id.editTextTextPersonName);
        return editText.getText().toString();
    }

    public void loginButton(View view){
        presenter.checkUsername();
        //[Zubair] Code for checking username should be moved to the userExists() method in the myModel class
        /*
        boolean value = true is the username is that of a store owner, false otherwise;
        if (value == false) {
            Intent intent = new Intent(this, CustomerUsage.class);
            startActivity(intent);
        }
        if (value == true) {
            Intent intent = new Intent(this, StoreOwnerUsage.class);
            startActivity(intent);
        }*/
    }


}