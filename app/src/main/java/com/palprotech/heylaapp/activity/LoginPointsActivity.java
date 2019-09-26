package com.palprotech.heylaapp.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
    ImageView dayOneStar, daytwoStar, dayThreeStar, dayFourStar, dayFiveStar;
    TextView dayTwoPoint, dayOnePoint, dayThreePoint, dayFourPoint, dayFivePoint, totalDailyPoint;
    String sCount;
    int one, two, three, four, five, total, count;

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

        dayOneStar = findViewById(R.id.dayonestar);
        daytwoStar = findViewById(R.id.daytwostar);
        dayThreeStar = findViewById(R.id.daythreestar);
        dayFourStar = findViewById(R.id.dayfourstar);
        dayFiveStar = findViewById(R.id.dayfivestar);


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
                sCount = response.getString("Totalpoints");
                totalDailyPoint.setText(sCount);

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
            Drawable dr = getResources().getDrawable(R.drawable.ic_point_hide);
            Drawable drStar = getResources().getDrawable(R.drawable.ic_leaderboard_star);
            Drawable drBig = getResources().getDrawable(R.drawable.ic_point_wide_hide);

            switch (consDays) {
                case 1:
                    day1.setBackground(dr);
                    dayOneStar.setBackground(drStar);
                    dayOnePoint.setText("1");

                    break;
                case 2:
                    day1.setBackground(dr);
                    day2.setBackground(dr);
                    dayOneStar.setBackground(drStar);
                    daytwoStar.setBackground(drStar);
                    dayOnePoint.setText("1");
                    dayTwoPoint.setText("2");

                    break;
                case 3:
                    day1.setBackground(dr);
                    day2.setBackground(dr);
                    day3.setBackground(dr);
                    dayOneStar.setBackground(drStar);
                    daytwoStar.setBackground(drStar);
                    dayThreeStar.setBackground(drStar);
                    dayOnePoint.setText("1");
                    dayTwoPoint.setText("2");
                    dayThreePoint.setText("3");

                    break;
                case 4:
                    day1.setBackground(dr);
                    day2.setBackground(dr);
                    day3.setBackground(dr);
                    day4.setBackground(dr);
                    dayOneStar.setBackground(drStar);
                    daytwoStar.setBackground(drStar);
                    dayThreeStar.setBackground(drStar);
                    dayFourStar.setBackground(drStar);
                    dayOnePoint.setText("1");
                    dayTwoPoint.setText("2");
                    dayThreePoint.setText("3");
                    dayFourPoint.setText("4");

                    break;
                case 5:
                    day1.setBackground(dr);
                    day2.setBackground(dr);
                    day3.setBackground(dr);
                    day4.setBackground(dr);
                    day5.setBackground(drBig);
                    dayOneStar.setBackground(drStar);
                    daytwoStar.setBackground(drStar);
                    dayThreeStar.setBackground(drStar);
                    dayFourStar.setBackground(drStar);
                    dayFiveStar.setBackground(drStar);
                    dayOnePoint.setText("1");
                    dayTwoPoint.setText("2");
                    dayThreePoint.setText("3");
                    dayFourPoint.setText("4");
                    dayFivePoint.setText("20");

                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
