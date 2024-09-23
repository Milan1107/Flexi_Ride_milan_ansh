package com.example.finalproject_texi_milan;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class password_reset extends AppCompatActivity {

    DatabaseHelper myDb;
    EditText emailInput, passwordNew, passwordConfirm;
    ImageView resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        myDb = new DatabaseHelper(this);

        // Initialize UI components
        emailInput = findViewById(R.id.email);
        passwordNew = findViewById(R.id.password_new);
        passwordConfirm = findViewById(R.id.password_confirm);
        resetButton = findViewById(R.id.login_button);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                String newPassword = passwordNew.getText().toString();
                String confirmPassword = passwordConfirm.getText().toString();

                if (email.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(password_reset.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if the provided email exists in the database
                boolean emailExists = myDb.checkEmailExists(email);

                if (!emailExists) {
                    Toast.makeText(password_reset.this, "Email is not registered", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (newPassword.equals(confirmPassword)) {
                    // Update the password in the database for the entered email
                    boolean isUpdated = myDb.updatePasswordByEmail(email, newPassword);

                    if (isUpdated) {
                        Toast.makeText(password_reset.this, "Password reset successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close activity after successful password reset
                    } else {
                        Toast.makeText(password_reset.this, "Error updating password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(password_reset.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
