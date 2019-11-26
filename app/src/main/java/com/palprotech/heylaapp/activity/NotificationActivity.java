package com.palprotech.heylaapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.adapter.NotificationListAdapter;
import com.palprotech.heylaapp.bean.support.Notification;
import com.palprotech.heylaapp.bean.support.NotificationList;
import com.palprotech.heylaapp.helper.AlertDialogHelper;
import com.palprotech.heylaapp.helper.HeylaAppHelper;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.interfaces.DialogClickListener;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;
import com.palprotech.heylaapp.serviceinterfaces.IServiceListener;
import com.palprotech.heylaapp.utils.HeylaAppConstants;
import com.palprotech.heylaapp.utils.PreferenceStorage;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NotificationActivity extends AppCompatActivity implements IServiceListener, View.OnClickListener, DialogClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = NotificationActivity.class.getName();
    private ImageView back;
    protected ProgressDialogHelper progressDialogHelper;
    private ServiceHelper serviceHelper;
    ArrayList<Notification> notificationArrayList = new ArrayList<>();
    NotificationList notification = new NotificationList();
    NotificationListAdapter notificationListAdapter;
    //    ListView notificationList;
    LinearLayout notificationList;
    Typeface typeface;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ImageView bckbtn = (ImageView) findViewById(R.id.back_res);
        bckbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);
        notificationList = findViewById(R.id.noti_list);

        typeface = Typeface.createFromAsset(this.getAssets(),"fonts/muli.ttf");

        if (PreferenceStorage.getUserType(this).equalsIgnoreCase("1")) {
            loadNotificationList();
        } else {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.login);
            alertDialogBuilder.setMessage("Log in to continue");
            alertDialogBuilder.setPositiveButton(R.string.alert_button_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    doLogout();
                }
            });
            alertDialogBuilder.setNegativeButton(R.string.alert_button_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialogBuilder.show();
        }
    }

    private void loadNotificationList() {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.VIEW_NOTIFICATION;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void doLogout() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().clear().apply();
//        TwitterUtil.getInstance().resetTwitterRequestToken();

        Intent homeIntent = new Intent(this, SplashScreenActivity.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
        finish();
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
            Gson gson = new Gson();
            notification = gson.fromJson(response.toString(), NotificationList.class);
            if (notification.getData() != null && notification.getData().size() > 0) {
//                updateListAdapter(notification.getData());
                createNotificationList(notification.getData().size());
            }
        }
    }

    private void createNotificationList(int memberCount) {

        try {

            for (int c1 = 0; c1 < memberCount; c1++) {


                LinearLayout.LayoutParams paramsCell = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                paramsCell.setMargins(20, 10, 20, 10);

                LinearLayout cell = new LinearLayout(this);
                cell.setLayoutParams(paramsCell);
                cell.setOrientation(LinearLayout.VERTICAL);
                cell.setPadding(0, 0, 0, 0);
                cell.setElevation(6.0f);
                cell.setBackground(ContextCompat.getDrawable(this, R.drawable.shadow_round));

                LinearLayout.LayoutParams imgParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 380);
                imgParam.setMargins(20, 0, 20, 0);
//                params1.addRule(LinearLayout.LEFT_OF, R.id.user_rank_txt);


                LinearLayout.LayoutParams paramDate = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                paramDate.setMargins(20, 20, 0, 0);

                LinearLayout.LayoutParams paramTitle = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                paramTitle.setMargins(20, 10, 0, 0);


                LinearLayout.LayoutParams paramDec = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                paramDec.setMargins(20, 0, 20, 20);

                TextView notificationDate = new TextView(this);
                notificationDate.setLayoutParams(paramDate);

                notificationDate.setText(notification.getData().get(c1).getTemplate_name());
                String start = HeylaAppHelper.getDate(notification.getData().get(c1).getCreated_at());

                try {
                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
                    Date date = (Date) formatter.parse(start);
                    SimpleDateFormat event_date = new SimpleDateFormat("dd MMM yyyy");
                    String date_name = event_date.format(date.getTime());
                    if ((start != null)) {
                        notificationDate.setText(date_name);
                    } else {
                        notificationDate.setText("N/A");
                    }
                } catch (final ParseException e) {
                    e.printStackTrace();
                }

                notificationDate.setId(R.id.notification_date);
                notificationDate.setTextSize(12.0f);
                notificationDate.setBackgroundColor(Color.parseColor("#ffffff"));
//                notificationDate.setSingleLine(true);
                notificationDate.setTextColor(Color.parseColor("#000000"));
                notificationDate.setTypeface(typeface);

                TextView notificationTitle = new TextView(this);
                notificationTitle.setLayoutParams(paramTitle);

                notificationTitle.setText(notification.getData().get(c1).getTemplate_name());

                notificationTitle.setId(R.id.notification_title);
                notificationTitle.setTextSize(14.0f);
                notificationTitle.setSingleLine(true);
                notificationTitle.setBackgroundColor(Color.parseColor("#ffffff"));
                notificationTitle.setTextColor(Color.parseColor("#000000"));
                notificationTitle.setTypeface(typeface, Typeface.BOLD);
                TextView notificationDec = new TextView(this);
                notificationDec.setLayoutParams(paramDec);
                notificationDec.setId(R.id.notification_detail);
                notificationDec.setBackgroundColor(Color.parseColor("#ffffff"));
                notificationDec.setTextSize(14.0f);
                notificationDec.setText(notification.getData().get(c1).getTemplate_content());
                notificationDec.setTypeface(typeface);
                notificationDec.setGravity(Gravity.FILL_HORIZONTAL);

                ImageView notificationImg = new ImageView(this);
                notificationImg.setLayoutParams(imgParam);

                String url = notification.getData().get(c1).getTemplate_pic();

                notificationImg.setId(R.id.user_img);
                if ((url != null) && !(url.isEmpty())) {
                    Picasso.get().load(url).into(notificationImg);
                }

//                notificationImg.setBackgroundColor(Color.parseColor("#ffffff"));

                cell.addView(notificationDate);
                if (((url != null) && !(url.isEmpty()))) {
                    cell.addView(notificationImg);
                }
                cell.addView(notificationTitle);
                cell.addView(notificationDec);
//                if (((url != null) && !(url.isEmpty()))) {
//                    notificationImg.setVisibility(View.GONE);
//                }

                notificationList.addView(cell);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void updateListAdapter(ArrayList<Notification> notifications) {
//        this.notificationArrayList.addAll(notifications);
//        if (notificationListAdapter == null) {
//            notificationListAdapter = new NotificationListAdapter(this, this.notificationArrayList);
//            notificationList.setAdapter(notificationListAdapter);
//        } else {
//            notificationListAdapter.notifyDataSetChanged();
//        }
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
