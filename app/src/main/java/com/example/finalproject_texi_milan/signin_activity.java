package com.example.finalproject_texi_milan;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class signin_activity extends AppCompatActivity {

    DatabaseHelper myDb;
    EditText fullname, email, password;
    ImageView arrowButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        // Initialize DatabaseHelper and UI elements
        myDb = new DatabaseHelper(this);
        fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        arrowButton = findViewById(R.id.login_button);

        // Set click listener for arrow button
        arrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = fullname.getText().toString().trim();
                String userEmail = email.getText().toString().trim();
                String userPassword = password.getText().toString().trim();

                // Check if any field is empty
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword)) {
                    Toast.makeText(signin_activity.this, "Enter feilds correctly", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Insert user data into the database
                boolean isInserted = myDb.insertUser(name, userEmail, userPassword);

                if (isInserted) {
                    Toast.makeText(signin_activity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                    // Navigate to login activity
                    finish();  // Close the activity after successful sign-up
                } else {
                    Toast.makeText(signin_activity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
