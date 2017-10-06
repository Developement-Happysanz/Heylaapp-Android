package com.palprotech.heylaapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.palprotech.heylaapp.R;

/**
 * Created by Narendar on 06/10/17.
 */

public class SignUpFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);

        return rootView;
    }
}
