package com.palprotech.heylaapp.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.bean.support.Category;
import com.palprotech.heylaapp.bean.support.StoreCity;
import com.palprotech.heylaapp.helper.AlertDialogHelper;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;
import com.palprotech.heylaapp.serviceinterfaces.IServiceListener;
import com.palprotech.heylaapp.utils.CommonUtils;
import com.palprotech.heylaapp.utils.HeylaAppConstants;
import com.palprotech.heylaapp.utils.PreferenceStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Narendar on 16/11/17.
 */

public class AdvanceFilterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, IServiceListener {
    private static final String TAG = AdvanceFilterActivity.class.getName();


    String cityId = "";
    String preferenceId = "";
    Boolean firstTime = false;

    private String checkState = "";
    String singleDate = "";
    boolean todayPressed = false, tomorrowPressed = false, datePressed = false;
    EditText eventType, eventPreferenceList, eventCategory, txtCityDropDown; //spincity

    AlertDialog.Builder builder;
    StringBuilder sb;

    private List<String> eventTypeList = new ArrayList<String>();
    private ArrayAdapter<String> eventTypeAdapter = null;

    private List<String> eventCategoryList = new ArrayList<String>();
    private ArrayAdapter<String> eventCategoryAdapter = null;

    ArrayAdapter<Category> mPreferenceAdapter = null;
    ArrayList<Category> PreferenceList;

    ArrayAdapter<StoreCity> mCityAdapter = null;
    ArrayList<StoreCity> cityList;

    private ProgressDialog mProgressDialog = null;
    private ProgressDialogHelper progressDialogHelper;

    private Activity activity;
    private boolean isdoneclick = false;

    DatePickerDialog mFromDatePickerDialog = null;
    private ServiceHelper serviceHelper;
    HashSet<Integer> mSelectedCategoryList = new HashSet<Integer>();
    private String mFromDateVal = null;
    private String mTodateVal = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);
