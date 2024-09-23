package com.example.finalproject_texi_milan;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class movingCar extends AppCompatActivity {

    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moving_car);
        VideoView vv = (VideoView) findViewById(R.id.videoView);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(vv);
        Uri videoUri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.movingcar);
        vv.setMediaController(mediaController);
        vv.setVideoURI(videoUri);
        vv.requestFocus();
        vv.start();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(movingCar.this,payment_activity.class);
                startActivity(i);
            }
        });
    }
}