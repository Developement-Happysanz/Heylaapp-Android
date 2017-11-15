package com.palprotech.heylaapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.adapter.EventCitiesAdapter;
import com.palprotech.heylaapp.bean.support.EventCities;
import com.palprotech.heylaapp.bean.support.EventCitiesList;
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

import java.util.ArrayList;

/**
 * Created by Admin on 01-11-2017.
 */

public class SelectCityActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = SelectCityActivity.class.getName();

    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    ListView loadMoreListView;
    EventCitiesAdapter eventCitiesAdapter;
    ArrayList<EventCities> eventCitiesArrayList;
    Handler mHandler = new Handler();
    int pageNumber = 0, totalCount = 0;
    protected boolean isLoadingForFirstTime = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);
        loadMoreListView = (ListView) findViewById(R.id.listView_events);
        loadMoreListView.setOnItemClickListener(this);
        eventCitiesArrayList = new ArrayList<>();

        GetEventCities();
    }

    private void GetEventCities() {

        if (CommonUtils.isNetworkAvailable(this)) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(getApplicationContext()));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
            String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.EVENT_CITY_LIST;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), url);


        } else {
            AlertDialogHelper.showSimpleAlertDialog(this, "No Network connection");
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onResponse(final JSONObject response) {

        progressDialogHelper.hideProgressDialog();
        if (validateSignInResponse(response)) {
            try {
                JSONArray getData = response.getJSONArray("Cities");
                if (getData != null && getData.length() > 0) {

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialogHelper.hideProgressDialog();
                            Gson gson = new Gson();
                            EventCitiesList eventCitiesList = gson.fromJson(response.toString(), EventCitiesList.class);
                            if (eventCitiesList.getEventCities() != null && eventCitiesList.getEventCities().size() > 0) {
                                totalCount = eventCitiesList.getCount();
                                isLoadingForFirstTime = false;
                                updateListAdapter(eventCitiesList.getEventCities());
                            }
                        }
                    });
                } else {
                    if (eventCitiesArrayList != null) {
                        eventCitiesArrayList.clear();
                        eventCitiesAdapter = new EventCitiesAdapter(this, this.eventCitiesArrayList);
                        loadMoreListView.setAdapter(eventCitiesAdapter);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(final String error) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                progressDialogHelper.hideProgressDialog();
                AlertDialogHelper.showSimpleAlertDialog(SelectCityActivity.this, error);
            }
        });

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

    protected void updateListAdapter(ArrayList<EventCities> eventCitiesArrayList) {
        this.eventCitiesArrayList.addAll(eventCitiesArrayList);
        if (eventCitiesAdapter == null) {
            eventCitiesAdapter = new EventCitiesAdapter(this, this.eventCitiesArrayList);
            loadMoreListView.setAdapter(eventCitiesAdapter);
        } else {
            eventCitiesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onEvent list item click" + position);
        EventCities eventCities = null;
        if ((eventCitiesAdapter != null) && (eventCitiesAdapter.ismSearching())) {
            Log.d(TAG, "while searching");
            int actualindex = eventCitiesAdapter.getActualEventPos(position);
            Log.d(TAG, "actual index" + actualindex);
            eventCities = eventCitiesArrayList.get(actualindex);
        } else {
            eventCities = eventCitiesArrayList.get(position);
        }
        PreferenceStorage.saveEventCityId(getApplicationContext(), eventCities.getId());
        PreferenceStorage.saveEventCityName(getApplicationContext(), eventCities.getCityName());
        Toast.makeText(getApplicationContext(), "You are in now " + eventCities.getCityName(), Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, SetUpPreferenceActivity.class);
//        intent.putExtra("eventObj", eventCities);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
