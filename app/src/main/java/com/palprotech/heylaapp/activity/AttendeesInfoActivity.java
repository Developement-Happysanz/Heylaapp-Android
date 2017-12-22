package com.palprotech.heylaapp.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.bean.support.BookPlan;
import com.palprotech.heylaapp.bean.support.Event;
import com.palprotech.heylaapp.ccavenue.activities.InitialScreenActivity;
import com.palprotech.heylaapp.helper.AlertDialogHelper;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.interfaces.DialogClickListener;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;
import com.palprotech.heylaapp.serviceinterfaces.IServiceListener;
import com.palprotech.heylaapp.utils.HeylaAppConstants;
import com.palprotech.heylaapp.utils.HeylaAppValidator;
import com.palprotech.heylaapp.utils.PreferenceStorage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Admin on 20-12-2017.
 */

public class AttendeesInfoActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener {

    private static final String TAG = AttendeesInfoActivity.class.getName();
    LinearLayout layout_all;
    private Event event;
    String  eventNoOfTicket, orderId;
    int noOfTickets;
    private Button btnProceed;
    protected ProgressDialogHelper progressDialogHelper;
    private ServiceHelper serviceHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendees_info);
        btnProceed = findViewById(R.id.btnproceed);
        btnProceed.setOnClickListener(this);
        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        event = (Event) getIntent().getSerializableExtra("eventObj");
        eventNoOfTicket = PreferenceStorage.getTotalNoOfTickets(getApplicationContext());
        orderId = PreferenceStorage.getOrderId(getApplicationContext());

        noOfTickets = Integer.parseInt(eventNoOfTicket);

        loadAttendeesView();
    }

    @Override
    public void onClick(View v) {
        if (v == btnProceed) {
            updateAttendees();
        }
    }

    private void updateAttendees() {

        EditText edtFullName, edtEmailId, edtMobileNo;
        try {
            if (validateFields()) {
                int nViews = layout_all.getChildCount();

                for (int i = 0; i < nViews; i++) {
                    View view = layout_all.getChildAt(i);

                    edtFullName = view.findViewById(R.id.my_edit_text_1);
                    edtEmailId = view.findViewById(R.id.my_edit_text_2);
                    edtMobileNo = view.findViewById(R.id.my_edit_text_3);

                    String FullName = edtFullName.getText().toString().trim();
                    String EmailId = edtEmailId.getText().toString().trim();
                    String MobileNo = edtMobileNo.getText().toString().trim();

                    if (FullName != null) {

                        updateAttendeesToServer(FullName, EmailId, MobileNo);
                    }
                }

                Intent intent = new Intent(getApplicationContext(), InitialScreenActivity.class);
                intent.putExtra("eventObj", event);
                startActivity(intent);
                finish();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(), "Try again...", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validateFields() {
        int getCount = 0;
        getCount = layout_all.getChildCount();

        EditText edtFullName, edtEmailId, edtMobileNo;

        int count = 0;

        int nViews = layout_all.getChildCount();

        for (int i = 0; i < nViews; i++) {

            View view = layout_all.getChildAt(i);
            edtFullName = view.findViewById(R.id.my_edit_text_1);
            edtEmailId = view.findViewById(R.id.my_edit_text_2);
            edtMobileNo = view.findViewById(R.id.my_edit_text_3);

            if (!HeylaAppValidator.checkNullString(edtFullName.getText().toString().trim())) {
                AlertDialogHelper.showSimpleAlertDialog(this, "Enter full name...");
            } else {
                count++;
            }
        }

        if (getCount == count) {
            return true;
        } else {
            return false;
        }
    }

    private void updateAttendeesToServer(String FullName, String EmailId, String MobileNo) {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.KEY_ORDER_ID, orderId);
            jsonObject.put(HeylaAppConstants.PARAMS_NAME, FullName);
            jsonObject.put(HeylaAppConstants.PARAMS_EMAIL_ID, EmailId);
            jsonObject.put(HeylaAppConstants.PARAMS_MOBILE_NUMBER, MobileNo);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.UPDATE_ATTENDEES;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);

    }

    @Override
    public void onResponse(JSONObject response) {

        progressDialogHelper.hideProgressDialog();
        if (validateSignInResponse(response)) {
            try {

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

    private void loadAttendeesView() {

        try {

            layout_all = findViewById(R.id.layout_timetable);

            for (int f = 1; f <= 1; f++) {

                for (int c1 = 1; c1 <= noOfTickets; c1++) {

                    LinearLayout cell = new LinearLayout(this);
                    cell.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    cell.setOrientation(LinearLayout.VERTICAL);
                    cell.setPadding(0, 5, 0, 0);
                    cell.setBackgroundColor(Color.parseColor("#FFFFFF"));

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(10, 0, 10, 0);

                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params1.setMargins(10, 0, 10, 20);

                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params2.setMargins(10, 10, 0, 20);

                    TextView title = new TextView(this);
                    title.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
                    title.setTextColor(Color.BLACK);
                    title.setText("Attendee Details " + c1);
                    title.setLayoutParams(params2);

                    EditText line1 = new EditText(this);
                    line1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));

                    if (c1 == 1) {
                        line1.setText(PreferenceStorage.getFullName(getApplicationContext()));
                    }

                    line1.setId(R.id.my_edit_text_1);
                    line1.setHint("Full Name");
                    line1.requestFocusFromTouch();
                    line1.setTextSize(13.0f);
                    line1.setTypeface(null, Typeface.BOLD);
                    line1.setKeyListener(DigitsKeyListener.getInstance("0123456789AB"));
                    line1.setInputType(InputType.TYPE_CLASS_TEXT);
                    line1.setAllCaps(true);
                    line1.setSingleLine(true);
                    line1.setTextColor(Color.parseColor("#FF68358E"));
                    line1.setPressed(true);
                    line1.setHeight(100);
                    line1.setPadding(5, 0, 5, 0);
                    line1.setLayoutParams(params);

                    EditText line2 = new EditText(this);
                    line2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));

                    if (c1 == 1) {
                        line2.setText(PreferenceStorage.getEmailId(getApplicationContext()));
                    }

                    line2.setId(R.id.my_edit_text_2);
                    line2.setHint("Email ID");
                    line2.requestFocusFromTouch();
                    line2.setTextSize(13.0f);
                    line2.setTypeface(null, Typeface.BOLD);
                    line2.setKeyListener(DigitsKeyListener.getInstance("0123456789AB"));
                    line2.setInputType(InputType.TYPE_CLASS_TEXT);
                    line2.setAllCaps(true);
                    line2.setSingleLine(true);
                    line2.setTextColor(Color.parseColor("#FF68358E"));
                    line2.setPressed(true);
                    line2.setHeight(100);
                    line2.setPadding(5, 0, 5, 0);
                    line2.setLayoutParams(params);

                    EditText line3 = new EditText(this);
                    line3.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

                    if (c1 == 1) {
                        line3.setText(PreferenceStorage.getMobileNo(getApplicationContext()));
                    }

                    line3.setId(R.id.my_edit_text_3);
                    line3.setHint("Mobile Number");
                    line3.requestFocusFromTouch();
                    line3.setTextSize(13.0f);
                    line3.setTypeface(null, Typeface.BOLD);
                    line3.setKeyListener(DigitsKeyListener.getInstance("0123456789AB"));
                    line3.setInputType(InputType.TYPE_CLASS_TEXT);
                    line3.setAllCaps(true);
                    line3.setSingleLine(true);
                    line3.setTextColor(Color.parseColor("#FF68358E"));
                    line3.setPressed(true);
                    line3.setHeight(100);
                    line3.setPadding(5, 0, 5, 0);
                    line3.setLayoutParams(params1);

                    TextView border = new TextView(this);
                    border.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                    border.setHeight(5);
                    border.setBackgroundColor(Color.BLACK);

                    cell.addView(title);
                    cell.addView(line1);
                    cell.addView(line2);
                    cell.addView(line3);
                    cell.addView(border);

                    layout_all.addView(cell);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
