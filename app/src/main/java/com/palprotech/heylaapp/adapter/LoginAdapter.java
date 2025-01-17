package com.palprotech.heylaapp.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.palprotech.heylaapp.fragment.SignInFragment;
import com.palprotech.heylaapp.fragment.SignUpFragment;

/**
 * Created by Admin on 11-10-2017.
 */

public class LoginAdapter extends FragmentStatePagerAdapter {

    public LoginAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SignInFragment();
            case 1:
                return new SignUpFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
