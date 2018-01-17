package com.palprotech.heylaapp.pageradapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.palprotech.heylaapp.fragment.FavouriteFragment;
import com.palprotech.heylaapp.fragment.HotspotFragment;
import com.palprotech.heylaapp.fragment.LeaderboardFragment;
import com.palprotech.heylaapp.fragment.PopularFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 16-01-2018.
 */

public class LandingPagerAdapter
//        extends FragmentPagerAdapter
{

    /*private static final String TAG = LandingPagerAdapter.class.getName();

    Context context;
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
//    private final String[] TITLES = {"FAVOURITES","POPULAR", "HOTSPOT", "LEADERBOARD"};
//    private final String[] TITLES1 = {"HEYLA", "HEYLA", "HEYLA", "HEYLA"};
    //    private List<String> mTabResources = new ArrayList<String>();
    private List<Integer> mTabResources = new ArrayList<Integer>();
    private List<Integer> mUnselectedTabResources = new ArrayList<Integer>();
    //    private List<String> mUnselectedTabResources = new ArrayList<String>();
    onFragmentsRegisteredListener onFragmentsRegisteredListener;
    private boolean instantiated;

    public LandingPagerAdapter(onFragmentsRegisteredListener onFragmentsRegisteredListener, FragmentManager fm, Context context) {
        super(fm);
        this.onFragmentsRegisteredListener = onFragmentsRegisteredListener;
        instantiated = false;
        this.context = context;


    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "getItem called" + position);

        switch (position) {
            case 0:
                Log.d(TAG, "returning Landing page fragment");
                return FavouriteFragment.newInstance(position);
//            return  LandingPagerFragment.newInstance(position);
            case 1:
                Log.d(TAG, "returning Popular fragment");
                return PopularFragment.newInstance(position);
//                return LandingPagerFragment.newInstance(position);
            case 2:
                Log.d(TAG, "returning Nearby fragment");
//                return NearbyFragment.newInstance(position);
                return HotspotFragment.newInstance(position);
            case 3:
                return LeaderboardFragment.newInstance(position);
        }
        return null;
    }

    @Override
    public int getCount() {
//        return TITLES.length;
        return 0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //  Log.d(TAG, "Instantiate page item" + position);
        *//*if(position>2){
            Intent navigation = new Intent(context, MapsActivity.class);
            navigation.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(navigation);
            return null;
        }else {*//*
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
        //}
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.d(TAG, "destroy item" + position);
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        Log.d(TAG, "getting registered fragment");
        return registeredFragments.get(position);
    }

    public interface onFragmentsRegisteredListener {
        void onFragmentsRegistered();
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
        Log.d(TAG, "finishedUpdating the PagerAdapter");
        //if (!instantiated) {
        instantiated = true;
        if (onFragmentsRegisteredListener != null) {
            onFragmentsRegisteredListener.onFragmentsRegistered();
        }
        //}
    }*/
}
