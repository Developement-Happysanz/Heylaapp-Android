package com.palprotech.heylaapp.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.palprotech.heylaapp.R;
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
 * Created by Admin on 11-01-2018.
 */

public class LoginPointsActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener {

    protected ProgressDialogHelper progressDialogHelper;
    private ServiceHelper serviceHelper;
    private static final String TAG = LoginPointsActivity.class.getName();
    RelativeLayout day1, day2, day3, day4, day5;
    TextView dayTwoPoint, dayOnePoint, dayThreePoint, dayFourPoint, dayFivePoint, totalDailyPoint;
    int one, two, three, four, five, total;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_login);
        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);
        day1 = findViewById(R.id.day_one);
        day2 = findViewById(R.id.day_two);
        day3 = findViewById(R.id.day_three);
        day4 = findViewById(R.id.day_four);
        day5 = findViewById(R.id.day_five);

        dayOnePoint = findViewById(R.id.day_one_points);
        dayTwoPoint = findViewById(R.id.day_two_points);
        dayThreePoint = findViewById(R.id.day_three_points);
        dayFourPoint = findViewById(R.id.day_four_points);
        dayFivePoint = findViewById(R.id.day_five_points);
        totalDailyPoint = findViewById(R.id.daily_point);

        one = Integer.parseInt(dayOnePoint.getText().toString());
        two = Integer.parseInt(dayTwoPoint.getText().toString());
        three = Integer.parseInt(dayThreePoint.getText().toString());
        four = Integer.parseInt(dayFourPoint.getText().toString());
        five = Integer.parseInt(dayFivePoint.getText().toString());
        total = Integer.parseInt(totalDailyPoint.getText().toString());

        findViewById(R.id.back_res).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadLoginPoints();
    }

    @Override
    public void onClick(View v) {

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

                JSONArray getData = response.getJSONArray("Data");
                generatePointsUI(getData);

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

    private void loadLoginPoints() {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.KEY_RULE_ID, "1");
            jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(getApplicationContext()));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.ACTIVITY_HISTORY;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void generatePointsUI(JSONArray dailyLogin) {
        String dayCount = "";
        int consDays = 0;
        try {


//            for (int i = 0; i < dailyLogin.length(); i++) {
                JSONObject jsonobj = dailyLogin.getJSONObject(0);

                dayCount = jsonobj.getString("cons_login_days");
                consDays = Integer.parseInt(dayCount);
//            }
            Drawable dr = getResources().getDrawable(R.drawable.ic_unhide_calender);

            switch (consDays) {
                case 1:
                    day1.setBackground(dr);
                    total=total + one;
                    totalDailyPoint.setText(String.valueOf(total));
                    break;
                case 2:
                    day1.setBackground(dr);
                    day2.setBackground(dr);
                    total = total + one + two;
                    totalDailyPoint.setText(String.valueOf(total));
                    break;
                case 3:
                    day1.setBackground(dr);
                    day2.setBackground(dr);
                    day3.setBackground(dr);
                    total = total + one + two + three;
                    totalDailyPoint.setText(String.valueOf(total));
                    break;
                case 4:
                    day1.setBackground(dr);
                    day2.setBackground(dr);
                    day3.setBackground(dr);
                    day4.setBackground(dr);
                    total = total + one + two + three + four;
                    totalDailyPoint.setText(String.valueOf(total));
                    break;
                case 5:
                    day1.setBackground(dr);
                    day2.setBackground(dr);
                    day3.setBackground(dr);
                    day4.setBackground(dr);
                    day5.setBackground(dr);
                    total = total + one + two + three + four + five;
                    totalDailyPoint.setText(String.valueOf(total));
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
