package com.palprotech.heylaapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.gson.JsonObject;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.helper.AlertDialogHelper;
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
    TextView profile, deactivate, resetPass, privacyPoclicy, paymentPolicy, reportProblem, aboutUs, termsConditions;
    ImageView ivBack;
    String res = "";
    Switch aSwitch;
    boolean firsttime = true;
    private RelativeLayout swLayout, daLayout, cpLayout;

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

    private void deactivateAcc() {
        res = "deactive";

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(this));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.USER_DEACTIVE;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void iniView() {
        profile = findViewById(R.id.profile_settings);
        profile.setOnClickListener(this);

        deactivate = findViewById(R.id.deactive_settings);
        deactivate.setOnClickListener(this);

        privacyPoclicy = findViewById(R.id.privacy_policy_settings);
        privacyPoclicy.setOnClickListener(this);

        reportProblem = findViewById(R.id.report_problem_settings);
        reportProblem.setOnClickListener(this);

        paymentPolicy = findViewById(R.id.payment_policy_settings);
        paymentPolicy.setOnClickListener(this);

        termsConditions = findViewById(R.id.terms_settings);
        termsConditions.setOnClickListener(this);

        aboutUs = findViewById(R.id.about_us);
        aboutUs.setOnClickListener(this);

        resetPass = findViewById(R.id.change_pass_settings);
        resetPass.setOnClickListener(this);

        swLayout = findViewById(R.id.sw_lay);
        daLayout = findViewById(R.id.da_lay);
        cpLayout = findViewById(R.id.cp_lay);

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
        checkLogIn();

    }

    private void checkLogIn() {
        if (PreferenceStorage.getUserType(getApplicationContext()).equalsIgnoreCase("1")) {
            swLayout.setVisibility(View.VISIBLE);
            daLayout.setVisibility(View.VISIBLE);
            cpLayout.setVisibility(View.VISIBLE);
        }
        else {
            swLayout.setVisibility(View.GONE);
            daLayout.setVisibility(View.GONE);
            cpLayout.setVisibility(View.GONE);
        }
    }

    private void forgotInit() {

        Intent newI = new Intent(new Intent(SettingsActivity.this, ChangePasswordActivity.class));
        startActivity(newI);
        finish();
    }

    @Override
    public void onClick(View view) {
        if (view == profile) {
            if (PreferenceStorage.getUserType(getApplicationContext()).equalsIgnoreCase("1")) {
                startActivity(new Intent(SettingsActivity.this, ProfileActivity.class));
            }
            else {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getApplicationContext());
                alertDialogBuilder.setTitle("Login");
                alertDialogBuilder.setMessage("Log in to Access");
                alertDialogBuilder.setPositiveButton("OK", (arg0, arg1) -> {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    sharedPreferences.edit().clear().apply();
//        TwitterUtil.getInstance().resetTwitterRequestToken();

                    Intent homeIntent = new Intent(getApplicationContext(), SplashScreenActivity.class);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homeIntent);

                });
                alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                alertDialogBuilder.show();
            }
        } else if (view == privacyPoclicy) {
            Intent newI = new Intent(new Intent(SettingsActivity.this, AboutHeylaActivity.class));
            newI.putExtra("pageval", "setting_privacy");
            startActivity(newI);
//            startActivity(new Intent(SettingsActivity.this, ForgotPasswordActivity.class));
        } else if (view == deactivate) {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Deactivate Account");
            alertDialogBuilder.setMessage("Are you are you want to deactivate your account?");
            alertDialogBuilder.setPositiveButton("Yes", (arg0, arg1) -> deactivateAcc());
            alertDialogBuilder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            alertDialogBuilder.show();
        } else if (view == resetPass) {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Reset Password");
            alertDialogBuilder.setMessage("Are you are you want to reset your account password? Doing this will log you out of all current sessions.");
            alertDialogBuilder.setPositiveButton("Yes", (arg0, arg1) -> forgotInit());
            alertDialogBuilder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            alertDialogBuilder.show();
        } else if (view == aboutUs) {
            Intent newI = new Intent(new Intent(SettingsActivity.this, AboutHeylaActivity.class));
            newI.putExtra("pageval", "setting_about");
            startActivity(newI);
//            startActivity(new Intent(SettingsActivity.this, ChangeNumberActivity.class));
        } else if (view == paymentPolicy) {
            Intent newI = new Intent(new Intent(SettingsActivity.this, AboutHeylaActivity.class));
            newI.putExtra("pageval", "setting_payment");
            startActivity(newI);
//            startActivity(new Intent(SettingsActivity.this, ChangeNumberActivity.class));
        } else if (view == termsConditions) {
            Intent newI = new Intent(new Intent(SettingsActivity.this, AboutHeylaActivity.class));
            newI.putExtra("pageval", "setting_terms");
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
            String msg = response.getString("msg");
            if (status.equalsIgnoreCase("success")) {
                if (res.equalsIgnoreCase("info")) {
                    JSONObject data = response.getJSONObject("userData");
                    String news = data.getString("newsletter_status");
                    if (news.equalsIgnoreCase("N")) {
                        aSwitch.setChecked(false);
                    } else {
                        aSwitch.setChecked(true);
                    }
                } if (res.equalsIgnoreCase("noti")) {
//                    if (aSwitch.isChecked()){
//                        if (!firsttime) {
//                            Toast.makeText(this, "Push notification enabled", Toast.LENGTH_SHORT).show();
//                        }
//                        firsttime = false;
//                    } else {
//                        if (!firsttime) {
//                            Toast.makeText(this, "Push notification disabled", Toast.LENGTH_SHORT).show();
//                        }
//                        firsttime = false;
//                    }
                } if (res.equalsIgnoreCase("deactive")) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    sharedPreferences.edit().clear().apply();
//        TwitterUtil.getInstance().resetTwitterRequestToken();

                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail()
                            .build();
                    // Build a GoogleSignInClient with the options specified by gso.
                    LoginManager.getInstance().logOut();
                    GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
                    mGoogleSignInClient.signOut();


                    Intent homeIntent = new Intent(getApplicationContext(), SplashScreenActivity.class);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homeIntent);
                    Toast.makeText(this, "Account deactivated successfully!", Toast.LENGTH_SHORT).show();
//                    AlertDialogHelper.showSimpleAlertDialog(this, "Account Deactivated");
                }
            } else {
                AlertDialogHelper.showSimpleAlertDialog(this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String error) {
        progressDialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(this, error);

    }
}