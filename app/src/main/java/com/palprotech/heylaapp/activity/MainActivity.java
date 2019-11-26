package com.palprotech.heylaapp.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.adapter.SideMenuAdapterTemp;
import com.palprotech.heylaapp.bean.support.EventCities;
import com.palprotech.heylaapp.bean.support.EventCitiesListAll;
import com.palprotech.heylaapp.customview.SideDrawerLayout;
import com.palprotech.heylaapp.customview.SideDrawerToggle;
import com.palprotech.heylaapp.customview.SideMenuView;
import com.palprotech.heylaapp.fragment.AllFragment;
import com.palprotech.heylaapp.fragment.FavouriteFragment;
import com.palprotech.heylaapp.fragment.HotspotFragment;
import com.palprotech.heylaapp.fragment.LeaderboardFragment;
import com.palprotech.heylaapp.fragment.PopularFragment;
import com.palprotech.heylaapp.helper.AlertDialogHelper;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.interfaces.DialogClickListener;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;
import com.palprotech.heylaapp.serviceinterfaces.IServiceListener;
import com.palprotech.heylaapp.utils.CommonUtils;
import com.palprotech.heylaapp.utils.HeylaAppConstants;
import com.palprotech.heylaapp.utils.PreferenceStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements SideMenuView.OnMenuClickListener, LocationListener, View.OnClickListener, ViewPager.OnPageChangeListener, DialogClickListener, IServiceListener {

    private static final String TAG = MainActivity.class.getName();
    private static final int TAG_LOGOUT = 111;
    Toolbar toolbar;
    protected LocationManager locationManager;
    private ViewPager viewPager;
    private SearchView mSearchView = null;

    ListView loadMoreListView;
    ArrayList<EventCities> eventCitiesArrayList;
    Handler mHandler = new Handler();
    int pageNumber = 0, totalCount = 0;
    protected boolean isLoadingForFirstTime = true;

    boolean doubleBackToExitPressedOnce = false;
    Context context;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private String isMenuEnable = "yes";
    String res = "";
    //Linear layout holding the Save submenu
    private LinearLayout layoutFabMapview;

    //Linear layout holding the Edit submenu
    private LinearLayout layoutFabNearby;
    private LinearLayout layoutFabListView;

    private TextView sample;

    private SideMenuAdapterTemp mMenuAdapter;
    private ViewHolder mViewHolder;

    private ArrayList<String> mTitles = new ArrayList<>();

    int checkPointSearch = 0;
    protected Double latitude, longitude;
    String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        initToolBar();
//        View logoView = getToolbarLogoIcon(toolbar);
//        logoView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        changeFragment(0);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    TAG_LOGOUT);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.action_favorites:
//                                changeFragment(0);
                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                                fabView.setVisibility(View.VISIBLE);
                                break;

                            case R.id.action_all:
                                changeFragment(2);
//                                fabView.setVisibility(View.VISIBLE);
                                break;

                            case R.id.action_popular:
                                changeFragment(1);
//                                fabView.setVisibility(View.VISIBLE);
                                break;
                            case R.id.action_hotspot:
                                changeFragment(3);
//                                fabView.setVisibility(View.VISIBLE);
                                break;
                            case R.id.action_leaderboard:
                                if (PreferenceStorage.getUserType(getApplicationContext()).equalsIgnoreCase("1")) {
                                    changeFragment(4);
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
        eventCitiesArrayList = new ArrayList<>();
        GetEventCities();
        sendLoginStatus();

//        mTitles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menuOptions)));

        // Initialize the views
        mViewHolder = new ViewHolder();

        // Handle toolbar actions
        handleToolbar();

        // Handle menu actions
        handleMenu();

        // Handle drawer actions
        handleDrawer();

        // Show main fragment in container
        changeFragment(0);
//        mMenuAdapter.setSelectView(0, true);
//        setTitle(mTitles.get(0));

    }

    public static View getToolbarLogoIcon(Toolbar toolbar) {
        //check if contentDescription previously was set
        boolean hadContentDescription = android.text.TextUtils.isEmpty(toolbar.getLogoDescription());
        String contentDescription = String.valueOf(!hadContentDescription ? toolbar.getLogoDescription() : "logoContentDescription");
        toolbar.setLogoDescription(contentDescription);
        ArrayList<View> potentialViews = new ArrayList<View>();
        //find the view based on it's content description, set programatically or with android:contentDescription
        toolbar.findViewsWithText(potentialViews, contentDescription, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        //Nav icon is always instantiated at this point because calling setLogoDescription ensures its existence
        View logoIcon = null;
        if (potentialViews.size() > 0) {
            logoIcon = potentialViews.get(0);
        }
        //Clear content description if not previously present
        if (hadContentDescription)
            toolbar.setLogoDescription(null);
        return logoIcon;
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
            newFragment = new PopularFragment();
            checkPointSearch = 1;
        } else if (position == 2) {
            checkPointSearch = 2;
            newFragment = new AllFragment();

        } else if (position == 3) {
            checkPointSearch = 3;
            newFragment = new HotspotFragment();
        } else if (position == 4) {
            newFragment = new LeaderboardFragment();
            isMenuEnable = "no";
        }


        getSupportFragmentManager().beginTransaction().replace(
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

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        //Workaround for SearchView close listener
//        switch (item.getItemId()) {
//            case R.id.action_filter:
//                //ajaz
//                // Toast.makeText(this, "advance filter clicked", Toast.LENGTH_SHORT).show();
//                Context appContext = this;
//
//                startActivity(new Intent(MainActivity.this, AdvanceFilterActivity.class));
//                break;
//
//            case R.id.notification_img:
//                if (userId.equalsIgnoreCase("1")) {
//                    startActivity(new Intent(MainActivity.this, NotificationActivity.class));
//                } else {
//                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getApplicationContext());
//                    alertDialogBuilder.setTitle("Login");
//                    alertDialogBuilder.setMessage("Log in to Access");
//                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface arg0, int arg1) {
//                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                            sharedPreferences.edit().clear().apply();
////        TwitterUtil.getInstance().resetTwitterRequestToken();
//
//                            Intent homeIntent = new Intent(getApplicationContext(), SplashScreenActivity.class);
//                            homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            getApplicationContext().startActivity(homeIntent);
//
//                        }
//                    });
//                }
//                break;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.activity_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_heyla_logo_toolbar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        toolbar.setNavigationIcon(R.drawable.ic_sidemenu);
//        toolbar.setNavigationOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent homeIntent = new Intent(getApplicationContext(), MenuActivity.class);
//                        startActivity(homeIntent);
//                    }
//                }
//        );
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
    public void onResponse(final JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        if (validateSignInResponse(response)) {
            try {
                if (res.equalsIgnoreCase("city")) {
                    JSONArray getData = response.getJSONArray("Cities");
                    if (getData != null && getData.length() > 0) {

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressDialogHelper.hideProgressDialog();
                                Gson gson = new Gson();
                                EventCitiesListAll eventCitiesList = gson.fromJson(response.toString(), EventCitiesListAll.class);
                                if (eventCitiesList.getEventCities() != null && eventCitiesList.getEventCities().size() > 0) {
                                    totalCount = eventCitiesList.getCount();
                                    isLoadingForFirstTime = false;
                                    updateListAdapter(eventCitiesList.getEventCities());
//                                try {
////                                    checkCurrentCity();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
                                }
                            }
                        });
                    } else {
                        if (eventCitiesArrayList != null) {
                            eventCitiesArrayList.clear();
                        }
                    }
                    profileInfo();
                } else if (res.equalsIgnoreCase("info")) {
                    JSONObject getData = response.getJSONObject("userData");
                    PreferenceStorage.saveUserPicture(this, getData.getString("picture_url"));
                    PreferenceStorage.saveFullName(this, getData.getString("full_name"));
                } else if (res.equalsIgnoreCase("login")) {
                    Log.d(TAG, "status val" + res);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
        res = "login";
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

    private void profileInfo() {
        res = "info";

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(this));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.USER_INFO;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void checkCurrentCity() throws IOException {
        String selectCity = "";
        selectCity = PreferenceStorage.getEventCityName(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getLocality(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//            if (!(address.equals(selectCity))) {
//                for (int i = 0; i < eventCitiesArrayList.size(); i++)
//                    if(address.equals(eventCitiesArrayList.get(i).getCityName())){
//                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
//                        alertDialogBuilder.setTitle("City change");
//                        alertDialogBuilder.setMessage("New events are available for "+address+" would you like to change city?");
//                        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface arg0, int arg1) {
//                                Intent homeIntent = new Intent(getApplicationContext(), SelectCityActivity.class);
//                                startActivity(homeIntent);
//                            }
//                        });
//                        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                        alertDialogBuilder.show();
//                    }
//            }
        } else {
            long currentTime = System.currentTimeMillis();
            long lastsharedTime = PreferenceStorage.getDailyAskTime(this);
            if ((currentTime - lastsharedTime) > HeylaAppConstants.TWENTY4HOURS) {
                Log.d(TAG, "event time elapsed more than 24hrs");
                showSettingsAlert();
                PreferenceStorage.saveDailyAsktime(this, currentTime);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing the Settings button.
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // On pressing the cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    private void GetEventCities() {
        res = "city";
        if (CommonUtils.isNetworkAvailable(this)) {

            JSONObject jsonObject = new JSONObject();
            userId = PreferenceStorage.getUserId(this);
            try {
                jsonObject.put(HeylaAppConstants.KEY_USER_ID, userId);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
            String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.EVENT_ALL_CITY_LIST;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), url);


        } else {
            AlertDialogHelper.showSimpleAlertDialog(this, "No Network connection");
        }
    }

    protected void updateListAdapter(ArrayList<EventCities> eventCitiesArrayList) {
        this.eventCitiesArrayList.addAll(eventCitiesArrayList);
    }

    private void handleToolbar() {
        setSupportActionBar(mViewHolder.mToolbar);
    }

    private void handleDrawer() {
        SideDrawerToggle SideDrawerToggle = new SideDrawerToggle(this,
                mViewHolder.mSideDrawerLayout,
                mViewHolder.mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        mViewHolder.mSideDrawerLayout.setDrawerListener(SideDrawerToggle);
        SideDrawerToggle.syncState();

    }

    private void handleMenu() {
//        mMenuAdapter = new SideMenuAdapterTemp(mTitles);

        mViewHolder.mSideMenuView.setOnMenuClickListener(this);
//        mViewHolder.mSideMenuView.setAdapter(mMenuAdapter);
    }

    @Override
    public void onFooterClicked() {
        Toast.makeText(this, "onFooterClicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHeaderClicked() {
//        Toast.makeText(this, "onHeaderClicked", Toast.LENGTH_SHORT).show();
        if (PreferenceStorage.getUserType(getApplicationContext()).equalsIgnoreCase("1")) {
            startPersonDetailsActivity(-1);
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
    }

    private void goToFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.add(R.id.container, fragment).commit();
    }

    @Override
    public void onOptionClicked(int position, Object objectClicked) {
        // Set the toolbar title
//        setTitle(mTitles.get(position));
//
//        // Set the right options selected
//        mMenuAdapter.setSelectView(position, true);
//
//        // Navigate to the right fragment
//        switch (position) {
//            default:
//                changeFragment(0);
//                break;
//        }

        // Close the drawer
        mViewHolder.mSideDrawerLayout.closeDrawer();
    }

    private class ViewHolder {
        private SideDrawerLayout mSideDrawerLayout;
        private SideMenuView mSideMenuView;
        private Toolbar mToolbar;

        ViewHolder() {
            mSideDrawerLayout = (SideDrawerLayout) findViewById(R.id.drawer);
            mSideMenuView = (SideMenuView) mSideDrawerLayout.getMenuView();
//            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            mToolbar = (Toolbar) findViewById(R.id.activity_toolbar);

            setSupportActionBar(mToolbar);
        }
    }

    public void startPersonDetailsActivity(long id) {
        Intent homeIntent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivityForResult(homeIntent, 0);
        finish();
    }

}
