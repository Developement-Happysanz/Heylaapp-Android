package com.palprotech.heylaapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.adapter.EventCitiesAdapter;
import com.palprotech.heylaapp.adapter.NotificationListAdapter;
import com.palprotech.heylaapp.bean.support.EventCities;
import com.palprotech.heylaapp.bean.support.EventCitiesList;
import com.palprotech.heylaapp.bean.support.Notification;
import com.palprotech.heylaapp.bean.support.NotificationList;
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

public class NotificationActivity extends AppCompatActivity implements IServiceListener, View.OnClickListener,  DialogClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = NotificationActivity.class.getName();
    private ImageView back;
    protected ProgressDialogHelper progressDialogHelper;
    private ServiceHelper serviceHelper;
    ArrayList<Notification> notificationArrayList = new ArrayList<>();
    NotificationListAdapter notificationListAdapter;
    ListView notificationList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);
        notificationList = findViewById(R.id.noti_list);

        loadNotificationList();
    }

    private void loadNotificationList() {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.VIEW_NOTIFICATION;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
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
            NotificationList notificationList = gson.fromJson(response.toString(), NotificationList.class);
            if (notificationList.getData() != null && notificationList.getData().size() > 0) {
                updateListAdapter(notificationList.getData());
            }
        }
    }

    protected void updateListAdapter(ArrayList<Notification> notifications) {
        this.notificationArrayList.addAll(notifications);
        if (notificationListAdapter == null) {
            notificationListAdapter = new NotificationListAdapter(this, this.notificationArrayList);
            notificationList.setAdapter(notificationListAdapter);
        } else {
            notificationListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
