package com.palprotech.heylaapp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Base64;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.bean.database.SQLiteHelper;
import com.palprotech.heylaapp.paytm.MainActivityPost;
import com.palprotech.heylaapp.utils.PreferenceStorage;

/**
 * Created by Admin on 06-10-2017.
 */

public class SplashScreenActivity extends Activity {

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
//            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(SplashScreenActivity.this, new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    String newToken = instanceIdResult.getToken();
                    Log.e("newToken", newToken);
                    PreferenceStorage.saveGCM(getApplicationContext(), newToken);
                }
            });
//            PreferenceStorage.saveGCM(getApplicationContext(), refreshedToken);
        }

        if(!checkAutoDT(this)){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            // Setting Dialog Title
            alertDialog.setTitle("Date and Time settings");

            // Setting Dialog Message
            alertDialog.setMessage("Automatic Date and Time is not enabled. Go to settings and enable to access application.");

            // On pressing the Settings button.
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
                    startActivity(intent);
                }
            });

            // On pressing the cancel button
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        } else {
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

    public void hashFromSHA1(String sha1) {
        String[] arr = sha1.split(":");
        byte[] byteArr = new  byte[arr.length];

        for (int i = 0; i< arr.length; i++) {
            byteArr[i] = Integer.decode("0x" + arr[i]).byteValue();
        }

        Log.e("hash : ", Base64.encodeToString(byteArr, Base64.NO_WRAP));
    }

    public static boolean checkAutoDT(Context c){
        return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
    }
}
