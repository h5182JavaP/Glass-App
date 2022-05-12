package com.my.app.glassapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.my.app.glassapp.R;

public class SplashActivity extends AppCompatActivity {

    private boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sh = getSharedPreferences("LoginPref", MODE_APPEND);
        if (sh != null)
            isLogin = sh.getBoolean("login", false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isLogin) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, OrderListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        },1);

    }
}