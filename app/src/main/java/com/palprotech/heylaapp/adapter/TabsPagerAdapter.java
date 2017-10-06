package com.palprotech.heylaapp.adapter;

/**
 * Created by Narendar on 06/10/17.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.palprotech.heylaapp.fragment.SignInFragment;
import com.palprotech.heylaapp.fragment.SignUpFragment;


public class TabsPagerAdapter extends FragmentStatePagerAdapter {

    private String[] titles = {"Sign In", "Sign Up"};

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch(i){
            case 0:
                return new SignInFragment();
            case 1:
                return new SignUpFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}