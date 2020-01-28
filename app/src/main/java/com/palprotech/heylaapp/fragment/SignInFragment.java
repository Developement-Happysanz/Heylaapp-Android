package com.palprotech.heylaapp.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

//import androidx.fragment.app.Fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.activity.ForgotPasswordActivity;
import com.palprotech.heylaapp.activity.LoginActivity;
import com.palprotech.heylaapp.activity.ReactivateAccountActivity;
import com.palprotech.heylaapp.activity.SelectCityActivity;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static com.facebook.FacebookSdk.getApplicationContext;

public class SignInFragment extends Fragment implements View.OnClickListener, IServiceListener, DialogClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = LoginActivity.class.getName();
    private CallbackManager callbackManager;
    private EditText edtUsername, edtPassword;
    private View rootView;
    private Button signIn;
    private CheckBox saveLoginCheckBox;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private TextView forgotPassword, btnGoogle, btnFacebook, txtGuestLogin;
    private static final int RC_SIGN_IN = 9001;
    private int mSelectedLoginMode = 0;
    private GoogleApiClient mGoogleApiClient;
    SQLiteHelper database;
    String IMEINo = "";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private GoogleSignInClient mGoogleSignInClient;

    public static SignInFragment newInstance(int position) {
        SignInFragment frag = new SignInFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);

        initializeViews();
        IMEINo = String.valueOf(generateRandom(12));

        return rootView;
    }

    protected void initializeViews() {
        database = new SQLiteHelper(getContext());
        edtUsername = rootView.findViewById(R.id.edtUsername);
        edtPassword = rootView.findViewById(R.id.edtPassword);
        signIn = rootView.findViewById(R.id.signin);
        signIn.setOnClickListener(this);
        forgotPassword = rootView.findViewById(R.id.forgotpassword);
        forgotPassword.setOnClickListener(this);
        saveLoginCheckBox = rootView.findViewById(R.id.saveLoginCheckBox);
        btnGoogle = rootView.findViewById(R.id.login_using_gplus);
        btnGoogle.setOnClickListener(this);
        btnFacebook = rootView.findViewById(R.id.login_using_fb);
        btnFacebook.setOnClickListener(this);
        txtGuestLogin = (TextView) rootView.findViewById(R.id.guest_txt);
        txtGuestLogin.setOnClickListener(this);

        serviceHelper = new ServiceHelper(getActivity());
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(getActivity());

        try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo(
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

        callbackManager = CallbackManager.Factory.create();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(rootView.getContext(), gso);

        Boolean savedLogin = database.checkRememberMe();
        if (savedLogin) {
            Cursor c = database.selectRememberMe();
            if (c.getCount() > 0) {
                if (c.moveToFirst()) {
                    do {
                        edtUsername.setText(c.getString(0));
                        edtPassword.setText(c.getString(1));
                        saveLoginCheckBox.setChecked(true);
                    } while (c.moveToNext());
                }
            }
        }
    }

    private void sendGoogleLogin(GoogleSignInAccount account) {
        String name = ""+account.getDisplayName();
        String mail = ""+account.getEmail();
        String photoUrl = "" + account.getPhotoUrl();
        PreferenceStorage.saveSocialNetworkProfilePic(getActivity(), photoUrl);

        String GCMKey = PreferenceStorage.getGCM(getContext());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(HeylaAppConstants.PARAMS_NAME, name);
            jsonObject.put(HeylaAppConstants.PARAMS_EMAIL_ID, mail);
            jsonObject.put(HeylaAppConstants.PARAMS_GCM_KEY, GCMKey);
            jsonObject.put(HeylaAppConstants.PARAMS_LOGIN_TYPE, "1");
            jsonObject.put(HeylaAppConstants.PARAMS_MOBILE_TYPE, "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String serverURL = HeylaAppConstants.BASE_URL + HeylaAppConstants.FB_GPLUS_LOGIN;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), serverURL);
    }

    // [START signIn]
    private void signIn() {

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(rootView.getContext());
        if (account != null) {
            sendGoogleLogin(account);
        } else {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }
    // [END signIn]

    // [START signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
//                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String name = ""+account.getDisplayName();
            String mail = ""+account.getEmail();
            String photoUrl = "" + account.getPhotoUrl();
            PreferenceStorage.saveSocialNetworkProfilePic(getActivity(), photoUrl);

            String GCMKey = PreferenceStorage.getGCM(getContext());
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(HeylaAppConstants.PARAMS_NAME, name);
                jsonObject.put(HeylaAppConstants.PARAMS_EMAIL_ID, mail);
                jsonObject.put(HeylaAppConstants.PARAMS_GCM_KEY, GCMKey);
                jsonObject.put(HeylaAppConstants.PARAMS_LOGIN_TYPE, "1");
                jsonObject.put(HeylaAppConstants.PARAMS_MOBILE_TYPE, "1");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
            String serverURL = HeylaAppConstants.BASE_URL + HeylaAppConstants.FB_GPLUS_LOGIN;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), serverURL);
            // Signed in successfully, show authenticated UI.
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            } else {
                // Signed out, show unauthenticated UI.
//                tvDetails.setText("error occured..!");
//                updateUI(false);
                String okSet = "0";
                String newOk = okSet;
            }
        }
        /*if(requestCode == RC_SIGN_IN_FB){

        }*/


    @Override
    public void onClick(View v) {
        if (CommonUtils.isNetworkAvailable(getActivity())) {
            if (v == signIn) {
                if (validateFields()) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edtUsername.getWindowToken(), 0);

                    String username = edtUsername.getText().toString();
                    String password = edtPassword.getText().toString();

                    String GCMKey = PreferenceStorage.getGCM(getContext());
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put(HeylaAppConstants.PARAMS_USERNAME, username);
                        jsonObject.put(HeylaAppConstants.PARAMS_PASSWORD, password);
                        jsonObject.put(HeylaAppConstants.PARAMS_GCM_KEY, GCMKey);
                        jsonObject.put(HeylaAppConstants.PARAMS_MOBILE_TYPE, "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
                    String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.SIGN_IN;
                    serviceHelper.makeGetServiceCall(jsonObject.toString(), url);

                    PreferenceStorage.saveLoginMode(getActivity(), HeylaAppConstants.NORMAL_SIGNUP);
                    mSelectedLoginMode = HeylaAppConstants.NORMAL_SIGNUP;
                }
            } if (v == forgotPassword) {
                Intent homeIntent = new Intent(getActivity(), ForgotPasswordActivity.class);
                startActivity(homeIntent);
//                getActivity().finish();
            } if (v == btnGoogle) {
                PreferenceStorage.saveLoginMode(getActivity(), HeylaAppConstants.GOOGLE_PLUS);
                mSelectedLoginMode = HeylaAppConstants.GOOGLE_PLUS;
                signIn();
//                signOut();
            } if (v == btnFacebook) {
                LoginManager.getInstance().logInWithReadPermissions(SignInFragment.this, (Arrays.asList("public_profile", "email")));
                initFacebook();
                PreferenceStorage.saveLoginMode(getActivity(), HeylaAppConstants.FACEBOOK);
                mSelectedLoginMode = HeylaAppConstants.FACEBOOK;
            } if (v == txtGuestLogin) {

                String GCMKey = PreferenceStorage.getGCM(getActivity());
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(HeylaAppConstants.PARAMS_UNIQUE_ID, IMEINo);
                    jsonObject.put(HeylaAppConstants.PARAMS_GCM_KEY, GCMKey);
                    jsonObject.put(HeylaAppConstants.PARAMS_MOBILE_TYPE, "1");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
                String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.GUEST_LOGIN;
                serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
            }
        } else {
            AlertDialogHelper.showSimpleAlertDialog(getActivity(), "No Network connection available");
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
    // Login with facebook
    private void initFacebook() {
        Log.d(TAG, "Initializing facebook");
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // Retrieving access token using the LoginResult
                AccessToken accessToken = loginResult.getAccessToken();
                GraphRequest request = GraphRequest.newMeRequest( accessToken, new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {
                                if (response.getError() != null) {
                                    // handle error
                                } else {
                                    String email = me.optString("email");
                                    String id = me.optString("id");
                                    String name = me.optString("name");
                                    String gender = me.optString("gender");
                                    String birthday = me.optString("birthday");
                                    Log.d(TAG, "facebook gender" + gender + "birthday" + birthday);
//                                            PreferenceStorage.saveUserEmail(getActivity(), email);
//                                            PreferenceStorage.saveUserName(getActivity(), name);
                                    String url = "https://graph.facebook.com/" + id + "/picture?type=large";
                                    Log.d(TAG, "facebook birthday" + birthday);
                                    PreferenceStorage.saveSocialNetworkProfilePic(getActivity(), url);
                                    if (gender != null) {
//                                                PreferenceStorage.saveUserGender(getActivity(), gender);
                                    }
                                    if (birthday != null) {
//                                                PreferenceStorage.saveUserBirthday(getActivity(), birthday);
                                    }
                                    // send email and id to your web server
                                    String GCMKey = PreferenceStorage.getGCM(getContext());
                                    JSONObject jsonObject = new JSONObject();
                                    try {
                                        jsonObject.put(HeylaAppConstants.PARAMS_NAME, name);
                                        jsonObject.put(HeylaAppConstants.PARAMS_EMAIL_ID, email);
                                        jsonObject.put(HeylaAppConstants.PARAMS_SOCIAL_IMAGE, url);
                                        jsonObject.put(HeylaAppConstants.PARAMS_GCM_KEY, GCMKey);
                                        jsonObject.put(HeylaAppConstants.PARAMS_LOGIN_TYPE, "1");
                                        jsonObject.put(HeylaAppConstants.PARAMS_MOBILE_TYPE, "1");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
                                    String serverURL = HeylaAppConstants.BASE_URL + HeylaAppConstants.FB_GPLUS_LOGIN;
                                    serviceHelper.makeGetServiceCall(jsonObject.toString(), serverURL);
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,email,name,link,birthday,gender");
                request.setParameters(parameters);
                request.executeAsync();

            }
            @Override
            public void onCancel() {
            }
            @Override
            public void onError(FacebookException error) {
            }
        });
    }

    private boolean validateFields() {
        if (!HeylaAppValidator.checkNullString(this.edtUsername.getText().toString().trim())) {
            edtUsername.setError(getString(R.string.err_empty_username));
            requestFocus(edtUsername);
            return false;
        } else if (!HeylaAppValidator.checkNullString(this.edtPassword.getText().toString().trim())) {
            edtPassword.setError(getString(R.string.err_empty_password_login));
            requestFocus(edtPassword);
            return false;
        } else if (!HeylaAppValidator.checkStringMinLength(6, this.edtPassword.getText().toString().trim())) {
            edtPassword.setError(getString(R.string.err_password));
            requestFocus(edtPassword);
            return false;
        } else {
            return true;
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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
                        if (msg.equalsIgnoreCase("Account Deactivated")) {
                            activateAccAlert();
                        } else {
                            AlertDialogHelper.showSimpleAlertDialog(getContext(), msg);
                        }
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                        sharedPreferences.edit().clear().apply();
                        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestEmail()
                                .build();
                        // Build a GoogleSignInClient with the options specified by gso.
                        LoginManager.getInstance().logOut();
                        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
                        mGoogleSignInClient.signOut();
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

    private void activateAccAlert() {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Deactivated Account");
        alertDialogBuilder.setMessage("This account has been deactivated. Do you wish to reactivate it?");
        alertDialogBuilder.setPositiveButton("Yes", (arg0, arg1) -> deactivateAcc());
        alertDialogBuilder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        alertDialogBuilder.show();
    }

    private void deactivateAcc() {
        Intent homeIntent = new Intent(getApplicationContext(), ReactivateAccountActivity.class);
        startActivity(homeIntent);
    }

    @Override
    public void onResponse(JSONObject response) {

        progressDialogHelper.hideProgressDialog();

        if (validateSignInResponse(response)) {
            try {
                JSONObject userData = response.getJSONObject("userData");
                String userId = userData.getString("user_id");
                String userName = userData.getString("user_name");
                String mobileNo = userData.getString("mobile_no");
                String emailId = userData.getString("email_id");
                String fullName = userData.getString("full_name");
                String birthDate = userData.getString("birth_date");
                String birthday = "";
                if (!birthDate.isEmpty()) {
                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
                    Date date = (Date) formatter.parse(birthDate);
                    SimpleDateFormat event_date = new SimpleDateFormat("dd-MM-yyyy", Locale.UK);
                    birthday = event_date.format(date.getTime());
                }
                String gender = userData.getString("gender");
                String occupation = userData.getString("occupation");
                String addressLine1 = userData.getString("address_line_1");
                String addressLine2 = userData.getString("address_line_2");
                String addressLine3 = userData.getString("address_line_3");
                String countryId = userData.getString("country_id");
                String countryName = userData.getString("country_name");
                String stateId = userData.getString("state_id");
                String stateName = userData.getString("state_name");
                String cityId = userData.getString("city_id");
                String cityName = userData.getString("city_name");
                String zip = userData.getString("zip");
                String pictureUrl = userData.getString("picture_url");
                String newsLetterStatus = userData.getString("newsletter_status");
                String emailVerifyStatus = userData.getString("email_verify_status");
                String userRole = userData.getString("user_role");
                String userRoleName = userData.getString("user_role_name");
                String referralCode = userData.getString("referal_code");

                if ((userId != null) && !(userId.isEmpty()) && !userId.equalsIgnoreCase("null")) {
                    PreferenceStorage.saveUserId(getActivity(), userId);
                }
                if ((userName != null) && !(userName.isEmpty()) && !userName.equalsIgnoreCase("null")) {
                    PreferenceStorage.saveUsername(getActivity(), userName);
                }
                if ((mobileNo != null) && !(mobileNo.isEmpty()) && !mobileNo.equalsIgnoreCase("null")) {
                    PreferenceStorage.saveMobileNo(getActivity(), mobileNo);
                }
                if ((emailId != null) && !(emailId.isEmpty()) && !emailId.equalsIgnoreCase("null")) {
                    PreferenceStorage.saveEmailId(getActivity(), emailId);
                }
                if ((fullName != null) && !(fullName.isEmpty()) && !fullName.equalsIgnoreCase("null")) {
                    PreferenceStorage.saveFullName(getActivity(), fullName);
                }
                if ((birthday != null) && !(birthday.isEmpty()) && !birthday.equalsIgnoreCase("null")) {
                    PreferenceStorage.saveUserBirthday(getActivity(), birthday);
                }
                if ((gender != null) && !(gender.isEmpty()) && !gender.equalsIgnoreCase("null")) {
                    PreferenceStorage.saveUserGender(getActivity(), gender);
                }
                if ((occupation != null) && !(occupation.isEmpty()) && !occupation.equalsIgnoreCase("null")) {
                    PreferenceStorage.saveUserOccupation(getActivity(), occupation);
                }
                if ((addressLine1 != null) && !(addressLine1.isEmpty()) && !addressLine1.equalsIgnoreCase("null")) {
                    PreferenceStorage.saveUserAddressLine1(getActivity(), addressLine1);
                }
                if ((addressLine2 != null) && !(addressLine2.isEmpty()) && !addressLine2.equalsIgnoreCase("null")) {
                    PreferenceStorage.saveUserAddressLine2(getActivity(), addressLine2);
                }
                if ((addressLine3 != null) && !(addressLine3.isEmpty()) && !addressLine3.equalsIgnoreCase("null")) {
                    PreferenceStorage.saveUserAddressLine3(getActivity(), addressLine3);
                }
                if ((countryId != null) && !(countryId.isEmpty()) && !countryId.equalsIgnoreCase("null")) {
                    PreferenceStorage.saveUserCountryId(getActivity(), countryId);
                }
                if ((countryName != null) && !(countryName.isEmpty()) && !countryName.equalsIgnoreCase("null")) {
                    PreferenceStorage.saveUserCountryName(getActivity(), countryName);
                }
                if ((stateId != null) && !(stateId.isEmpty()) && !stateId.equalsIgnoreCase("null")) {
                    PreferenceStorage.saveUserStateId(getActivity(), stateId);
                }
                if ((stateName != null) && !(stateName.isEmpty()) && !stateName.equalsIgnoreCase("null")) {
                    PreferenceStorage.saveUserStateName(getActivity(), stateName);
                }
                if ((cityId != null) && !(cityId.isEmpty()) && !cityId.equalsIgnoreCase("null")) {
                    PreferenceStorage.saveUserCityId(getActivity(), cityId);
                }
                if ((cityName != null) && !(cityName.isEmpty()) && !cityName.equalsIgnoreCase("null")) {
                    PreferenceStorage.saveUserCityName(getActivity(), cityName);
                }
                if ((zip != null) && !(zip.isEmpty()) && !zip.equalsIgnoreCase("null")) {
                    PreferenceStorage.saveUserZipCode(getActivity(), zip);
                }
                if ((pictureUrl != null) && !(pictureUrl.isEmpty()) && !pictureUrl.equalsIgnoreCase("null")) {
                    PreferenceStorage.saveUserPicture(getActivity(), pictureUrl);
                }
                if ((newsLetterStatus != null) && !(newsLetterStatus.isEmpty()) && !newsLetterStatus.equalsIgnoreCase("null")) {
                    PreferenceStorage.saveUserNewsLetterStatus(getActivity(), newsLetterStatus);
                }
                if ((emailVerifyStatus != null) && !(emailVerifyStatus.isEmpty()) && !emailVerifyStatus.equalsIgnoreCase("null")) {
                    PreferenceStorage.saveUserEmailVerifyStatus(getActivity(), emailVerifyStatus);
                }
                if ((userRole != null) && !(userRole.isEmpty()) && !userRole.equalsIgnoreCase("null")) {
                    PreferenceStorage.saveUserRole(getActivity(), userRole);
                }
                if ((userRoleName != null) && !(userRoleName.isEmpty()) && !userRoleName.equalsIgnoreCase("null")) {
                    PreferenceStorage.saveUserRoleName(getActivity(), userRoleName);
                }
                if ((referralCode != null) && !(referralCode.isEmpty()) && !referralCode.equalsIgnoreCase("null")) {
                    PreferenceStorage.saveUserReferralCode(getActivity(), referralCode);
                }

                PreferenceStorage.saveUserType(getActivity(), "1");

                if (saveLoginCheckBox.isChecked()) {
                    database.deleteRememberMe();
                    database.remember_me_insert(edtUsername.getText().toString().trim(), edtPassword.getText().toString().trim(), "yes");
                } else {
                    database.deleteRememberMe();
                    database.remember_me_insert(edtUsername.getText().toString().trim(), edtPassword.getText().toString().trim(), "no");
                }

            } catch (JSONException ex) {
                ex.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Intent homeIntent = new Intent(getActivity(), SelectCityActivity.class);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            getActivity().finish();
        }
    }

    @Override
    public void onError(String error) {
        progressDialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(getContext(), error);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}