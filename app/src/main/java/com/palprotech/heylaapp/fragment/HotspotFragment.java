package com.palprotech.heylaapp.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
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
import com.palprotech.heylaapp.activity.NearbyActivity;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Narendar on 28/10/17.
 */

public class HotspotFragment extends Fragment implements AdapterView.OnItemClickListener, IServiceListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = HotspotFragment.class.getName();
    private String listFlag = null;
    String className;
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

    private SearchView mSearchView = null;

    public static HotspotFragment newInstance(int position) {
        HotspotFragment frag = new HotspotFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_hotspot, container, false);
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


//                mTotalEventCount.setText(Integer.toString(eventsArrayList.size()) + " Favorite Events");
            }
        });

        layoutFabNearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                performSlideRightAnimation();

                Intent homeIntent = new Intent(getActivity(), NearbyActivity.class);
                homeIntent.putExtra("event_type", "Hotspot");
                startActivity(homeIntent);
            }
        });

        layoutFabListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //mMapView.setVisibility(View.GONE);
                listFlag = "Full";
                performSlideRightAnimation();
                //LoadListView(response);
               /* mLocationBtn.setBackgroundDrawable(mLocationUnselected);
                listAppearence.setBackgroundDrawable(mListSelected);
                listAppearenceNearBy.setBackgroundDrawable(mNearbyTabUnselected);
                mLocationBtn.setImageDrawable(munselectedlocationicon);
                listAppearence.setImageDrawable(mselectedlisticon);
                listAppearenceNearBy.setImageDrawable(munselectednearbyicon);

                mTotalEventCount.setText(Integer.toString(eventsArrayList.size()) + " Favorite Events");*/

          /*      final Dialog dialog = new Dialog(getContext(),android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
                dialog.setContentView(R.layout.transparent_favourite);
                dialog.show();

                if (isFirstRunList) {

                    final TextView txtList = (TextView) dialog.findViewById(R.id.trans_evntlist);

                    txtList.setVisibility(View.VISIBLE);
                    txtList.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();

                        }
                    });

                    isFirstRunList=false;
                    TransPrefs.edit().putBoolean("isFirstRunList", isFirstRunList).commit();


                } else {
                    dialog.dismiss();

                } */

                loadMoreListView.setVisibility(View.VISIBLE);
                if (eventsListAdapter != null) {
                    eventsListAdapter.notifyDataSetChanged();
                }
            }
        });
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {



        inflater.inflate(R.menu.menu_landing, menu);
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        mSearchView =
                (SearchView) menu.findItem(R.id.action_search_view).getActionView();
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));

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

                if (s != null) {
                    searchForEvent(s);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
//                int currentpage = viewPager.getCurrentItem();
//                Log.d(TAG, "current item is" + currentpage);

                if ((s != null) && (!s.isEmpty())) {
                    if (s != null) {
                        searchForEvent(s);
                    }
                } else {
                    if (s != null) {
                        Log.d(TAG, "call exit search");
                        exitSearch();
                    }
                }

                return false;
            }
        });

        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {

            @Override
            public boolean onClose() {
                Log.d(TAG, "searchView closed");

                exitSearch();

                return false;
            }
        });

        mSearchView.setQueryHint("Search Event name");

        super.onCreateOptionsMenu(menu, inflater);
    }

    protected void initializeViews() {
        Log.d(TAG, "initialize pull to refresh view");
        loadMoreListView = (ListView) rootView.findViewById(R.id.listView_events);
        className = this.getClass().getSimpleName();
       /* mNoEventsFound = (TextView) view.findViewById(R.id.no_home_events);
        if (mNoEventsFound != null)
            mNoEventsFound.setVisibility(View.GONE);
        loadMoreListView.setOnLoadMoreListener(this); */
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
       /* if(mGoogleMap != null){
            mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    Log.d(TAG,"Getting the info view contents");

                    View infowindow = getActivity().getLayoutInflater().inflate(R.layout.map_info_window_layout, null);
                    LatLng pos = marker.getPosition();
                    if(pos != null) {
                        Event event = mDisplayedEvents.get(pos);
                        if(event != null) {
                            TextView title = (TextView) infowindow.findViewById(R.id.info_window_Title);
                            TextView subTitle = (TextView) infowindow.findViewById(R.id.info_window_subtext);
                            String eventname = event.getEventName();
                            if((eventname != null) && !eventname.isEmpty()){
                                if(eventname.length() > 15){
                                    Log.d(TAG,"length more that 15");
                                    String substr = eventname.substring(0,14);
                                    Log.d(TAG,"title is"+ substr);
                                    title.setText(substr + "..");
                                }else{
                                    Log.d(TAG,"title less that 15 is"+ eventname);
                                    title.setText(eventname);
                                }
                            }
                           // title.setText(event.getEventName());
                            subTitle.setText(event.getCategoryName());
                        }
                    }
                    return infowindow;
                }

                @Override
                public View getInfoContents(Marker marker) {
                   return null;
                }
            });
        }*/

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        try {
            MapsInitializer.initialize(this.getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Updates the location and zoom of the MapView
        /*CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1, -87.9), 10);
        mGoogleMap.animateCamera(cameraUpdate);*/
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
        /*if(eventsListAdapter != null){
            eventsListAdapter.clearSearchFlag();
        }*/

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

            jsonObject.put(HeylaAppConstants.KEY_EVENT_TYPE, "Hotspot");
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

//    @Override
//    public void onWindowFocusChanged() {
//        Log.d(TAG, "List view coordinates" + loadMoreListView.getX() + "yval" + loadMoreListView.getLeft() + "width" + loadMoreListView.getRight());
//        mStartX = loadMoreListView.getLeft();
//        mEndX = loadMoreListView.getRight();
//    }

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
        //alphaAnim.start();
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
        //alphaAnim.start();
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
                            //mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 14), 1000, null);
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
               /* mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos,10));*/
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
        // intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
//        // getActivity().overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
    }

    @Override
    public void onResponse(JSONObject response) {
        LoadListView(response);
    }

    private void LoadListView(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
//        loadMoreListView.onLoadMoreComplete();
        Gson gson = new Gson();
        EventList eventsList = gson.fromJson(response.toString(), EventList.class);
        if (eventsList != null) {
            Log.d(TAG, "fetched all event list count" + eventsList.getCount());
        }
//        updateListAdapter(eventsList.getEvents());
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
                    if(latitude.get(i) != null){
                        event.setEventLatitude(latitude.get(i));
                    }
                    if(longitude.get(i) != null){
                        event.setEventLongitude(longitude.get(i));
                    }
                    //end of testing
                    mTotalReceivedEvents++;

                    if ((event.getEventLatitude() != null) && (event.getEventLongitude() != null)) {
                        temEventLoc.setLatitude(Double.parseDouble(event.getEventLatitude()));
                        temEventLoc.setLongitude(Double.parseDouble(event.getEventLongitude()));
                        float distance = mLastLocation.distanceTo(temEventLoc);
                        Log.d(TAG, "calculated distance is" + distance);
                        if (distanceFlag==2){
                            if(distance < (5 * 1000)) {
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

        if (totalCount > 0) {
//            mLocationBtn.setEnabled(true);
        } else {
//            mAddddLocations = true;
        }

//        mTotalEventCount.setText(Integer.toString(eventsArrayList.size()) + " Favorite Events");

       /* progressDialogHelper.hideProgressDialog();
        loadMoreListView.onLoadMoreComplete();
        Gson gson = new Gson();
        EventList eventsList = gson.fromJson(response.toString(), EventList.class);
        if (eventsList.getEvents() != null && eventsList.getEvents().size() > 0) {
            totalCount = eventsList.getCount();
            isLoadingForFirstTime = false;
            updateListAdapter(eventsList.getEvents());
        }*/
    }

    protected void updateListAdapter(ArrayList<Event> eventsArrayList) {
        this.eventsArrayList.addAll(eventsArrayList);
       /* if (mNoEventsFound != null)
            mNoEventsFound.setVisibility(View.GONE);*/

        if (eventsListAdapter == null) {
            eventsListAdapter = new EventsListAdapter(getActivity(), this.eventsArrayList, className);
            loadMoreListView.setAdapter(eventsListAdapter);
        } else {
            eventsListAdapter.notifyDataSetChanged();
        }
    }

    public void searchForEvent(String eventname) {
        Log.d(TAG, "searchevent called");
        if (eventsListAdapter != null) {
            eventsListAdapter.startSearch(eventname);
            eventsListAdapter.notifyDataSetChanged();
            loadMoreListView.invalidateViews();
        }
    }

    public void exitSearch() {
        Log.d(TAG, "exit event called");
        if (eventsListAdapter != null) {
            eventsListAdapter.exitSearch();
            eventsListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(String error) {
        if (totalCount > 0) {
//            mLocationBtn.setEnabled(true);
        }
//        mAddddLocations = true;
    }

    private void fetchCurrentLocation() {
        Log.d(TAG, "fetch the current location");
        try {
            try {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
            } catch (SecurityException e) {
//                dialogGPS(this.getContext()); // lets the user know there is a problem with the gps
            }
            // Log.e(TAG, "Current location is" + "Lat" + String.valueOf(mLastLocation.getLatitude()) + "Long" + String.valueOf(mLastLocation.getLongitude()));
            if (mLocationProgress != null) {
                mLocationProgress.cancel();
            }
            if (mNearbySelected && (mLastLocation != null)) {
                mTotalReceivedEvents = 0;
                callGetEventService(1);
                // getNearbyLIst(2);
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
            //getNearbyLIst(2);
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
                                // title.setText(event.getEventName());
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

                /*if ( mGoogleApiClient.isConnected() &&(mLastLocation != null) && (!mDisplayCurrentLocation)) {
                    showMyLocation();
                    // mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
                    //mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
                }*/

                }
            });
        } catch (SecurityException e) {
//            dialogGPS(this.getContext()); // lets the user know there is a problem with the gps
        }
    }
}
