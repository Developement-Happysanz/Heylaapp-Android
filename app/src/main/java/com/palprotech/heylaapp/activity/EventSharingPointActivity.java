package com.palprotech.heylaapp.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.adapter.EventShareHistoryListAdapter;
import com.palprotech.heylaapp.bean.support.EventShareHistory;
import com.palprotech.heylaapp.bean.support.EventShareHistoryList;
import com.palprotech.heylaapp.helper.AlertDialogHelper;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.interfaces.DialogClickListener;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;
import com.palprotech.heylaapp.serviceinterfaces.IServiceListener;
import com.palprotech.heylaapp.utils.HeylaAppConstants;
import com.palprotech.heylaapp.utils.PreferenceStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 12-01-2018.
 */

public class EventSharingPointActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener {

    protected ProgressDialogHelper progressDialogHelper;
    private ServiceHelper serviceHelper;
    private static final String TAG = EventSharingPointActivity.class.getName();
    int pageNumber = 0, totalCount = 0;
    Handler mHandler = new Handler();
    protected EventShareHistoryListAdapter eventShareHistoryListAdapter;
    protected ArrayList<EventShareHistory> eventShareHistoryArrayList;
    protected boolean isLoadingForFirstTime = true;
    protected ListView loadMoreListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_sharing_points);
        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);
        eventShareHistoryArrayList = new ArrayList<>();

        loadEventSharingPoints();
    }

    @Override
    public void onClick(View v) {

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

                final JSONArray getData = response.getJSONArray("Data");
                if (getData != null && getData.length() > 0) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            Gson gson = new Gson();
                            EventShareHistoryList eventShareHistoryList = gson.fromJson(response.toString(), EventShareHistoryList.class);
                            if (eventShareHistoryList.getEventShareHistory() != null && eventShareHistoryList.getEventShareHistory().size() > 0) {
                                totalCount = eventShareHistoryList.getCount();
                                isLoadingForFirstTime = false;
                                updateListAdapter(eventShareHistoryList.getEventShareHistory());
                            }
                        }
                    });
                } else {
                    if (eventShareHistoryArrayList != null) {
                        eventShareHistoryArrayList.clear();
                        eventShareHistoryListAdapter = new EventShareHistoryListAdapter(this, this.eventShareHistoryArrayList);
                        loadMoreListView.setAdapter(eventShareHistoryListAdapter);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onError(String error) {
        progressDialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(this, error);
    }

    private void loadEventSharingPoints() {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.KEY_RULE_ID, "2");
            jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(getApplicationContext()));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.ACTIVITY_HISTORY;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    protected void updateListAdapter(ArrayList<EventShareHistory> eventShareHistoryArrayList) {
        this.eventShareHistoryArrayList.addAll(eventShareHistoryArrayList);

        if (eventShareHistoryListAdapter == null) {
            eventShareHistoryListAdapter = new EventShareHistoryListAdapter(this, this.eventShareHistoryArrayList);
            loadMoreListView.setAdapter(eventShareHistoryListAdapter);
        } else {
            eventShareHistoryListAdapter.notifyDataSetChanged();
        }
    }

}
