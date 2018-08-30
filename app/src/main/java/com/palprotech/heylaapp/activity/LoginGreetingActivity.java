package com.palprotech.heylaapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.utils.PreferenceStorage;

public class LoginGreetingActivity extends AppCompatActivity implements View.OnClickListener{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login_greeting);

        findViewById(R.id.get_started).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceStorage.saveUserType(getApplicationContext(), "1");
                Intent homeIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    homeIntent.putExtra("profile_state", "new");
                startActivity(homeIntent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
