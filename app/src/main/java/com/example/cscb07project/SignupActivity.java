package com.example.cscb07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class SignupActivity extends AppCompatActivity implements Contract.View {

    private Contract.Presenter presenter; // This class will contain the presenter that will validate the login process
    private Contract.Model database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        presenter = new MyPresenter(this, new Database());
        Database.user = null;
    }

    public void moveToLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    // Will display a message if there is any error with the login (user not found, wrong password, etc.)
    public void displayMessage(String message){
        TextView textView = (TextView) findViewById(R.id.signupText);
        textView.setText(message);
    }

    //Will get the username from the username text box
    public String getUsername(){
        EditText editText = findViewById(R.id.signupUsername);
        return editText.getText().toString();
    }

    //Will get the password from the password text box
    public String getPassword(){
        EditText editText = findViewById(R.id.signupPassword);
        return editText.getText().toString();
    }

    public void signupButton(View view) {
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        String value = String.valueOf(spinner.getSelectedItem());
        presenter.checkSignup(value);
    }


    public void validateLogin(User user){

    }

    public void validateSignup(User user){
        if (!user.isStoreOwner) {

            Intent intent = new Intent(this, CustomerUsageEntryScreen.class);
            intent.putExtra("username", getUsername());
            startActivity(intent);
        }
        if (user.isStoreOwner) {

            Intent intent = new Intent(this, RegisterStoreActivity.class);
            intent.putExtra("username", getUsername());
            startActivity(intent);
        }
    }
}