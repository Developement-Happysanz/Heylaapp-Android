package com.palprotech.heylaapp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.utils.PreferenceStorage;

import static android.content.Context.MODE_PRIVATE;

public class SignInFragment extends Fragment implements View.OnClickListener {

    EditText edtUsername, edtPassword;
    View rootView;
    Button signIn;

    private String username, password;
    private CheckBox saveLoginCheckBox;
    private Boolean saveLogin;

    public static SignInFragment newInstance(int position) {
        SignInFragment frag = new SignInFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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

        saveLogin = PreferenceStorage.isRemembered(getContext());
        if (saveLogin == true) {
            edtUsername.setText(PreferenceStorage.getUsername(getContext()));
            edtPassword.setText(PreferenceStorage.getPassword(getContext()));
            saveLoginCheckBox.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == signIn) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtUsername.getWindowToken(), 0);

            username = edtUsername.getText().toString();
            password = edtPassword.getText().toString();

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