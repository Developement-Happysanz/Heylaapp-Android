package com.palprotech.heylaapp.adapter;

/**
 * Created by Narendar on 06/10/17.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.palprotech.heylaapp.fragment.SignInFragment;
import com.palprotech.heylaapp.fragment.SignUpFragment;


public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // First fragment activity
                return new SignInFragment();
            case 1:
                // Second fragment activity
                return new SignUpFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }

}
