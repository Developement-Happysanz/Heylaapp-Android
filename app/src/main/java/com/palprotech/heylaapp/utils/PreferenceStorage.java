package com.palprotech.heylaapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Admin on 11-10-2017.
 */

public class PreferenceStorage {

    /*To check welcome screen to launch*/
    public static void setFirstTimeLaunch(Context context, boolean isFirstTime) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(HeylaAppConstants.IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.apply();
    }

    public static boolean isFirstTimeLaunch(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(HeylaAppConstants.IS_FIRST_TIME_LAUNCH, true);
    }

    /*To save FCM key locally*/
    public static void saveGCM(Context context, String gcmId) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.KEY_FCM_ID, gcmId);
        editor.apply();
    }

    public static String getGCM(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getString(HeylaAppConstants.KEY_FCM_ID, "");
    }

    /*To save mobile IMEI number */
    public static void saveIMEI(Context context, String imei) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.KEY_IMEI, imei);
        editor.apply();
    }

    public static String getIMEI(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getString(HeylaAppConstants.KEY_IMEI, "");
    }

    /*To check remember me enabled or not*/
    public static void setRememberMe(Context context, boolean isRemembered) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(HeylaAppConstants.IS_REMEMBER_ENABLED, isRemembered);
        editor.apply();
    }

    public static boolean isRemembered(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(HeylaAppConstants.IS_REMEMBER_ENABLED, false);
    }

    /*To store username*/
    public static void saveUsername(Context context, String userName) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.KEY_USERNAME, userName);
        editor.apply();
    }

    public static String getUsername(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getString(HeylaAppConstants.KEY_USERNAME, "");
    }

    /*To store username*/
    public static void savePassword(Context context, String password) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HeylaAppConstants.KEY_PASSWORD, password);
        editor.apply();
    }

    public static String getPassword(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getString(HeylaAppConstants.KEY_PASSWORD, "");
    }
}
