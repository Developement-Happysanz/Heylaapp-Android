package com.palprotech.heylaapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
        password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_login_password,0,R.drawable.ic_login_show_password,0);
        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                Boolean hide = true;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (password.getRight() - password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        password.setTransformationMethod(new PasswordTransformationMethod());
                        password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_login_password,0,R.drawable.ic_login_hide_password,0);

                        return true;
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_DOWN){
                    password.setTransformationMethod(new HideReturnsTransformationMethod());
                    password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_login_password,0,R.drawable.ic_login_show_password,0);
                    return true;
                }
                return false;
            }
        });

        return rootView;
    }

    protected void initializeViews() {
        username = rootView.findViewById(R.id.username);
        password = rootView.findViewById(R.id.password);
        signin = rootView.findViewById(R.id.signin);
    }
}