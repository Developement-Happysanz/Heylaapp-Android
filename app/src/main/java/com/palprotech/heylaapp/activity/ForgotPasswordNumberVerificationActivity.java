package com.palprotech.heylaapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.customview.CustomOtpEditText;
import com.palprotech.heylaapp.helper.AlertDialogHelper;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.interfaces.DialogClickListener;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;
import com.palprotech.heylaapp.serviceinterfaces.IServiceListener;
import com.palprotech.heylaapp.utils.CommonUtils;
import com.palprotech.heylaapp.utils.HeylaAppConstants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Admin on 24-10-2017.
 */

public class ForgotPasswordNumberVerificationActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener {

    private static final String TAG = ForgotPasswordNumberVerificationActivity.class.getName();

    private ProgressDialogHelper progressDialogHelper;
    private ServiceHelper serviceHelper;
    private Button btnSubmit;
    private TextView txtResend;
    private CustomOtpEditText otpEditText;
    private String mobileNo;
    private String pageCheck;
    private String resCheck;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_number_verification);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        mobileNo = getIntent().getStringExtra("mobile_no");
        pageCheck = getIntent().getStringExtra("page_from");

        btnSubmit = (Button) findViewById(R.id.sendcode);
        btnSubmit.setOnClickListener(this);

        txtResend = (TextView) findViewById(R.id.resend);
        txtResend.setOnClickListener(this);

        otpEditText = (CustomOtpEditText) findViewById(R.id.otp_view);

        findViewById(R.id.back_res).setVisibility(View.VISIBLE);
        findViewById(R.id.back_res).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    @Override
    public void onClick(View v) {

        if (CommonUtils.isNetworkAvailable(getApplicationContext())) {
            if (v == btnSubmit) {
                if (otpEditText.hasValidOTP()) {
                    if (pageCheck.equalsIgnoreCase("forgot")) {
                        forgotSubmit();
                    } else {
                        reactivateSubmit();
                    }

                } else {
                    AlertDialogHelper.showSimpleAlertDialog(this, "Invalid OTP");
                }

            } else if (v == txtResend) {
                if (pageCheck.equalsIgnoreCase("forgot")) {
                    forgotResend();
                } else {
                    reactivateResend();
                }

            }
        } else {
            AlertDialogHelper.showSimpleAlertDialog(getApplicationContext(), "No Network connection available");
        }
    }

    private void forgotSubmit() {
        resCheck = "forgotSubmit";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(HeylaAppConstants.PARAMS_MOBILE_NUMBER, mobileNo);
            jsonObject.put(HeylaAppConstants.PARAMS_OTP, otpEditText.getOTP());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.FORGOT_PASSWORD_OTP_REQUEST;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void reactivateSubmit() {
        resCheck = "reactivateSubmit";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(HeylaAppConstants.PARAMS_EMAIL_OR_MOB, mobileNo);
            jsonObject.put(HeylaAppConstants.PARAMS_CODE, otpEditText.getOTP());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.USER_REACTIVATE;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void forgotResend() {
        resCheck = "forgotResend";
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.PARAMS_MOBILE_NUMBER, mobileNo);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.RESEND_OTP_REQUEST;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void reactivateResend() {
        resCheck = "reactivateResend";
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.PARAMS_EMAIL_OR_MOB, mobileNo);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.USER_REACTIVATE_CHECK;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    private boolean validateSignInResponse(JSONObject response) {
        boolean signInSuccess = false;
        if ((response != null)) {
            try {
                String status = response.getString("status");
                String msg = response.getString(HeylaAppConstants.PARAM_MESSAGE);
                Log.d(TAG, "status val" + status + "msg" + msg);

                if ((status != null)) {
                    if (((status.equalsIgnoreCase("activationError")) || (status.equalsIgnoreCase("alreadyRegistered")) ||
                            (status.equalsIgnoreCase("notRegistered")) || (status.equalsIgnoreCase("error")))) {
                        signInSuccess = false;
                        Log.d(TAG, "Show error dialog");
                        AlertDialogHelper.showSimpleAlertDialog(this, msg);

                    } else {
                        signInSuccess = true;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return signInSuccess;
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        if (validateSignInResponse(response)) {
            try {
                if (resCheck.equalsIgnoreCase("reactivateSubmit")) {
                    Toast.makeText(this, "Account has been reactivated!", Toast.LENGTH_SHORT).show();
                    Intent homeIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homeIntent);
                    this.finish();
                } else if (resCheck.equalsIgnoreCase("forgotSubmit")) {
                    String userId = response.getString("User_id");
                    Intent homeIntent = new Intent(getApplicationContext(), UpdatePasswordActivity.class);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    homeIntent.putExtra("user_id", userId);
                    startActivity(homeIntent);
                    this.finish();
                } else {
                    Toast.makeText(this, "OTP has been resent", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(String error) {

    }
}
