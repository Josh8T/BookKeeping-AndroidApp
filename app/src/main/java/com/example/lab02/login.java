package com.example.lab02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class login extends AppCompatActivity {

    EditText username, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.edt_username);
        password = findViewById(R.id.edt_password);

    }

    public void navigate_main(View v){
        String strUsername = username.getText().toString();
        String strPassword = password.getText().toString();
        if (strUsername.isEmpty() || strPassword.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Username and Password is required!", Toast.LENGTH_SHORT).show();
            return;
        }

        // get the registered username and password to check
        SharedPreferences myData = getSharedPreferences("f2",0);
        String dataUser = myData.getString("savedUsername","");
        String dataPass = myData.getString("savedPassword","");

        if (dataUser.equals(strUsername) && dataPass.equals(strPassword)) {
            // successful login take to next activity
            Intent main = new Intent(getApplicationContext(), MainActivity.class);

            // start the user details activity using startActivity()
            startActivity(main);

            // to end the current activity
            finish();

        } else {
            // invalid login, display toast message
            Toast.makeText(getApplicationContext(), "Invalid Username and Password!", Toast.LENGTH_SHORT).show();
        }

    }

    public void navigate_sign_up(View v){
        Intent signUp = new Intent(getApplicationContext(), sign_up.class);
        startActivity(signUp);
    }
}