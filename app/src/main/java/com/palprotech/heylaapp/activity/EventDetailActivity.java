package com.palprotech.heylaapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.bean.support.Event;

import org.w3c.dom.Text;

/**
 * Created by Narendar on 03/11/17.
 */

public class EventDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Event event;
    private ImageView imEventBanner;
    private TextView txtEventName;
    private ImageView imEventShare;
    private ImageView imEventQuestionAnswer;
    private ImageView imEventsView;
    private ImageView imEventFavourite;
    private TextView txtEventAddress;
    private TextView txtEventStartTime;
    private TextView txtEventEndTime;
    private TextView txtEventStartDate;
    private TextView txtEventEndDate;
    private TextView txtEventDetails;
    MapView mMapView = null;
    private ImageView imEventOrganiserRequest;
    private TextView txtOrganiserName;
    private TextView txtOrganiserMobileNumber;
    private TextView txtOrganiserEmailId;
    private TextView txtOrganiserFacebookId;
    private TextView txtOrganiserWebsite;
    private TextView txtEventReview;
    private TextView txtCheckInEvent;
    private TextView txtBookEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        event = (Event) getIntent().getSerializableExtra("eventObj");
        String okNew = "send";
        String newOk = okNew;

    }

    @Override
    public void onClick(View v) {
        if (v == findViewById(R.id.back_res)) {
            finish();
        }
    }
}