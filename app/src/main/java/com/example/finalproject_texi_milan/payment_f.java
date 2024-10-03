package com.example.finalproject_texi_milan;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.*;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class payment_f extends Fragment {
    private Spinner paymentMethodSpinner;
    private Button proceedPaymentButton;
    private EditText walletAmountEditText;
    private TextView walletAmountLabel;
    private String selectedPaymentMethod;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_f, container, false);
        paymentMethodSpinner = view.findViewById(R.id.paymentMethodSpinner);
        proceedPaymentButton = view.findViewById(R.id.proceedPaymentButton);
        walletAmountEditText = view.findViewById(R.id.walletAmountEditText);
        walletAmountLabel = view.findViewById(R.id.walletAmountLabel);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.payment_options, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        paymentMethodSpinner.setAdapter(adapter);

        // Hide wallet amount options initially
        walletAmountEditText.setVisibility(View.GONE);
        walletAmountLabel.setVisibility(View.GONE);
        paymentMethodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedPaymentMethod = paymentMethodSpinner.getSelectedItem().toString();
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
        return view;
    }

    // Handle the payment based on the selected method
    private void handlePayment() {
        switch (selectedPaymentMethod) {
            case "UPI (Google Pay)":
                launchGooglePay();
                break;
            case "Cash":
                Toast.makeText(getContext(), "Please hand over cash to the driver", Toast.LENGTH_SHORT).show();
                break;
            case "Wallet":
                String amount = walletAmountEditText.getText().toString();
                if (!amount.isEmpty()) {
                    // Process wallet payment (mocked for now)
                    Toast.makeText(getContext(), "Wallet payment of ₹" + amount + " successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Please enter an amount", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                Toast.makeText(getContext(), "Please select a payment method", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), "Google Pay not installed on your device", Toast.LENGTH_SHORT).show();
        }
    }


}