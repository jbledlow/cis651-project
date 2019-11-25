package com.example.mainproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView imageView = findViewById(R.id.splash_logo);
        animateLogo(imageView);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        /*
        final Handler handler = new Handler();
        final Intent intent = new Intent(this, MainActivity.class);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
            }
        },1000);*/

    }

    public void animateLogo (ImageView view) {
        view.setAlpha(0F);
        view.animate().setDuration(2000).alpha(1F);
    }
}
