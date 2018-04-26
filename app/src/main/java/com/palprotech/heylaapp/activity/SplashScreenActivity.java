package com.palprotech.heylaapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.bean.database.SQLiteHelper;
import com.palprotech.heylaapp.fcm.MyFirebaseInstanceIDService;
import com.palprotech.heylaapp.utils.PreferenceStorage;

/**
 * Created by Admin on 06-10-2017.
 */

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    SQLiteHelper database;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        database = new SQLiteHelper(getApplicationContext());

        final int getStatus = database.appInfoCheck();
        String GCMKey = PreferenceStorage.getGCM(getApplicationContext());
        if (GCMKey.equalsIgnoreCase("")) {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            PreferenceStorage.saveGCM(getApplicationContext(), refreshedToken);
        }

        /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_DENIED) {

                *//*Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.READ_PHONE_STATE};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);*//*

                *//*SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                sharedPreferences.edit().clear().apply();*//*

                Intent i = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
                startActivity(i);
                finish();

            } else {*/

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
//            }
//        }
    }
}
