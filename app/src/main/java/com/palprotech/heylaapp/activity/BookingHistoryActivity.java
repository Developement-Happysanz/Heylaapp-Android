package com.palprotech.heylaapp.activity;

import android.content.Intent;
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
import com.palprotech.heylaapp.adapter.BookingHistoryListAdapter;
import com.palprotech.heylaapp.adapter.EventsListAdapter;
import com.palprotech.heylaapp.bean.support.BookingHistory;
import com.palprotech.heylaapp.bean.support.BookingHistoryList;
import com.palprotech.heylaapp.bean.support.Event;
import com.palprotech.heylaapp.bean.support.EventList;
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
 * Created by Admin on 26-12-2017.
 */

public class BookingHistoryActivity extends AppCompatActivity implements IServiceListener, AdapterView.OnItemClickListener, DialogClickListener {

    private static final String TAG = BookingHistoryActivity.class.getName();
    protected ProgressDialogHelper progressDialogHelper;
    private ServiceHelper serviceHelper;
    protected BookingHistoryListAdapter bookingHistoryListAdapter;
    protected ArrayList<BookingHistory> bookingHistoryArrayList;
    protected ListView listView;
    int totalCount = 0;
    protected boolean isLoadingForFirstTime = true;
    private ImageView ivBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);
        loadUI();
        loadBookingHistory();
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        if (validateSignInResponse(response)) {
            LoadListView(response);
        }
    }

    private void LoadListView(JSONObject response) {

        progressDialogHelper.hideProgressDialog();
        Gson gson = new Gson();
        BookingHistoryList bookingHistoryList = gson.fromJson(response.toString(), BookingHistoryList.class);
        if (bookingHistoryList.getBookingHistories() != null && bookingHistoryList.getBookingHistories().size() > 0) {
            totalCount = bookingHistoryList.getCount();
            isLoadingForFirstTime = false;
            updateListAdapter(bookingHistoryList.getBookingHistories());
        }
    }

    protected void updateListAdapter(ArrayList<BookingHistory> bookingHistoryArrayList) {
        this.bookingHistoryArrayList.addAll(bookingHistoryArrayList);
        if (bookingHistoryListAdapter == null) {
            bookingHistoryListAdapter = new BookingHistoryListAdapter(this, this.bookingHistoryArrayList);
            listView.setAdapter(bookingHistoryListAdapter);
        } else {
            bookingHistoryListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(String error) {
        progressDialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(this, error);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onEvent list item click" + position);
        BookingHistory bookingHistory = null;
        if ((bookingHistoryListAdapter != null) && (bookingHistoryListAdapter.ismSearching())) {
            Log.d(TAG, "while searching");
            int actualindex = bookingHistoryListAdapter.getActualEventPos(position);
            Log.d(TAG, "actual index" + actualindex);
            bookingHistory = bookingHistoryArrayList.get(actualindex);
        } else {
            bookingHistory = bookingHistoryArrayList.get(position);
        }
        Intent intent = new Intent(this, BookingHistoryDetailsActivity.class);
        intent.putExtra("bookingObj", bookingHistory);
        startActivity(intent);
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

    private void loadUI() {
        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);
        ivBack = findViewById(R.id.back_res);
        listView = findViewById(R.id.listView_booked);
        listView.setOnItemClickListener(this);
        bookingHistoryArrayList = new ArrayList<>();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadBookingHistory() {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(getApplicationContext()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.BOOKING_HISTORY;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }


}
