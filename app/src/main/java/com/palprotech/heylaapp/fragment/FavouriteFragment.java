package com.palprotech.heylaapp.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.activity.EventDetailActivity;
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
 * Created by Narendar on 28/10/17.
 */

public class FavouriteFragment extends Fragment implements AdapterView.OnItemClickListener, IServiceListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = FavouriteFragment.class.getName();
    private String listFlag = null;
    private View rootView;
    MapView mMapView = null;
    GoogleMap mGoogleMap = null;
    GoogleApiClient mGoogleApiClient = null;
    private boolean mMapLoaded = false;
    Location mLastLocation = null;
    private List<Marker> mAddedMarkers = new ArrayList<Marker>();
    private HashMap<LatLng, Event> mDisplayedEvents = new HashMap<LatLng, Event>();
    private boolean mAddddLocations = true;
    private TextView mTotalEventCount = null;
    private boolean isFirstRunLocation = true;
    private boolean isFirstRunNearby = true;
    private boolean isFirstRunList = true;
    private SharedPreferences TransPrefs;
    private ProgressDialog mLocationProgress = null;

    private BitmapDescriptor mMapIcon = null;

    private float mStartX;
    private float mStartY;
    private float mEndX;
    private float mEndY;

    private boolean mNearbySelected = false;
    private int mTotalReceivedEvents = 0;

    private int distanceFlag = 1;

    public static final CameraPosition COIMBATORE =
            new CameraPosition.Builder().target(new LatLng(11.00, 77.00))
                    .zoom(15.5f)
                    .bearing(0)
                    .tilt(25)
                    .build();


    HashMap<Integer, String> latitude = new HashMap<Integer, String>();
    HashMap<Integer, String> longitude = new HashMap<Integer, String>();

    protected ListView loadMoreListView;
    protected EventsListAdapter eventsListAdapter;
    protected ArrayList<Event> eventsArrayList;
    //boolean flag to know if main FAB is in open or closed state.
    private boolean fabExpanded = false;
    private FloatingActionButton fabView;
    //Linear layout holding the Save submenu
    private LinearLayout layoutFabMapview;

    //Linear layout holding the Edit submenu
    private LinearLayout layoutFabNearby;
    private LinearLayout layoutFabListView;

    protected ProgressDialogHelper progressDialogHelper;
    private ServiceHelper serviceHelper;
    protected boolean isLoadingForFirstTime = true;
    int pageNumber = 0, totalCount = 0;

    public static FavouriteFragment newInstance(int position) {
        FavouriteFragment frag = new FavouriteFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_favourite, container, false);

        TransPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        isFirstRunLocation = TransPrefs.getBoolean("isFirstRunLocation", true);
        isFirstRunNearby = TransPrefs.getBoolean("isFirstRunNearby", true);
        isFirstRunList = TransPrefs.getBoolean("isFirstRunList", true);

        initializeViews();
        initializeEventHelpers();
        // Gets the MapView from the XML layout and creates it
        listFlag = "Full";

        mAddddLocations = true;
        mMapView = (MapView) rootView.findViewById(R.id.mapview);
        mMapView.onCreate(savedInstanceState);
        setUpGoogleMaps();

        fabView = (FloatingActionButton) rootView.findViewById(R.id.viewOptions);
        layoutFabListView = (LinearLayout) rootView.findViewById(R.id.layoutFabListView);
        layoutFabNearby = (LinearLayout) rootView.findViewById(R.id.layoutFabNearby);
        layoutFabMapview = (LinearLayout) rootView.findViewById(R.id.layoutFabMapView);

        mMapIcon = BitmapDescriptorFactory.fromResource(R.drawable.location_dot_img);

        //When main Fab (Settings) is clicked, it expands if not expanded already.
        //Collapses if main FAB was open already.
        //This gives FAB (Settings) open/close behavior
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

        //Only main FAB is visible in the beginning
        closeSubMenusFab();

        layoutFabMapview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMoreListView.setVisibility(View.GONE);
                LocationHelper.FindLocationManager(getActivity());

                mMapView.setVisibility(View.VISIBLE);
                performSlideLeftAnimation();
            }
        });

        layoutFabListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listFlag = "Full";
                performSlideRightAnimation();

                loadMoreListView.setVisibility(View.VISIBLE);
                if (eventsListAdapter != null) {
                    eventsListAdapter.notifyDataSetChanged();
                }
            }
        });

        return rootView;
    }

    protected void initializeViews() {
        Log.d(TAG, "initialize pull to refresh view");
        loadMoreListView = (ListView) rootView.findViewById(R.id.listView_events);

        loadMoreListView.setOnItemClickListener(this);
        eventsArrayList = new ArrayList<>();
    }

    protected void initializeEventHelpers() {
        serviceHelper = new ServiceHelper(getActivity());
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(getActivity());
        callGetEventService(1);
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
            MapsInitializer.initialize(this.getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void buildGoogleApiClient() {
        Log.d(TAG, "Initiate GoogleApi connection");
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public void callGetEventService(int position) {
        Log.d(TAG, "fetch event list" + position);

        if (isLoadingForFirstTime) {
            Log.d(TAG, "Loading for the first time");
            if (eventsArrayList != null)
                eventsArrayList.clear();

            if (CommonUtils.isNetworkAvailable(getActivity())) {
                progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
                makeEventListServiceCall();
            } else {
                AlertDialogHelper.showSimpleAlertDialog(getActivity(), getString(R.string.no_connectivity));
            }
        } else {
            Log.d(TAG, "Do nothing");
        }
    }

    private void makeEventListServiceCall() {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.KEY_EVENT_TYPE, "General");
            jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(getActivity()));
            jsonObject.put(HeylaAppConstants.KEY_USER_TYPE, PreferenceStorage.getUserType(getActivity()));
            jsonObject.put(HeylaAppConstants.KEY_EVENT_CITY_ID, PreferenceStorage.getEventCityId(getActivity()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.EVENT_LIST;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    //closes FAB submenus
    private void closeSubMenusFab() {
        layoutFabListView.setVisibility(View.INVISIBLE);
        layoutFabNearby.setVisibility(View.INVISIBLE);
        layoutFabMapview.setVisibility(View.INVISIBLE);
        fabView.setImageResource(R.drawable.ic_plus);
        fabExpanded = false;
    }

    //Opens FAB submenus
    private void openSubMenusFab() {
        layoutFabListView.setVisibility(View.VISIBLE);
        layoutFabNearby.setVisibility(View.VISIBLE);
        layoutFabMapview.setVisibility(View.VISIBLE);
//        Change settings icon to 'X' icon
        fabView.setImageResource(R.drawable.ic_close);
        fabExpanded = true;
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
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "Activity created");
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

        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        intent.putExtra("eventObj", event);
        startActivity(intent);
    }

    @Override
    public void onResponse(JSONObject response) {
        LoadListView(response);
    }

    private void LoadListView(JSONObject response) {
        progressDialogHelper.hideProgressDialog();

        Gson gson = new Gson();
        EventList eventsList = gson.fromJson(response.toString(), EventList.class);
        if (eventsList != null) {
            Log.d(TAG, "fetched all event list count" + eventsList.getCount());
        }

        int totalNearbyCount = 0;
        if (eventsList.getEvents() != null && eventsList.getEvents().size() > 0) {
            if (mLastLocation != null) {
                Log.d(TAG, "Location is set");
                ArrayList<Event> mNearbyLIst = new ArrayList<Event>();
                Location temEventLoc = new Location("temp");
                //Only add those locations which are within 35km
                int i = 0;
                for (Event event : eventsList.getEvents()) {
                    //Testing. remove later
                    if (latitude.get(i) != null) {
                        event.setEventLatitude(latitude.get(i));
                    }
                    if (longitude.get(i) != null) {
                        event.setEventLongitude(longitude.get(i));
                    }
                    //end of testing
                    mTotalReceivedEvents++;

                    if ((event.getEventLatitude() != null) && (event.getEventLongitude() != null)) {
                        temEventLoc.setLatitude(Double.parseDouble(event.getEventLatitude()));
                        temEventLoc.setLongitude(Double.parseDouble(event.getEventLongitude()));
                        float distance = mLastLocation.distanceTo(temEventLoc);
                        Log.d(TAG, "calculated distance is" + distance);
                        if (distanceFlag == 2) {
                            if (distance < (5 * 1000)) {
                                mNearbyLIst.add(event);
                            }
                        } else {
                            mNearbyLIst.add(event);
                        }
                    }
                    i++;

                }
                totalNearbyCount = mNearbyLIst.size();
                Log.d(TAG, "Total event close by 35km " + totalNearbyCount);
                isLoadingForFirstTime = false;
                totalCount = eventsList.getCount();
                updateListAdapter(mNearbyLIst);
                if (mTotalReceivedEvents < totalCount) {
                    Log.d(TAG, "fetch remaining events");
                    if (eventsArrayList.size() < 10) {

                        pageNumber = (mTotalReceivedEvents / 10) + 1;

                        Log.d(TAG, "Page number" + pageNumber);
                        makeEventListServiceCall();
                    }
                } else {
                    Log.d(TAG, "Total received count greater than total count");
                }
            } else {

                isLoadingForFirstTime = false;
                totalCount = eventsList.getCount();
                updateListAdapter(eventsList.getEvents());
            }
        }
        // Updates the location and zoom of the MapView

    }

    protected void updateListAdapter(ArrayList<Event> eventsArrayList) {
        this.eventsArrayList.addAll(eventsArrayList);

        if (eventsListAdapter == null) {
            eventsListAdapter = new EventsListAdapter(getActivity(), this.eventsArrayList);
            loadMoreListView.setAdapter(eventsListAdapter);
        } else {
            eventsListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(String error) {
        if (totalCount > 0) {

        }

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
                callGetEventService(1);

            }
            if (mLastLocation == null) {
                Log.e(TAG, "Received location is null");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "API CLient connected. fetch the list");
        fetchCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "Connection to google locations failed");
        if (mLocationProgress != null) {
            mLocationProgress.cancel();
        }
        if (mNearbySelected) {
            callGetEventService(1);
        }
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

                        View infowindow = getActivity().getLayoutInflater().inflate(R.layout.map_info_window_layout, null);
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
                        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
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
