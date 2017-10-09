package com.palprotech.heylaapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.palprotech.heylaapp.R;

public class SignInFragment extends Fragment {

    EditText username,password;
    View rootView;
    Button signin;

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
        username = rootView.findViewById(R.id.username);
        password = rootView.findViewById(R.id.password);
        signin = rootView.findViewById(R.id.signin);
    }
}