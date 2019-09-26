package com.palprotech.heylaapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.bean.support.Event;
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

/**
 * Created by Admin on 30-12-2017.
 */

public class EventReviewAddActivity extends AppCompatActivity implements DialogClickListener, IServiceListener, View.OnClickListener {

    private static final String TAG = EventReviewAddActivity.class.getName();
    private ProgressDialogHelper progressDialogHelper;
    private ServiceHelper serviceHelper;
    private Event event;
    private RatingBar rtbComments;
    private EditText edtComments;
    private Button btnSubmit;
    private String checkString;
    private String reviewId = "";
    private ImageView ivBack;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event_review);
        event = (Event) getIntent().getSerializableExtra("eventObj");
        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);
        rtbComments = findViewById(R.id.ratingBar);
        edtComments = findViewById(R.id.edtComments);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        ivBack = findViewById(R.id.back_res);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loadCheckUserReviewStatus();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    private void loadCheckUserReviewStatus() {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.KEY_EVENT_ID, event.getId());
            jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(getApplicationContext()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.EVENT_REVIEW_CHECK;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    @Override
    public void onClick(View v) {
        if (v == btnSubmit) {
            if (checkString.equalsIgnoreCase("new")) {
                submitNewRecord();

            } else if (checkString.equalsIgnoreCase("update")) {
                submitUpdatedRecord();

            } else {

            }
        }

    }

    private void submitNewRecord() {

        float getrate = rtbComments.getRating();
        int getrate1 = rtbComments.getNumStars();
        checkString = "new";

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.KEY_EVENT_ID, event.getId());
            jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(getApplicationContext()));
            jsonObject.put(HeylaAppConstants.KEY_RATINGS, "" + rtbComments.getRating());
            jsonObject.put(HeylaAppConstants.KEY_COMMENTS, edtComments.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.EVENT_REVIEW_ADD;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void submitUpdatedRecord() {

        float getrate = rtbComments.getRating();
        int getrate1 = rtbComments.getNumStars();
        checkString = "update";

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.KEY_REVIEW_ID, reviewId);
            jsonObject.put(HeylaAppConstants.KEY_RATINGS, "" + rtbComments.getRating());
            jsonObject.put(HeylaAppConstants.KEY_COMMENTS, edtComments.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.EVENT_REVIEW_UPDATE;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void updateEventReviews(JSONArray getEventReviews) {
        try {
            JSONObject jsonobj = getEventReviews.getJSONObject(0);
            String eventRating = "";
            String eventComments = "";
            reviewId = jsonobj.getString("id");
            eventRating = jsonobj.getString("event_rating");
            eventComments = jsonobj.getString("comments");
            rtbComments.setRating(Integer.parseInt(eventRating));
            edtComments.setText(eventComments);
        } catch (Exception ex) {
            ex.printStackTrace();
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
            try {
                String status = response.getString("status");
                if (status.equalsIgnoreCase("new")) {
                    checkString = "new";
                } else if (status.equalsIgnoreCase("success")) {
                    Intent intent = new Intent(getApplicationContext(), EventReviewActivity.class);
                    intent.putExtra("eventObj", event);
                    startActivity(intent);
                    finish();
                } else if (status.equalsIgnoreCase("exist")) {
                    checkString = "update";
                    JSONArray getEventReviews = response.getJSONArray("Reviewdetails");
                    updateEventReviews(getEventReviews);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onError(String error) {
        progressDialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(this, error);
    }
}
