package com.palprotech.heylaapp.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.palprotech.heylaapp.BuildConfig;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.bean.support.StoreCity;
import com.palprotech.heylaapp.bean.support.StoreCountry;
import com.palprotech.heylaapp.bean.support.StoreState;
import com.palprotech.heylaapp.helper.AlertDialogHelper;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.interfaces.DialogClickListener;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;
import com.palprotech.heylaapp.serviceinterfaces.IServiceListener;
import com.palprotech.heylaapp.utils.AndroidMultiPartEntity;
import com.palprotech.heylaapp.utils.CommonUtils;
import com.palprotech.heylaapp.utils.HeylaAppConstants;
import com.palprotech.heylaapp.utils.HeylaAppValidator;
import com.palprotech.heylaapp.utils.PreferenceStorage;
import com.squareup.picasso.Picasso;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;
import com.yalantis.ucrop.UCrop;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Narendar on 23/10/17.
 */

public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener, View.OnClickListener, IServiceListener, DialogClickListener, com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 12;
    public static final int MY_PERMISSIONS_REQUEST_CAM = 123;
    private static final String TAG = ProfileActivity.class.getName();

    private List<String> mOccupationList = new ArrayList<String>();
    private ArrayAdapter<String> mOccupationAdapter = null;

    private ArrayAdapter<String> mGenderAdapter = null;
    private List<String> mGenderList = new ArrayList<String>();

    ArrayAdapter<StoreCountry> mCountryAdapter = null;
    ArrayList<StoreCountry> countryList;

    ArrayAdapter<StoreState> mStateAdapter = null;
    ArrayList<StoreState> stateList;

    ArrayAdapter<StoreCity> mCityAdapter = null;
    ArrayList<StoreCity> cityList;

    private TextInputLayout inputGender, inputAddress1, inputAddress2, inputAddress3, inputPincode, inputName,
            inputUsername, inputBirthday, inputOccupation, inputCountry, inputState, inputCity;
    EditText mBirthday, mGender, mOccupation, address1, address2, address3, pincode, name,
            username, country, state, city;
    private CheckBox cbSubscription;
    private ImageView mProfileImage = null;
    private Uri outputFileUri;
    static final int REQUEST_IMAGE_GET = 1;
    static final int CROP_PIC = 2;
    Button save;
    private String checkProfileState = "";
    private String checkInternalState = "";
    private DatePickerDialog mDatePicker;
    private SimpleDateFormat mDateFormatter;

    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private String mActualFilePath = null;
    private Uri mSelectedImageUri = null;
    private Bitmap mCurrentUserImageBitmap = null;
    private ProgressDialog mProgressDialog = null;
    private String mUpdatedImageUrl = null;
    private UploadFileToServer mUploadTask = null;
    long totalSize = 0;

    String fullName = "";
    String userName = "";
    String birthDay = "";
    String occupation = "";
    String gender = "";
    String addressLineOne = "";
    String addressLineTwo = "";
    String landMark = "";
    String countryName = "";
    String countryId = "";
    String stateName = "";
    String stateId = "";
    String cityName = "";
    String cityId = "";
    String pinCode = "";
    boolean newsLetter = false;
    String newsLetterStatus = "N";

    File image = null;

