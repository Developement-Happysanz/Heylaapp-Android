package com.palprotech.heylaapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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
import com.palprotech.heylaapp.utils.PreferenceStorage;

import org.json.JSONException;
import org.json.JSONObject;

import static android.util.Log.d;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener {

    private static final String TAG = "ChangePasswordActivity";
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private EditText etOldPassword;
    private EditText etNewPassword;
    private EditText etConfirmPassword;
    private Button btnSubmit;
    private TextView forgot;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);
        etOldPassword = findViewById(R.id.edtOldPassword);
        etNewPassword = findViewById(R.id.edtNewPassword);
        etConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnSubmit = findViewById(R.id.sendcode);
        btnSubmit.setOnClickListener(this);
        forgot = findViewById(R.id.forgotpassword);
        forgot.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v == btnSubmit) {
            if (validateFields()) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(this));
                    jsonObject.put(HeylaAppConstants.PARAMS_OLD_PASSWORD, etOldPassword.getText().toString());
                    jsonObject.put(HeylaAppConstants.PARAMS_NEW_PASSWORD, etNewPassword.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
                String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.CHANGE_PASSWORD;
                serviceHelper.makeGetServiceCall(jsonObject.toString(), url);

            }
        }if (v == forgot) {
            Intent homeIntent = new Intent(this, ForgotPasswordActivity.class);
            startActivity(homeIntent);
//                getActivity().finish();
        }
    }

    private boolean validateFields() {

        if (!HeylaAppValidator.checkNullString(this.etOldPassword.getText().toString().trim())) {
            AlertDialogHelper.showSimpleAlertDialog(this, "Old password required");
            return false;
        } else if (!HeylaAppValidator.checkNullString(this.etNewPassword.getText().toString().trim())) {
            AlertDialogHelper.showSimpleAlertDialog(this, "New password required");
            return false;
        } else if (!HeylaAppValidator.checkNullString(this.etConfirmPassword.getText().toString().trim())) {
            AlertDialogHelper.showSimpleAlertDialog(this, "Confirm password required");
            return false;
        } else if (!this.etNewPassword.getText().toString().trim().equalsIgnoreCase(this.etNewPassword.getText().toString().trim())) {
            AlertDialogHelper.showSimpleAlertDialog(this, "Password does not match new password");
            return false;
        } else {
            return true;
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
                d(TAG, "status val" + status + "msg" + msg);

                if ((status != null)) {
                    if (((status.equalsIgnoreCase("activationError")) || (status.equalsIgnoreCase("alreadyRegistered")) ||
                            (status.equalsIgnoreCase("notRegistered")) || (status.equalsIgnoreCase("error")))) {
                        signInSuccess = false;
                        d(TAG, "Show error dialog");
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
            Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show();
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
            Intent navigationIntent = new Intent(this, LoginActivity.class);
            navigationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(navigationIntent);
            finish();
        }
    }

    @Override
    public void onError(String error) {
        progressDialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(getApplicationContext(), error);
    }
}
