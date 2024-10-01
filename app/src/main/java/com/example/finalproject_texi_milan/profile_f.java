package com.example.finalproject_texi_milan;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class profile_f extends Fragment {

    TextView displayName, displayUsername, displayPassword, displayEmail;
    DBHelper dbHelper;

    public profile_f() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_f, container, false);

        // Initialize TextViews
        displayName = view.findViewById(R.id.displayName);
        displayUsername = view.findViewById(R.id.displayUsername);
        displayPassword = view.findViewById(R.id.displayPassword);
        displayEmail = view.findViewById(R.id.displayEmail);

        // Initialize the database helper
        dbHelper = new DBHelper(getContext());

        // Fetch the user profile from the database and set it to the TextViews
        loadUserProfile();

        return view;
    }

    private void loadUserProfile() {
        // Get user data from the database
        Cursor cursor = dbHelper.getUserProfile();

        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
            String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));

            // Set the user profile data to TextViews
            displayName.setText(name);
            displayUsername.setText(username);
            displayPassword.setText(password);
            displayEmail.setText(email);
        }

        cursor.close();
    }
}
