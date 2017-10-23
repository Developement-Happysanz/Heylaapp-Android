package com.palprotech.heylaapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
 * Created by Narendar on 16/10/17.
 */

public class NumberVerificationActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener {

    private static final String TAG = NumberVerificationActivity.class.getName();

    private CustomOtpEditText otpEditText;
    private TextView tvResendOTP;
    private Button btnConfirm;
    private Button btnChangeNumber;
    private String mobileNo;

    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_verification);

        mobileNo = getIntent().getStringExtra("mobile_no");
        otpEditText = (CustomOtpEditText) findViewById(R.id.otp_view);
        tvResendOTP = (TextView) findViewById(R.id.resend);
        tvResendOTP.setOnClickListener(this);
        btnConfirm = (Button) findViewById(R.id.sendcode);
        btnConfirm.setOnClickListener(this);
        btnChangeNumber = (Button) findViewById(R.id.changenumber);
        btnChangeNumber.setOnClickListener(this);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);
    }

    @Override
    public void onClick(View v) {
        if (CommonUtils.isNetworkAvailable(getApplicationContext())) {
            if (v == tvResendOTP) {

            } else if (v == btnConfirm) {

                if (otpEditText.hasValidOTP()) {

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put(HeylaAppConstants.PARAMS_MOBILE_NUMBER, mobileNo);
                        jsonObject.put(HeylaAppConstants.PARAMS_OTP, otpEditText.getOTP());
                        jsonObject.put(HeylaAppConstants.PARAMS_REQUEST_MODE, "1");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
                    String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.MOBILE_NUMBER_VERIFY;
                    serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
                } else {
                    AlertDialogHelper.showSimpleAlertDialog(this, "Invalid OTP");
                }

            } else if (v == btnChangeNumber) {

            }
        } else {
            AlertDialogHelper.showSimpleAlertDialog(this, "No Network connection available");
        }
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
            Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            this.finish();
        }
    }

    @Override
    public void onError(String error) {

    }
}