//        fetchCategoryValues();
        iniView();
    }

    private void iniView() {

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
//        getSupportActionBar().hide();
        eventType = findViewById(R.id.eventTypeList);
        eventType.setOnClickListener(this);
        eventType.setFocusable(false);
        eventCategory = findViewById(R.id.eventCategoryList);
        eventCategory.setOnClickListener(this);
        eventCategory.setFocusable(false);
        eventPreferenceList = findViewById(R.id.eventPreferenceList);
        eventPreferenceList.setOnClickListener(this);
        eventPreferenceList.setFocusable(false);
        txtCityDropDown = findViewById(R.id.selectCityList);
        txtCityDropDown.setOnClickListener(this);
        txtCityDropDown.setFocusable(false);

        findViewById(R.id.btnselectdate).setOnClickListener(this);
        findViewById(R.id.btntomorrow).setOnClickListener(this);
        findViewById(R.id.btntoday).setOnClickListener(this);

        GetPreferences();

        findViewById(R.id.back_tic_his).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        DatePickerSelection();
        findViewById(R.id.btnapply).setOnClickListener(this);
        findViewById(R.id.btncancel).setOnClickListener(this);


        eventTypeList.add("Free");
        eventTypeList.add("Paid");

        eventTypeAdapter = new ArrayAdapter<String>(this, R.layout.gender_layout, R.id.gender_name, eventTypeList) { // The third parameter works around ugly Android legacy. http://stackoverflow.com/a/18529511/145173
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Log.d(TAG, "getview called" + position);
                View view = getLayoutInflater().inflate(R.layout.gender_layout, parent, false);
                TextView gendername = (TextView) view.findViewById(R.id.gender_name);
                gendername.setText(eventTypeList.get(position));

                // ... Fill in other views ...
                return view;
            }
        };

        String occupation = PreferenceStorage.getUserOccupation(this);
        if (occupation != null) {
            eventType.setText(occupation);
        }

        eventCategoryList.add("General");
        eventCategoryList.add("Hotspot");

        eventCategoryAdapter = new ArrayAdapter<String>(this, R.layout.gender_layout, R.id.gender_name, eventCategoryList) { // The third parameter works around ugly Android legacy. http://stackoverflow.com/a/18529511/145173
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Log.d(TAG, "getview called" + position);
                View view = getLayoutInflater().inflate(R.layout.gender_layout, parent, false);
                TextView gendername = (TextView) view.findViewById(R.id.gender_name);
                gendername.setText(eventCategoryList.get(position));

                // ... Fill in other views ...
                return view;
            }
        };

        String category = PreferenceStorage.getUserOccupation(this);
        if (category != null) {
            eventCategory.setText(occupation);
        }

    }

    private void showeventTypeList() {
        Log.d(TAG, "Show occupation list");
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.gender_header_layout, null);
        TextView header = (TextView) view.findViewById(R.id.gender_header);
        header.setText("Select Occupation");
        builderSingle.setCustomTitle(view);

        builderSingle.setAdapter(eventTypeAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = eventTypeList.get(which);
                        eventType.setText(strName);
                    }
                });
        builderSingle.show();
    }

    private void showeventCategoryList() {
        Log.d(TAG, "Show occupation list");
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.gender_header_layout, null);
        TextView header = (TextView) view.findViewById(R.id.gender_header);
        header.setText("Select Occupation");
        builderSingle.setCustomTitle(view);

        builderSingle.setAdapter(eventCategoryAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = eventCategoryList.get(which);
                        eventCategory.setText(strName);
                    }
                });
        builderSingle.show();
    }

    private void DatePickerSelection() {
        final Calendar c = Calendar.getInstance();
        final int currentYear = c.get(Calendar.YEAR);
        final int currentMonth = c.get(Calendar.MONTH);
        final int currentDay = c.get(Calendar.DAY_OF_MONTH);

       /* String singleDateVal = PreferenceStorage.getFilterSingleDate(this);
        if( (singleDateVal != null) && !(singleDateVal.isEmpty())){
            ((Button)findViewById(R.id.btnselectdate)).setText(singleDateVal);
        }*/

        final DatePickerDialog.OnDateSetListener fromdate = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int month, int day) {
                Log.d(TAG, "From selected");
                // isdoneclick = true;
                if (isdoneclick) {
                    ((Button) findViewById(R.id.btnfrom)).setText(formatDate(year, month, day));
                    mFromDateVal = formatDateServer(year, month, day);
                } else {
                    Log.e("Clear", "Clear");
                    ((Button) findViewById(R.id.btnfrom)).setText("");
                    mFromDateVal = "";
                }
            }

        };

        findViewById(R.id.btnfrom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findViewById(R.id.btnselectdate).setBackgroundResource(R.drawable.bg_advanced_filter_properties);
                findViewById(R.id.btntomorrow).setBackgroundResource(R.drawable.bg_advanced_filter_properties);
                findViewById(R.id.btntoday).setBackgroundResource(R.drawable.bg_advanced_filter_properties);
                ((Button) findViewById(R.id.btnselectdate)).setText("DD-MM-YYYY");

                singleDate = "";
                todayPressed = false;
                datePressed = false;
                tomorrowPressed = false;
                mFromDatePickerDialog = new DatePickerDialog(AdvanceFilterActivity.this, R.style.datePickerTheme, fromdate, currentYear,
                        currentMonth, currentDay);

                mFromDatePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isdoneclick = true;
                        Log.d(TAG, "Done clicked");
                        DatePicker datePicker = mFromDatePickerDialog.getDatePicker();
                        fromdate.onDateSet(datePicker, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                        mFromDatePickerDialog.dismiss();
                    }
                });
                mFromDatePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isdoneclick = false;
                        ((Button) findViewById(R.id.btnfrom)).setText("");
                        mFromDatePickerDialog.dismiss();
                    }
                });
                mFromDatePickerDialog.show();
            }
        });
        final DatePickerDialog.OnDateSetListener todate = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int month, int day) {
                // isdoneclick = true;

                if (isdoneclick) {
                    ((Button) findViewById(R.id.btnto)).setText(formatDate(year, month, day));
                    mTodateVal = formatDateServer(year, month, day);

                } else {
                    // Log.e("Clear", "Clear");
                    ((Button) findViewById(R.id.btnto)).setText("");
                    mTodateVal = "";
                }

            }

        };
        findViewById(R.id.btnto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.btnselectdate).setBackgroundResource(R.drawable.bg_advanced_filter_properties);
                findViewById(R.id.btntomorrow).setBackgroundResource(R.drawable.bg_advanced_filter_properties);
                findViewById(R.id.btntoday).setBackgroundResource(R.drawable.bg_advanced_filter_properties);
                ((Button) findViewById(R.id.btnselectdate)).setText("DD-MM-YYYY");
                singleDate = "";
                todayPressed = false;
                datePressed = false;
                tomorrowPressed = false;
                final DatePickerDialog dpd = new DatePickerDialog(AdvanceFilterActivity.this, R.style.datePickerTheme, todate, currentYear,
                        currentMonth, currentDay);
                dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isdoneclick = true;
                        DatePicker datePicker = dpd.getDatePicker();
                        todate.onDateSet(datePicker, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                        dpd.dismiss();
                    }
                });
                dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isdoneclick = false;
                        ((Button) findViewById(R.id.btnto)).setText("");
                        dpd.dismiss();
                    }
                });
                dpd.show();
            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        PreferenceStorage.saveFilterCitySelection(this, position);
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private static String formatDateServer(int year, int month, int day) {

        String formattedDay = "", formattedMonth = "";
        month = month + 1;
        if (day < 10) {
            formattedDay = "0" + day;
        } else {
            formattedDay = "" + day;
        }

        if (month < 10) {
            formattedMonth = "0" + month;
        } else {
            formattedMonth = "" + month;
        }

        return year + "-" + formattedMonth + "-" + formattedDay;
    }

    private static String formatDate(int year, int month, int day) {

        String formattedDay = "", formattedMonth = "";
        month = month + 1;
        if (day < 10) {
            formattedDay = "0" + day;
        } else {
            formattedDay = "" + day;
        }

        if (month < 10) {
            formattedMonth = "0" + month;
        } else {
            formattedMonth = "" + month;
        }

        return formattedDay + "-" + formattedMonth + "-" + year;
    }

    @Override
    public void onClick(View v) {

        if (v == eventType) {
            showeventTypeList();
            checkState = "type";

        }

        if (v == eventCategory) {
            showeventCategoryList();
            checkState = "category";

        }

        if (v == eventPreferenceList) {
            showPreferenceList();
            checkState = "preference";

        }

        if (v == txtCityDropDown) {
            GetEventCities();
            showCityList();
            checkState = "city";

        }

        switch (v.getId()) {
            case R.id.btnselectdate:
                ((Button) findViewById(R.id.btnfrom)).setText("");
                ((Button) findViewById(R.id.btnto)).setText("");
                if (!datePressed) {
                    findViewById(R.id.btnselectdate).setBackgroundResource(R.drawable.bg_advance_filter_orange);
                    findViewById(R.id.btntomorrow).setBackgroundResource(R.drawable.bg_advanced_filter_properties);
                    findViewById(R.id.btntoday).setBackgroundResource(R.drawable.bg_advanced_filter_properties);
                    final DatePickerDialog.OnDateSetListener singledate = new DatePickerDialog.OnDateSetListener() {

                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            //Log.e("Singledate", "singleDate : " + singleDate);
                            if (isdoneclick) {
                                ((Button) findViewById(R.id.btnselectdate)).setText(formatDate(year, month, day));
                                singleDate = formatDateServer(year, month, day);
                            } else {
                                Log.e("Clear", "Clear");
                                ((Button) findViewById(R.id.btnselectdate)).setText("DD-MM-YYYY");
                            }

                        }

                    };
                    final Calendar c2 = Calendar.getInstance();
                    final int currentYear2 = c2.get(Calendar.YEAR);
                    final int currentMonth2 = c2.get(Calendar.MONTH);
                    final int currentDay2 = (c2.get(Calendar.DAY_OF_MONTH));
                    final DatePickerDialog dpd = new DatePickerDialog(AdvanceFilterActivity.this, R.style.datePickerTheme, singledate, currentYear2,
                            currentMonth2, currentDay2);
                    dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isdoneclick = true;
                            DatePicker datePicker = dpd.getDatePicker();
                            singledate.onDateSet(datePicker, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                            dpd.dismiss();
                        }
                    });
                    dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Clear", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isdoneclick = false;
                            dpd.dismiss();
                        }
                    });
                    dpd.show();

                    datePressed = true;

                } else {
                    findViewById(R.id.btnselectdate).setBackgroundResource(R.drawable.bg_advanced_filter_properties);
                    findViewById(R.id.btntomorrow).setBackgroundResource(R.drawable.bg_advanced_filter_properties);
                    findViewById(R.id.btntoday).setBackgroundResource(R.drawable.bg_advanced_filter_properties);
                    singleDate = "";
                    datePressed = false;
                    todayPressed = false;
                    tomorrowPressed = false;
                }

                break;
            case R.id.btntomorrow:
                ((Button) findViewById(R.id.btnfrom)).setText("");
                ((Button) findViewById(R.id.btnto)).setText("");
                if (!tomorrowPressed) {

                    findViewById(R.id.btnselectdate).setBackgroundResource(R.drawable.bg_advanced_filter_properties);
                    findViewById(R.id.btntomorrow).setBackgroundResource(R.drawable.bg_advance_filter_orange);
                    findViewById(R.id.btntoday).setBackgroundResource(R.drawable.bg_advanced_filter_properties);
                    // singleDate=((Button)findViewById(R.id.btntomorrow)).getText().toString();

                    final Calendar c = Calendar.getInstance();
                    final int currentYear = c.get(Calendar.YEAR);
                    final int currentMonth = c.get(Calendar.MONTH);
                    final int currentDay = (c.get(Calendar.DAY_OF_MONTH));
                    singleDate = formatDateServer(currentYear, currentMonth, currentDay);

                    //  ((Button) findViewById(R.id.btnselectdate)).setText(singleDate);
                    Log.e("Singledate", "singleDate : " + singleDate);
                    tomorrowPressed = true;
                } else {

                    findViewById(R.id.btnselectdate).setBackgroundColor(getResources().getColor(R.color.appColorBase));
                    findViewById(R.id.btntomorrow).setBackgroundColor(getResources().getColor(R.color.appColorBase));
                    findViewById(R.id.btntoday).setBackgroundColor(getResources().getColor(R.color.appColorBase));
                    singleDate = "";
                    tomorrowPressed = false;
                    datePressed = false;
                    todayPressed = false;
                }
                ((Button) findViewById(R.id.btnselectdate)).setText("DD-MM-YYYY");
                break;
            case R.id.btntoday:
                ((Button) findViewById(R.id.btnfrom)).setText("");
                ((Button) findViewById(R.id.btnto)).setText("");
                ((Button) findViewById(R.id.btnselectdate)).setText("DD-MM-YYYY");
                if (!todayPressed) {
                    findViewById(R.id.btnselectdate).setBackgroundResource(R.drawable.bg_advanced_filter_properties);
                    findViewById(R.id.btntomorrow).setBackgroundResource(R.drawable.bg_advanced_filter_properties);
                    findViewById(R.id.btntoday).setBackgroundResource(R.drawable.bg_advance_filter_orange);
                    final Calendar c1 = Calendar.getInstance();
                    final int currentYear1 = c1.get(Calendar.YEAR);
                    final int currentMonth1 = c1.get(Calendar.MONTH);
                    final int currentDay1 = (c1.get(Calendar.DAY_OF_MONTH));
                    singleDate = formatDateServer(currentYear1, currentMonth1, currentDay1);
                    // ((Button) findViewById(R.id.btnselectdate)).setText(singleDate);

                    Log.e("Singledate", "singleDate : " + singleDate);
                    todayPressed = true;
                } else {
                    findViewById(R.id.btnselectdate).setBackgroundResource(R.drawable.bg_advanced_filter_properties);
                    findViewById(R.id.btntomorrow).setBackgroundResource(R.drawable.bg_advanced_filter_properties);
                    findViewById(R.id.btntoday).setBackgroundResource(R.drawable.bg_advanced_filter_properties);
                    singleDate = "";
                    todayPressed = false;
                    datePressed = false;
                    tomorrowPressed = false;
                }
                break;
            case R.id.btnapply:
                findViewById(R.id.btnapply).setBackgroundResource(R.drawable.bg_advanced_filter_properties);
                String eventTypeStr = eventType.getText().toString();
                String eventPreferenceStr = eventPreferenceList.getText().toString();
                String city = txtCityDropDown.getText().toString();
