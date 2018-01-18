package com.palprotech.heylaapp.activity;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
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

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.fragment.FavouriteFragment;
import com.palprotech.heylaapp.fragment.HotspotFragment;
import com.palprotech.heylaapp.fragment.LeaderboardFragment;
import com.palprotech.heylaapp.fragment.PopularFragment;
import com.palprotech.heylaapp.helper.AlertDialogHelper;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.interfaces.DialogClickListener;
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


public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, DialogClickListener, IServiceListener {

    private static final String TAG = MainActivity.class.getName();
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

    int checkPointSearch = 0;

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
                                if (PreferenceStorage.getUserType(getApplicationContext()).equalsIgnoreCase("1")) {
                                    changeFragment(3);
                                } else {
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                                    alertDialogBuilder.setTitle("Login");
                                    alertDialogBuilder.setMessage("Log in to Access");
                                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            doLogout();
                                        }
                                    });
                                    alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    alertDialogBuilder.show();
                                }
//                                fabView.setVisibility(View.INVISIBLE);
//                                closeSubMenusFab();
                                break;

                        }
                        return true;
                    }
                });

        sendLoginStatus();

    }

    public void doLogout() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().clear().commit();
//        TwitterUtil.getInstance().resetTwitterRequestToken();

        Intent homeIntent = new Intent(this, SplashScreenActivity.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
        this.finish();
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
            checkPointSearch = 0;
            newFragment = new FavouriteFragment();
        } else if (position == 1) {
            checkPointSearch = 1;
            newFragment = new PopularFragment();
        } else if (position == 2) {
            checkPointSearch = 2;
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
        return super.onCreateOptionsMenu(menu);
    }

    /*@Override
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
//                int currentpage = viewPager.getCurrentItem();
//                Log.d(TAG, "current item is" + currentpage);
                if (checkPointSearch == 0) {
                    FavouriteFragment favouriteFragment = new FavouriteFragment();
                    if (favouriteFragment != null) {
                        favouriteFragment.searchForEvent(s);
                    }
                } else if (checkPointSearch == 1) {
                    PopularFragment popularFragment = new PopularFragment();
                    if (popularFragment != null) {
                        popularFragment.searchForEvent(s);
                    }
                } else if (checkPointSearch ==2){
                    HotspotFragment hotspotFragment = new HotspotFragment();
                    if (hotspotFragment != null) {
                        hotspotFragment.searchForEvent(s);
                    }
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
//                int currentpage = viewPager.getCurrentItem();
//                Log.d(TAG, "current item is" + currentpage);

                if (checkPointSearch == 0) {
                    FavouriteFragment favouriteFragment = new FavouriteFragment();
                    if ((s != null) && (!s.isEmpty())) {
                        if (favouriteFragment != null) {
                            favouriteFragment.searchForEvent(s);
                        }
                    } else {
                        if (favouriteFragment != null) {
                            Log.d(TAG, "call exit search");
                            favouriteFragment.exitSearch();
                        }
                    }
                } else if (checkPointSearch == 1) {
                    PopularFragment popularFragment = new PopularFragment();
                    if ((s != null) && (!s.isEmpty())) {
                        if (popularFragment != null) {
                            popularFragment.searchForEvent(s);
                        }
                    } else {
                        if (popularFragment != null) {
                            Log.d(TAG, "call exit search");
                            popularFragment.exitSearch();
                        }
                    }
                } else if (checkPointSearch ==2){
                    HotspotFragment hotspotFragment = new HotspotFragment();
                    if ((s != null) && (!s.isEmpty())) {
                        if (hotspotFragment != null) {
                            hotspotFragment.searchForEvent(s);
                        }
                    } else {
                        if (hotspotFragment != null) {
                            Log.d(TAG, "call exit search");
                            hotspotFragment.exitSearch();
                        }
                    }
                }

                return false;
            }
        });

        return true;
    }*/

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

        }
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
    }
}
