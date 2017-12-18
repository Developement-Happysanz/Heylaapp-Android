package com.palprotech.heylaapp.activity;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Admin on 09-11-2017.
 */

public class BookingActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener {

    private static final String TAG = BookingActivity.class.getName();
    private Event event;
    private ImageView back;
    DayScrollDatePicker datePicker;
    protected ProgressDialogHelper progressDialogHelper;
    private ServiceHelper serviceHelper;
    private String checkPoint;
    LinearLayout layout_all;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_booking);

        event = (Event) getIntent().getSerializableExtra("eventObj");
        loadUI();
        loadBooking();

    }

    private void loadBooking() {
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

    @Override
    public void onClick(View v) {
        if (v == back) {
            finish();
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
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        if (validateSignInResponse(response)) {
            try {
                if (checkPoint.equalsIgnoreCase("loadDate")) {
//                    JSONObject getEventDates = response.getJSONObject("Eventdates");
                    JSONArray getEventDates = response.getJSONArray("Eventdates");
                    loadDate(getEventDates);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void loadDate(JSONArray getEventDates) {
        try {
            layout_all = findViewById(R.id.layout_timetable);
            TableLayout layout = new TableLayout(this);
            layout.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            layout_all.setScrollbarFadingEnabled(false);
            layout.setPadding(0, 50, 0, 50);

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
            for (int f = 0; f <= 1; f++) {

                TableRow tr = new TableRow(this);

                tr.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                tr.setBackgroundColor(Color.BLACK);
                tr.setPadding(0, 0, 0, 1);

                TableRow.LayoutParams llp = new
                        TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                llp.setMargins(1, 1, 1, 1);//2px right-margin

                for (int c1 = 0; c1 <= getEventDates.length(); c1++) {

                    LinearLayout cell = new LinearLayout(this);
                    cell.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
                    TextView b = new TextView(this);

                    String name = "";
                    JSONObject jsonobj = getEventDates.getJSONObject(i);

                    String showDates = "";
                    showDates = jsonobj.getString("show_date");
                    System.out.println("showDates : " + i + " = " + showDates);

                    /*if (((r == 0) && (col == 0)) || ((r == 0) && (col == 1)) || ((r == 0) && (col == 2))
                            || ((r == 0) && (col == 3)) || ((r == 0) && (col == 4)) || ((r == 0) && (col == 5))
                            || ((r == 0) && (col == 6)) || ((r == 0) && (col == 7)) || ((r == 0) && (col == 8))
                            || ((r == 1) && (col == 9)) || ((r == 2) && (col == 18)) || ((r == 3) && (col == 27))
                            || ((r == 4) && (col == 36)) || ((r == 5) && (col == 45)) || ((r == 6) && (col == 54))) {*/
                    b.setBackgroundColor(Color.parseColor("#708090"));
                    b.setTextColor(Color.parseColor("#FFFF00"));
                    name = showDates;
                        /*if ((r == 0) && (col == 0)) {
                            b.setTextColor(Color.parseColor("#FFFFFF"));
                            name = "Period\n&\nDay";
                        }
                        if ((r == 0) && (col == 1)) {
                            name = "" + 1;
                        }
                        if ((r == 0) && (col == 2)) {
                            name = "" + 2;
                        }
                        if ((r == 0) && (col == 3)) {
                            name = "" + 3;
                        }
                        if ((r == 0) && (col == 4)) {
                            name = "" + 4;
                        }
                        if ((r == 0) && (col == 5)) {
                            name = "" + 5;
                        }
                        if ((r == 0) && (col == 6)) {
                            name = "" + 6;
                        }
                        if ((r == 0) && (col == 7)) {
                            name = "" + 7;
                        }
                        if ((r == 0) && (col == 8)) {
                            name = "" + 8;
                        }
                        if ((r == 1) && (col == 9)) {
                            name = "Monday";
                        }
                        if ((r == 2) && (col == 18)) {
                            name = "Tuesday";
                        }
                        if ((r == 3) && (col == 27)) {
                            name = "Wednesday";
                        }
                        if ((r == 4) && (col == 36)) {
                            name = "Thursday";
                        }
                        if ((r == 5) && (col == 45)) {
                            name = "Friday";
                        }
                        if ((r == 6) && (col == 54)) {
                            name = "Saturday";
                        }*/
                    /*} else {

                        *//*String fValue = String.valueOf(f);
                        String c1Value = String.valueOf(c1);
                        Cursor c = db.getTeacherTimeTableValue(fValue, c1Value);
                        if (c.getCount() > 0) {
                            if (c.moveToFirst()) {
                                do {
                                    ClassName = c.getString(0);
                                    SectionName = c.getString(1);
                                    SubjectName = c.getString(2);
                                    ClassId = c.getString(3);
                                    SubjectId = c.getString(4);
                                    PeriodId = c.getString(5);
                                } while (c.moveToNext());
                            }*//*
                        name = showDates;//SubjectName;
                            *//*name1 = "ClassName:" + ClassName +
                                    "-" + SectionName + ",ClassId:" + ClassId + ",SubjectName:" + SubjectName +
                                    ",SubjectId:" + SubjectId + ",PeriodId:" + PeriodId;*//*
//                            name1 = ClassName + "-" + SectionName + "," + ClassId + "," + SubjectName + "," + SubjectId + "," + PeriodId;
                        *//*} else {
                            name = "";
//                            name1 = "";
                        }*//*
                    }*/
//                    db.close();

//                    cell.setBackgroundColor(Color.WHITE);//argb(255,104,53,142)

                    b.setText(name);
                    b.setTextSize(13.0f);
                    b.setTypeface(null, Typeface.BOLD);
                    b.setAllCaps(true);
                    b.setTextColor(Color.parseColor("#FF68358E"));
                    b.setGravity(Gravity.CENTER);

                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub

                        }
                    });
                    b.setPressed(true);

                    b.setHeight(160);
                    b.setWidth(160);
                    b.setPadding(1, 0, 2, 0);
                    cell.addView(b);
                    cell.setLayoutParams(llp);//2px border on the right for the cell

                    tr.addView(cell, cellLp);
                    i++;
                    col++;
                } // for
                layout.addView(tr, rowLp);
                r++;
            }
            // for

            layout_all.addView(layout);

        } catch (Exception ex) {
            ex.printStackTrace();
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
        ImageView imEventBanner = findViewById(R.id.img_logo);
        String url = event.getEventBanner();
        if (((url != null) && !(url.isEmpty()))) {
            Picasso.with(this).load(url).placeholder(R.drawable.event_img).error(R.drawable.event_img).into(imEventBanner);
        }
        TextView txtEventName = findViewById(R.id.txt_event_name);
        txtEventName.setText(event.getEventName());
        TextView txtEventTime = findViewById(R.id.txt_event_time);
        txtEventTime.setText(event.getStartTime());
        TextView txtEventPlace = findViewById(R.id.txt_event_location);
        txtEventPlace.setText(event.getEventVenue());
        TextView txtEventAddress = findViewById(R.id.event_address);
        txtEventAddress.setText(event.getEventAddress());
    }
}