//                String city = spincity.getSelectedItem().toString();
                String eventTypeCategoryStr = eventCategory.getText().toString();
                String fromdate = ((Button) findViewById(R.id.btnfrom)).getText().toString();
                String todate = ((Button) findViewById(R.id.btnto)).getText().toString();
                if (!singleDate.equalsIgnoreCase("") && singleDate != null) {
                    PreferenceStorage.IsFilterApply(this, true);
                    PreferenceStorage.saveFilterSingleDate(this, singleDate);
                    //  Toast.makeText(this, "Filter applied", Toast.LENGTH_SHORT).show();
                    PreferenceStorage.saveFilterFromDate(this, "");
                    PreferenceStorage.saveFilterToDate(this, "");
                    if (!city.equalsIgnoreCase("Select Your City")) {
                        PreferenceStorage.saveFilterCity(this, city);
                    }
                    //if (!eventType.equalsIgnoreCase("Select Your Event Type")) {
                    PreferenceStorage.saveFilterEventType(this, eventTypeStr);
                    PreferenceStorage.saveFilterEventTypeCategory(this, eventPreferenceStr);

                    //}
                    if (!eventTypeCategoryStr.equalsIgnoreCase("Select Category")) {
                        PreferenceStorage.saveFilterCatgry(this, eventTypeCategoryStr);
                    }

                    startActivity(new Intent(AdvanceFilterActivity.this, AdvancedFilterResultActivity.class));
                    //finish();
                } else if (fromdate.trim().length() > 0 || todate.trim().length() > 0) {
                    singleDate = "";
                    PreferenceStorage.saveFilterSingleDate(this, singleDate);
                    PreferenceStorage.IsFilterApply(this, true);

                    if (fromdate.equalsIgnoreCase("")) {
                        Toast.makeText(this, "Select From Date", Toast.LENGTH_SHORT).show();
                    } else if (todate.equalsIgnoreCase("")) {
                        Toast.makeText(this, "Select To Date", Toast.LENGTH_SHORT).show();
                    } else {

                        PreferenceStorage.saveFilterFromDate(this, mFromDateVal);
                        PreferenceStorage.saveFilterToDate(this, mTodateVal);
                        if (!city.equalsIgnoreCase("Select Your City")) {
                            PreferenceStorage.saveFilterCity(this, city);
                        }
                        PreferenceStorage.saveFilterEventType(this, eventTypeStr);
                        PreferenceStorage.saveFilterEventTypeCategory(this, eventPreferenceStr);

                        if (!eventTypeCategoryStr.equalsIgnoreCase("Select Category")) {
                            PreferenceStorage.saveFilterCatgry(this, eventTypeCategoryStr);
                        }
                        startActivity(new Intent(AdvanceFilterActivity.this, AdvancedFilterResultActivity.class));
                        //finish();
                    }

                } else if (!city.equalsIgnoreCase("Select Your City") || !eventTypeCategoryStr.equalsIgnoreCase("Select Category")) {
                    PreferenceStorage.IsFilterApply(this, true);
                    singleDate = "";
                    PreferenceStorage.saveFilterSingleDate(this, singleDate);
                    if (!city.equalsIgnoreCase("Select Your City")) {
                        PreferenceStorage.saveFilterCity(this, city);
                    }

                    PreferenceStorage.saveFilterEventType(this, eventTypeStr);
                    PreferenceStorage.saveFilterEventTypeCategory(this, eventPreferenceStr);

                    if (!eventTypeCategoryStr.equalsIgnoreCase("Select Category")) {
                        PreferenceStorage.saveFilterCatgry(this, eventTypeCategoryStr);
                    }
                    startActivity(new Intent(AdvanceFilterActivity.this, AdvancedFilterResultActivity.class));
                    //finish();
                } else {
                    Toast.makeText(AdvanceFilterActivity.this, "select any criteria", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void GetPreferences() {
        if (CommonUtils.isNetworkAvailable(this)) {
            checkState = "preference";
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(getApplicationContext()));
                jsonObject.put(HeylaAppConstants.KEY_USER_TYPE, PreferenceStorage.getUserType(getApplicationContext()));

            } catch (JSONException e) {
                e.printStackTrace();
            }


            String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.USER_PREFERENCES_LIST;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), url);


        } else {
            AlertDialogHelper.showSimpleAlertDialog(this, "No Network connection");
        }
    }

    private void GetEventCities() {

        checkState = "city";

        if (CommonUtils.isNetworkAvailable(this)) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(getApplicationContext()));

            } catch (JSONException e) {
                e.printStackTrace();
            }


            String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.EVENT_CITY_LIST;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), url);


        } else {
            AlertDialogHelper.showSimpleAlertDialog(this, "No Network connection");
        }
    }

    private void showPreferenceList() {

        Log.d(TAG, "Show city list");
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.gender_header_layout, null);
        TextView header = (TextView) view.findViewById(R.id.gender_header);
        header.setText("Select City");
        builderSingle.setCustomTitle(view);

        builderSingle.setAdapter(mPreferenceAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Category category = PreferenceList.get(which);
                        eventPreferenceList.setText(category.getCategory());
                        preferenceId = category.getId();
                    }
                });
        builderSingle.show();
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
                        txtCityDropDown.setText(cty.getCityName());
                        cityId = cty.getCityId();
                    }
                });
        builderSingle.show();
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


        if (validateSignInResponse(response)) {
            try {
                if (checkState.equalsIgnoreCase("preference")) {
                    JSONArray getData = response.getJSONArray("Categories");
                    JSONObject getPreference = getData.getJSONObject(0);
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<Category>>() {
                    }.getType();
                    PreferenceList = gson.fromJson(getData.toString(), listType);
                    mPreferenceAdapter = new ArrayAdapter<Category>(getApplicationContext(), R.layout.gender_layout, R.id.gender_name, PreferenceList) { // The third parameter works around ugly Android legacy. http://stackoverflow.com/a/18529511/145173
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            Log.d(TAG, "getview called" + position);
                            View view = getLayoutInflater().inflate(R.layout.gender_layout, parent, false);
                            TextView gendername = (TextView) view.findViewById(R.id.gender_name);
                            gendername.setText(PreferenceList.get(position).getCategory());
                            return view;
                        }
                    };
                    if (!firstTime) {
                        GetEventCities();
                    }
                } else if (checkState.equalsIgnoreCase("city")) {
                    firstTime = true;
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "Error while sign In");
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
}

