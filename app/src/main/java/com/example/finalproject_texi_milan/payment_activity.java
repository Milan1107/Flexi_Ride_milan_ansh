package com.example.finalproject_texi_milan;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class payment_activity extends AppCompatActivity {

    private Spinner paymentMethodSpinner;
    private Button proceedPaymentButton;
    private EditText walletAmountEditText;
    private TextView walletAmountLabel;
    private String selectedPaymentMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        paymentMethodSpinner = findViewById(R.id.paymentMethodSpinner);
        proceedPaymentButton = findViewById(R.id.proceedPaymentButton);
        walletAmountEditText = findViewById(R.id.walletAmountEditText);
        walletAmountLabel = findViewById(R.id.walletAmountLabel);

        // Hide wallet amount options initially
        walletAmountEditText.setVisibility(View.GONE);
        walletAmountLabel.setVisibility(View.GONE);

        // Setup Spinner (Dropdown) options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.payment_methods, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        paymentMethodSpinner.setAdapter(adapter);

        // Listener for dropdown menu selection
        paymentMethodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedPaymentMethod = paymentMethodSpinner.getSelectedItem().toString();

                // Show or hide wallet amount option based on selected method
                if (selectedPaymentMethod.equals("Wallet")) {
                    walletAmountEditText.setVisibility(View.VISIBLE);
                    walletAmountLabel.setVisibility(View.VISIBLE);
                } else {
                    walletAmountEditText.setVisibility(View.GONE);
                    walletAmountLabel.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        // Handle payment button click
        proceedPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePayment();
            }
        });
    }

    // Handle the payment based on the selected method
    private void handlePayment() {
        switch (selectedPaymentMethod) {
            case "UPI (Google Pay)":
                launchGooglePay();
                break;
            case "Cash":
                Toast.makeText(this, "Please hand over cash to the driver", Toast.LENGTH_SHORT).show();
                break;
            case "Wallet":
                String amount = walletAmountEditText.getText().toString();
                if (!amount.isEmpty()) {
                    // Process wallet payment (mocked for now)
                    Toast.makeText(this, "Wallet payment of ₹" + amount + " successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
        }
    }

    // Redirect to Google Pay for UPI payment
    private void launchGooglePay() {
        Uri uri = Uri.parse("upi://pay?pa=yourupiid@okaxis&pn=FlexiRide&mc=0000&tid=02125412&tr=25568484&tn=Ride Payment&am=500&cu=INR");
        Intent gpayIntent = new Intent(Intent.ACTION_VIEW, uri);
        gpayIntent.setPackage("com.google.android.apps.nbu.paisa.user");

        try {
            startActivity(gpayIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Google Pay not installed on your device", Toast.LENGTH_SHORT).show();
 }
}
}