package com.palprotech.heylaapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
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
import com.palprotech.heylaapp.helper.AlertDialogHelper;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.interfaces.DialogClickListener;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;
import com.palprotech.heylaapp.serviceinterfaces.IServiceListener;
import com.palprotech.heylaapp.utils.HeylaAppConstants;
import com.palprotech.heylaapp.utils.PreferenceStorage;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Narendar on 03/11/17.
 */

public class EventDetailActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, IServiceListener, DialogClickListener {

    private static final String TAG = EventDetailActivity.class.getName();
    private ProgressDialogHelper progressDialogHelper;
    private ServiceHelper serviceHelper;
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

        findViewById(R.id.detail_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        event = (Event) getIntent().getSerializableExtra("eventObj");
        setUpUI();
    }

    @Override
    public void onClick(View v) {
        if (v == imBack) {
            finish();
        }
        if (v == imEventBanner) {
            Toast.makeText(getApplicationContext(), "Hi", Toast.LENGTH_SHORT).show();
        }
        if (v == imEventShare) {
            SpannableString content = new SpannableString("http://www.heylaapp.com/");
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            String text = event.getEventName() + "\n" + event.getDescription() + "\n" + content;


            Intent i = new Intent(android.content.Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share with");
            i.putExtra(android.content.Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(i, "Share via"));

            sendShareStatus();
        }
        if (v == imEventQuestionAnswer) {
            Toast.makeText(getApplicationContext(), "Hi", Toast.LENGTH_SHORT).show();
        }
        if (v == imEventFavourite) {
            Toast.makeText(getApplicationContext(), "Hi", Toast.LENGTH_SHORT).show();
        }
        if (v == imEventOrganiserRequest) {
            Toast.makeText(getApplicationContext(), "Hi", Toast.LENGTH_SHORT).show();
        }
        if (v == txtEventReview) {
            Intent intent = new Intent(getApplicationContext(), EventReviewActivity.class);
            intent.putExtra("eventObj", event);
            startActivity(intent);
//            finish();
        }
        if (v == txtCheckInEvent) {
            Toast.makeText(getApplicationContext(), "You have successfully checked-in for the event - " + event.getEventName().toString() + "\nGet ready for the fun! ", Toast.LENGTH_LONG).show();
            sendShareStatusUserActivity(2);
        }
        if (v == txtBookEvent) {
            Intent intent = new Intent(getApplicationContext(), BookingActivity.class);
            intent.putExtra("eventObj", event);
            startActivity(intent);
            finish();
        }
    }

    //    Setup UI page
    void setUpUI() {
        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);
//        Back button
        imBack = findViewById(R.id.back_res);
//        Event banner
        imEventBanner = findViewById(R.id.event_detail_img);
        imEventBanner.setOnClickListener(this);
        String url = event.getEventBanner();
        if (((url != null) && !(url.isEmpty()))) {
            Picasso.with(this).load(url).placeholder(R.drawable.event_img).error(R.drawable.event_img).into(imEventBanner);
        }
        imEventBanner.setMaxWidth(500);
//        Event title
        TextView txtEventName = findViewById(R.id.event_detail_name);
        txtEventName.setText(event.getEventName());
//        Share the event
        imEventShare = findViewById(R.id.share_event);
        imEventShare.setOnClickListener(this);
//        Chat live with event organiser
        imEventQuestionAnswer = findViewById(R.id.event_qa);
        imEventQuestionAnswer.setOnClickListener(this);
//        Event popularity views
        imEventsView = findViewById(R.id.event_views);
        imEventsView.setText("" + event.getPopularity());
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
        updateEventViews();
    }

