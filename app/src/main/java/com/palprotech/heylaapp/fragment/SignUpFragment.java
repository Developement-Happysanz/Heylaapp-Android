package com.palprotech.heylaapp.fragment;

import android.os.Bundle;
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
import com.palprotech.heylaapp.utils.HeylaAppValidator;

/**
 * Created by Narendar on 06/10/17.
 */

public class SignUpFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getName();

    private EditText mobile, password, email;
    private TextInputLayout inputMobile, inputPassword, inputEmail;
    View rootView;
    Button signUp;

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
    }

    @Override
    public void onClick(View v) {
        if (validateFields()) {
            Log.d(TAG, "Initiating google plus connection");
        } else {
            Log.d(TAG, "Initiating google plus connection");
        }
    }

    private boolean validateFields() {
        inputEmail.setErrorEnabled(false);
        inputMobile.setErrorEnabled(false);
        inputPassword.setErrorEnabled(false);
        if (!HeylaAppValidator.checkNullString(this.email.getText().toString().trim())) {
            inputEmail.setError(getString(R.string.err_email));
            requestFocus(email);
            return false;
        } else if (!HeylaAppValidator.isEmailValid(this.email.getText().toString().trim())) {
            inputEmail.setError(getString(R.string.err_email));
            requestFocus(email);
            return false;
        } else if (!HeylaAppValidator.checkNullString(this.mobile.getText().toString().trim())) {
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
}
