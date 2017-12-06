package com.palprotech.heylaapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.palprotech.heylaapp.R;

/**
 * Created by Narendar on 06/12/17.
 */

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    TextView profile, changePassword, changeNumber, verifyMail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        iniView();
    }

    private void iniView() {
        profile = findViewById(R.id.profile_settings);
        profile.setOnClickListener(this);

        changePassword = findViewById(R.id.change_password_settings);
        changePassword.setOnClickListener(this);

        changeNumber = findViewById(R.id.change_number_settings);
        changeNumber.setOnClickListener(this);

        verifyMail = findViewById(R.id.verify_mail_settings);
        verifyMail.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == profile){
            startActivity(new Intent(SettingsActivity.this, ProfileActivity.class));
        }
        else if(view == changePassword){
            startActivity(new Intent(SettingsActivity.this, ForgotPasswordActivity.class));
        }
        else if(view == changeNumber){
            startActivity(new Intent(SettingsActivity.this, ChangeNumberActivity.class));
        }
        else if(view == verifyMail){
            startActivity(new Intent(SettingsActivity.this, ProfileActivity.class));
        }
    }
}