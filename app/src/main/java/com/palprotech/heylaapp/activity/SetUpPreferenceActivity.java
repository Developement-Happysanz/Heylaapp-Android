package com.palprotech.heylaapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.bean.support.EventCities;

/**
 * Created by Admin on 06-11-2017.
 */

public class SetUpPreferenceActivity extends AppCompatActivity {

    private static final String TAG = SetUpPreferenceActivity.class.getName();
    private EventCities eventCities;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_preference);

        eventCities = (EventCities) getIntent().getSerializableExtra("eventObj");

        String ok = "";
    }
}
