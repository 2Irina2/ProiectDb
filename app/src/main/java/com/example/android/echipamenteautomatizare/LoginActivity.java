package com.example.android.echipamenteautomatizare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final String client = "client";
        final String clientP = "clientP";
        final String admin = "admin";
        final String adminP = "adminP";

        Button submitButton = findViewById(R.id.login_submit);
        final EditText usernameEditText = findViewById(R.id.login_username);
        final EditText passwordEditText = findViewById(R.id.login_password);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean credentials = true;
                if(usernameEditText.getText().toString().isEmpty()){
                    usernameEditText.setError("Please insert username");
                    credentials = false;
                }
                if(passwordEditText.getText().toString().isEmpty()){
                    passwordEditText.setError("Please insert password");
                    credentials = false;
                }

                if(credentials){
                    if(usernameEditText.getText().toString().equals(admin) && passwordEditText.getText().toString().equals(adminP)){
                        Intent adminIntent = new Intent(getApplicationContext(), AdminActivity.class);
                        startActivity(adminIntent);
                    } else if(usernameEditText.getText().toString().equals(client) && passwordEditText.getText().toString().equals(clientP)){
                        Intent clientIntent = new Intent(getApplicationContext(), ClientActivity.class);
                        startActivity(clientIntent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Unknown credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
