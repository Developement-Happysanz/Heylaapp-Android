package com.palprotech.heylaapp.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.adapter.ReviewAdapter;
import com.palprotech.heylaapp.bean.support.Event;
import com.palprotech.heylaapp.bean.support.Review;
import com.palprotech.heylaapp.bean.support.ReviewList;
import com.palprotech.heylaapp.helper.AlertDialogHelper;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.interfaces.DialogClickListener;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;
import com.palprotech.heylaapp.serviceinterfaces.IServiceListener;
import com.palprotech.heylaapp.utils.HeylaAppConstants;
import com.palprotech.heylaapp.utils.PreferenceStorage;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Narendar on 03/11/17.
 */

public class EventDetailActivity extends AppCompatActivity implements LocationListener, View.OnClickListener, OnMapReadyCallback, IServiceListener, DialogClickListener {

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
    private String eventType = "";
    private String wishliststatus = "";
    private String wishlist_id = "";
    private String res = "";

    private ImageView imEventOrganiserRequest;
    private TextView txtEventReview;
    private TextView txtCheckInEvent;
    private TextView txtBookEvent;

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    TextView txtLat;
    String lat;
    String provider;
    protected Double latitude, longitude, latitude1, longitude1;
    protected boolean gps_enabled, network_enabled;

    ListView reviewsListView;
    Handler mHandler = new Handler();
    int totalCount = 0;
    protected boolean isLoadingForFirstTime = true;
    ReviewAdapter reviewAdapter;
    ArrayList<Review> reviewArrayList;
    TextView viewMore;
    Button writeReview;

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
        eventType = event.getHotspotStatus();
        setUpServices();
        getWishlistStatus();
        setUpUI();
    }

    @Override
    public void onClick(View v) {
        if (v == imBack) {
            finish();
        }
        if (v == imEventBanner) {
//            Toast.makeText(getApplicationContext(), "Hi", Toast.LENGTH_SHORT).show(); EventImagesActivity
            Intent intent = new Intent(getApplicationContext(), EventImagesActivitySwipe.class);
            intent.putExtra("eventObj", event);
            startActivity(intent);
        }
        if (v == imEventShare) {
            SpannableString content = new SpannableString("http://www.heylaapp.com/");
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            String text = event.getEventName() + "\n" + "From : " + event.getStartDate() + " To : " + event.getEndDate() + "\n"
                    + event.getStartTime() + "-" + event.getEndTime() + "\n" + event.getDescription() + "\n" + content;


            Intent i = new Intent(android.content.Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share with");
            i.putExtra(android.content.Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(i, "Share via"));

            sendShareStatus();
        }
        if (v == imEventQuestionAnswer) {
//            Toast.makeText(getApplicationContext(), "Hi", Toast.LENGTH_SHORT).show();
        }
        if (v == imEventFavourite) {
            if (PreferenceStorage.getUserType(getApplicationContext()).equalsIgnoreCase("1")) {
                getWishlistStatus();
                if (wishliststatus.equalsIgnoreCase("exist")) {
                    imEventFavourite.setImageResource(R.drawable.ic_fav_deselect);
                    removeFromWishlist();
                } else {
                    imEventFavourite.setImageResource(R.drawable.ic_fav_select);
                    addToWishlist();
                }

            } else {
                guestLoginAlert();
            }
        }
        if (v == imEventOrganiserRequest) {
//            Toast.makeText(getApplicationContext(), "Hi", Toast.LENGTH_SHORT).show();
        }
        if (v == viewMore) {
            if (PreferenceStorage.getUserType(getApplicationContext()).equalsIgnoreCase("1")) {
                Intent intent = new Intent(getApplicationContext(), EventReviewActivity.class);
                intent.putExtra("eventObj", event);
                startActivity(intent);
            } else {
                guestLoginAlert();
            }
//            finish();
        }
        if (v == writeReview) {
            if (PreferenceStorage.getUserType(getApplicationContext()).equalsIgnoreCase("1")) {
                Intent intent = new Intent(getApplicationContext(), EventReviewAddActivity.class);
                intent.putExtra("eventObj", event);
                startActivity(intent);
            } else {
                guestLoginAlert();
            }
//            finish();
        }
        if (v == txtCheckInEvent) {
            if (PreferenceStorage.getUserType(getApplicationContext()).equalsIgnoreCase("1")) {
                checkdistance();
            } else {
                guestLoginAlert();
            }
        }
        if (v == txtBookEvent) {
            if (PreferenceStorage.getUserType(getApplicationContext()).equalsIgnoreCase("1")) {
                Intent intent = new Intent(getApplicationContext(), BookingActivity.class);
                intent.putExtra("eventObj", event);
                startActivity(intent);
                finish();
            } else {
                guestLoginAlert();
            }
        }
    }

    void setUpServices() {
        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);
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
//        Event date layout
        RelativeLayout rl = findViewById(R.id.datelayout);
        if (eventType.contentEquals("Y")) {
            rl.setVisibility(View.GONE);
        }
//        Event start date
        TextView txtEventStartDate = findViewById(R.id.start_date_txt);
        String start = dateConversion(event.getStartDate());
        txtEventStartDate.setText(start);
        if (eventType.contentEquals("Y")) {
            txtEventStartDate.setVisibility(View.GONE);
        }
//        text between date
        TextView txtTo = findViewById(R.id.date_to);
        if (eventType.contentEquals("Y")) {
            txtTo.setVisibility(View.GONE);
        }
//        Event end date
        TextView txtEventEndDate = findViewById(R.id.end_date_txt);
        String end = dateConversion(event.getEndDate());
        txtEventEndDate.setText(end);
        if (eventType.contentEquals("Y")) {
            txtEventEndDate.setVisibility(View.GONE);
        }
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

        reviewArrayList = new ArrayList<>();
        reviewsListView = findViewById(R.id.listView_reviews);
        viewMore = findViewById(R.id.view_more);
        viewMore.setOnClickListener(this);
        writeReview = findViewById(R.id.write_review);
        writeReview.setOnClickListener(this);
        updateEventViews();
        loadReviewList();
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

    private void getWishlistStatus() {

        res = "wishlistStatus";

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.KEY_EVENT_ID, event.getId());
            jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(getApplicationContext()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.EVENT_WISHLIST_STATUS;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private String dateConversion(String inputDate) {
        String outputDate = inputDate;
        try {
            DateFormat format = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
            Date date = format.parse(outputDate);
            System.out.println(date);
            SimpleDateFormat startFormat = new SimpleDateFormat("dd-mm-yyyy");
            outputDate = startFormat.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return outputDate;
    }

    private void sendShareStatus() {

        //A user can only get points 3 times a day for event detail sharing. So restrict beyond that
        long currentTime = System.currentTimeMillis();
        long lastsharedTime = PreferenceStorage.getEventSharedTime(this);
        int sharedCount = PreferenceStorage.getEventSharedcount(this);

        if ((currentTime - lastsharedTime) > HeylaAppConstants.TWENTY4HOURS) {
            Log.d(TAG, "event time elapsed more than 24hrs");
            PreferenceStorage.saveEventSharedtime(this, currentTime);
            PreferenceStorage.saveEventSharedcount(this, 1);

            shareStatus();

        } else {
            if (sharedCount < 5) {
                Log.d(TAG, "event shared count is" + sharedCount);
                sharedCount++;
                PreferenceStorage.saveEventSharedcount(this, sharedCount);
                shareStatus();
            }
        }
    }

    private void shareStatus() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        String date_activity = (dateFormat.format(date)).toString();
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(this));
            jsonObject.put(HeylaAppConstants.KEY_RULE_ID, "2");
            jsonObject.put(HeylaAppConstants.KEY_EVENT_ID, event.getId());
            jsonObject.put(HeylaAppConstants.PARAMS_DATE, date_activity);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.USER_ACTIVITY;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void checkdistance() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {

            longitude = location.getLongitude();
            latitude = location.getLatitude();
            longitude1 = Double.parseDouble(event.getEventLongitude());
            latitude1 = Double.parseDouble(event.getEventLatitude());
            if (distance(latitude, longitude, latitude1, longitude1) < 0.1) {
                Toast.makeText(getApplicationContext(), "You have successfully checked-in for the event - " + event.getEventName().toString() + "\nGet ready for the fun! ", Toast.LENGTH_LONG).show();
                sendCheckinStatus();
            } else {
                Toast.makeText(getApplicationContext(), "Try again at - " + event.getEventName().toString() + "\nOnce you reached! ", Toast.LENGTH_LONG).show();
            }
        } else {
            showSettingsAlert();
        }
    }

    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 6371; // in miles, change to 6371 for kilometer output

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double dist = earthRadius * c;

        return dist; // output distance, in MILES
    }

    private void sendCheckinStatus() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        String date_activity = (dateFormat.format(date)).toString();
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(this));
            jsonObject.put(HeylaAppConstants.KEY_RULE_ID, "3");
            jsonObject.put(HeylaAppConstants.KEY_EVENT_ID, event.getId());
            jsonObject.put(HeylaAppConstants.PARAMS_DATE, date_activity);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.USER_ACTIVITY;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void addToWishlist() {

        res = "wishlistADD";

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(this));
            jsonObject.put(HeylaAppConstants.PARAMS_WISH_LIST_MASTER_ID, "1");
            jsonObject.put(HeylaAppConstants.KEY_EVENT_ID, event.getId());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.WISH_LIST_ADD;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void removeFromWishlist() {

        res = "wishlistDEL";

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(this));
            jsonObject.put(HeylaAppConstants.PARAMS_WISH_LIST_ID, wishlist_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.WISH_LIST_DELETE;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(event.getEventLatitude()), Double.parseDouble(event.getEventLongitude())))
                .title(event.getEventName()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(event.getEventLatitude()), Double.parseDouble(event.getEventLongitude())), 14.0f));
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
                if (res.equalsIgnoreCase("wishlistStatus")) {
                    wishliststatus = response.getString("status");
                    wishlist_id = response.getString("wishlist_id");
                    if (wishliststatus.equalsIgnoreCase("success")) {
                        imEventFavourite.setImageResource(R.drawable.ic_fav_select);
                    } else {
                        imEventFavourite.setImageResource(R.drawable.ic_fav_deselect);
                    }
                } else if (res.equalsIgnoreCase("wishlistADD")) {
                    wishliststatus = response.getString("status");
                    Toast.makeText(this, "Wishlist Updated!!", Toast.LENGTH_SHORT).show();

                } else if (res.equalsIgnoreCase("wishlistDEL")) {
                    wishliststatus = response.getString("status");
                    Toast.makeText(this, "Wishlist Updated!!", Toast.LENGTH_SHORT).show();
                } else if (res.equalsIgnoreCase("reviewList")) {
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
                }
            } catch (JSONException e) {
                e.printStackTrace();
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

    public void guestLoginAlert() {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EventDetailActivity.this);
        alertDialogBuilder.setTitle("Login");
        alertDialogBuilder.setMessage("Log in to Access");
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                doLogout();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.show();
    }

    private void loadReviewList() {
        res = "reviewList";
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.KEY_EVENT_ID, event.getId());
            jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.EVENT_REVIEW_LIST;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    public void doLogout() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().clear().commit();
//        TwitterUtil.getInstance().resetTwitterRequestToken();

        Intent homeIntent = new Intent(this, SplashScreenActivity.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
        this.finish();
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

    }

    public void getlatlong(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing the Settings button.
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // On pressing the cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}