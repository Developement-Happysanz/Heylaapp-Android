package com.palprotech.heylaapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.adapter.BookingPlanAdapter;
import com.palprotech.heylaapp.adapter.ReviewAdapter;
import com.palprotech.heylaapp.bean.support.BookPlan;
import com.palprotech.heylaapp.bean.support.BookPlanList;
import com.palprotech.heylaapp.bean.support.Event;
import com.palprotech.heylaapp.bean.support.Review;
import com.palprotech.heylaapp.bean.support.ReviewList;
import com.palprotech.heylaapp.helper.AlertDialogHelper;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.interfaces.DialogClickListener;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;
import com.palprotech.heylaapp.serviceinterfaces.IServiceListener;
import com.palprotech.heylaapp.utils.HeylaAppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 30-12-2017.
 */

public class EventReviewActivity extends AppCompatActivity implements DialogClickListener, IServiceListener, View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = EventReviewActivity.class.getName();
    private ProgressDialogHelper progressDialogHelper;
    private ServiceHelper serviceHelper;
    ListView reviewsListView;
    private Event event;
    private FloatingActionButton fabAddReview;
    Handler mHandler = new Handler();
    int totalCount = 0;
    protected boolean isLoadingForFirstTime = true;
    ReviewAdapter reviewAdapter;
    ArrayList<Review> reviewArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_review);
        event = (Event) getIntent().getSerializableExtra("eventObj");
        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);
        reviewsListView = findViewById(R.id.listView_reviews);
        reviewsListView.setOnItemClickListener(this);
        fabAddReview = findViewById(R.id.fab_add_review);
        fabAddReview.setOnClickListener(this);
        reviewArrayList = new ArrayList<>();
        loadReviewList();

    }

    private void loadReviewList() {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.KEY_EVENT_ID, event.getId());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.EVENT_REVIEW_LIST;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    @Override
    public void onClick(View v) {
        if (v == fabAddReview) {
            Intent intent = new Intent(getApplicationContext(), EventReviewAddActivity.class);
            intent.putExtra("eventObj", event);
            startActivity(intent);
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
    public void onResponse(final JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        if (validateSignInResponse(response)) {
            try {

                JSONArray getData = response.getJSONArray("Reviewdetails");
                if (getData != null && getData.length() > 0) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            Gson gson = new Gson();
                            ReviewList reviewList = gson.fromJson(response.toString(), ReviewList.class);
                            if (reviewList.getReviews() != null && reviewList.getReviews().size() > 0) {
                                totalCount = reviewList.getCount();
                                isLoadingForFirstTime = false;
                                updateListAdapter(reviewList.getReviews());
                            }
                        }
                    });
                } else {
                    if (reviewArrayList != null) {
                        reviewArrayList.clear();
                        reviewAdapter = new ReviewAdapter(this, this.reviewArrayList);
                        reviewsListView.setAdapter(reviewAdapter);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    protected void updateListAdapter(ArrayList<Review> reviewArrayList) {
        this.reviewArrayList.addAll(reviewArrayList);
//        if (bookingPlanAdapter == null) {
        reviewAdapter = new ReviewAdapter(this, this.reviewArrayList);
        reviewsListView.setAdapter(reviewAdapter);
//        } else {
        reviewAdapter.notifyDataSetChanged();
//        }
    }

    @Override
    public void onError(String error) {
        progressDialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(this, error);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
