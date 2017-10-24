package com.palprotech.heylaapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.palprotech.heylaapp.R;

/**
 * Created by Narendar on 24/10/17.
 */

public class ForgotPasswordReset extends AppCompatActivity implements View.OnClickListener{

    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_otp);

        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == submit) {
            Intent navigationIntent = new Intent(this, ForgotPasswordOtp.class);
            navigationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(navigationIntent);
        }
    }
}