//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    public static boolean checkPermission(final Context context)
//    {
//        int currentAPIVersion = Build.VERSION.SDK_INT;
//        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
//        {
//            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
//                    alertBuilder.setCancelable(true);
//                    alertBuilder.setTitle("Permission necessary");
//                    alertBuilder.setMessage("External storage permission is necessary");
//                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//                        public void onClick(DialogInterface dialog, int which) {
//                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
//                        }
//                    });
//                    AlertDialog alert = alertBuilder.create();
//                    alert.show();
//
//                } else {
//                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
//                }
//                return false;
//            } else {
//                return true;
//            }
//        } else {
//            return true;
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setUI();
    }

    void setUI() {

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

//        Bundle extras = getIntent().getExtras();
//        checkProfileState = extras.getString("profile_state");
        checkProfileState = PreferenceStorage.getCheckFirstTimeProfile(getApplicationContext());

        findViewById(R.id.back_res).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        mProfileImage = (ImageView) findViewById(R.id.profile_img);
        mProfileImage.setOnClickListener(this);

        String url = PreferenceStorage.getUserPicture(this);
        if (((url != null) && !(url.isEmpty()))) {
            Picasso.with(this).load(url).placeholder(R.drawable.ic_default_profile).error(R.drawable.ic_default_profile).into(mProfileImage);
        }
        setupUI(findViewById(R.id.scrollID));
        inputName = (TextInputLayout) findViewById(R.id.ti_name);
        name = (EditText) findViewById(R.id.edtName);
        if (PreferenceStorage.getFullName(this) != null) {
            name.setText(PreferenceStorage.getFullName(this));
        }
        inputUsername = (TextInputLayout) findViewById(R.id.ti_username);
        username = (EditText) findViewById(R.id.edtUsername);
        if (PreferenceStorage.getUsername(this) != null) {
            username.setText(PreferenceStorage.getUsername(this));
        }
        inputBirthday = (TextInputLayout) findViewById(R.id.ti_birthday);
        mBirthday = (EditText) findViewById(R.id.edtBirthday);
        if (PreferenceStorage.getUserBirthday(this) != null) {
            mBirthday.setText(PreferenceStorage.getUserBirthday(this));
        }
        mBirthday.setFocusable(false);
        inputOccupation = (TextInputLayout) findViewById(R.id.ti_occupation);
        mOccupation = (EditText) findViewById(R.id.occupationlist);
        if (PreferenceStorage.getUserOccupation(this) != null) {
            mOccupation.setText(PreferenceStorage.getUserOccupation(this));
        }
        mOccupation.setFocusable(false);
        inputGender = (TextInputLayout) findViewById(R.id.ti_gender);
        mGender = (EditText) findViewById(R.id.genderList);
        if (PreferenceStorage.getUserGender(this) != null) {
            mGender.setText(PreferenceStorage.getUserGender(this));
        }
        mGender.setFocusable(false);
        inputAddress1 = (TextInputLayout) findViewById(R.id.ti_address_line_one);
        address1 = (EditText) findViewById(R.id.edtAddressLineOne);
        if (PreferenceStorage.getUserAddressLine1(this) != null) {
            address1.setText(PreferenceStorage.getUserAddressLine1(this));
        }
        inputAddress2 = (TextInputLayout) findViewById(R.id.ti_address_line_two);
        address2 = (EditText) findViewById(R.id.edtAddressLinetwo);
        if (PreferenceStorage.getUserAddressLine2(this) != null) {
            address2.setText(PreferenceStorage.getUserAddressLine2(this));
        }
        inputAddress3 = (TextInputLayout) findViewById(R.id.ti_address_line_three);
        address3 = (EditText) findViewById(R.id.edtAddressLinethree);
        if (PreferenceStorage.getUserAddressLine3(this) != null) {
            address3.setText(PreferenceStorage.getUserAddressLine3(this));
        }
        inputCountry = (TextInputLayout) findViewById(R.id.ti_country);
        country = (EditText) findViewById(R.id.countryList);
        if (PreferenceStorage.getUserCountryName(this) != null) {
            country.setText(PreferenceStorage.getUserCountryName(this));
        }
//        country.setOnClickListener(this);
        inputState = (TextInputLayout) findViewById(R.id.ti_state);
        state = (EditText) findViewById(R.id.stateList);
        if (PreferenceStorage.getUserStateName(this) != null) {
            state.setText(PreferenceStorage.getUserStateName(this));
        }
//        state.setOnClickListener(this);
//        state.setFocusable(false);
        inputCity = (TextInputLayout) findViewById(R.id.ti_city);
        city = (EditText) findViewById(R.id.cityList);
        if (PreferenceStorage.getEventCityName(this) != null) {
            city.setText(PreferenceStorage.getEventCityName(this));
        }
//        city.setOnClickListener(this);
//        city.setFocusable(false);
        inputPincode = (TextInputLayout) findViewById(R.id.ti_pincode);
        pincode = (EditText) findViewById(R.id.edtPincode);
        if (PreferenceStorage.getUserZipcode(this) != null) {
            pincode.setText(PreferenceStorage.getUserZipcode(this));
        }
        cbSubscription = (CheckBox) findViewById(R.id.subscription);
        if (PreferenceStorage.getUserNewsLetterStatus(this).equalsIgnoreCase(String.valueOf('Y'))){
            cbSubscription.setChecked(Boolean.TRUE);
        }
        save = (Button) findViewById(R.id.saveprofile);
        save.setOnClickListener(this);

        mDateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        String birthdayval = PreferenceStorage.getUserBirthday(this);
        if (birthdayval != null) {

            mBirthday.setText(birthdayval);
        }

        mBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "birthday widget selected");
                showBirthdayDate();

            }
        });

        GetCountry();

        mGenderList.add("Male");
        mGenderList.add("Female");
        mGenderList.add("Other");
        mGenderList.add("Rather not say");

        mGenderAdapter = new ArrayAdapter<String>(this, R.layout.gender_layout, R.id.gender_name, mGenderList) { // The third parameter works around ugly Android legacy. http://stackoverflow.com/a/18529511/145173
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Log.d(TAG, "getview called" + position);
                View view = getLayoutInflater().inflate(R.layout.gender_layout, parent, false);
                TextView gendername = (TextView) view.findViewById(R.id.gender_name);
                gendername.setText(mGenderList.get(position));

                // ... Fill in other views ...
                return view;
            }
        };

        String genderVal = CommonUtils.getGenderVal(PreferenceStorage.getUserGender(this));
        if (genderVal != null) {
            mGender.setText(genderVal);
        }
        mGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGenderList();
            }
        });

        //occupation related data
        mOccupationList.add("Student");
        mOccupationList.add("Employed");
        mOccupationList.add("Self Employed/Business");
        mOccupationList.add("Home Maker");
        mOccupationList.add("Other");

        mOccupationAdapter = new ArrayAdapter<String>(this, R.layout.gender_layout, R.id.gender_name, mOccupationList) { // The third parameter works around ugly Android legacy. http://stackoverflow.com/a/18529511/145173
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Log.d(TAG, "getview called" + position);
                View view = getLayoutInflater().inflate(R.layout.gender_layout, parent, false);
                TextView gendername = (TextView) view.findViewById(R.id.gender_name);
                gendername.setText(mOccupationList.get(position));

                // ... Fill in other views ...
                return view;
            }
        };

        String occupation = PreferenceStorage.getUserOccupation(this);
        if (occupation != null) {
            mOccupation.setText(occupation);
        }

        mOccupation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOccupationList();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == save) {
            if (validateFields()) {
                checkInternalState = "profile_update";
                saveProfile();
            }
        } else if (view == mProfileImage) {
//            checkPermission(this);
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//                    == PackageManager.PERMISSION_DENIED) {
//
//                Log.d("permission", "permission denied to SEND_SMS - requesting it");
//                String[] permissions = {Manifest.permission.CAMERA};
//
//                ActivityCompat.requestPermissions(ProfileActivity.this, permissions, MY_PERMISSIONS_REQUEST_CAM);
//
//            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] perm = {Manifest.permission.READ_EXTERNAL_STORAGE};

                ActivityCompat.requestPermissions(ProfileActivity.this, perm, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            }
            else {
                openImageIntent();
            }
        } else if (view == country) {
            if (stateList != null)
                stateList.clear();
            if (cityList != null)
                cityList.clear();
//            showCountryList();

        } else if (view == state) {
            if (cityList != null)
                cityList.clear();
//            showStateList();
        } else if (view == city) {
//            showCityList();
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String[] permissions, int[] grantResults) {
//        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
//
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
//                openImageIntent();
//            } else {
//
//                Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
//
//            }
//
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 123:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED) {

                        Log.d("permission", "permission denied to SEND_SMS - requesting it");
                        String[] perm = {Manifest.permission.READ_EXTERNAL_STORAGE};

                        ActivityCompat.requestPermissions(ProfileActivity.this, perm, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                    } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED) {

                        Log.d("permission", "permission denied to SEND_SMS - requesting it");
                        String[] perm = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

                        ActivityCompat.requestPermissions(ProfileActivity.this, perm, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                    } else {
                        openImageIntent();
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;

            case 1:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED) {

                        Log.d("permission", "permission denied to SEND_SMS - requesting it");
                        String[] perm = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

                        ActivityCompat.requestPermissions(ProfileActivity.this, perm, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                    } else {
                        openImageIntent();
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;

            case 12:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    openImageIntent();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;

            default:
                openImageIntent();

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }



    void saveProfile() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Updating Profile");
        mProgressDialog.show();
        if ((mActualFilePath != null)) {
            Log.d(TAG, "Update profile picture");
            saveUserImage();
        } else {
            saveProfileData();
        }
    }

    private void saveUserImage() {

        mUpdatedImageUrl = null;

        new UploadFileToServer().execute();
    }

    private void saveProfileData() {

        fullName = name.getText().toString();
        PreferenceStorage.saveFullName(this, fullName);
        userName = username.getText().toString();
        PreferenceStorage.saveUsername(this, userName);
        birthDay = mBirthday.getText().toString();
        PreferenceStorage.saveUserBirthday(this, birthDay);
        occupation = mOccupation.getText().toString();
        PreferenceStorage.saveUserOccupation(this, occupation);
        gender = mGender.getText().toString();
        PreferenceStorage.saveUserGender(this, gender);
        addressLineOne = address1.getText().toString();
        PreferenceStorage.saveUserAddressLine1(this, addressLineOne);
        addressLineTwo = address2.getText().toString();
        PreferenceStorage.saveUserAddressLine2(this, addressLineTwo);
        landMark = address3.getText().toString();
        PreferenceStorage.saveUserAddressLine3(this, landMark);
        countryName = country.getText().toString();
        PreferenceStorage.saveUserCountryName(this, countryName);
        stateName = state.getText().toString();
        PreferenceStorage.saveUserStateName(this, stateName);
        cityName = city.getText().toString();
        PreferenceStorage.saveEventCityName(this, cityName);
        pinCode = pincode.getText().toString();
        PreferenceStorage.saveUserZipCode(this, pinCode);
        newsLetter = cbSubscription.isChecked();

        if (newsLetter) {
            newsLetterStatus = "Y";
        } else {
            newsLetterStatus = "N";
        }
        PreferenceStorage.saveUserNewsLetterStatus(this, newsLetterStatus);

        String newFormat = "";
        if (mBirthday.getText().toString() != null && mBirthday.getText().toString() == "") {

            String date = mBirthday.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/mm/dd HH:MM:SS");
            Date testDate = null;
            try {
                testDate = sdf.parse(date);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/mm/dd");
            newFormat = formatter.format(testDate);
            System.out.println(".....Date..." + newFormat);
        }

        if (validateFields()) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(HeylaAppConstants.PARAMS_USER_ID, PreferenceStorage.getUserId(getApplicationContext()));
                jsonObject.put(HeylaAppConstants.PARAMS_FULL_NAME, fullName);
                jsonObject.put(HeylaAppConstants.PARAMS_USERNAME, userName);
                jsonObject.put(HeylaAppConstants.PARAMS_DATE_OF_BIRTH, newFormat);
                jsonObject.put(HeylaAppConstants.PARAMS_GENDER, gender);
                jsonObject.put(HeylaAppConstants.PARAMS_OCCUPATION, occupation);
                jsonObject.put(HeylaAppConstants.PARAMS_ADDRESS_LINE_1, addressLineOne);
                jsonObject.put(HeylaAppConstants.PARAMS_ADDRESS_LINE_2, addressLineTwo);
                jsonObject.put(HeylaAppConstants.PARAMS_ADDRESS_LINE_3, landMark);
                jsonObject.put(HeylaAppConstants.PARAMS_COUNTRY_ID, countryId);
                jsonObject.put(HeylaAppConstants.PARAMS_STATE_ID, stateId);
                jsonObject.put(HeylaAppConstants.PARAMS_CITY_ID, cityId);
                jsonObject.put(HeylaAppConstants.PARAMS_ZIP_CODE, pinCode);
                jsonObject.put(HeylaAppConstants.PARAMS_NEWS_LETTER, newsLetterStatus);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
            String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.PROFILE_DATA;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
        } else {
            if (mProgressDialog != null) {
                mProgressDialog.cancel();
            }
        }
    }

    @Override
    public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar newDate = Calendar.getInstance();
        newDate.set(year, monthOfYear, dayOfMonth);
        mBirthday.setText(mDateFormatter.format(newDate.getTime()));
    }

    /**
     * Uploading the file to server
     */
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        private static final String TAG = "UploadFileToServer";
        private HttpClient httpclient;
        HttpPost httppost;
        public boolean isTaskAborted = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            httpclient = new DefaultHttpClient();
            httppost = new HttpPost(String.format(HeylaAppConstants.BASE_URL + HeylaAppConstants.PROFILE_IMAGE + Integer.parseInt(PreferenceStorage.getUserId(ProfileActivity.this))));

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {

                            }
                        });
                Log.d(TAG, "actual file path is" + mActualFilePath);
                if (mActualFilePath != null) {

                    File sourceFile = new File(mActualFilePath);

                    // Adding file data to http body
                    //fileToUpload
                    entity.addPart("user_pic", new FileBody(sourceFile));

                    // Extra parameters if you want to pass to server
                    entity.addPart("user_id", new StringBody(PreferenceStorage.getUserId(ProfileActivity.this)));
//                    entity.addPart("user_type", new StringBody(PreferenceStorage.getUserType(ProfileActivity.this)));

                    totalSize = entity.getContentLength();
                    httppost.setEntity(entity);

                    // Making server call
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity r_entity = response.getEntity();

                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        // Server response
                        responseString = EntityUtils.toString(r_entity);
                        try {
                            JSONObject resp = new JSONObject(responseString);
                            String successVal = resp.getString("status");

                            mUpdatedImageUrl = resp.getString("picture_url");

                            Log.d(TAG, "updated image url is" + mUpdatedImageUrl);
                            if (successVal.equalsIgnoreCase("success")) {
                                Log.d(TAG, "Updated image succesfully");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        responseString = "Error occurred! Http Status Code: "
                                + statusCode;
                    }
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);

            super.onPostExecute(result);
            if ((result == null) || (result.isEmpty()) || (result.contains("Error"))) {
                Toast.makeText(ProfileActivity.this, "Unable to save profile picture", Toast.LENGTH_SHORT).show();
            } else {
                if (mUpdatedImageUrl != null) {
                    PreferenceStorage.saveUserPicture(ProfileActivity.this, mUpdatedImageUrl);
                }
            }
            saveProfileData();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    private void openImagesDocument() {
        Intent pictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pictureIntent.setType("image/*");
        pictureIntent.addCategory(Intent.CATEGORY_OPENABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String[] mimeTypes = new String[]{"image/jpeg", "image/png"};
            pictureIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }
        startActivityForResult(Intent.createChooser(pictureIntent, "Select Picture"), 2);
    }

    private void openImageIntent() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Remove Photo", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
//                    openCamera();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    Uri f = FileProvider.getUriForFile(ProfileActivity.this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            createImageFile());
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, f);
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    openImagesDocument();
                } else if (options[item].equals("Remove Photo")) {
                    PreferenceStorage.saveUserPicture(ProfileActivity.this, "");
                    mProfileImage.setBackground(ContextCompat.getDrawable(ProfileActivity.this, R.drawable.ic_default_profile));
                    mSelectedImageUri = Uri.parse("android.resource://com.palprotech.heylaapp/drawable/ic_default_profile");
                    mActualFilePath = mSelectedImageUri.getPath();
                    saveUserImage();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
//                Uri uri = Uri.parse(mActualFilePath);
//                openCropActivity(uri, uri);
                final File file = new File(mActualFilePath);
                try {
                    InputStream ims = new FileInputStream(file);
                    mProfileImage.setImageBitmap(BitmapFactory.decodeStream(ims));
                } catch (FileNotFoundException e) {
                    return;
                }

                // ScanFile so it will be appeared on Gallery
                MediaScannerConnection.scanFile(ProfileActivity.this,
                        new String[]{mActualFilePath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
//                                performCrop(uri);
                                Uri destinationUri = Uri.fromFile(file);  // 3
                                openCropActivity(uri, destinationUri);
                            }
                        });
            } else if (requestCode == 2) {
                Uri sourceUri = data.getData(); // 1
                File file = null; // 2
                try {
                    file = getImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Uri destinationUri = Uri.fromFile(file);  // 3
                openCropActivity(sourceUri, destinationUri);  // 4
            } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
                if (data != null) {
                    Uri uri = UCrop.getOutput(data);
                    mProfileImage.setImageURI(uri);
//                    mActualFilePath = uri.getPath();
                    saveUserImage();
                }
            }
        }
    }

    private File getImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".png",         /* suffix */
                storageDir      /* directory */
        );

        mActualFilePath = image.getAbsolutePath();
        return image;
    }

    private File createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PNG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".png",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        mActualFilePath = image.getAbsolutePath();
        return image;
    }


    private void openCropActivity(Uri sourceUri, Uri destinationUri) {
        UCrop.Options options = new UCrop.Options();
        options.setCircleDimmedLayer(true);
        options.setCropFrameColor(ContextCompat.getColor(this, R.color.appColorBase));
        UCrop.of(sourceUri, destinationUri)
                .withMaxResultSize(100, 100)
                .withAspectRatio(5f, 5f)
                .start(this);
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

        if (mProgressDialog != null) {
            mProgressDialog.cancel();
        }
        progressDialogHelper.hideProgressDialog();

        try {
            if (validateSignInResponse(response)) {

                if (checkInternalState.equalsIgnoreCase("profile_update")) {
                    if (checkProfileState.equalsIgnoreCase("new")) {
                        PreferenceStorage.saveCheckFirstTimeProfile(getApplicationContext(), "reuse");
                        Intent homeIntent = new Intent(getApplicationContext(), SelectCityActivity.class);
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                        finish();
                    } else if (checkProfileState.equalsIgnoreCase("reuse")) {
                        Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                        Toast.makeText(this,"Profile Updated!!", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                }
                else if (checkInternalState.equalsIgnoreCase("country")) {

                    JSONArray getData = response.getJSONArray("Countries");
                    int getLength = getData.length();
                    String countryId = "";
                    String countryName = "";
                    countryList = new ArrayList<>();

                    for (int i = 0; i < getLength; i++) {
                        countryId = getData.getJSONObject(i).getString("id");
                        countryName = getData.getJSONObject(i).getString("country_name");
                        countryList.add(new StoreCountry(countryId, countryName));
                    }

                    //fill data in spinner
                    mCountryAdapter = new ArrayAdapter<StoreCountry>(getApplicationContext(), R.layout.gender_layout, R.id.gender_name, countryList) { // The third parameter works around ugly Android legacy. http://stackoverflow.com/a/18529511/145173
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            Log.d(TAG, "getview called" + position);
                            View view = getLayoutInflater().inflate(R.layout.gender_layout, parent, false);
                            TextView gendername = (TextView) view.findViewById(R.id.gender_name);
                            gendername.setText(countryList.get(position).getCountryName());

                            // ... Fill in other views ...
                            return view;
                        }
                    };

                }
                else if (checkInternalState.equalsIgnoreCase("state")) {

                    JSONArray getData = response.getJSONArray("States");
                    int getLength = getData.length();
                    String stateId = "";
                    String stateName = "";
                    stateList = new ArrayList<>();

                    for (int i = 0; i < getLength; i++) {

                        stateId = getData.getJSONObject(i).getString("id");
                        stateName = getData.getJSONObject(i).getString("state_name");

                        stateList.add(new StoreState(stateId, stateName));
                    }

                    //fill data in spinner
                    mStateAdapter = new ArrayAdapter<StoreState>(getApplicationContext(), R.layout.gender_layout, R.id.gender_name, stateList) { // The third parameter works around ugly Android legacy. http://stackoverflow.com/a/18529511/145173
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            Log.d(TAG, "getview called" + position);
                            View view = getLayoutInflater().inflate(R.layout.gender_layout, parent, false);
                            TextView gendername = (TextView) view.findViewById(R.id.gender_name);
                            gendername.setText(stateList.get(position).getStateName());

                            // ... Fill in other views ...
                            return view;
                        }
                    };

                } else if (checkInternalState.equalsIgnoreCase("city")) {

                    JSONArray getData = response.getJSONArray("Cities");
                    int getLength = getData.length();
                    String cityId = "";
                    String cityName = "";
                    cityList = new ArrayList<>();

                    for (int i = 0; i < getLength; i++) {

                        cityId = getData.getJSONObject(i).getString("id");
                        cityName = getData.getJSONObject(i).getString("city_name");

                        cityList.add(new StoreCity(cityId, cityName));
                    }

                    //fill data in spinner
                    mCityAdapter = new ArrayAdapter<StoreCity>(getApplicationContext(), R.layout.gender_layout, R.id.gender_name, cityList) { // The third parameter works around ugly Android legacy. http://stackoverflow.com/a/18529511/145173
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            Log.d(TAG, "getview called" + position);
                            View view = getLayoutInflater().inflate(R.layout.gender_layout, parent, false);
                            TextView gendername = (TextView) view.findViewById(R.id.gender_name);
                            gendername.setText(cityList.get(position).getCityName());

                            // ... Fill in other views ...
                            return view;
                        }
                    };
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String error) {
        if (mProgressDialog != null) {
            mProgressDialog.cancel();
        }
        progressDialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(this, "Error saving your profile. Try again");
    }

    private void showBirthdayDate() {

        Log.d(TAG, "Show the birthday date");
        Calendar newCalendar = Calendar.getInstance();
        String currentdate = mBirthday.getText().toString();
        Log.d(TAG, "current date is" + currentdate);
        int month = newCalendar.get(Calendar.MONTH);
        int day = newCalendar.get(Calendar.DAY_OF_MONTH);
        int year = newCalendar.get(Calendar.YEAR);
        if ((currentdate != null) && !(currentdate.isEmpty())) {
            //extract the date/month and year
            try {
                Date startDate = mDateFormatter.parse(currentdate);
                Calendar newDate = Calendar.getInstance();

                newDate.setTime(startDate);
                month = newDate.get(Calendar.MONTH);
                day = newDate.get(Calendar.DAY_OF_MONTH);
                year = newDate.get(Calendar.YEAR);
                Log.d(TAG, "month" + month + "day" + day + "year" + year);

            } catch (ParseException e) {
                e.printStackTrace();
            } finally {
//                mDatePicker = new DatePickerDialog(this, R.style.datePickerTheme, this, year, month, day);
//                mDatePicker.show();
                Calendar now = Calendar.getInstance();
                int year1 = now.get(Calendar.YEAR);
                int month1 = now.get(Calendar.MONTH) ; // Note: zero based!
                int day1 = now.get(Calendar.DAY_OF_MONTH);
                new SpinnerDatePickerDialogBuilder()
                        .context(ProfileActivity.this)
                        .callback(ProfileActivity.this)
                        .spinnerTheme(R.style.NumberPickerStyle)
                        .showTitle(true)
                        .showDaySpinner(true)
                        .defaultDate(2018, 0, 1)
                        .maxDate(year1, month1, day1)
                        .minDate(1900, 0, 1)
                        .build()
                        .show();
            }
        } else {
            Log.d(TAG, "show default date");

//            mDatePicker = new DatePickerDialog(this, R.style.datePickerTheme, this, year, month, day);
//            mDatePicker.show();
            new SpinnerDatePickerDialogBuilder()
                    .context(ProfileActivity.this)
                    .callback(ProfileActivity.this)
                    .spinnerTheme(R.style.NumberPickerStyle)
                    .showTitle(true)
                    .showDaySpinner(true)
                    .defaultDate(2018, 0, 1)
                    .maxDate(2100, 11, 31)
                    .minDate(1900, 0, 1)
                    .build()
                    .show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar newDate = Calendar.getInstance();
        newDate.set(year, monthOfYear, dayOfMonth);
        mBirthday.setText(mDateFormatter.format(newDate.getTime()));
    }

    private void showOccupationList() {
        Log.d(TAG, "Show occupation list");
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.gender_header_layout, null);
        TextView header = (TextView) view.findViewById(R.id.gender_header);
        header.setText("Select Occupation");
        builderSingle.setCustomTitle(view);

        builderSingle.setAdapter(mOccupationAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = mOccupationList.get(which);
                        mOccupation.setText(strName);
                    }
                });
        builderSingle.show();
    }

    private void showGenderList() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);

        builderSingle.setTitle("Select Gender");
        View view = getLayoutInflater().inflate(R.layout.gender_header_layout, null);
        TextView header = (TextView) view.findViewById(R.id.gender_header);
        header.setText("Select Gender");
        builderSingle.setCustomTitle(view);

        builderSingle.setAdapter(mGenderAdapter
                ,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = mGenderList.get(which);
                        mGender.setText(strName);
                    }
                });
        builderSingle.show();
    }

    private void GetCountry() {

        checkInternalState = "country";

        if (CommonUtils.isNetworkAvailable(this)) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(getApplicationContext()));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
            String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.COUNTRY_LIST;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), url);


        } else {
            AlertDialogHelper.showSimpleAlertDialog(this, "No Network connection");
        }
    }

    private void showCountryList() {
        Log.d(TAG, "Show country list");
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);

        View view = getLayoutInflater().inflate(R.layout.gender_header_layout, null);
        TextView header = (TextView) view.findViewById(R.id.gender_header);
        header.setText("Select Country");
        builderSingle.setCustomTitle(view);

        builderSingle.setAdapter(mCountryAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StoreCountry county = countryList.get(which);
                        country.setText(county.getCountryName());
                        countryId = county.getCountryId();
                        state.setText("");
                        stateId = "";
                        city.setText("");
                        cityId = "";
                        callStates(countryId);
                    }
                });
        builderSingle.show();
    }


    private void showStateList() {
        Log.d(TAG, "Show state list");
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.gender_header_layout, null);
        TextView header = (TextView) view.findViewById(R.id.gender_header);
        header.setText("Select State");
        builderSingle.setCustomTitle(view);

        builderSingle.setAdapter(mStateAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StoreState stae = stateList.get(which);
                        state.setText(stae.getStateName());
                        stateId = stae.getStateId();
                        city.setText("");
                        cityId = "";
                        callCity(countryId, stateId);
                    }
                });
        builderSingle.show();
    }

    private void callStates(String CountryId) {
        checkInternalState = "state";

        if (CommonUtils.isNetworkAvailable(this)) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(HeylaAppConstants.PARAMS_COUNTRY_ID, CountryId);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
            String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.STATE_LIST;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), url);


        } else {
            AlertDialogHelper.showSimpleAlertDialog(this, "No Network connection");
        }
    }

    private void showCityList() {
        Log.d(TAG, "Show city list");
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.gender_header_layout, null);
        TextView header = (TextView) view.findViewById(R.id.gender_header);
        header.setText("Select City");
        builderSingle.setCustomTitle(view);

        builderSingle.setAdapter(mCityAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StoreCity cty = cityList.get(which);
                        city.setText(cty.getCityName());
                        cityId = cty.getCityId();
                    }
                });
        builderSingle.show();
    }

    private void callCity(String CountryId, String StateId) {
        checkInternalState = "city";

        if (CommonUtils.isNetworkAvailable(this)) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(HeylaAppConstants.PARAMS_COUNTRY_ID, CountryId);
                jsonObject.put(HeylaAppConstants.PARAMS_STATE_ID, StateId);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
            String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.CITY_LIST;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), url);

        } else {
            AlertDialogHelper.showSimpleAlertDialog(this, "No Network connection");
        }
    }

    private boolean validateFields() {
        if (!HeylaAppValidator.checkNullString(this.name.getText().toString().trim())) {
            inputName.setError(getString(R.string.err_name));
            requestFocus(name);
            return false;
        } else if (!HeylaAppValidator.checkNullString(this.username.getText().toString().trim())) {
            inputUsername.setError(getString(R.string.err_username));
            requestFocus(username);
            return false;
        } else if (!HeylaAppValidator.checkStringMinLength(4, this.username.getText().toString().trim())) {
            inputUsername.setError(getString(R.string.err_username));
            requestFocus(username);
            return false;
        } else {
            return true;
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(ProfileActivity.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    @Override
    public void onAlertPositiveClicked(int tag) {
    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    private void performCrop() {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(mSelectedImageUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast
                    .makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
