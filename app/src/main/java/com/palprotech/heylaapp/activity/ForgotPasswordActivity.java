package com.palprotech.heylaapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

/**
 * Created by Narendar on 12/10/17.
 */

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener {

    private static final String TAG = ForgotPasswordActivity.class.getName();

    private TextInputLayout inputEmailOrMobileNo;
    private EditText edtEmailOrMobileNo;
    private Button btnSubmit;
    private ProgressDialogHelper progressDialogHelper;
    private ServiceHelper serviceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

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


                    progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
                    try {
                        JSONObject jsonObject = new JSONObject();

                        jsonObject.put(HeylaAppConstants.PARAMS_USERNAME, username);
                        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.FORGOT_PASSWORD;
                        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    AlertDialogHelper.showSimpleAlertDialog(getApplicationContext(), "Email ID/Mobile number is mandatory");
                }
            }
        } else {
            AlertDialogHelper.showSimpleAlertDialog(getApplicationContext(), "No Network connection available");
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
        try {
            if (validateSignInResponse(response)) {

                String reqType = response.getString("type");
                if (reqType.equalsIgnoreCase("Mobile")) {
                    Intent homeIntent = new Intent(getApplicationContext(), ForgotPasswordNumberVerificationActivity.class);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    homeIntent.putExtra("mobile_no", edtEmailOrMobileNo.getText().toString());
                    startActivity(homeIntent);
                    finish();
                } else {
                  /*  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getApplicationContext());
                    alertDialogBuilder.setTitle("Reset Successful");

                    alertDialogBuilder.setMessage("Activation Link sent to your email.");
                    alertDialogBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {*/
                    Toast.makeText(getApplicationContext(), "We have mailed you a link to reset your password", Toast.LENGTH_LONG).show();
                    Intent homeIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    homeIntent.putExtra("mobile_no", edtEmailOrMobileNo.getText().toString());
                    startActivity(homeIntent);
                    finish();
                               /* }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();*/
                }
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
