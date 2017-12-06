package com.palprotech.heylaapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.bean.database.SQLiteHelper;

/**
 * Created by Admin on 06-10-2017.
 */

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    SQLiteHelper database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        database = new SQLiteHelper(getApplicationContext());

        final int getStatus = database.appInfoCheck();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (getStatus == 1) {
                    Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}
