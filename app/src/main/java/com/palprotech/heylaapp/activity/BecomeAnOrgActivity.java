package com.palprotech.heylaapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.bean.support.Event;
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

public class BecomeAnOrgActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener {

    private static final String TAG = AttendeesInfoActivity.class.getName();
    LinearLayout layout_all;
    private Event event;
    private ImageView back;
    String  eventNoOfTicket, orderId;
    int noOfTickets;
    private Button btnProceed;
    private Button btnSkip;
    protected ProgressDialogHelper progressDialogHelper;
    private ServiceHelper serviceHelper;
    String res = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_org);
        btnProceed = findViewById(R.id.request);
        btnProceed.setOnClickListener(this);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        findViewById(R.id.back_res).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        orgStatus();

    }

    @Override
    public void onClick(View v) {
        if (v == btnProceed) {
            updateAttendeesToServer();
        }
    }

    private void updateAttendeesToServer() {
        res = "request";
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.REQUEST_ORG;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);

    }

    private void orgStatus() {
        res = "status";

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.ORG_STATUS;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);

    }

    @Override
    public void onResponse(JSONObject response) {

        progressDialogHelper.hideProgressDialog();
        if (validateSignInResponse(response)) {
            try {
                if (res.equalsIgnoreCase("request")) {
                    AlertDialogHelper.showSimpleAlertDialog(this, response.getString("msg"));
                } else if (res.equalsIgnoreCase("status")) {
                    if (response.getString("msg").equalsIgnoreCase("Approved")) {
                        btnProceed.setClickable(false);
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
                        alertDialogBuilder.setTitle("Organiser status");
                        alertDialogBuilder.setMessage("You have been approved as an organizer!\nSign in to Heyla web app to create and organize events.");
                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent i = new Intent (getApplicationContext(), MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                        });
                        alertDialogBuilder.setCancelable(false);
                        alertDialogBuilder.show();
                        PreferenceStorage.saveUserRole(this, "2");
                    } else {
                        AlertDialogHelper.showSimpleAlertDialog(this, response.getString("msg"));
                        btnProceed.setVisibility(View.GONE);
                    }
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
                        if (msg.equalsIgnoreCase("No request found")) {

                        } else {
                            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
                            alertDialogBuilder.setTitle("Organiser status");
                            alertDialogBuilder.setMessage(msg);
                            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Intent i = new Intent (getApplicationContext(), MainActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            });
                            alertDialogBuilder.setCancelable(false);
                            alertDialogBuilder.show();
//                            btnProceed.setVisibility(View.GONE);
                        }
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

}
