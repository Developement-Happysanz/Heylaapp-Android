package com.palprotech.heylaapp.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.activity.LoginActivity;
import com.palprotech.heylaapp.activity.NumberVerificationActivity;
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

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Narendar on 06/10/17.
 */

public class SignUpFragment extends Fragment implements View.OnClickListener, IServiceListener, DialogClickListener {

    private static final String TAG = LoginActivity.class.getName();

    private EditText mobile, password, email;
    private TextInputLayout inputMobile, inputPassword, inputEmail;
    View rootView;
    Button signUp;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;

    public static SignUpFragment newInstance(int position) {
        SignUpFragment frag = new SignUpFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        initializeViews();
        return rootView;
    }

    protected void initializeViews() {
        inputEmail = rootView.findViewById(R.id.ti_mail);
        email = rootView.findViewById(R.id.edtMail);
        inputMobile = rootView.findViewById(R.id.ti_mobilenum);
        mobile = rootView.findViewById(R.id.edtMobilenum);
        inputPassword = rootView.findViewById(R.id.ti_password);
        password = rootView.findViewById(R.id.edtPassword);
        signUp = rootView.findViewById(R.id.btnSignup);
        signUp.setOnClickListener(this);

        serviceHelper = new ServiceHelper(getActivity());
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(getActivity());
    }

    @Override
    public void onClick(View v) {
        if (CommonUtils.isNetworkAvailable(getActivity())) {
            if (v == signUp) {
                if (validateFields()) {
                    String GCMKey = PreferenceStorage.getGCM(getContext());
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put(HeylaAppConstants.PARAMS_EMAIL_ID, email.getText().toString());
                        jsonObject.put(HeylaAppConstants.PARAMS_MOBILE_NUMBER, mobile.getText().toString());
                        jsonObject.put(HeylaAppConstants.PARAMS_PASSWORD, password.getText().toString());
                        jsonObject.put(HeylaAppConstants.PARAMS_GCM_KEY, GCMKey);
                        jsonObject.put(HeylaAppConstants.PARAMS_MOBILE_TYPE, "1");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
                    String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.SIGN_UP;
                    serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
                }
            }
        } else {
            AlertDialogHelper.showSimpleAlertDialog(getActivity(), "No Network connection available");
        }
    }

    private boolean validateFields() {
//        inputEmail.setErrorEnabled(false);
        inputMobile.setErrorEnabled(false);
        inputPassword.setErrorEnabled(false);
//        if (!HeylaAppValidator.checkNullString(this.email.getText().toString().trim())) {
//            inputEmail.setError(getString(R.string.err_email));
//            requestFocus(email);
//            return false;
//        }
//        else if (!HeylaAppValidator.isEmailValid(this.email.getText().toString().trim())) {
//            inputEmail.setError(getString(R.string.err_email));
//            requestFocus(email);
//            return false;
//        }
        if (!HeylaAppValidator.checkNullString(this.mobile.getText().toString().trim())) {
            inputMobile.setError(getString(R.string.err_mobile));
            requestFocus(mobile);
            return false;
        } else if (!HeylaAppValidator.checkMobileNumLength(this.mobile.getText().toString().trim())) {
            inputMobile.setError(getString(R.string.err_mobile));
            requestFocus(mobile);
            return false;
        } else if (!HeylaAppValidator.checkNullString(this.password.getText().toString().trim())) {
            inputPassword.setError(getString(R.string.err_empty_password));
            requestFocus(password);
            return false;
        } else if (!HeylaAppValidator.checkStringMinLength(6, this.password.getText().toString().trim())) {
            inputPassword.setError(getString(R.string.err_min_pass_length));
            requestFocus(password);
            return false;
        } else {
            return true;
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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

        if (validateSignInResponse(response)) {

           /* AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setTitle("Registration Successful");

            alertDialogBuilder.setMessage("Activation Link sent to your email.");
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {*/

            PreferenceStorage.saveMobileNo(getApplicationContext(), mobile.getText().toString());
            Intent homeIntent = new Intent(getActivity(), NumberVerificationActivity.class);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            homeIntent.putExtra("mobile_no", mobile.getText().toString());
            startActivity(homeIntent);
            getActivity().finish();
                     /*   }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();*/
        }
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
                        AlertDialogHelper.showSimpleAlertDialog(getContext(), msg);

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
    public void onError(String error) {
        progressDialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(getContext(), error);
    }
}
