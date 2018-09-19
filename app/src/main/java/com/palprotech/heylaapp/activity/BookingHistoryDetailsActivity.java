package com.palprotech.heylaapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.adapter.AttendeesListAdapter;
import com.palprotech.heylaapp.bean.support.Attendees;
import com.palprotech.heylaapp.bean.support.AttendeesList;
import com.palprotech.heylaapp.bean.support.BookingHistory;
import com.palprotech.heylaapp.helper.AlertDialogHelper;
import com.palprotech.heylaapp.helper.HeylaAppHelper;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.interfaces.DialogClickListener;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;
import com.palprotech.heylaapp.serviceinterfaces.IServiceListener;
import com.palprotech.heylaapp.utils.HeylaAppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Admin on 26-12-2017.
 */

public class BookingHistoryDetailsActivity extends AppCompatActivity implements IServiceListener, DialogClickListener, AdapterView.OnItemClickListener {

    private BookingHistory bookingHistory;
    private TextView txtEventName, txtEventDate, txtEventDate1, txtEventDate2, txtEventTime, txtEventAddress, txtEventAttendees, txtEventTicktClass, txtEventTicketCount;
    static String[] suffixes =
            //    0     1     2     3     4     5     6     7     8     9
            {"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                    //    10    11    12    13    14    15    16    17    18    19
                    "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
                    //    20    21    22    23    24    25    26    27    28    29
                    "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                    //    30    31
                    "th", "st"};
    private static final String TAG = BookingHistoryActivity.class.getName();
    protected ProgressDialogHelper progressDialogHelper;
    private ServiceHelper serviceHelper;
    protected AttendeesListAdapter attendeesListAdapter;
    protected ArrayList<Attendees> attendeesArrayList;
    protected ListView listView;
    int totalCount = 0;
    protected boolean isLoadingForFirstTime = true;
    private ImageView ivBack;
    private TextView attendee, totalAmount;
    private LinearLayout attendeeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history_detail_temp);
        bookingHistory = (BookingHistory) getIntent().getSerializableExtra("bookingObj");
        findViewById(R.id.back_res).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loadUI();
        loadAttendees();
    }

    private void loadUI() {
        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);
        attendeeLayout = findViewById(R.id.attendee_layout);
        attendee = findViewById(R.id.attendee_visibility);
        attendee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               attendeeLayout.setVisibility(View.VISIBLE);
            }
        });
        ivBack = findViewById(R.id.back_res);
        listView = findViewById(R.id.listView_attendees);
        listView.setOnItemClickListener(this);

        listView.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        attendeesArrayList = new ArrayList<>();

        txtEventName = findViewById(R.id.txt_event_name);
        txtEventDate = findViewById(R.id.txt_event_booked_date);
        txtEventDate1 = findViewById(R.id.txt_event_booked_date1);
        txtEventDate2 = findViewById(R.id.txt_event_booked_date2);
        txtEventTime = findViewById(R.id.txt_event_booked_time);
        txtEventAddress = findViewById(R.id.txt_event_location);
        txtEventAttendees = findViewById(R.id.txt_event_attendees_count);
        txtEventTicktClass = findViewById(R.id.txt_event_booking_plan);
        txtEventTicketCount = findViewById(R.id.txt_event_booking_count);
        totalAmount = findViewById(R.id.total_amount);

        txtEventName.setText(bookingHistory.getEventName());
        txtEventTime.setText(bookingHistory.getPlanTime());
        txtEventAddress.setText(bookingHistory.getEventVenue());
        totalAmount.setText("Total amount : "+bookingHistory.getTotalAmount());
//        txtEventDate.setText(bookingHistory.getBookingDate());

        String bookingDate = HeylaAppHelper.getDate(bookingHistory.getBookingDate());

        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
            Date date = (Date) formatter.parse(bookingDate);
            SimpleDateFormat month_date = new SimpleDateFormat("MMM");
            String month_name = month_date.format(date.getTime());
            SimpleDateFormat event_date = new SimpleDateFormat("dd");
            String date_name = event_date.format(date.getTime());
            SimpleDateFormat event_year = new SimpleDateFormat("yyyy");
            String year_name = event_year.format(date.getTime());

            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            Date dt1 = format1.parse(bookingDate);
            DateFormat format2 = new SimpleDateFormat("EEEE");
            String finalDay = format2.format(dt1);

            int day = Integer.parseInt(date_name);
            String dayStr = day + suffixes[day];

            if ((bookingDate != null)) {
                txtEventDate.setText(" " + month_name );
                txtEventDate1.setText(" " + dayStr);
                txtEventDate2.setText(finalDay + " ");
            } else {
                txtEventDate.setText("N/A");
                txtEventDate1.setText("N/A");
                txtEventDate2.setText("N/A");
            }

        } catch (final ParseException e) {
            e.printStackTrace();
        }

        txtEventAttendees.setText(bookingHistory.getNumberOfSeats());
        txtEventTicktClass.setText(bookingHistory.getPlanName());
        txtEventTicketCount.setText(bookingHistory.getNumberOfSeats());

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void loadAttendees() {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.KEY_ORDER_ID, bookingHistory.getOrderId());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.BOOKING_DETAILS;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);

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
        AttendeesList attendeesList = gson.fromJson(response.toString(), AttendeesList.class);
        if (attendeesList.getAttendees() != null && attendeesList.getAttendees().size() > 0) {
            totalCount = attendeesList.getCount();
            isLoadingForFirstTime = false;
            updateListAdapter(attendeesList.getAttendees());
        }
    }

    protected void updateListAdapter(ArrayList<Attendees> attendeesArrayList) {
        this.attendeesArrayList.addAll(attendeesArrayList);
        if (attendeesListAdapter == null) {
            attendeesListAdapter = new AttendeesListAdapter(this, this.attendeesArrayList);
            listView.setAdapter(attendeesListAdapter);
        } else {
            attendeesListAdapter.notifyDataSetChanged();
        }
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
