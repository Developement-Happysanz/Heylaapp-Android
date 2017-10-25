package com.palprotech.heylaapp.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.fcm.SharedPrefManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        final String token = SharedPrefManager.getInstance(this).getDeviceToken();
//        String ok = token;

    }
}
