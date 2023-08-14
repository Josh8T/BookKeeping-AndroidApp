package com.example.lab02;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class sign_up extends AppCompatActivity {
    EditText inputUsername, inputPassword;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        inputUsername = findViewById(R.id.edt_regis_user);
        inputPassword = findViewById(R.id.edt_regis_pass);
    }

    public void registerInfo(View v){
        String user = inputUsername.getText().toString();
        String pass = inputPassword.getText().toString();

        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Username and Password is required!", Toast.LENGTH_SHORT).show();
        }
        else{
            SharedPreferences myData = getSharedPreferences("f2",0);
            SharedPreferences.Editor myEditor = myData.edit();
            myEditor.putString("savedUsername", user);
            myEditor.putString("savedPassword", pass);
            myEditor.commit();

            Toast.makeText(this, "Sign Up Confirmed", Toast.LENGTH_SHORT).show();
        }



    }
}