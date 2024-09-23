package com.example.finalproject_texi_milan;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Notification_activity extends AppCompatActivity {

    private static final String CHANNEL_ID = "ride_notifications";
    private static final int NOTIFICATION_ID_1 = 1;
    private static final int NOTIFICATION_ID_2 = 2;
    private static final int REQUEST_CODE_POST_NOTIFICATIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // Create the notification channel for Android 8.0+
        createNotificationChannel();

        // Check for notification permission (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_POST_NOTIFICATIONS);
            }
        }

        // Find the button and set the click listener
        Button bookRideButton = findViewById(R.id.bookRideButton);
        bookRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Send the first notification when the button is clicked
                sendFirstNotification();
            }
        });
    }

    // Create notification channel (required for Android 8.0+)
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Ride Notifications";
            String description = "Notification channel for ride booking updates";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Method to send the first notification
    private void sendFirstNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.tick) // Default icon
                .setContentTitle("Ride Booked!")
                .setContentText("Your Ride is Booked Successfully!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return; // Permission is not granted, so we cannot send the notification
        }

        notificationManager.notify(NOTIFICATION_ID_1, builder.build());

        // Delay the second notification by 5 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sendSecondNotification();
            }
        }, 5000);
    }

    // Method to send the second notification
    private void sendSecondNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.carbook) // Default icon
                .setContentTitle("Driver Assigned!")
                .setContentText("Your Driver is on the way to pick you up!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return; // Permission is not granted, so we cannot send the notification
        }

        notificationManager.notify(NOTIFICATION_ID_2, builder.build());
    }
}
