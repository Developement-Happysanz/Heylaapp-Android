package com.palprotech.heylaapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.utils.HeylaAppValidator;
import com.palprotech.heylaapp.utils.PreferenceStorage;

public class SignInFragment extends Fragment implements View.OnClickListener {

    private EditText edtUsername, edtPassword;
    private View rootView;
    private Button signIn;
    private CheckBox saveLoginCheckBox;

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
        edtUsername = rootView.findViewById(R.id.edtUsername);
        edtPassword = rootView.findViewById(R.id.edtPassword);
        signIn = rootView.findViewById(R.id.signin);
        signIn.setOnClickListener(this);
        saveLoginCheckBox = rootView.findViewById(R.id.saveLoginCheckBox);

        Boolean saveLogin = PreferenceStorage.isRemembered(getContext());
        if (saveLogin) {
            edtUsername.setText(PreferenceStorage.getUsername(getContext()));
            edtPassword.setText(PreferenceStorage.getPassword(getContext()));
            saveLoginCheckBox.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == signIn) {
            if (validateFields()) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtUsername.getWindowToken(), 0);

                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();

                if (saveLoginCheckBox.isChecked()) {
                    PreferenceStorage.saveUsername(getContext(), username);
                    PreferenceStorage.savePassword(getContext(), password);
                    PreferenceStorage.setRememberMe(getContext(), true);
                } else {
                    PreferenceStorage.saveUsername(getContext(), "");
                    PreferenceStorage.savePassword(getContext(), "");
                    PreferenceStorage.setRememberMe(getContext(), false);
                }
            }
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
}