    private void updateEventViews() {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.KEY_EVENT_ID, event.getId());
            jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(getApplicationContext()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.EVENT_POPULARITY;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void sendShareStatus() {

        //A user can only get points 3 times a day for photo sharing. So restrict beyond that
        long currentTime = System.currentTimeMillis();
        long lastsharedTime = PreferenceStorage.getEventSharedTime(this);
        int sharedCount = PreferenceStorage.getEventSharedcount(this);

        if ((currentTime - lastsharedTime) > HeylaAppConstants.TWENTY4HOURS) {
            Log.d(TAG, "event time elapsed more than 24hrs");
            PreferenceStorage.saveEventSharedtime(this, currentTime);
            PreferenceStorage.saveEventSharedcount(this, 1);

            //testing
            int ruleid = 2;
            int ticketcount = 0;
            String activitydetail = "You have shared event" + event.getEventName();
            int eventId = Integer.parseInt(event.getId());
            ServiceHelper serviceHelper = new ServiceHelper(this);
            serviceHelper.postShareDetails(String.format(HeylaAppConstants.SHARE_EVENT_URL, eventId, Integer.parseInt(PreferenceStorage.getUserId(this)),
                    ruleid, Uri.encode(activitydetail), event.getEventBanner(), ticketcount), this);
            //testing

            sendShareStatustoServer();
        } else {
            if (sharedCount < 3) {
                Log.d(TAG, "event shared cout is" + sharedCount);
                sharedCount++;
                PreferenceStorage.saveEventSharedcount(this, sharedCount);
                sendShareStatustoServer();
            }
        }
    }

    private void sendShareStatustoServer() {
        ServiceHelper serviceHelper = new ServiceHelper(this);
        int eventId = Integer.parseInt(event.getId());
        int ruleid = 2;
        int ticketcount = 0;
        String activitydetail = "You have shared photo" + event.getEventName();
        serviceHelper.postShareDetails(String.format(HeylaAppConstants.USER_ACTIVITY, eventId, Integer.parseInt(PreferenceStorage.getUserId(this)),
                ruleid, Uri.encode(activitydetail), event.getEventBanner(), ticketcount), (IServiceListener) this);

    }

    private void sendShareStatusUserActivity(int RuleId) {

        //testing
        int ruleid = RuleId;
        int ticketcount = 0;
        String statusCheckins = "";
        if (RuleId == 1) {
            statusCheckins = "You have shared photo ";
        }
        if (RuleId == 2) {
            statusCheckins = "You have checked in for the ";
        }
        if (RuleId == 3) {
            statusCheckins = "You have engaged for the ";
        }
        if (RuleId == 3) {
            statusCheckins = "You have engaged for the ";
        }

        String activitydetail = "You have shared photo" + event.getEventName();
        int eventId = Integer.parseInt(event.getId());
        ServiceHelper serviceHelper = new ServiceHelper(this);
        serviceHelper.postShareDetails(String.format(HeylaAppConstants.SHARE_EVENT_URL, eventId, Integer.parseInt(PreferenceStorage.getUserId(this)),
                ruleid, Uri.encode(activitydetail), event.getEventBanner(), ticketcount), this);
        //testing
        sendShareStatustoServerUserActivity(RuleId);
    }

    private void sendShareStatustoServerUserActivity(int RuleId) {
        ServiceHelper serviceHelper = new ServiceHelper(this);
        int eventId = Integer.parseInt(event.getId());
        int ruleid = RuleId;
        int ticketcount = 0;
        String activitydetail = "You have shared photo" + event.getEventName();
        serviceHelper.postShareDetails(String.format(HeylaAppConstants.SHARE_EVENT_URL, eventId, Integer.parseInt(PreferenceStorage.getUserId(this)),
                ruleid, Uri.encode(activitydetail), event.getEventBanner(), ticketcount), this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(event.getEventLatitude()), Double.parseDouble(event.getEventLongitude())))
                .title(event.getEventName()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(event.getEventLatitude()), Double.parseDouble(event.getEventLongitude())), 14.0f));
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

        }
    }

    @Override
    public void onError(String error) {
        progressDialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(this, error);
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }
}