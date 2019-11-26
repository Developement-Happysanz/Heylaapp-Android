package com.palprotech.heylaapp.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.palprotech.heylaapp.R;

public class AboutHeylaActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getStringExtra("pageval").equalsIgnoreCase("setting_about")) {
            setContentView(R.layout.activity_about_us);
        } else if (getIntent().getStringExtra("pageval").equalsIgnoreCase("setting_payment")) {
            setContentView(R.layout.activity_privacy_policy);
        } else if (getIntent().getStringExtra("pageval").equalsIgnoreCase("setting_privacy")) {
            setContentView(R.layout.activity_payment_policy);
        } else if (getIntent().getStringExtra("pageval").equalsIgnoreCase("setting_terms")) {
            setContentView(R.layout.activity_terms);
        }
        findViewById(R.id.back_res).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
