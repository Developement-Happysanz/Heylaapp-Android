package com.palprotech.heylaapp.activity;

import android.Manifest;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.bean.support.Event;
import com.palprotech.heylaapp.bean.support.EventList;
import com.palprotech.heylaapp.fragment.FavouriteFragment;
import com.palprotech.heylaapp.fragment.HotspotFragment;
import com.palprotech.heylaapp.fragment.LandingPagerFragment;
import com.palprotech.heylaapp.fragment.LeaderboardFragment;
import com.palprotech.heylaapp.fragment.PopularFragment;
import com.palprotech.heylaapp.helper.AlertDialogHelper;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.interfaces.DialogClickListener;
import com.palprotech.heylaapp.pageradapter.LandingPagerAdapter;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;
import com.palprotech.heylaapp.serviceinterfaces.IServiceListener;
import com.palprotech.heylaapp.utils.HeylaAppConstants;
import com.palprotech.heylaapp.utils.PreferenceStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Admin on 16-01-2018.
 */

public class MainNewActivity extends AppCompatActivity
//        implements ViewPager.OnPageChangeListener, LandingPagerAdapter.onFragmentsRegisteredListener, DialogClickListener, IServiceListener
{

  /*  private static final String TAG = MainActivity.class.getName();
    private static final int TAG_LOGOUT = 100;
    Toolbar toolbar;

    private ViewPager viewPager;
    private SearchView mSearchView = null;

    boolean doubleBackToExitPressedOnce = false;
    Context context;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private String isMenuEnable = "yes";

    //Linear layout holding the Save submenu
    private LinearLayout layoutFabMapview;

    //Linear layout holding the Edit submenu
    private LinearLayout layoutFabNearby;
    private LinearLayout layoutFabListView;

    private TextView sample;
    private LandingPagerAdapter landingPagerAdapter;
    private boolean mFragmentsLoaded = false;
    public static final int TAG_FAVOURITES = 0, TAG_FEATURED = 1, TAG_ALL = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolBar();
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);
        context = getApplicationContext();
        landingPagerAdapter = new LandingPagerAdapter(this, getSupportFragmentManager(), getApplicationContext());
        viewPager.setAdapter(landingPagerAdapter);
//        viewPager.setOffscreenPageLimit(3);
        Log.d(TAG, "initializing view pager");
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        doesUserHavePermission();
        doesUserHavePermission1();
        sendLoginStatus();

//        changeFragment(0);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.action_favorites:
//                                changeFragment(0);
                                FavouriteFragment.newInstance(0);
//                                fabView.setVisibility(View.VISIBLE);
                                break;

                            case R.id.action_popular:
                                PopularFragment.newInstance(1);
//                                changeFragment(1);
//                                fabView.setVisibility(View.VISIBLE);
                                break;
                            case R.id.action_hotspot:
                                HotspotFragment.newInstance(2);
//                                changeFragment(2);
//                                fabView.setVisibility(View.VISIBLE);
                                break;
                            case R.id.action_leaderboard:
                                LeaderboardFragment.newInstance(3);
//                                changeFragment(3);
//                                fabView.setVisibility(View.INVISIBLE);
//                                closeSubMenusFab();
                                break;

                        }
                        return true;
                    }
                });
    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.activity_toolbar);

        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_sidemenu);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent homeIntent = new Intent(getApplicationContext(), MenuActivity.class);
                        startActivity(homeIntent);
                    }
                }
        );
    }

    private boolean doesUserHavePermission() {
        int result = context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private boolean doesUserHavePermission1() {

        int result1 = context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);

        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onBackPressed() {
        //Checking for fragment count on backstack
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit.", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
            return;
        }
    }

    *//*private void changeFragment(int position) {

        Fragment newFragment = null;

        if (position == 0) {
            FavouriteFragment.newInstance(position);
        } else if (position == 1) {
            PopularFragment.newInstance(position);
//            newFragment = new PopularFragment();
        } else if (position == 2) {
            HotspotFragment.newInstance(position);
//            newFragment = new HotspotFragment();
        } else if (position == 3) {
            LeaderboardFragment.newInstance(position);
//            newFragment = new LeaderboardFragment();
            isMenuEnable = "no";
        }

        getFragmentManager().beginTransaction().replace(
                R.id.fragmentContainer, newFragment)
                .commit();
    }*//*

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("LandingonPause", "LandingonPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("LandingonResume", "LandingonResume");
        if (PreferenceStorage.getFilterApply(this)) {
            PreferenceStorage.IsFilterApply(this, false);
            String singledate = PreferenceStorage.getFilterSingleDate(this);

            LandingPagerFragment landingPagerFragment = (LandingPagerFragment)
                    landingPagerAdapter.getRegisteredFragment(getTaskId());
            if (landingPagerFragment != null) {
//                checkPointSearch = 1;
                landingPagerFragment.callGetFilterService();
            }

//            LandingPagerFragment landingPagerFragment1 = (LandingPagerFragment)
//                    landingPagerAdapter.getRegisteredFragment(getTaskId());
//            if (landingPagerFragment1 != null) {
//                checkPointSearch = 1;
//                landingPagerFragment1.callGetFilterService();
//            }
//
//            StaticEventFragment staticEventFragment = (StaticEventFragment)
//                    landingPagerAdapter.getRegisteredFragment(getTaskId());
//            if (staticEventFragment != null) {
//                checkPointSearch = 2;
//                staticEventFragment.callGetFilterService();
//            }

            //Toast.makeText(this,"filter service called",Toast.LENGTH_SHORT).show();
        }
    }

    *//*private boolean shouldUploadSocialNetworkPic() {
        boolean upload = false;
        String url = PreferenceStorage.getSocialNetworkProfileUrl(this);
        String userimageUrl = PreferenceStorage.getUserPicture(this);
        int loginMode = PreferenceStorage.getLoginMode(this);
        if ((userimageUrl == null) || (userimageUrl.isEmpty())) {
            if ((loginMode == 1) || (loginMode == 3)) {
                if ((url != null) && !(url.isEmpty())) {
                    Bitmap imageBitmap = ((BitmapDrawable) imgNavProfileImage.getDrawable()).getBitmap();
                    Log.d(TAG, "valid URL present");
                    if (imageBitmap != null) {
                        upload = true;
                    } else {
                        Log.e(TAG, "No Bitmap present");
                    }

                } else {
                    Log.e(TAG, "No image present for social network sites");
                }
            }
        }
        return upload;
    }*//*

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_landi*//*ng, menu);
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.menu_landing, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView =
                (SearchView) menu.findItem(R.id.action_search_view).getActionView();
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        AutoCompleteTextView searchTextView = (AutoCompleteTextView) mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, R.drawable.cursor); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (Exception e) {
        }

        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Search button clicked");
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {

                Log.d(TAG, "Query submitted with String:" + s);
                int currentpage = viewPager.getCurrentItem();
                Log.d(TAG, "current item is" + currentpage);
               *//* if (checkPointSearch == 2) {
                    StaticFragment staticFragment = (StaticFragment)
                            landingPagerAdapter.getRegisteredFragment(currentpage);
                    if (staticFragment != null) {
                        staticFragment.searchForEvent(s);
                    }
                } else if (checkPointSearch == 1) {*//*
                LandingPagerFragment landingPagerFragment = (LandingPagerFragment)
                        landingPagerAdapter.getRegisteredFragment(currentpage);
                if (landingPagerFragment != null) {
                    landingPagerFragment.searchForEvent(s);
                }
//                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                int currentpage = viewPager.getCurrentItem();
                Log.d(TAG, "current item is" + currentpage);
                LandingPagerFragment landingPagerFragment = (LandingPagerFragment)
                        landingPagerAdapter.getRegisteredFragment(currentpage);

                if ((s != null) && (!s.isEmpty())) {
                    if (landingPagerFragment != null) {
                        landingPagerFragment.searchForEvent(s);
                    }
                } else {
                    if (landingPagerFragment != null) {
                        Log.d(TAG, "call exit search");
                        landingPagerFragment.exitSearch();
                    }
                }

                return false;
            }
        });
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {

            @Override
            public boolean onClose() {
                Log.d(TAG, "searchView closed");

                int currentpage = viewPager.getCurrentItem();
                Log.d(TAG, "current item is" + currentpage);
                LandingPagerFragment landingPagerFragment = (LandingPagerFragment)
                        landingPagerAdapter.getRegisteredFragment(currentpage);
                if (landingPagerFragment != null) {
                    Log.d(TAG, "call exit search");
                    landingPagerFragment.exitSearch();
                }

                return false;
            }
        });

        mSearchView.setQueryHint("Search Event name");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //Workaround for SearchView close listener
        switch (item.getItemId()) {
            case R.id.action_filter:
                //ajaz
                // Toast.makeText(this, "advance filter clicked", Toast.LENGTH_SHORT).show();
                Context appContext = this;

                startActivity(new Intent(this, AdvanceFilterActivity.class));

            default:
                return super.onOptionsItemSelected(item);
        }

        // return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d(TAG, "page selected" + position);
        if (landingPagerAdapter != null) {
//            mPagerSlidingTabStrip.notifyDataSetChanged();

            getSupportActionBar().setTitle("HEYLA");
            if (position == 3) { //Rewards Fragment. So search bar is not needed
                if (mSearchView != null) {
                    mSearchView.setVisibility(View.GONE);
                }

            } else {
                if (mSearchView != null) {
                    mSearchView.setVisibility(View.VISIBLE);
                }
                LandingPagerFragment landingPagerFragment = (LandingPagerFragment)
                        landingPagerAdapter.getRegisteredFragment(position);
                if (landingPagerFragment != null) {
                    landingPagerFragment.exitSearch();
                }
            }
        }
        makeGetEventListServiceCall(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void makeGetEventListServiceCall(int position) {
        Log.d(TAG, "Fetching the event list for pos" + position);
        LandingPagerFragment landingPagerFragment = (LandingPagerFragment)
                landingPagerAdapter.getRegisteredFragment(position);
        if (landingPagerFragment != null) {
            landingPagerFragment.callGetEventService(position);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean focus) {
        if (focus) {
            LandingPagerFragment landingPagerFragment = (LandingPagerFragment)
                    landingPagerAdapter.getRegisteredFragment(viewPager.getCurrentItem());
            if (landingPagerFragment != null) {
                landingPagerFragment.onWindowFocusChanged();
            }
        }
    }

    @Override
    public void onFragmentsRegistered() {
        Log.d(TAG, "Fragment registered called");
        // makeGetEventListServiceCall(TAG_FAVOURITES);
        if (!mFragmentsLoaded) {
            Log.d(TAG, "fetch for the first time");
            makeGetEventListServiceCall(TAG_FAVOURITES);
            mFragmentsLoaded = true;
        }
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    private boolean validateSignInResponse(JSONObject response) {
        boolean signInSuccess = false;
        if ((response != null)) {
            try {
                String status = response.getString("status");
                String msg = response.getString(HeylaAppConstants.PARAM_MESSAGE);
                Log.d(TAG, "status val" + status + "msg" + msg);

                if ((status != null)) {
                    if (((status.equalsIgnoreCase("activationError")) || (status.equalsIgnoreCase("alreadyRegistered")) ||
                            (status.equalsIgnoreCase("notRegistered")) || (status.equalsIgnoreCase("error")))) {
                        signInSuccess = false;
                        Log.d(TAG, "Show error dialog");
                        AlertDialogHelper.showSimpleAlertDialog(this, msg);

                    } else {
                        signInSuccess = true;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return signInSuccess;
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        if (validateSignInResponse(response)) {
        Gson gson = new Gson();
        EventList eventsList = gson.fromJson(response.toString(), EventList.class);
        if (eventsList.getEvents() != null && eventsList.getEvents().size() > 0) {
            // totalCount = eventsList.getCount();
//            GamificationDataHolder.getInstance().clearBookmarks();
            for (Event event : eventsList.getEvents()) {
//                GamificationDataHolder.getInstance().addBookmarkedEvent(event.getId());
            }
        }}
    }

    @Override
    public void onError(String error) {
        progressDialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(this, error);
    }

    private void sendLoginStatus() {

        //A user can only get points 1 time a day for login. So restrict beyond that
        long currentTime = System.currentTimeMillis();
        long lastsharedTime = PreferenceStorage.getDailyLoginTime(this);

        if ((currentTime - lastsharedTime) > HeylaAppConstants.TWENTY4HOURS) {
            Log.d(TAG, "event time elapsed more than 24hrs");
            dailyLoginActivity();
            PreferenceStorage.saveDailyLogintime(this, currentTime);
        }
    }

    private void dailyLoginActivity() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        String date_activity = (dateFormat.format(date)).toString();
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(this));
            jsonObject.put(HeylaAppConstants.KEY_RULE_ID, "1");
            jsonObject.put(HeylaAppConstants.KEY_EVENT_ID, "");
            jsonObject.put(HeylaAppConstants.PARAMS_DATE, date_activity);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.USER_ACTIVITY;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }*/
}
