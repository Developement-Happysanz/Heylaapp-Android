package com.palprotech.heylaapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.interfaces.DialogClickListener;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;
import com.palprotech.heylaapp.serviceinterfaces.IServiceListener;
import com.palprotech.heylaapp.utils.HeylaAppConstants;
import com.palprotech.heylaapp.utils.PreferenceStorage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Narendar on 06/12/17.
 */

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener {

    private static final String TAG = EventReviewAddActivity.class.getName();
    private ProgressDialogHelper progressDialogHelper;
    private ServiceHelper serviceHelper;
    TextView profile, privacyPoclicy, changeNumber, reportProblem, aboutUs;
    ImageView ivBack;
    String res = "";
    Switch aSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        profileInfo();

        iniView();
    }

    private void profileInfo() {
        res = "info";

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(this));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.USER_INFO;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void sendNotif() {
        res = "noti";

        JSONObject jsonObject = new JSONObject();
        boolean stat = aSwitch.isChecked();
        String stt = "";
        if (stat) {
            stt = "Y";
        } else {
            stt = "N";
        }
        try {

            jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(this));
            jsonObject.put(HeylaAppConstants.KEY_NOTIFICATION_STATUS, stt);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.USER_NOTIFICATION;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void iniView() {
        profile = findViewById(R.id.profile_settings);
        profile.setOnClickListener(this);

        privacyPoclicy = findViewById(R.id.privacy_policy_settings);
        privacyPoclicy.setOnClickListener(this);

        reportProblem = findViewById(R.id.report_problem_settings);
        reportProblem.setOnClickListener(this);

        aboutUs = findViewById(R.id.about_us);
        aboutUs.setOnClickListener(this);

        ivBack = findViewById(R.id.back_res);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        aSwitch = findViewById(R.id.notification_status);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                sendNotif();
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view == profile) {
            startActivity(new Intent(SettingsActivity.this, ProfileActivity.class));
        } else if (view == privacyPoclicy) {
            Intent newI = new Intent(new Intent(SettingsActivity.this, BlogViewActivity.class));
            newI.putExtra("pageval", "setting_privacy");
            startActivity(newI);
//            startActivity(new Intent(SettingsActivity.this, ForgotPasswordActivity.class));
        } else if (view == aboutUs) {
            Intent newI = new Intent(new Intent(SettingsActivity.this, BlogViewActivity.class));
            newI.putExtra("pageval", "setting_about");
            startActivity(newI);
//            startActivity(new Intent(SettingsActivity.this, ChangeNumberActivity.class));
        } else if (view == reportProblem) {
            Intent newI = new Intent(new Intent(SettingsActivity.this, FeedbackActivity.class));
            newI.putExtra("pageval", "setting_report");
            startActivity(newI);
//            startActivity(new Intent(SettingsActivity.this, FeedbackActivity.class));
        }
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        try {
            String status = response.getString("status");
            if (status.equalsIgnoreCase("success")) {
                if (res.equalsIgnoreCase("info")) {
                    JSONObject data = response.getJSONObject("userData");
                    String news = data.getString("newsletter_status");
                    if (news.equalsIgnoreCase("N")) {
                        aSwitch.setChecked(false);
                    } else {
                        aSwitch.setChecked(true);
                    }
                } else {
                    Toast.makeText(this, "Notification", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String error) {

    }
}