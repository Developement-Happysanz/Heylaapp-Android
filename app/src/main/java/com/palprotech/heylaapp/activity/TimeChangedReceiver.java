package com.palprotech.heylaapp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.palprotech.heylaapp.utils.PreferenceStorage;

public class TimeChangedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Action: " + intent.getAction(), Toast.LENGTH_LONG).show();
//        if (intent.getAction().equalsIgnoreCase("android.intent.action.TIME_SET") ||
//                intent.getAction().equalsIgnoreCase("android.intent.action.DATE_CHANGED") ||
//                        intent.getAction().equalsIgnoreCase("android.intent.action.TIMEZONE_CHANGED" )) {
//            PreferenceStorage.setDateChanged(context, true);
//        } else {
//            PreferenceStorage.setDateChanged(context, false);
//        }
    }

}
