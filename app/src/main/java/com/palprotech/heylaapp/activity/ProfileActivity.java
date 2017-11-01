package com.palprotech.heylaapp.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.interfaces.DialogClickListener;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;
import com.palprotech.heylaapp.serviceinterfaces.IServiceListener;
import com.palprotech.heylaapp.utils.AndroidMultiPartEntity;
import com.palprotech.heylaapp.utils.CommonUtils;
import com.palprotech.heylaapp.utils.HeylaAppConstants;
import com.palprotech.heylaapp.utils.HeylaAppValidator;
import com.palprotech.heylaapp.utils.PreferenceStorage;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
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

public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener, View.OnClickListener, IServiceListener, DialogClickListener {

    private static final String TAG = ProfileActivity.class.getName();

    private List<String> mOccupationList = new ArrayList<String>();
    private ArrayAdapter<String> mOccupationAdapter = null;

    private ArrayAdapter<String> mGenderAdapter = null;
    private List<String> mGenderList = new ArrayList<String>();

    private TextInputLayout inputGender, inputAddress1, inputAddress2, inputAddress3, inputPincode, inputName,
            inputUsername, inputBirthday, inputOccupation, inputCountry, inputState, inputCity;
    EditText mBirthday, mGender, mOccupation, address1, address2, address3, pincode, name,
            username, country, state, city;
    private CheckBox cbSubscription;
    Button save;

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

        inputName = (TextInputLayout) findViewById(R.id.ti_name);
        name = (EditText) findViewById(R.id.edtName);
        inputUsername = (TextInputLayout) findViewById(R.id.ti_username);
        username = (EditText) findViewById(R.id.edtUsername);
        inputBirthday = (TextInputLayout) findViewById(R.id.ti_birthday);
        mBirthday = (EditText) findViewById(R.id.edtBirthday);
        mBirthday.setFocusable(false);
        inputOccupation = (TextInputLayout) findViewById(R.id.ti_occupation);
        mOccupation = (EditText) findViewById(R.id.occupationlist);
        mOccupation.setFocusable(false);
        inputGender = (TextInputLayout) findViewById(R.id.ti_gender);
        mGender = (EditText) findViewById(R.id.genderList);
        mGender.setFocusable(false);
        inputAddress1 = (TextInputLayout) findViewById(R.id.ti_address_line_one);
        address1 = (EditText) findViewById(R.id.edtAddressLineOne);
        inputAddress2 = (TextInputLayout) findViewById(R.id.ti_address_line_two);
        address2 = (EditText) findViewById(R.id.edtAddressLinetwo);
        inputAddress3 = (TextInputLayout) findViewById(R.id.ti_address_line_three);
        address3 = (EditText) findViewById(R.id.edtAddressLinethree);
        inputCountry = (TextInputLayout) findViewById(R.id.ti_country);
        country = (EditText) findViewById(R.id.countryList);
        country.setFocusable(false);
        inputState = (TextInputLayout) findViewById(R.id.ti_state);
        state = (EditText) findViewById(R.id.stateList);
        state.setFocusable(false);
        inputCity = (TextInputLayout) findViewById(R.id.ti_city);
        city = (EditText) findViewById(R.id.cityList);
        city.setFocusable(false);
        inputPincode = (TextInputLayout) findViewById(R.id.ti_pincode);
        pincode = (EditText) findViewById(R.id.edtPincode);
        cbSubscription = (CheckBox) findViewById(R.id.subscription);
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

        fullName = name.getText().toString();
        userName = username.getText().toString();
        birthDay = mBirthday.getText().toString();
        occupation = mOccupation.getText().toString();
        gender = mGender.getText().toString();
        addressLineOne = address1.getText().toString();
        addressLineTwo = address2.getText().toString();
        landMark = address3.getText().toString();
        countryName = country.getText().toString();
        stateName = state.getText().toString();
        cityName = city.getText().toString();
        pinCode = pincode.getText().toString();
        newsLetter = cbSubscription.isChecked();
        if (newsLetter) {
            newsLetterStatus = "Y";
        }

        String url = newsLetterStatus;
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

                            mUpdatedImageUrl = resp.getString("user_picture");

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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
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
                mDatePicker = new DatePickerDialog(this, R.style.datePickerTheme, this, year, month, day);
                mDatePicker.show();
            }
        } else {
            Log.d(TAG, "show default date");

            mDatePicker = new DatePickerDialog(this, R.style.datePickerTheme, this, year, month, day);
            mDatePicker.show();
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
        } else {
            return true;
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {

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

    @Override
    public void onClick(View view) {
        if (view == save) {
            if (validateFields()) {
//                saveProfile();
                Intent homeIntent = new Intent(this.getApplicationContext(), MainActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
//                finish();
            }
        }
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    @Override
    public void onResponse(JSONObject response) {

    }

    @Override
    public void onError(String error) {

    }
}
