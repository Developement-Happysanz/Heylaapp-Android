package com.palprotech.heylaapp.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.adapter.EventPictureListAdapter;
import com.palprotech.heylaapp.bean.support.Event;
import com.palprotech.heylaapp.bean.support.EventPicture;
import com.palprotech.heylaapp.bean.support.EventPictureList;
import com.palprotech.heylaapp.helper.AlertDialogHelper;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.interfaces.DialogClickListener;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;
import com.palprotech.heylaapp.serviceinterfaces.IServiceListener;
import com.palprotech.heylaapp.utils.HeylaAppConstants;
import com.palprotech.heylaapp.utils.PreferenceStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 12-01-2018.
 */

public class EventImagesActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener {

    protected ProgressDialogHelper progressDialogHelper;
    private ServiceHelper serviceHelper;
    private static final String TAG = EventImagesActivity.class.getName();
    private Event event;
    private ImageView ivBack;
    protected ListView loadMoreListView;
    protected EventPictureListAdapter eventPictureListAdapter;
    protected ArrayList<EventPicture> eventPictureArrayList;
    Handler mHandler = new Handler();
    int pageNumber = 0, totalCount = 0;
    protected boolean isLoadingForFirstTime = true;

    @Override
    protected void  onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);
        event = (Event) getIntent().getSerializableExtra("eventObj");

        ivBack = findViewById(R.id.back_res);
        ivBack.setOnClickListener(this);

        loadMoreListView = (ListView) findViewById(R.id.listView_events);
        eventPictureArrayList = new ArrayList<>();

        loadEventImages();
    }

    @Override
    public void onClick(View v) {
        if (v == ivBack) {
            finish();
        }
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
            Gson gson = new Gson();
            EventPictureList eventPictureList = gson.fromJson(response.toString(), EventPictureList.class);
            if (eventPictureList.getEventPicture() != null && eventPictureList.getEventPicture().size() > 0) {
                totalCount = eventPictureList.getCount();
                isLoadingForFirstTime = false;
                updateListAdapter(eventPictureList.getEventPicture());
            }
        }
    }

    @Override
    public void onError(String error) {
        progressDialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(this, error);
    }

    private void loadEventImages() {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.KEY_EVENT_ID, event.getId());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.EVENT_IMAGES;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    protected void updateListAdapter(ArrayList<EventPicture> eventPictureArrayList) {
        this.eventPictureArrayList.addAll(eventPictureArrayList);
//        if (taskDataListAdapter == null) {
        eventPictureListAdapter = new EventPictureListAdapter(EventImagesActivity.this, this.eventPictureArrayList);
        loadMoreListView.setAdapter(eventPictureListAdapter);
//        } else {
        eventPictureListAdapter.notifyDataSetChanged();
//        }
    }
}
