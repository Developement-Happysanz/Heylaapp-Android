package com.palprotech.heylaapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.adapter.BookingPlanAdapter;
import com.palprotech.heylaapp.bean.support.BookPlan;
import com.palprotech.heylaapp.bean.support.BookPlanList;
import com.palprotech.heylaapp.bean.support.Event;
import com.palprotech.heylaapp.ccavenue.utilities.ServiceUtility;
import com.palprotech.heylaapp.helper.AlertDialogHelper;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.interfaces.DialogClickListener;
import com.palprotech.heylaapp.paytm.MainActivityPost;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;
import com.palprotech.heylaapp.serviceinterfaces.IServiceListener;
import com.palprotech.heylaapp.utils.HeylaAppConstants;
import com.palprotech.heylaapp.utils.PreferenceStorage;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Admin on 09-11-2017.
 */

public class BookingActivity extends AppCompatActivity implements IServiceListener, View.OnClickListener,  DialogClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = BookingActivity.class.getName();
    private Event event;
    private ImageView back;
    protected ProgressDialogHelper progressDialogHelper;
    private ServiceHelper serviceHelper;
    private String checkPoint;
    LinearLayout layout_date;
    LinearLayout layout_timing;
    private String showDate = "", showTime = "", showTimeId = "", transactionDate = "";
    ListView plansListView;
    BookingPlanAdapter bookingPlanAdapter;
    ArrayList<BookPlan> bookPlanArrayList;
    int totalCount = 0;
    protected boolean isLoadingForFirstTime = true;
    private BookPlan bookPlan = null;
    private String rate;
    private String flagPlan = "no", flagTicket = "no", flagBookingDate = "no";
    int counter;
    ImageButton inc, dec;
    TextView result;
    Button bookNow;
    String orderId;
    Handler mHandler = new Handler();
    String noOfTickets = "0";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_booking);

        event = (Event) getIntent().getSerializableExtra("eventObj");
        loadUI();
        loadBookingDates();
    }

    @Override
    public void onClick(View v) {

        if (v == back) {
            finish();
        }
        if (v == dec) {
            String stringTicketCount = result.getText().toString();
            int getTicketCount = Integer.parseInt(stringTicketCount);
            int setTicketCount = 1;

            if (getTicketCount >= 1 & getTicketCount <= 10) {
                dec.setEnabled(true);
                inc.setEnabled(true);
            }

            if (getTicketCount == 1) {
                dec.setEnabled(false);
            } else {
                setTicketCount = getTicketCount - 1;
                if (setTicketCount == 1) {
                    dec.setEnabled(false);
                    String setValue = String.valueOf(setTicketCount);
                    result.setText(setValue);
                    double TotalAmountForPlan = setTicketCount * Double.parseDouble(rate);
                    bookNow.setText("Pay S$" + TotalAmountForPlan);
                } else {
                    String setValue = String.valueOf(setTicketCount);
                    result.setText(setValue);
                    double TotalAmountForPlan = setTicketCount * Double.parseDouble(rate);
                    bookNow.setText("Pay S$" + TotalAmountForPlan);
                }
                totalCount = setTicketCount;
            }

            if (setTicketCount == 0) {
                flagTicket = "no";
            } else {
                flagTicket = "yes";
            }
        }
        if (v == inc) {
            String stringTicketCount = result.getText().toString();
            int getTicketCount = Integer.parseInt(stringTicketCount);
            int setTicketCount = 0;

            if (getTicketCount > 0 & getTicketCount <= 10) {
                dec.setEnabled(true);
                inc.setEnabled(true);
            }

            if (getTicketCount == 10) {
                inc.setEnabled(false);
                Toast.makeText(this, "Selected maximum tickets for the booking...", Toast.LENGTH_SHORT).show();
            } else {
                inc.setEnabled(true);
                setTicketCount = getTicketCount + 1;
                result.setText(String.valueOf(setTicketCount));
                totalCount = setTicketCount;
                double TotalAmountForPlan = setTicketCount * Double.parseDouble(rate);
                bookNow.setText("Pay - Rs : " + TotalAmountForPlan + "/-");
            }

            if (setTicketCount == 0) {
                flagTicket = "no";
            } else {
                flagTicket = "yes";
            }
        }
        if (v == bookNow) {

            String getTickets = result.getText().toString();

            if ((flagPlan.equalsIgnoreCase("no")) || (flagTicket.equalsIgnoreCase("no")) || (flagBookingDate.equalsIgnoreCase("no"))) {
                Toast.makeText(this, "Select ticket or plan or date", Toast.LENGTH_SHORT).show();
            } else if (Integer.parseInt(getTickets) > Integer.parseInt(noOfTickets)) {
                Toast.makeText(this, "Only " + noOfTickets + " tickets available", Toast.LENGTH_SHORT).show();
            } else {
                updateBookingProcess();
            }
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
    public void onResponse(final JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        if (validateSignInResponse(response)) {
            try {
                if (checkPoint.equalsIgnoreCase("loadDate")) {

                    JSONArray getEventDates = response.getJSONArray("Eventdates");
                    loadDateUI(getEventDates);
                }
                if (checkPoint.equalsIgnoreCase("loadTime")) {

                    JSONArray getEventTimings = response.getJSONArray("Eventtiming");
                    loadTimingUI(getEventTimings);
                }
                if (checkPoint.equalsIgnoreCase("loadPlan")) {

                    JSONArray getData = response.getJSONArray("Plandetails");
                    if (getData != null && getData.length() > 0) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                Gson gson = new Gson();
                                BookPlanList planList = gson.fromJson(response.toString(), BookPlanList.class);
                                if (planList.getPlans() != null && planList.getPlans().size() > 0) {
                                    totalCount = planList.getCount();
                                    isLoadingForFirstTime = false;
                                    updateListAdapter(planList.getPlans());
                                }
                            }
                        });
                    } else {
                        if (bookPlanArrayList != null) {
                            bookPlanArrayList.clear();
                            bookingPlanAdapter = new BookingPlanAdapter(this, this.bookPlanArrayList);
                            plansListView.setAdapter(bookingPlanAdapter);
                        }
                    }
                }
                if (checkPoint.equalsIgnoreCase("bookNow")) {

                    String totalTicketNo = result.getText().toString();
                    int noOfTicket = Integer.parseInt(totalTicketNo);

                    double _rate = 0.0;
                    _rate = Double.parseDouble(rate);

                    Double totalRate = noOfTicket * _rate;

                    Intent intent = new Intent(getApplicationContext(), MainActivityPost.class);
                    intent.putExtra("eventObj", event);
                    PreferenceStorage.saveOrderId(getApplicationContext(), orderId);
                    PreferenceStorage.savePaymentAmount(getApplicationContext(), "" + totalRate);
                    PreferenceStorage.saveTotalNoOfTickets(getApplicationContext(), totalTicketNo);
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
                    Date date = new Date();
                    transactionDate = formatter.format(date);
                    PreferenceStorage.saveTransactionDate(getApplicationContext(), transactionDate);
                    PreferenceStorage.saveBookingDate(getApplicationContext(), showDate);
                    PreferenceStorage.saveBookingTime(getApplicationContext(), showTime);
                    startActivity(intent);
                    finish();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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

    private void loadUI() {
        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);
        back = findViewById(R.id.back_res);
        back.setOnClickListener(this);
        final ImageView imEventBanner = findViewById(R.id.img_logo);
        String url = event.getEventBanner();
        /*if (((url != null) && !(url.isEmpty()))) {
            Picasso.with(this).load(url).placeholder(R.drawable.event_img).error(R.drawable.event_img).into(imEventBanner);
        }*/
        final ImageView img = new ImageView(this);
//        Picasso.with(img.getContext())
//                .load(url)
//                .into(img, new com.squareup.picasso.Callback() {
//                    @Override
//                    public void onSuccess() {
//                        imEventBanner.setBackgroundDrawable(img.getDrawable());
//                    }
//
//                    @Override
//                    public void onError() {
//                    }
//                });
        Picasso.get().load(url).placeholder(R.drawable.ic_default_profile).error(R.drawable.ic_default_profile).into(imEventBanner);
        TextView txtEventName = findViewById(R.id.txt_event_name);
        txtEventName.setText(event.getEventName());
        TextView txtEventTime = findViewById(R.id.txt_event_time);
        txtEventTime.setText(event.getStartTime());
        TextView txtEventPlace = findViewById(R.id.txt_event_location);
        txtEventPlace.setText(event.getEventVenue());
        TextView txtEventAddress = findViewById(R.id.event_address);
        txtEventAddress.setText(event.getEventAddress());
        plansListView = findViewById(R.id.listView_plans);
        plansListView.setOnItemClickListener(this);

        plansListView.setOnTouchListener(new ListView.OnTouchListener() {
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
        bookPlanArrayList = new ArrayList<>();
        counter = 0;
        inc = findViewById(R.id.count_increase);
        inc.setOnClickListener(this);
        dec = findViewById(R.id.count_decrease);
        dec.setOnClickListener(this);
        dec.setEnabled(false);
        result = findViewById(R.id.tcktcount);
        bookNow = findViewById(R.id.btnBookNow);
        bookNow.setOnClickListener(this);

        //generating order number
        Integer randomNum = ServiceUtility.randInt(0, 9999999);
        orderId = randomNum.toString() + "-" + PreferenceStorage.getUserId(getApplicationContext());
    }

    private void loadBookingDates() {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.KEY_EVENT_ID, event.getId());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        checkPoint = "loadDate";

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.BOOKING_DATES;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void loadDateUI(JSONArray getEventDates) {
        try {
            layout_date = findViewById(R.id.layout_date);
            TableLayout layout = new TableLayout(this);
            layout.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            layout_date.setScrollbarFadingEnabled(false);
            layout.setPadding(0, 5, 0, 5);

            TableLayout.LayoutParams rowLp = new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            TableRow.LayoutParams cellLp = new TableRow.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

            cellLp.setMargins(2, 2, 2, 2);

            int i = 0;
            int r = 0;
            int col = 0;
            for (int f = 0; f < 1; f++) {

                TableRow tr = new TableRow(this);

                tr.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                tr.setBackgroundColor(Color.WHITE);
                tr.setPadding(0, 0, 0, 1);

                TableRow.LayoutParams llp = new
                        TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                llp.setMargins(1, 1, 1, 1);//2px right-margin

                for (int c1 = 0; c1 < getEventDates.length(); c1++) {

                    LinearLayout cell = new LinearLayout(this);
                    cell.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
                    final TextView viewDateFormat = new TextView(this);
                    final TextView functionalDateFormat = new TextView(this);
                    final boolean clicked = false;

                    JSONObject jsonobj = getEventDates.getJSONObject(i);

                    String showDates = "";
                    showDates = jsonobj.getString("show_date");
                    System.out.println("showDates : " + i + " = " + showDates);

                    try {
                        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
                        Date date = (Date) formatter.parse(showDates);
                        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
                        String month_name = month_date.format(date.getTime());
                        SimpleDateFormat event_date = new SimpleDateFormat("dd");
                        String date_name = event_date.format(date.getTime());
                        if ((showDates != null)) {
                            viewDateFormat.setText(date_name + "\n" + month_name);
                        } else {
                            viewDateFormat.setText("N/A");
                        }
                    } catch (final ParseException e) {
                        e.printStackTrace();
                    }

                    cell.setBackgroundColor(Color.WHITE);//argb(255,104,53,142)

                    functionalDateFormat.setText(showDates);

                    viewDateFormat.setBackground(getResources().getDrawable(R.drawable.bg_advanced_filter_properties));
                    viewDateFormat.setTextColor(getResources().getColor(R.color.appColorBase));

                    viewDateFormat.setTextSize(13.0f);
                    functionalDateFormat.setTextSize(13.0f);

                    viewDateFormat.setTypeface(null, Typeface.BOLD);
                    functionalDateFormat.setTypeface(null, Typeface.BOLD);

                    viewDateFormat.setAllCaps(true);
                    functionalDateFormat.setAllCaps(true);

                    viewDateFormat.setGravity(Gravity.CENTER);
                    functionalDateFormat.setGravity(Gravity.CENTER);

                    viewDateFormat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
//                            viewDateFormat.setBackgroundColor(Color.parseColor("#708090"));
//                            Toast.makeText(getApplicationContext(), functionalDateFormat.getText(), Toast.LENGTH_SHORT).show();
                            showDate = functionalDateFormat.getText().toString();
                            viewDateFormat.setBackground(getResources().getDrawable(R.drawable.bg_advanced_filter_properties_filled));
                            viewDateFormat.setTextColor(getResources().getColor(R.color.white));
                            loadBookingTimings();
                            if (showDate.equalsIgnoreCase("")) {
                                flagBookingDate = "no";
                            } else {
                                flagBookingDate = "yes";
                            }
                        }
                    });

                    viewDateFormat.setPressed(true);
                    viewDateFormat.setHeight(120);
                    viewDateFormat.setWidth(100);
                    functionalDateFormat.setHeight(0);
                    functionalDateFormat.setWidth(0);

                    viewDateFormat.setPadding(1, 0, 2, 0);
                    functionalDateFormat.setPadding(0, 0, 0, 0);
                    functionalDateFormat.setVisibility(View.INVISIBLE);
                    cell.addView(viewDateFormat);
                    cell.addView(functionalDateFormat);
                    cell.setLayoutParams(llp);//2px border on the right for the cell

                    tr.addView(cell, cellLp);
                    i++;
                    col++;
                } // for
                layout.addView(tr, rowLp);
                r++;
            }
            // for

            layout_date.addView(layout);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadBookingTimings() {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.KEY_EVENT_ID, event.getId());
            jsonObject.put(HeylaAppConstants.KEY_EVENT_SHOW_DATE, showDate);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        checkPoint = "loadTime";

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.BOOKING_SHOW_TIMES;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void loadTimingUI(JSONArray getEventTimings) {

        try {

            layout_timing = findViewById(R.id.layout_time);
            layout_timing.removeAllViews();
            TableLayout layout = new TableLayout(this);
            layout.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            layout_timing.setScrollbarFadingEnabled(false);
            layout.setPadding(0, 5, 0, 5);

            TableLayout.LayoutParams rowLp = new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            TableRow.LayoutParams cellLp = new TableRow.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

            cellLp.setMargins(2, 2, 2, 2);

            int i = 0;
            int r = 0;
            int col = 0;
            for (int f = 0; f < 1; f++) {

                TableRow tr = new TableRow(this);

                tr.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                tr.setBackgroundColor(Color.WHITE);
                tr.setPadding(0, 0, 0, 1);

                TableRow.LayoutParams llp = new
                        TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                llp.setMargins(1, 1, 1, 1);//2px right-margin

                for (int c1 = 0; c1 < getEventTimings.length(); c1++) {

                    LinearLayout cell = new LinearLayout(this);
                    cell.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
                    final TextView viewTimeFormat = new TextView(this);
                    final TextView storeTimeId = new TextView(this);

                    JSONObject jsonobj = getEventTimings.getJSONObject(i);

                    String show_time_id, showTimes = "";
                    showTimes = jsonobj.getString("show_time");
                    show_time_id = jsonobj.getString("id");
                    System.out.println("showTimes : " + i + " = " + showTimes);

                    viewTimeFormat.setText(showTimes);
                    storeTimeId.setText("" + show_time_id);

                    cell.setBackgroundColor(Color.WHITE);//argb(255,104,53,142)

                    viewTimeFormat.setBackground(getResources().getDrawable(R.drawable.bg_advanced_filter_properties));
                    storeTimeId.setBackground(getResources().getDrawable(R.drawable.bg_advanced_filter_properties));
                    viewTimeFormat.setTextColor(getResources().getColor(R.color.appColorBase));
                    storeTimeId.setTextColor(getResources().getColor(R.color.appColorBase));

                    viewTimeFormat.setTextSize(13.0f);
                    storeTimeId.setTextSize(13.0f);

                    viewTimeFormat.setTypeface(null, Typeface.BOLD);
                    storeTimeId.setTypeface(null, Typeface.BOLD);

                    viewTimeFormat.setAllCaps(true);
                    storeTimeId.setAllCaps(true);

                    viewTimeFormat.setGravity(Gravity.CENTER);
                    storeTimeId.setGravity(Gravity.CENTER);

                    viewTimeFormat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
//                            viewDateFormat.setBackgroundColor(Color.parseColor("#708090"));
//                            Toast.makeText(getApplicationContext(), viewTimeFormat.getText() + "ID : " + storeTimeId.getText(), Toast.LENGTH_SHORT).show();
                            showTime = viewTimeFormat.getText().toString();
                            showTimeId = storeTimeId.getText().toString();
                            viewTimeFormat.setTextColor(getResources().getColor(R.color.white));
                            viewTimeFormat.setBackground(getResources().getDrawable(R.drawable.bg_advanced_filter_properties_filled));
                            if (bookPlanArrayList != null) {
                                bookPlanArrayList.clear();
                                plansListView.setAdapter(bookingPlanAdapter);
                            }
                            loadBookingPlans();
                        }
                    });

                    viewTimeFormat.setPressed(true);
//                    storeTimeId.setPressed(true);
                    viewTimeFormat.setHeight(120);
                    viewTimeFormat.setWidth(100);
                    storeTimeId.setHeight(0);
                    storeTimeId.setWidth(0);
                    viewTimeFormat.setPadding(1, 0, 2, 0);
                    storeTimeId.setPadding(1, 0, 2, 0);
                    storeTimeId.setVisibility(View.INVISIBLE);
                    cell.addView(viewTimeFormat);
                    cell.addView(storeTimeId);
                    cell.setLayoutParams(llp);//2px border on the right for the cell

                    tr.addView(cell, cellLp);
                    i++;
                    col++;
                } // for
                layout.addView(tr, rowLp);
                r++;
            }
            // for

            layout_timing.addView(layout);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadBookingPlans() {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.KEY_EVENT_ID, event.getId());
            jsonObject.put(HeylaAppConstants.KEY_EVENT_SHOW_DATE, showDate);
            jsonObject.put(HeylaAppConstants.KEY_EVENT_SHOW_TIME, showTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        checkPoint = "loadPlan";

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.BOOKING_SHOW_PLANS;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    protected void updateListAdapter(ArrayList<BookPlan> bookPlanArrayList) {
        this.bookPlanArrayList.addAll(bookPlanArrayList);
//        if (bookingPlanAdapter == null) {
        bookingPlanAdapter = new BookingPlanAdapter(this, this.bookPlanArrayList);
        plansListView.setAdapter(bookingPlanAdapter);
//        } else {
        bookingPlanAdapter.notifyDataSetChanged();
//        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
        Log.d(TAG, "onEvent list item clicked" + i);
        view.setSelected(true);
//        view.setBackgroundColor(getResources().getColor(R.color.appColorBase));
        LinearLayout planLayout = view.findViewById(R.id.plan_layout);
        TextView planName = view.findViewById(R.id.txt_plan_name);
        TextView planRate = view.findViewById(R.id.txt_plan_rate);
        LinearLayout ll = findViewById(R.id.layoutTickets);
        ll.setVisibility(View.VISIBLE);

        for (int a = 0; a < parent.getChildCount(); a++) {
            parent.getChildAt(a).findViewById(R.id.plan_layout).setBackground(getResources().getDrawable(R.drawable.bg_advanced_filter_properties));
            TextView planName1 = parent.getChildAt(a).findViewById(R.id.txt_plan_name);
            planName1.setTextColor(Color.parseColor("#468DCB"));
            TextView planRate1 = parent.getChildAt(a).findViewById(R.id.txt_plan_rate);
            planRate1.setTextColor(Color.parseColor("#468DCB"));
           }
        planLayout.setBackground(getResources().getDrawable(R.drawable.bg_advanced_filter_properties_filled));
        planName.setTextColor(Color.WHITE);
        planRate.setTextColor(Color.WHITE);

//        view.setBackgroundColor(getResources().getColor(R.color.appColorBase));

        if ((bookingPlanAdapter != null) && (bookingPlanAdapter.ismSearching())) {
            Log.d(TAG, "while searching");
            int actualindex = bookingPlanAdapter.getActualEventPos(i);
            Log.d(TAG, "actual index" + actualindex);
            bookPlan = bookPlanArrayList.get(actualindex);
        } else {
            bookPlan = bookPlanArrayList.get(i);
        }

        noOfTickets = bookPlan.getSeatAvailable();
        rate = bookPlan.getSeatRate();
//        Toast.makeText(this, "Select ticket plan" + rate, Toast.LENGTH_SHORT).show();
        flagPlan = "yes";
//        bookingPlanAdapter.
    }

    private void updateBookingProcess() {

        checkPoint = "bookNow";

        String totalTicketNo = result.getText().toString();
        int noOfTicket = Integer.parseInt(totalTicketNo);

        double _rate = 0.0;
        _rate = Double.parseDouble(rate);

        Double totalRate = noOfTicket * _rate;

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.KEY_ORDER_ID, orderId);
            jsonObject.put(HeylaAppConstants.KEY_EVENT_ID, event.getId());
            jsonObject.put(HeylaAppConstants.KEY_PLAN_ID, bookPlan.getPlanId());
            jsonObject.put(HeylaAppConstants.KEY_PLAN_TIME_ID, bookPlan.getPlanTimeId());
            jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(getApplicationContext()));
            jsonObject.put(HeylaAppConstants.KEY_NO_OF_SEATS, noOfTicket);
            jsonObject.put(HeylaAppConstants.KEY_TOTAL_AMOUNT, "" + totalRate);
            jsonObject.put(HeylaAppConstants.KEY_BOOKING_DATE, showDate);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.BOOKING_PROCESS;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

}
