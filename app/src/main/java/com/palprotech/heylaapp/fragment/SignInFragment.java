package com.palprotech.heylaapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.activity.ForgotPasswordActivity;
import com.palprotech.heylaapp.activity.LoginActivity;
import com.palprotech.heylaapp.activity.ProfileActivity;
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

public class SignInFragment extends Fragment implements View.OnClickListener, IServiceListener, DialogClickListener {

    private static final String TAG = LoginActivity.class.getName();

    private EditText edtUsername, edtPassword;
    private TextInputLayout inputUsername, inputPassword;
    private View rootView;
    private Button signIn;
    private CheckBox saveLoginCheckBox;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private TextView forgotPassword;

    public static SignInFragment newInstance(int position) {
        SignInFragment frag = new SignInFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);

        initializeViews();

        return rootView;
    }

    protected void initializeViews() {
        inputUsername = rootView.findViewById(R.id.ti_username);
        inputPassword = rootView.findViewById(R.id.ti_password);
        edtUsername = rootView.findViewById(R.id.edtUsername);
        edtPassword = rootView.findViewById(R.id.edtPassword);
        signIn = rootView.findViewById(R.id.signin);
        signIn.setOnClickListener(this);
        forgotPassword = rootView.findViewById(R.id.forgotpassword);
        forgotPassword.setOnClickListener(this);
        saveLoginCheckBox = rootView.findViewById(R.id.saveLoginCheckBox);

        serviceHelper = new ServiceHelper(getActivity());
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(getActivity());

        Boolean saveLogin = PreferenceStorage.isRemembered(getContext());
        if (saveLogin) {
            edtUsername.setText(PreferenceStorage.getUsername(getContext()));
            edtPassword.setText(PreferenceStorage.getPassword(getContext()));
            saveLoginCheckBox.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        if (CommonUtils.isNetworkAvailable(getActivity())) {
            if (v == signIn) {
                if (validateFields()) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edtUsername.getWindowToken(), 0);

                    String username = edtUsername.getText().toString();
                    String password = edtPassword.getText().toString();
                    String GCMKey = PreferenceStorage.getGCM(getContext());
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put(HeylaAppConstants.PARAMS_USERNAME, username);
                        jsonObject.put(HeylaAppConstants.PARAMS_PASSWORD, password);
                        jsonObject.put(HeylaAppConstants.PARAMS_GCM_KEY, GCMKey);
                        jsonObject.put(HeylaAppConstants.PARAMS_LOGIN_TYPE, "1");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
                    String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.SIGN_IN;
                    serviceHelper.makeGetServiceCall(jsonObject.toString(), url);

                    if (saveLoginCheckBox.isChecked()) {
                        PreferenceStorage.saveUsername(getContext(), username);
                        PreferenceStorage.savePassword(getContext(), password);
                        PreferenceStorage.setRememberMe(getContext(), true);
                    } else {
                        PreferenceStorage.saveUsername(getContext(), "");
                        PreferenceStorage.savePassword(getContext(), "");
                        PreferenceStorage.setRememberMe(getContext(), false);
                    }
                    Intent homeIntent = new Intent(getActivity(), ProfileActivity.class);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homeIntent);
                    getActivity().finish();
                }
            } else if (v == forgotPassword) {
                Intent homeIntent = new Intent(getActivity(), ForgotPasswordActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                getActivity().finish();
            }
        } else {
            AlertDialogHelper.showSimpleAlertDialog(getActivity(), "No Network connection available");
        }
    }

    private boolean validateFields() {
        if (!HeylaAppValidator.checkNullString(this.edtUsername.getText().toString().trim())) {
            edtUsername.setError(getString(R.string.err_email));
            requestFocus(edtUsername);
            return false;
        } else if (!HeylaAppValidator.checkNullString(this.edtPassword.getText().toString().trim())) {
            edtPassword.setError(getString(R.string.err_empty_password));
            requestFocus(edtPassword);
            return false;
        } else if (!HeylaAppValidator.checkStringMinLength(6, this.edtPassword.getText().toString().trim())) {
            edtPassword.setError(getString(R.string.err_min_pass_length));
            requestFocus(edtPassword);
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
    public void onResponse(JSONObject response) {

        progressDialogHelper.hideProgressDialog();

        if (validateSignInResponse(response)) {

        }
    }

    @Override
    public void onError(String error) {
        progressDialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(getContext(), error);
    }
}