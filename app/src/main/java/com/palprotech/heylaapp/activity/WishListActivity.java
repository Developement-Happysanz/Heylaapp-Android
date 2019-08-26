package com.palprotech.heylaapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
 * Created by Admin on 12-01-2018.
 */

public class WishListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, IServiceListener, DialogClickListener {

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
    private String checkLoading = "events";
    private boolean click = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
        //        getSupportActionBar().hide();
        loadMoreListView = findViewById(R.id.listView_events);
        className = this.getClass().getSimpleName();
//        loadMoreListView.setOnLoadMoreListener(this);
        loadMoreListView.setOnItemClickListener(this);
        loadMoreListView.setOnItemLongClickListener(this);
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
        loadWishList();
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
//        if(!click) {
        startActivity(intent);
//        }
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        click = true;
        Log.d(TAG, "onEvent list item clicked" + position);
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(WishListActivity.this);
        alertDialogBuilder.setTitle("Delete");
        alertDialogBuilder.setMessage("Do you want delete the wishlist item ?");
        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                Event event = null;
                if ((eventsListAdapter != null) && (eventsListAdapter.ismSearching())) {
                    Log.d(TAG, "while searching");
                    int actualindex = eventsListAdapter.getActualEventPos(position);
                    Log.d(TAG, "actual index" + actualindex);
                    event = eventsArrayList.get(actualindex);
                    eventsArrayList.remove(actualindex);
                    eventsListAdapter.notifyDataSetChanged();
                } else {
                    event = eventsArrayList.get(position);
                    eventsArrayList.remove(position);
                    eventsListAdapter.notifyDataSetChanged();
                }
                deleteWishList(event.getWishlistId());
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.show();


        return true;
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    private boolean validateSignInResponse(final JSONObject response) {
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
//                        if (!res.equalsIgnoreCase("reviewList")){
//                        }
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
            Log.d("ajazFilterresponse : ", response.toString());

            if (checkLoading.equalsIgnoreCase("eventDelete")) {
//            loadWishList();
                Toast.makeText(this, "Wishlist Removed!!", Toast.LENGTH_SHORT).show();

            } else {

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
//                        progressDialogHelper.hideProgressDialog();
//                loadMoreListView.onLoadMoreComplete();

                        Gson gson = new Gson();
                        EventList eventsList = gson.fromJson(response.toString(), EventList.class);
                        if (eventsList.getEvents() != null && eventsList.getEvents().size() >= 0) {
                            totalCount = eventsList.getCount();
                            isLoadingForFirstTime = false;
                            updateListAdapter(eventsList.getEvents());
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onError(final String error) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                progressDialogHelper.hideProgressDialog();
//                loadMoreListView.onLoadMoreComplete();
                AlertDialogHelper.showSimpleAlertDialog(WishListActivity.this, error);
            }
        });
    }

    private void loadWishList() {
        if (eventsArrayList != null) {
            eventsArrayList.clear();
            if (CommonUtils.isNetworkAvailable(this)) {
                JSONObject jsonObject = new JSONObject();
                try {

                    jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(this));
                    jsonObject.put(HeylaAppConstants.PARAMS_WISH_LIST_MASTER_ID, "1");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
                String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.WISH_LIST;
                serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
            } else {
                AlertDialogHelper.showSimpleAlertDialog(this, getString(R.string.no_connectivity));
            }
        }
    }

        protected void updateListAdapter (ArrayList < Event > eventsArrayList) {
            this.eventsArrayList.addAll(eventsArrayList);
            if (eventsListAdapter == null) {
                eventsListAdapter = new EventsListAdapter(this, this.eventsArrayList, className);
                loadMoreListView.setAdapter(eventsListAdapter);
            } else {
                eventsListAdapter.notifyDataSetChanged();
            }
        }

        private void deleteWishList (String eventId){
            if (CommonUtils.isNetworkAvailable(this)) {
                checkLoading = "eventDelete";
                JSONObject jsonObject = new JSONObject();
                try {

                    jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(this));
                    jsonObject.put(HeylaAppConstants.PARAMS_WISH_LIST_ID, eventId);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
                String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.WISH_LIST_DELETE;
                serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
            } else {
                AlertDialogHelper.showSimpleAlertDialog(this, getString(R.string.no_connectivity));
            }
        }

    }
