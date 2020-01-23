package com.palprotech.heylaapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.helper.AlertDialogHelper;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.interfaces.DialogClickListener;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;
import com.palprotech.heylaapp.serviceinterfaces.IServiceListener;
import com.palprotech.heylaapp.utils.CommonUtils;
import com.palprotech.heylaapp.utils.HeylaAppConstants;
import com.palprotech.heylaapp.utils.HeylaAppValidator;

import org.json.JSONException;
import org.json.JSONObject;

public class ReactivateAccountActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener {

    private static final String TAG = ForgotPasswordActivity.class.getName();

    private TextInputLayout inputEmailOrMobileNo;
    private EditText edtEmailOrMobileNo;
    private Button btnSubmit;
    private ProgressDialogHelper progressDialogHelper;
    private ServiceHelper serviceHelper;
    private String resCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reactivate_account);

        inputEmailOrMobileNo = (TextInputLayout) findViewById(R.id.ti_mailormobile);
        edtEmailOrMobileNo = (EditText) findViewById(R.id.edtMailormobile);
        btnSubmit = (Button) findViewById(R.id.signin);
        btnSubmit.setOnClickListener(this);

        findViewById(R.id.back_res).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);
    }

    @Override
    public void onClick(View v) {
        if (CommonUtils.isNetworkAvailable(getApplicationContext())) {
            if (v == btnSubmit) {
                String username = edtEmailOrMobileNo.getText().toString();
                if ((HeylaAppValidator.checkNullString(username))) {
                    resCheck = "active_check";

                    progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
                    try {
                        JSONObject jsonObject = new JSONObject();

                        jsonObject.put(HeylaAppConstants.PARAMS_EMAIL_OR_MOB, username);
                        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.USER_REACTIVATE_CHECK;
                        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    AlertDialogHelper.showSimpleAlertDialog(this, "Email ID/Mobile number is mandatory");
                }
            }
        } else {
            AlertDialogHelper.showSimpleAlertDialog(this, "No Network connection available");
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
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
                        if (msg.equalsIgnoreCase("Please Contact Heyla Team!.")) {
                            activateAccAlert();
                        } else {
                            AlertDialogHelper.showSimpleAlertDialog(this, msg);
                        }
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

    private void activateAccAlert() {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Deactivated Account");
        alertDialogBuilder.setMessage("This account has been deactivated by the admin. Do you wish to send a request to reactivate it?");
        alertDialogBuilder.setPositiveButton("Yes", (arg0, arg1) -> deactivateAcc());
        alertDialogBuilder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        alertDialogBuilder.show();
    }

    private void deactivateAcc() {
        resCheck = "admin_request";
        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        try {
            JSONObject jsonObject = new JSONObject();
            String username = edtEmailOrMobileNo.getText().toString();

            jsonObject.put(HeylaAppConstants.PARAMS_EMAIL_OR_MOB, username);
            String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.USER_REACTIVATE_ADMIN_REQUEST;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), url);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        if (validateSignInResponse(response)) {
            if (resCheck.equalsIgnoreCase("active_check")) {
                Intent homeIntent = new Intent(getApplicationContext(), ForgotPasswordNumberVerificationActivity.class);
                homeIntent.putExtra("mobile_no", edtEmailOrMobileNo.getText().toString());
                homeIntent.putExtra("page_from", "activate");
                startActivity(homeIntent);
            } else {
                Toast.makeText(this, "Request sent to the admin!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onError(String error) {
        progressDialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(this, error);
    }
}