package com.palprotech.heylaapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.palprotech.heylaapp.fragment.AllFragment;
import com.palprotech.heylaapp.fragment.ThisMonthFragment;
import com.palprotech.heylaapp.fragment.ThisWeekFragment;
import com.palprotech.heylaapp.fragment.TodayFragment;
import com.palprotech.heylaapp.fragment.TomorrowFragment;

public class DaySortAdapter extends FragmentStatePagerAdapter {

    public DaySortAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
//                return new AllFragment();
            case 1:
                return new TodayFragment();
            case 2:
                return new TomorrowFragment();
            case 3:
                return new ThisWeekFragment();
            case 4:
                return new ThisMonthFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}