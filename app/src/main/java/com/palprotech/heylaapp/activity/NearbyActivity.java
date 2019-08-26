package com.palprotech.heylaapp.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.adapter.EventsListAdapter;
import com.palprotech.heylaapp.bean.support.Event;
import com.palprotech.heylaapp.bean.support.EventList;
import com.palprotech.heylaapp.helper.AlertDialogHelper;
import com.palprotech.heylaapp.helper.LocationHelper;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;
import com.palprotech.heylaapp.serviceinterfaces.IServiceListener;
import com.palprotech.heylaapp.utils.CommonUtils;
import com.palprotech.heylaapp.utils.HeylaAppConstants;
import com.palprotech.heylaapp.utils.PreferenceStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Admin on 01-02-2018.
 */

public class NearbyActivity extends AppCompatActivity implements IServiceListener, AdapterView.OnItemClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, OnMapReadyCallback {

    private static final String TAG = NearbyActivity.class.getName();
    Spinner spinNearby;
    ImageView imgMapbg;
    String className;
    private boolean fabExpanded = false;
    ListView loadMoreListView;
    View view;
    protected EventsListAdapter eventsListAdapter;
    protected ArrayList<Event> eventsArrayList;
    protected ProgressDialogHelper progressDialogHelper;
    private ServiceHelper serviceHelper;
    protected boolean isLoadingForFirstTime = true;
    int pageNumber = 0, totalCount = 0;

    //Linear layout holding the Edit submenu
    private FloatingActionButton fabView;
    private LinearLayout layoutFabMapview;
    private LinearLayout layoutFabListView;
    GoogleMap mGoogleMap = null;
    private List<Marker> mAddedMarkers = new ArrayList<Marker>();
    private HashMap<LatLng, Event> mDisplayedEvents = new HashMap<LatLng, Event>();
    Location mLastLocation = null;
    private String listFlag = null;

    MapView mMapView = null;
    private BitmapDescriptor mMapIcon = null;
    private boolean mAddddLocations = true;
    private float mStartX;
    private float mStartY;
    private float mEndX;
    private float mEndY;

    Handler mHandler = new Handler();
    private SearchView mSearchView = null;

    //Define a request code to send to Google Play services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;
    private boolean mNearbySelected = false;
    private int mTotalReceivedEvents = 0;
    private ProgressDialog mLocationProgress = null;
    private int nearByDistance = 0;
    private TextView mTotalEventCount = null;
    private ImageView imgFiller;
    private String eventType;
    private RelativeLayout mainl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_right, 0);
        setContentView(R.layout.activity_nearby_event);
        mMapView = (MapView) findViewById(R.id.mapview);
        mMapView.onCreate(savedInstanceState);
        iniView();

        callGetFilterService(35);

        layoutFabMapview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMoreListView.setVisibility(View.GONE);
                LocationHelper.FindLocationManager(getApplicationContext());

                mMapView.setVisibility(View.VISIBLE);
                performSlideLeftAnimation();
            }
        });

        layoutFabListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listFlag = "Full";
                performSlideRightAnimation();
                setUpGoogleMaps();
                loadMoreListView.setVisibility(View.VISIBLE);
                if (eventsListAdapter != null) {
                    eventsListAdapter.notifyDataSetChanged();
                }
            }
        });
    }

