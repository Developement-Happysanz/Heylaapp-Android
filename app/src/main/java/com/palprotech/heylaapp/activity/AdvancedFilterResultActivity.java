package com.palprotech.heylaapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.adapter.EventsListAdapter;
import com.palprotech.heylaapp.bean.support.Event;
import com.palprotech.heylaapp.bean.support.EventList;
import com.palprotech.heylaapp.helper.AlertDialogHelper;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.interfaces.DialogClickListener;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;
import com.palprotech.heylaapp.serviceinterfaces.IServiceListener;
import com.palprotech.heylaapp.utils.CommonUtils;
import com.palprotech.heylaapp.utils.HeylaAppConstants;
import com.palprotech.heylaapp.utils.PreferenceStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Narendar on 27/11/17.
 */

public class AdvancedFilterResultActivity extends AppCompatActivity implements IServiceListener, AdapterView.OnItemClickListener, DialogClickListener {
    private static final String TAG = "AdvaSearchResAct";
    protected ListView loadMoreListView;
    View view;
    String className;
    EventsListAdapter eventsListAdapter;
    private ServiceHelper serviceHelper;
    ArrayList<Event> eventsArrayList;
    int pageNumber = 0, totalCount = 0;
    protected ProgressDialogHelper progressDialogHelper;
    protected boolean isLoadingForFirstTime = true;
    Handler mHandler = new Handler();
    private SearchView mSearchView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search_result);
//        getSupportActionBar().hide();
        loadMoreListView = findViewById(R.id.listView_events);
//        loadMoreListView.setOnLoadMoreListener(this);
        loadMoreListView.setOnItemClickListener(this);
        className = this.getClass().getSimpleName();
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
        callGetFilterService();
    }

    public void callGetFilterService() {
        /*if(eventsListAdapter != null){
            eventsListAdapter.clearSearchFlag();
        }*/
        if (eventsArrayList != null)
            eventsArrayList.clear();

        if (CommonUtils.isNetworkAvailable(this)) {
            JSONObject jsonObject = new JSONObject();
            try {

                jsonObject.put(HeylaAppConstants.SINGLEDATEFILTER, PreferenceStorage.getFilterSingleDate(this));
                jsonObject.put(HeylaAppConstants.FROMDATE, PreferenceStorage.getFilterFromDate(this));
                jsonObject.put(HeylaAppConstants.TODATE, PreferenceStorage.getFilterToDate(this));
                jsonObject.put(HeylaAppConstants.FILTERCITY, PreferenceStorage.getFilterCity(this));
                jsonObject.put(HeylaAppConstants.FILTEREVENTTYPE, PreferenceStorage.getFilterEventType(this));
                jsonObject.put(HeylaAppConstants.FILTEREVENTCATEGORY, PreferenceStorage.getFilterEventCategory(this));
                jsonObject.put(HeylaAppConstants.FILTERPREF, PreferenceStorage.getFilterPreference(this));
                jsonObject.put(HeylaAppConstants.FILTERRANGE, PreferenceStorage.getFilterRange(this));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
            String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.GET_ADVANCE_SINGLE_SEARCH;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
        } else {
            AlertDialogHelper.showSimpleAlertDialog(this, getString(R.string.no_connectivity));
        }

    }

    @Override
    public void onResponse(final JSONObject response) {
        Log.d("ajazFilterresponse : ", response.toString());

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                progressDialogHelper.hideProgressDialog();
//                loadMoreListView.onLoadMoreComplete();

                Gson gson = new Gson();
                EventList eventsList = gson.fromJson(response.toString(), EventList.class);
                if (eventsList.getEvents() != null && eventsList.getEvents().size() > 0) {
                    totalCount = eventsList.getCount();
                    isLoadingForFirstTime = false;
                    updateListAdapter(eventsList.getEvents());
                }
            }
        });
    }

    @Override
    public void onError(final String error) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                progressDialogHelper.hideProgressDialog();
//                loadMoreListView.onLoadMoreComplete();
                AlertDialogHelper.showSimpleAlertDialog(AdvancedFilterResultActivity.this, error);
            }
        });
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
        String eventType = "";
        if (PreferenceStorage.getFilterEventType(this).equalsIgnoreCase("Hotspot")) {
            eventType = "Hotspot";
        } else {
            eventType = "General";
        }
        Intent intent = new Intent(this, EventDetailActivity.class);
        intent.putExtra("eventObj", event);
        intent.putExtra("eventType", eventType);
        // intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    protected void updateListAdapter(ArrayList<Event> eventsArrayList) {
        this.eventsArrayList.addAll(eventsArrayList);
        if (eventsListAdapter == null) {
            eventsListAdapter = new EventsListAdapter(this, this.eventsArrayList, className);
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
            //loadMoreListView.invalidateViews();
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
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }
}
