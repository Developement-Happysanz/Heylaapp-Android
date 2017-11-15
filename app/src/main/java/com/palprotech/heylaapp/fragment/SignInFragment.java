package com.palprotech.heylaapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.activity.ForgotPasswordActivity;
import com.palprotech.heylaapp.activity.LoginActivity;
import com.palprotech.heylaapp.activity.MainActivity;
import com.palprotech.heylaapp.activity.SelectCityActivity;
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
import java.util.Arrays;

public class SignInFragment extends Fragment implements View.OnClickListener, IServiceListener, DialogClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = LoginActivity.class.getName();
    private CallbackManager callbackManager;
    private EditText edtUsername, edtPassword;
    private View rootView;
    private Button signIn;
    private CheckBox saveLoginCheckBox;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private TextView forgotPassword;
    private ImageView btnFacebook, btnGoogle;
    private static final int RC_SIGN_IN = 9001;
    private int mSelectedLoginMode = 0;
    private GoogleApiClient mGoogleApiClient;

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

        return rootView;
    }

    protected void initializeViews() {
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

        FacebookSdk.sdkInitialize(getActivity());
        callbackManager = CallbackManager.Factory.create();

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]

        Boolean saveLogin = PreferenceStorage.isRemembered(getContext());
        if (saveLogin) {
            edtUsername.setText(PreferenceStorage.getUsername(getContext()));
            edtPassword.setText(PreferenceStorage.getPassword(getContext()));
            saveLoginCheckBox.setChecked(true);
        }
    }

    // [START signIn]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d(TAG, "handleSignInResult:" + result.isSuccess());
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount acct = result.getSignInAccount();

                /*tvDetails.setText("Profile Name :" + acct.getDisplayName() +
                        "\nEmail : " + acct.getEmail() +
                        "\nFamily Name :" + acct.getFamilyName() +
                        "\n Given Name :" + acct.getGivenName() +
                        "\n ID :" + acct.getId());*/

                String okSet = "Profile Name :" + acct.getDisplayName() +
                        "\nEmail : " + acct.getEmail() +
                        "\nFamily Name :" + acct.getFamilyName() +
                        "\n Given Name :" + acct.getGivenName() +
                        "\n ID :" + acct.getId() + "\n Image URL :" + acct.getPhotoUrl();
                String newOk = okSet;

                String GCMKey = PreferenceStorage.getGCM(getContext());
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(HeylaAppConstants.PARAMS_NAME, acct.getDisplayName());
                    jsonObject.put(HeylaAppConstants.PARAMS_EMAIL_ID, acct.getEmail());
                    jsonObject.put(HeylaAppConstants.PARAMS_GCM_KEY, GCMKey);
                    jsonObject.put(HeylaAppConstants.PARAMS_LOGIN_TYPE, "1");
                    jsonObject.put(HeylaAppConstants.PARAMS_MOBILE_TYPE, "1");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
                String serverURL = HeylaAppConstants.BASE_URL + HeylaAppConstants.FB_GPLUS_LOGIN;
                serviceHelper.makeGetServiceCall(jsonObject.toString(), serverURL);

                /*Picasso.with(getContext())
                        .load(acct.getPhotoUrl())
                        .into(ivProfileImage);*/
//                updateUI(true);
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
    }

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

                    if (saveLoginCheckBox.isChecked()) {
                        PreferenceStorage.saveUsername(getContext(), username);
                        PreferenceStorage.savePassword(getContext(), password);
                        PreferenceStorage.setRememberMe(getContext(), true);
                    } else {
                        PreferenceStorage.saveUsername(getContext(), "");
                        PreferenceStorage.savePassword(getContext(), "");
                        PreferenceStorage.setRememberMe(getContext(), false);
                    }

                    PreferenceStorage.saveLoginMode(getActivity(), HeylaAppConstants.NORMAL_SIGNUP);
                    mSelectedLoginMode = HeylaAppConstants.NORMAL_SIGNUP;
                }
            } else if (v == forgotPassword) {
                Intent homeIntent = new Intent(getActivity(), ForgotPasswordActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                getActivity().finish();
            } else if (v == btnGoogle) {
                PreferenceStorage.saveLoginMode(getActivity(), HeylaAppConstants.GOOGLE_PLUS);
                mSelectedLoginMode = HeylaAppConstants.GOOGLE_PLUS;
                signIn();
//                signOut();
            } else if (v == btnFacebook) {
                FacebookSdk.sdkInitialize(getActivity());
//                LoginManager.getInstance().logOut();
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email"));
                initFacebook();
                PreferenceStorage.saveLoginMode(getActivity(), HeylaAppConstants.FACEBOOK);
                mSelectedLoginMode = HeylaAppConstants.FACEBOOK;
            }
        } else {
            AlertDialogHelper.showSimpleAlertDialog(getActivity(), "No Network connection available");
        }
    }

    // Login with facebook
    private void initFacebook() {
        Log.d(TAG, "Initializing facebook");
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "facebook Login Registration success");
                        // App code
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
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
//                                            PreferenceStorage.saveSocialNetworkProfilePic(getActivity(), url);
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
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.e(TAG, "" + exception.toString());
                    }
                });
    }

    private boolean validateFields() {
        if (!HeylaAppValidator.checkNullString(this.edtUsername.getText().toString().trim())) {
            edtUsername.setError(getString(R.string.err_email));
            requestFocus(edtUsername);
            return false;
        } else if (!HeylaAppValidator.checkNullString(this.edtPassword.getText().toString().trim())) {
            edtPassword.setError(getString(R.string.err_empty_password));
            requestFocus(edtPassword);
            return false;
        } else if (!HeylaAppValidator.checkStringMinLength(6, this.edtPassword.getText().toString().trim())) {
            edtPassword.setError(getString(R.string.err_min_pass_length));
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
                        AlertDialogHelper.showSimpleAlertDialog(getContext(), msg);

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
                String userName = userData.getString("user_name");
                String mobileNo = userData.getString("mobile_no");
                String emailId = userData.getString("email_id");
                String fullName = userData.getString("full_name");
                String birthDate = userData.getString("birth_date");
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
                if ((birthDate != null) && !(birthDate.isEmpty()) && !birthDate.equalsIgnoreCase("null")) {
                    PreferenceStorage.saveUserBirthday(getActivity(), birthDate);
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

            } catch (JSONException ex) {
                ex.printStackTrace();
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