//    private void closeSubMenusFab() {
//        layoutFabListView.setVisibility(View.INVISIBLE);
//        layoutFabMapview.setVisibility(View.INVISIBLE);
//        fabView.setImageResource(R.drawable.ic_plus);
//        fabExpanded = false;
//    }
//
//    //Opens FAB submenus
//    private void openSubMenusFab() {
//        layoutFabListView.setVisibility(View.VISIBLE);
//        layoutFabMapview.setVisibility(View.VISIBLE);
////        Change settings icon to 'X' icon
//        fabView.setImageResource(R.drawable.ic_close);
//        fabExpanded = true;
//    }
//closes FAB submenus
private void closeSubMenusFab() {
    layoutFabListView.setVisibility(View.INVISIBLE);
//    layoutFabNearby.setVisibility(View.INVISIBLE);
    layoutFabMapview.setVisibility(View.INVISIBLE);
    fabView.setImageResource(R.drawable.ic_plus_icon);
    mainl.setForeground(ContextCompat.getDrawable(this, R.color.transparent) );
    fabExpanded = false;
//    Animation show_fab_1 = AnimationUtils.loadAnimation(this, R.anim.fab_show);
//    Animation hide_fab_1 = AnimationUtils.loadAnimation(this, R.anim.fab_hide);
//
//    layoutFabListView.startAnimation(hide_fab_1);
//    layoutFabMapview.startAnimation(hide_fab_1);
}

    //Opens FAB submenus
    private void openSubMenusFab() {
        layoutFabListView.setVisibility(View.VISIBLE);
        layoutFabMapview.setVisibility(View.VISIBLE);
        mainl.setForeground(ContextCompat.getDrawable(this, R.color.light_line_color) );
//        Change settings icon to 'X' icon
        fabView.setImageResource(R.drawable.ic_cross_icon);
        fabExpanded = true;
//        Animation show_fab_1 = AnimationUtils.loadAnimation(this, R.anim.fab_show);
//        Animation hide_fab_1 = AnimationUtils.loadAnimation(this, R.anim.fab_hide);
//
//        layoutFabListView.startAnimation(show_fab_1);
//        layoutFabMapview.startAnimation(show_fab_1);
    }


    public void callGetFilterService(int kms) {
        /*if(eventsListAdapter != null){
            eventsListAdapter.clearSearchFlag();
        }*/

        if (eventsArrayList != null)
            eventsArrayList.clear();

        if (CommonUtils.isNetworkAvailable(this)) {
            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
            //    eventServiceHelper.makeRawRequest(FindAFunConstants.GET_ADVANCE_SINGLE_SEARCH);
            makeEventListServiceCall(kms);
        } else {
            AlertDialogHelper.showSimpleAlertDialog(this, getString(R.string.no_connectivity));
        }
    }

    private void makeEventListServiceCall(int kms) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.KEY_EVENT_TYPE, eventType);
            jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(this));
            jsonObject.put(HeylaAppConstants.KEY_USER_TYPE, PreferenceStorage.getUserType(this));
            jsonObject.put(HeylaAppConstants.CURRENT_LATITUDE, currentLatitude);
            jsonObject.put(HeylaAppConstants.CURRENT_LONGITUDE, currentLongitude);
            jsonObject.put(HeylaAppConstants.NEAR_BY_DISTANCE, nearByDistance);
            jsonObject.put(HeylaAppConstants.PARAMS_CITY_ID, PreferenceStorage.getEventCityId(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.EVENT_NEAR_BY;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    @Override
    public void onResponse(final JSONObject response) {
        Log.d("ajazFilterresponse : ", response.toString());

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                progressDialogHelper.hideProgressDialog();
                // loadMoreListView.onLoadMoreComplete();

                Gson gson = new Gson();
                EventList eventsList = gson.fromJson(response.toString(), EventList.class);
                if (eventsList.getEvents() != null && eventsList.getEvents().size() > 0) {
                    totalCount = eventsList.getCount();
                    //isLoadingForFirstTime = false;
                    if (totalCount != 0) {
                        imgMapbg.setVisibility(View.GONE);
                    }
                    imgFiller.setVisibility(View.GONE);
                    updateListAdapter(eventsList.getEvents());
                }
            }
        });
    }

    @Override
    public void onError(String error) {
        AlertDialogHelper.showSimpleAlertDialog(NearbyActivity.this, getApplicationContext().getResources().getString(R.string.error_occured));
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                progressDialogHelper.hideProgressDialog();
                // loadMoreListView.onLoadMoreComplete();
                AlertDialogHelper.showSimpleAlertDialog(NearbyActivity.this, getApplicationContext().getResources().getString(R.string.error_occured));
            }
        });
    }

    protected void updateListAdapter(ArrayList<Event> eventsArrayList) {
        mTotalEventCount.setText(Integer.toString(eventsArrayList.size()) + " Nearby Events");
        this.eventsArrayList.addAll(eventsArrayList);
        if (eventsListAdapter == null) {
            eventsListAdapter = new EventsListAdapter(this, this.eventsArrayList, className);
            loadMoreListView.setAdapter(eventsListAdapter);
        } else {
            eventsListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onEvent list item clicked" + position);
        Event event = null;
        if ((eventsListAdapter != null) && (eventsListAdapter.ismSearching())) {
            Log.d(TAG, "while searching");
            int actualindex = eventsListAdapter.getActualEventPos(position);
            Log.d(TAG, "actual index" + actualindex);
            event = eventsArrayList.get(actualindex);
        } else {
            event = eventsArrayList.get(position);
        }
        Intent intent = new Intent(this, EventDetailActivity.class);
        intent.putExtra("eventObj", event);
        // intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void iniView() {
// initiate functions
        setUpGoogleMaps();
        className = this.getClass().getSimpleName();
        loadMoreListView = (ListView) findViewById(R.id.listView_events);
        //loadMoreListView.setOnLoadMoreListener(this);
        loadMoreListView.setOnItemClickListener(this);
        mTotalEventCount = (TextView) findViewById(R.id.totalnearby);
        imgMapbg = (ImageView) findViewById(R.id.nearby_bg);
        imgFiller = (ImageView) findViewById(R.id.logo);
        eventType = getIntent().getStringExtra("event_type");
        eventsArrayList = new ArrayList<>();
        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        findViewById(R.id.back_res).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        layoutFabListView = (LinearLayout) findViewById(R.id.layoutFabListView);
        layoutFabMapview = (LinearLayout) findViewById(R.id.layoutFabMapView);

        fabView = (FloatingActionButton) findViewById(R.id.viewOptions);
        mainl = (RelativeLayout) findViewById(R.id.layout);

        fabView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabExpanded) {
                    closeSubMenusFab();
                } else {
                    openSubMenusFab();
                }
            }
        });
        closeSubMenusFab();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        spinNearby = (Spinner) findViewById(R.id.nearbyspinner);

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, R.layout.spinner_item_ns, getResources().getStringArray(R.array.nearby_distance));
        spinNearby.setAdapter(dataAdapter2);
        /*int index = PreferenceStorage.getFilterEventTypeIndex(this);
        if((index >=0) && index < (getResources().getStringArray(R.array.nearby_distance).length)){
            spinnearby.setSelection(index);
        }*/

        spinNearby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

                if (item.equalsIgnoreCase("Select Distance Range")) {
                    nearByDistance = 0;
                } else {
                    item = item.replace(" kms", "");
                    int distance = Integer.parseInt(item);
                    eventsArrayList.clear();
                    progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
                    //PreferenceStorage.saveFilterEventTypeSelection(getApplicationContext(), position);
                    //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

                    nearByDistance = distance;
                    callGetFilterService(distance);
                    // mTotalEventCount.setText(Integer.toString(eventsArrayList.size()) + " Nearby Events");
                }
                closeSubMenusFab();
                listFlag = "Full";
                performSlideRightAnimation();

                loadMoreListView.setVisibility(View.VISIBLE);
                if (eventsListAdapter != null) {
                    eventsListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
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
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

            //  Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
        }
        fetchCurrentLocation();
    }

    private void fetchCurrentLocation() {
        Log.d(TAG, "fetch the current location");
        try {
            try {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
            } catch (SecurityException e) {

            }
            if (mLocationProgress != null) {
                mLocationProgress.cancel();
            }
            if (mNearbySelected && (mLastLocation != null)) {
                mTotalReceivedEvents = 0;
//                callGetEventService(1);

            }
            if (mLastLocation == null) {
                Log.e(TAG, "Received location is null");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
/*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        Log.w("myApp", currentLatitude + " WORKS " + currentLongitude);

        // Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
        mMapView.onResume();

        if ((mGoogleApiClient != null) && !mGoogleApiClient.isConnected()) {
            Log.d(TAG, "make api connect");
            mGoogleApiClient.connect();

        } else {
            Log.e(TAG, "googleapi is null");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private void setUpGoogleMaps() {
        Log.d(TAG, "Setting up google maps");
        buildGoogleApiClient();
        mGoogleMap = null;
        mMapView.getMapAsync(this);
        mAddedMarkers.clear();
        mDisplayedEvents.clear();

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        try {
            MapsInitializer.initialize(this.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void buildGoogleApiClient() {
        Log.d(TAG, "Initiate GoogleApi connection");
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    private void showMapsView() {

        if (mMapView.getVisibility() == View.VISIBLE) {
            Log.d(TAG, "displaying the Map view");
            //fetch the lat and longitudes
            int i = 0;

            if (mMapIcon == null) {
                mMapIcon = BitmapDescriptorFactory.fromResource(R.drawable.location_dot_img);
            }

            for (Event event : eventsArrayList) {
                if ((event.getEventLatitude() != null) && (event.getEventLongitude() != null)) {
                    double lat = Double.parseDouble(event.getEventLatitude());
                    double longitude = Double.parseDouble(event.getEventLongitude());
                    if ((lat > 0) | (longitude > 0)) {
                        LatLng pos = new LatLng(lat, longitude);
                        if ((pos != null) && (mGoogleMap != null)) {
                            Log.d(TAG, "has lat lon" + "lat:" + event.getEventLatitude() + "long:" + event.getEventLongitude());

                            Marker marker = null;
                            if (mMapIcon != null) {
                                Log.d(TAG, "Valid bitmap icon");
                                marker = mGoogleMap.addMarker(new MarkerOptions().position(pos).icon(mMapIcon));

                            } else {
                                Log.d(TAG, "No valid map icon");
                                marker = mGoogleMap.addMarker(new MarkerOptions().position(pos));
                            }

                            mAddedMarkers.add(marker);
                            mDisplayedEvents.put(pos, event);
                            marker.showInfoWindow();
                        } else {
                            Log.d(TAG, "Google maps was not created properly");
                        }
                    }
                }
            }
            //zoom the camera to current location
            if (mLastLocation != null) {
                LatLng pos = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 14), 1000, null);
            }

            mAddddLocations = false;
        }
    }

    private void performSlideLeftAnimation() {
        PropertyValuesHolder transX = PropertyValuesHolder.ofFloat("x", mEndX, mStartX);
        PropertyValuesHolder alphaV = PropertyValuesHolder.ofFloat("alpha", 1, 0);

        ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(mMapView, transX);
        ObjectAnimator alphaAnim = ObjectAnimator.ofPropertyValuesHolder(loadMoreListView, alphaV);
        anim.setDuration(500);
        alphaAnim.setDuration(500);

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                loadMoreListView.setVisibility(View.GONE);

                if (mAddddLocations) {
                    showMapsView();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.start();
    }

    private void performSlideRightAnimation() {
        PropertyValuesHolder transX = PropertyValuesHolder.ofFloat("x", mStartX, mEndX);
        PropertyValuesHolder alphaV = PropertyValuesHolder.ofFloat("alpha", 1, 0);

        ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(mMapView, transX);

        anim.setDuration(500);


        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mMapView.setVisibility(View.GONE);
                loadMoreListView.setVisibility(View.VISIBLE);
                if (eventsListAdapter != null) {
                    eventsListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.start();
        mAddddLocations = true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "ON Google map ready");
        try {
            mGoogleMap = googleMap;
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
            mGoogleMap.setMyLocationEnabled(true);
            if (mGoogleMap != null) {
                mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        Log.d(TAG, "Getting the info view contents");

                        View infowindow = getLayoutInflater().inflate(R.layout.map_info_window_layout, null);
                        LatLng pos = marker.getPosition();
                        if (pos != null) {
                            Event event = mDisplayedEvents.get(pos);

                            if (event != null) {
                                TextView title = (TextView) infowindow.findViewById(R.id.info_window_Title);
                                TextView subTitle = (TextView) infowindow.findViewById(R.id.info_window_subtext);
                                String eventname = event.getEventName();
                                if ((eventname != null) && !eventname.isEmpty()) {
                                    if (eventname.length() > 15) {
                                        Log.d(TAG, "length more that 15");
                                        String substr = eventname.substring(0, 14);
                                        Log.d(TAG, "title is" + substr);
                                        title.setText(substr + "..");
                                    } else {
                                        Log.d(TAG, "title less that 15 is" + eventname);
                                        title.setText(eventname);
                                    }
                                }

                                subTitle.setText(event.getEventName());
                            }
                        }
                        return infowindow;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        return null;
                    }
                });
            }
            mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    LatLng pos = marker.getPosition();
                    Log.d(TAG, "Marker Info window clicked");

                    Event event = mDisplayedEvents.get(pos);
                    if (event != null) {
                        Log.d(TAG, "map info view clicked");
                        Intent intent = new Intent(getApplicationContext(), EventDetailActivity.class);
                        intent.putExtra("eventObj", event);
                        startActivity(intent);
                    }
                }
            });
            mGoogleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    // mMapLoaded = true;
                    Log.d(TAG, "Map loaded");

                }
            });
        } catch (SecurityException e) {

        }
    }
}
