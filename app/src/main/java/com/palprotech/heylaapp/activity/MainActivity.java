package com.palprotech.heylaapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.fcm.SharedPrefManager;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        final String token = SharedPrefManager.getInstance(this).getDeviceToken();
//
//        String ok = token;

    }
}
