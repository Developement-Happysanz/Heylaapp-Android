package com.palprotech.heylaapp.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.utils.CommonUtils;
import com.palprotech.heylaapp.utils.HeylaAppValidator;
import com.palprotech.heylaapp.utils.PreferenceStorage;

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

public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener, View.OnClickListener {
    private static final String TAG = ProfileActivity.class.getName();

    private List<String> mOccupationList = new ArrayList<String>();
    private ArrayAdapter<String> mOccupationAdapter = null;

    private ArrayAdapter<String> mGenderAdapter = null;
    private List<String> mGenderList = new ArrayList<String>();

    private TextInputLayout inputGender, inputAddress1, inputAddress2, inputAddress3, inputPincode, inputName,
            inputUsername, inputBirthday, inputOccupation;
    EditText mBirthday, mGender, mOccupation, address1, address2, address3, pincode, name,
            username, birthday, occupation;

    Button save;

    private DatePickerDialog mDatePicker;
    private SimpleDateFormat mDateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setUI();

    }

    void setUI() {
        save = (Button) findViewById(R.id.saveprofile);
        mGender = (EditText) findViewById(R.id.genderList);
        mBirthday = (EditText) findViewById(R.id.edtBirthday);
        inputGender = (TextInputLayout) findViewById(R.id.ti_gender);
        inputAddress1 = (TextInputLayout) findViewById(R.id.ti_address_line_one);
        inputAddress2 = (TextInputLayout) findViewById(R.id.ti_address_line_two);
        inputAddress3 = (TextInputLayout) findViewById(R.id.ti_address_line_three);
        inputPincode = (TextInputLayout) findViewById(R.id.ti_pincode);
        inputName = (TextInputLayout) findViewById(R.id.ti_name);
        inputUsername = (TextInputLayout) findViewById(R.id.ti_username);
        inputBirthday = (TextInputLayout) findViewById(R.id.ti_birthday);
        inputOccupation = (TextInputLayout) findViewById(R.id.ti_occupation);
        address1 = (EditText) findViewById(R.id.edtAddressLineOne);
        address2 = (EditText) findViewById(R.id.edtAddressLinetwo);
        address3 = (EditText) findViewById(R.id.edtAddressLinethree);
        pincode = (EditText) findViewById(R.id.edtPincode);
        name = (EditText) findViewById(R.id.edtName);
        username = (EditText) findViewById(R.id.edtUsername);
        occupation = (EditText) findViewById(R.id.occupationlist);

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



        String sexVal = CommonUtils.getGenderVal(PreferenceStorage.getUserGender(this));
        if (sexVal != null) {
            mGender.setText(sexVal);
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
        }
        else {
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
        if(view == save){

        }
    }
}
