package com.palprotech.heylaapp.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.activity.NormalEventDetailActivity;
import com.palprotech.heylaapp.adapter.EventsListAdapter;
import com.palprotech.heylaapp.bean.support.Event;
import com.palprotech.heylaapp.bean.support.EventList;
import com.palprotech.heylaapp.helper.AlertDialogHelper;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;
import com.palprotech.heylaapp.serviceinterfaces.IServiceListener;
import com.palprotech.heylaapp.utils.CommonUtils;
import com.palprotech.heylaapp.utils.HeylaAppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Narendar on 28/10/17.
 */

public class FavouriteFragment extends Fragment implements AdapterView.OnItemClickListener, IServiceListener {

    private static final String TAG = FavouriteFragment.class.getName();
    private View rootView;
    MapView mapView;
    GoogleMap map;
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
        initializeViews();
        initializeEventHelpers();
        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) rootView.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        fabView = (FloatingActionButton) rootView.findViewById(R.id.viewOptions);
        layoutFabListView = (LinearLayout) rootView.findViewById(R.id.layoutFabListView);
        layoutFabNearby = (LinearLayout) rootView.findViewById(R.id.layoutFabNearby);
        layoutFabMapview = (LinearLayout) rootView.findViewById(R.id.layoutFabMapView);

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


        return rootView;
    }

    protected void initializeViews() {
        Log.d(TAG, "initialize pull to refresh view");
        loadMoreListView = (ListView) rootView.findViewById(R.id.listView_events);
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

            jsonObject.put(HeylaAppConstants.PARMAS_OLD_MOBILE_NUMBER, "");
//            jsonObject.put(HeylaAppConstants.PARMAS_NEW_MOBILE_NUMBER, edtMobileNo.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.TEST_EVENT_LIST;
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
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
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

        Intent intent = new Intent(getActivity(), NormalEventDetailActivity.class);
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
        updateListAdapter(eventsList.getEvents());
        int totalNearbyCount = 0;
      /*  if (eventsList.getEvents() != null && eventsList.getEvents().size() > 0) {
            if (mLastLocation != null) {
                Log.d(TAG, "Location is set");
                ArrayList<Event> mNearbyLIst = new ArrayList<Event>();
                Location temEventLoc = new Location("temp");
                //Only add those locations which are within 35km
                int i = 0;
                for (Event event : eventsList.getEvents()) {
                    //Testing. remove later
                   *//* if(latitude.get(i) != null){
                        event.setEventLatitude(latitude.get(i));
                    }
                    if(longitude.get(i) != null){
                        event.setEventLongitude(longitude.get(i));
                    }*//*
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
                        makeEventListServiceCall(pageNumber);
                    }
                } else {
                    Log.d(TAG, "Total received count greater than total count");
                }
            } else {

                isLoadingForFirstTime = false;
                totalCount = eventsList.getCount();
                updateListAdapter(eventsList.getEvents());
            }
        }*/
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
            eventsListAdapter = new EventsListAdapter(getActivity(), this.eventsArrayList);
            loadMoreListView.setAdapter(eventsListAdapter);
        } else {
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
}
