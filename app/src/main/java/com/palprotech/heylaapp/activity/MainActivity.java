package com.palprotech.heylaapp.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.fragment.FavouriteFragment;
import com.palprotech.heylaapp.fragment.HotspotFragment;
import com.palprotech.heylaapp.fragment.LeaderboardFragment;
import com.palprotech.heylaapp.fragment.PopularFragment;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.interfaces.DialogClickListener;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;
import com.palprotech.heylaapp.serviceinterfaces.IServiceListener;

import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, DialogClickListener, IServiceListener {

    private static final String TAG = MainActivity.class.getName();
    private static final int TAG_LOGOUT = 100;
    Toolbar toolbar;
    private ViewPager viewPager;
    boolean doubleBackToExitPressedOnce = false;
    private SearchView mSearchView = null;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolBar();
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        changeFragment(0);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.action_favorites:
                                changeFragment(0);
//                                fabView.setVisibility(View.VISIBLE);
                                break;

                            case R.id.action_popular:
                                changeFragment(1);
//                                fabView.setVisibility(View.VISIBLE);
                                break;
                            case R.id.action_hotspot:
                                changeFragment(2);
//                                fabView.setVisibility(View.VISIBLE);
                                break;
                            case R.id.action_leaderboard:
                                changeFragment(3);
//                                fabView.setVisibility(View.INVISIBLE);
//                                closeSubMenusFab();
                                break;

                        }
                        return true;
                    }
                });

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

    private void changeFragment(int position) {

        Fragment newFragment = null;

        if (position == 0) {
            newFragment = new FavouriteFragment();
        } else if (position == 1) {
            newFragment = new PopularFragment();
        } else if (position == 2) {
            newFragment = new HotspotFragment();
        } else if (position == 3) {
            newFragment = new LeaderboardFragment();
            isMenuEnable = "no";
        }

        getFragmentManager().beginTransaction().replace(
                R.id.fragmentContainer, newFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_landing, menu);

        /*if (isMenuEnable.equalsIgnoreCase("no")) {
            menu.findItem(R.id.action_filter).setVisible(false);
            menu.findItem(R.id.action_search_view).setVisible(false);
        }*/
       /* SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

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

              *//*  LandingPagerFragment landingPagerFragment = (LandingPagerFragment)
                        landingPagerAdapter.getRegisteredFragment(currentpage);
                if (landingPagerFragment != null) {
                    landingPagerFragment.searchForEvent(s);
                }   *//*

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                int currentpage = viewPager.getCurrentItem();
                Log.d(TAG, "current item is" + currentpage);
              *//*  LandingPagerFragment landingPagerFragment = (LandingPagerFragment)
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
                }*//*

                return false;
            }
        });
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {

            @Override
            public boolean onClose() {
                Log.d(TAG, "searchView closed");

                int currentpage = viewPager.getCurrentItem();
                Log.d(TAG, "current item is" + currentpage);
                *//*LandingPagerFragment landingPagerFragment = (LandingPagerFragment)
                        landingPagerAdapter.getRegisteredFragment(currentpage);
                if (landingPagerFragment != null) {
                    Log.d(TAG, "call exit search");
                    landingPagerFragment.exitSearch();
                }*//*

                return false;
            }
        });

        mSearchView.setQueryHint("Search Event name");*/

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

                startActivity(new Intent(MainActivity.this, AdvanceFilterActivity.class));

            default:
                return super.onOptionsItemSelected(item);
        }

        // return super.onOptionsItemSelected(item);
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

    @Override
    public void onClick(View view) {

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

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    @Override
    public void onResponse(JSONObject response) {

    }

    @Override
    public void onError(String error) {

    }
}
