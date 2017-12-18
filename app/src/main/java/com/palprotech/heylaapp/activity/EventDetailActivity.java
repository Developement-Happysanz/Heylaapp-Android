package com.palprotech.heylaapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.bean.support.Event;
import com.squareup.picasso.Picasso;

/**
 * Created by Narendar on 03/11/17.
 */

public class EventDetailActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    private Event event;
    private ImageView imBack;
    private ImageView imEventBanner;
    private ImageView imEventShare;
    private TextView imEventQuestionAnswer;
    private TextView imEventsView;
    private ImageView imEventFavourite;
    MapView mMapView = null;
    private ImageView imEventOrganiserRequest;
    private TextView txtEventReview;
    private TextView txtCheckInEvent;
    private TextView txtBookEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        event = (Event) getIntent().getSerializableExtra("eventObj");
        setUpUI();
    }

    @Override
    public void onClick(View v) {
        if (v == imBack) {
            finish();
        }
        if (v == imEventBanner) {
            Toast.makeText(getApplicationContext(), "Hi", Toast.LENGTH_LONG).show();
        }
        if (v == imEventShare) {
            Toast.makeText(getApplicationContext(), "Hi", Toast.LENGTH_LONG).show();
        }
        if (v == imEventQuestionAnswer) {
            Toast.makeText(getApplicationContext(), "Hi", Toast.LENGTH_LONG).show();
        }
        if (v == imEventFavourite) {
            Toast.makeText(getApplicationContext(), "Hi", Toast.LENGTH_LONG).show();
        }
        if (v == imEventOrganiserRequest) {
            Toast.makeText(getApplicationContext(), "Hi", Toast.LENGTH_LONG).show();
        }
        if (v == txtEventReview) {
            Toast.makeText(getApplicationContext(), "Hi", Toast.LENGTH_LONG).show();
        }
        if (v == txtCheckInEvent) {
            Toast.makeText(getApplicationContext(), "Hi", Toast.LENGTH_LONG).show();
        }
        if (v == txtBookEvent) {
            Intent intent = new Intent(getApplicationContext(), BookingActivity.class);
            intent.putExtra("eventObj", event);
            startActivity(intent);
        }
    }

    //    Setup UI page
    void setUpUI() {
//        Back button
        imBack = findViewById(R.id.back_res);
//        Event banner
        imEventBanner = findViewById(R.id.event_detail_img);
        imEventBanner.setOnClickListener(this);
        String url = event.getEventBanner();
        if (((url != null) && !(url.isEmpty()))) {
            Picasso.with(this).load(url).placeholder(R.drawable.event_img).error(R.drawable.event_img).into(imEventBanner);
        }
//        Event title
        TextView txtEventName = findViewById(R.id.event_detail_name);
        txtEventName.setText(event.getEventName());
//        Share the event
        imEventShare = findViewById(R.id.share_event);
        imEventShare.setOnClickListener(this);
//        Chat live with event organiser
        imEventQuestionAnswer = findViewById(R.id.event_qa);
        imEventQuestionAnswer.setOnClickListener(this);
//        Mark as favourite event
        imEventFavourite = findViewById(R.id.addfav);
        imEventFavourite.setOnClickListener(this);
//        Event address
        TextView txtEventAddress = findViewById(R.id.addresstxt);
        txtEventAddress.setText(event.getEventAddress());
//        Event start time
        TextView txtEventStartTime = findViewById(R.id.start_time_txt);
        txtEventStartTime.setText(event.getStartTime());
//        Event end time
        TextView txtEventEndTime = findViewById(R.id.end_time_txt);
        txtEventEndTime.setText(event.getEndTime());
//        Event start date
        TextView txtEventStartDate = findViewById(R.id.start_date_txt);
        txtEventStartDate.setText(event.getStartDate());
//        Event end date
        TextView txtEventEndDate = findViewById(R.id.end_date_txt);
        txtEventEndDate.setText(event.getEndDate());
//        Event details
        TextView txtEventDetails = findViewById(R.id.eventdetailtxt);
        txtEventDetails.setText(event.getDescription());
//        Event venue mapview
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//        Event organiser follow
        imEventOrganiserRequest = findViewById(R.id.followrequest);
        imEventOrganiserRequest.setOnClickListener(this);
//        Event organiser name
        TextView txtOrganiserName = findViewById(R.id.organiser_name);
        txtOrganiserName.setText(event.getContactPerson());
//        Event organiser mobile number - primary and secondary
        TextView txtOrganiserMobileNumber = findViewById(R.id.organisermobiletxt);
        String eventOrganiserContactNumber = event.getPrimaryContactNo() + ", " + event.getSecondaryContactNo();
        txtOrganiserMobileNumber.setText(eventOrganiserContactNumber);
//        Event organiser contact emailId
        TextView txtOrganiserEmailId = findViewById(R.id.organisermailtxt);
        txtOrganiserEmailId.setText(event.getContactMail());
//        Event review
        txtEventReview = findViewById(R.id.event_review);
        txtEventReview.setOnClickListener(this);
//        Event check in
        txtCheckInEvent = findViewById(R.id.checkin);
        txtCheckInEvent.setOnClickListener(this);
//        Event booking
        txtBookEvent = findViewById(R.id.book_tickets);
        txtBookEvent.setOnClickListener(this);
        String isBooking = event.getEventBookingStatus();
        if (isBooking.equalsIgnoreCase("N")) {
            txtBookEvent.setVisibility(View.GONE);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(event.getEventLatitude()), Double.parseDouble(event.getEventLongitude())))
                .title(event.getEventName()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(event.getEventLatitude()), Double.parseDouble(event.getEventLongitude())), 14.0f));
    }
}