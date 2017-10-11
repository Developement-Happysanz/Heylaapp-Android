package com.palprotech.heylaapp.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.adapter.TabsPagerAdapter;
import com.palprotech.heylaapp.customview.PagerSlidingTabStrip;

/**
 * Created by Narendar on 06/10/17.
 */

public class LoginActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, TabsPagerAdapter.onFragmentsRegisteredListener {

    private ViewPager viewPager;
    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private TabsPagerAdapter landingPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mPagerSlidingTabStrip.setShouldExpand(true);
        landingPagerAdapter = new TabsPagerAdapter(this, getSupportFragmentManager(), getApplicationContext());
        viewPager.setAdapter(landingPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        mPagerSlidingTabStrip.setViewPager(viewPager);
        mPagerSlidingTabStrip.setOnPageChangeListener(this);

        mPagerSlidingTabStrip.setViewPager(viewPager);
    }

    @Override
    public void onFragmentsRegistered() {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}