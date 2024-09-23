package com.example.finalproject_texi_milan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class login_activity extends AppCompatActivity {

    DatabaseHelper myDb;
    EditText user_email, password;
    ImageView loginButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize DatabaseHelper and UI elements
        myDb = new DatabaseHelper(this);
        user_email = findViewById(R.id.user_email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);

        // Set click listener for login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = user_email.getText().toString();
                String userPassword = password.getText().toString();

                // Check if user exists in the database
                boolean isValidUser = myDb.checkUser(email, userPassword);

                if (isValidUser) {
                    Toast.makeText(login_activity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    // Navigate to the next activity (e.g., MainActivity)
                    Intent intent = new Intent(login_activity.this, MainActivity3_bottom.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(login_activity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to handle the "Register" text view click
    public void openSignUpActivity(View view) {
        Intent intent = new Intent(login_activity.this, signin_activity.class);
        startActivity(intent);
    }

    public void openPasswordResetActivity(View view) {
        Intent intent = new Intent(login_activity.this, password_reset.class);
        startActivity(intent);
    }
}
