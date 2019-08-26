package com.palprotech.heylaapp.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.adapter.LoginAdapter;
import com.palprotech.heylaapp.bean.database.SQLiteHelper;
import com.palprotech.heylaapp.helper.AlertDialogHelper;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.interfaces.DialogClickListener;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;
import com.palprotech.heylaapp.serviceinterfaces.IServiceListener;
import com.palprotech.heylaapp.utils.CommonUtils;
import com.palprotech.heylaapp.utils.HeylaAppConstants;
import com.palprotech.heylaapp.utils.HeylaAppValidator;
import com.palprotech.heylaapp.utils.PreferenceStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Created by Narendar on 06/10/17.
 */

public class LoginActivity extends AppCompatActivity implements DialogClickListener, IServiceListener, View.OnClickListener {

    private static final String TAG = LoginActivity.class.getName();
    Context context;
    String IMEINo = "";
    private ProgressDialogHelper progressDialogHelper;
    TextView txtGuestLogin;
    private ServiceHelper serviceHelper;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if(!checkAutoDT(this)){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            // Setting Dialog Title
            alertDialog.setTitle("Date and Time settings");

            // Setting Dialog Message
            alertDialog.setMessage("Automatic Date and Time is not enabled. Go to settings and enable to access application.");

            // On pressing the Settings button.
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
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

        IMEINo = String.valueOf(generateRandom(12));


//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//
//            TelephonyManager tm = (TelephonyManager)
//                    getSystemService(Context.TELEPHONY_SERVICE);
//            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
//                    == PackageManager.PERMISSION_DENIED) {
//
//                Log.d("permission", "permission denied to SEND_SMS - requesting it");
//                String[] permissions = {Manifest.permission.READ_PHONE_STATE};
//
//                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
//            }
//            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                IMEINo = tm.getImei();
//            } else {
//                if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
//                        == PackageManager.PERMISSION_DENIED) {
//                    IMEINo = "";
//                } else {
//                    IMEINo = tm.getDeviceId();
//                }
//            }
//        }

        /*if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            TelephonyManager tm = (TelephonyManager)
                    getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            IMEINo = tm.getImei();
        } else {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            IMEINo = telephonyManager.getDeviceId();
        }*/

        if (PreferenceStorage.getUserId(getApplicationContext()) != null && HeylaAppValidator.checkNullString(PreferenceStorage.getUserId(getApplicationContext()))) {
            String city = PreferenceStorage.getEventCityName(getApplicationContext());
//            String isResetOver = PreferenceStorage.getForgotPasswordStatusEnable(getApplicationContext());
            boolean haspreferences = PreferenceStorage.isPreferencesPresent(getApplicationContext());

            /*if (isResetOver.equalsIgnoreCase("no")) {
                Intent intent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                startActivity(intent);
                this.finish();
            } else */
            if (HeylaAppValidator.checkNullString(city) && haspreferences) {
//                Intent intent = new Intent(getApplicationContext(), MainNewActivity.class);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                this.finish();
            } else if (!HeylaAppValidator.checkNullString(city)) {
                Log.d(TAG, "No city yet, show city activity");
                Intent intent = new Intent(getApplicationContext(), SelectCityActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
            } else if (!haspreferences) {
                Log.d(TAG, "No preferences, so launch preferences activity");
                Intent intent = new Intent(getApplicationContext(), SetUpPreferenceActivity.class);
                intent.putExtra("selectedCity", city);
                startActivity(intent);
                this.overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                this.finish();
            }
        } else {
            serviceHelper = new ServiceHelper(this);
            serviceHelper.setServiceListener(this);
            progressDialogHelper = new ProgressDialogHelper(this);


            TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

            tabLayout.addTab(tabLayout.newTab().setText("SIGN IN"));
            tabLayout.addTab(tabLayout.newTab().setText("SIGN UP"));
            tabLayout.setTabTextColors(getResources().getColor(R.color.text_gray), getResources().getColor(R.color.appColorBase));


            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

            final LoginAdapter adapter = new LoginAdapter
                    (getSupportFragmentManager());

            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                    viewPager.getCurrentItem();
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            try {
                PackageInfo info = getPackageManager().getPackageInfo(
                        "com.palprotech.heylaapp",
                        PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
            } catch (PackageManager.NameNotFoundException e) {

            } catch (NoSuchAlgorithmException e) {

            }
        }
    }

    public static long generateRandom(int length) {
        Random random = new Random();
        char[] digits = new char[length];
        digits[0] = (char) (random.nextInt(9) + '1');
        for (int i = 1; i < length; i++) {
            digits[i] = (char) (random.nextInt(10) + '0');
        }
        return Long.parseLong(new String(digits));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
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
                JSONObject userData = response.getJSONObject("userData");
                String userId = userData.getString("user_id");

                if ((userId != null) && !(userId.isEmpty()) && !userId.equalsIgnoreCase("null")) {
                    PreferenceStorage.saveUserId(getApplicationContext(), userId);
                }

                Intent homeIntent = new Intent(getApplicationContext(), SelectCityActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                finish();

                PreferenceStorage.saveUserType(getApplicationContext(), "2");

            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static boolean checkAutoDT(Context c){
        return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
    }

    @Override
    public void onError(String error) {
        progressDialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(this, error);
    }

    @Override
    public void onClick(View v) {
        if (CommonUtils.isNetworkAvailable(this)) {

        } else {
            AlertDialogHelper.showSimpleAlertDialog(this, "No Network connection available");
        }
    }
}