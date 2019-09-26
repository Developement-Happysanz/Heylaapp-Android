package com.palprotech.heylaapp.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.bean.support.Category;
import com.palprotech.heylaapp.bean.support.StoreCity;
import com.palprotech.heylaapp.helper.AlertDialogHelper;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.interfaces.DialogClickListener;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;
import com.palprotech.heylaapp.serviceinterfaces.IServiceListener;
import com.palprotech.heylaapp.utils.CommonUtils;
import com.palprotech.heylaapp.utils.HeylaAppConstants;
import com.palprotech.heylaapp.utils.PreferenceStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

/**
 * Created by Narendar on 16/11/17.
 */

public class AdvanceFilterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, IServiceListener, DialogClickListener, SeekBar.OnSeekBarChangeListener {

    private static final String TAG = AdvanceFilterActivity.class.getName();

    String cityId = "";
    String preferenceId = "";
    Boolean firstTime = false;

    private String checkState = "";
    String singleDate = "";
    boolean todayPressed = false, tomorrowPressed = false, datePressed = false;

    AlertDialog.Builder builder;
    StringBuilder sb = null, sb1 = null;
    private AlertDialog ddialog = null;
    private List<String> eventTypeList = new ArrayList<String>();
    private ArrayAdapter<String> eventTypeAdapter = null;

    private List<String> eventCategoryList = new ArrayList<String>();
    private ArrayAdapter<String> eventCategoryAdapter = null;

    ArrayAdapter<String> mPreferenceAdapter = null;
    private ArrayList<String> PreferenceList = new ArrayList<String>();
    private ArrayList<String> PreferenceIdList = new ArrayList<String>();

    ArrayAdapter<StoreCity> mCityAdapter = null;
    ArrayList<StoreCity> cityList;

    private ProgressDialog mProgressDialog = null;
    private ProgressDialogHelper progressDialogHelper;

    private Activity activity;
    private boolean isdoneclick = false;

    DatePickerDialog mFromDatePickerDialog = null;
    private ServiceHelper serviceHelper;
    HashSet<Integer> mSelectedPreferenceList = new HashSet<Integer>();
    private String mFromDateVal = null;
    private String mTodateVal = null;

    private ImageView ivBack;
    private Button btnToday, btnTomorrow, btnSelectedDate;
    private EditText etEventTypeList, etEventCategoryList, etPreferenceList, etCityList;
    private SeekBar etPriceList;
    private Button btnFromDate, btnToDate;
    private Button btnCancel, btnSubmit;

    private TextView endRangeText;
    double startRange, mSelectedPriceRange;
    String range = "";
    private boolean lalala = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);
        iniView();
    }

    private void iniView() {

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);

        ivBack = findViewById(R.id.back_res);
        ivBack.setOnClickListener(this);

        btnToday = findViewById(R.id.btntoday);
        btnToday.setOnClickListener(this);

        btnTomorrow = findViewById(R.id.btntomorrow);
        btnTomorrow.setOnClickListener(this);

        btnSelectedDate = findViewById(R.id.btnselectdate);
        btnSelectedDate.setOnClickListener(this);

        etEventTypeList = findViewById(R.id.eventTypeList);
        etEventTypeList.setOnClickListener(this);
        etEventTypeList.setFocusable(false);

        etEventCategoryList = findViewById(R.id.eventCategoryList);
        etEventCategoryList.setOnClickListener(this);
        etEventCategoryList.setFocusable(false);

        etPreferenceList = findViewById(R.id.eventPreferenceList);
        etPreferenceList.setOnClickListener(this);
        etPreferenceList.setFocusable(false);

        etCityList = findViewById(R.id.selectCityList);
        etCityList.setOnClickListener(this);
        etCityList.setFocusable(false);

        etPriceList = findViewById(R.id.price_range);
        etPriceList.setOnSeekBarChangeListener(this);
        endRangeText = findViewById(R.id.end_range_text);

        btnFromDate = findViewById(R.id.btnfrom);
        btnFromDate.setOnClickListener(this);

        btnToDate = findViewById(R.id.btnto);
        btnToDate.setOnClickListener(this);

        btnCancel = findViewById(R.id.btncancel);
        btnCancel.setOnClickListener(this);

        btnSubmit = findViewById(R.id.btnapply);
        btnSubmit.setOnClickListener(this);

        GetPreferences();

        DatePickerSelection();

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


        eventCategoryList.add("Popular");
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

        mPreferenceAdapter = new ArrayAdapter<String>(this, R.layout.category_list_item, R.id.category_list_name, PreferenceList) { // The third parameter works around ugly Android legacy. http://stackoverflow.com/a/18529511/145173
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Log.d(TAG, "getview called" + position);
                View view = getLayoutInflater().inflate(R.layout.category_list_item, parent, false);
                TextView name = (TextView) view.findViewById(R.id.category_list_name);
                String prefid = "";
                name.setText(PreferenceList.get(position));
                prefid = (PreferenceList.get(position));
                CheckBox checkbox = (CheckBox) view.findViewById(R.id.item_selection);
                checkbox.setTag(Integer.toString(position));
                if (mSelectedPreferenceList.contains(position)) {
                    checkbox.setChecked(true);
                } else {
                    checkbox.setChecked(false);
                }
                checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        String tag = (String) buttonView.getTag();
                        if (tag != null) {
                            int index = Integer.parseInt(tag);
                            if (mSelectedPreferenceList.contains(index)) {
                                mSelectedPreferenceList.remove(index);
                            } else {
                                mSelectedPreferenceList.add(index);
                            }
                        }
                    }
                });

                // ... Fill in other views ...
                return view;
            }
        };
    }

    @Override
    public void onClick(View v) {

        if (v == ivBack) {
            PreferenceStorage.saveFilterSingleDate(this, "");
            PreferenceStorage.saveFilterCity(this, "");
            PreferenceStorage.saveFilterRange(this, "");
            PreferenceStorage.saveFilterEventType(this, "");
            PreferenceStorage.saveFilterEventCategory(this, "");
            PreferenceStorage.saveFilterPreference(this, "");
            PreferenceStorage.saveFilterToDate(this, "");
            PreferenceStorage.saveFilterFromDate(this, "");
            finish();
        }
        if (v == etEventTypeList) {
//            checkState = "eventType";
            showEventTypeList();
        }
        if (v == etEventCategoryList) {
//            checkState = "category";
            showEventCategoryList();
        }
        if (v == etPreferenceList) {
//            checkState = "preference";
            showPreferenceList();
        }
        if (v == etCityList) {
//            checkState = "city";
            showCityList();
        }
        if (v == etPriceList) {
//            checkState = "city";
//            showPriceList();
        }

        if (v == btnCancel) {
            etEventTypeList.setText("");

            etEventCategoryList.setText("");

            etPreferenceList.setText("");
            mSelectedPreferenceList.clear();

            etPriceList.setProgress(0);
            mSelectedPriceRange = 0.0;

            etCityList.setText("");

            btnFromDate.setText("DD-MM-YYYY");

            btnToDate.setText("DD-MM-YYYY");
            sb = new StringBuilder();
            sb1 = new StringBuilder();
            sb.append("");
            sb1.append(" ");
            PreferenceStorage.saveFilterSingleDate(this, "");
            PreferenceStorage.saveFilterCity(this, "");
            PreferenceStorage.saveFilterRange(this, "");
            PreferenceStorage.saveFilterEventType(this, "");
            PreferenceStorage.saveFilterEventCategory(this, "");
            PreferenceStorage.saveFilterPreference(this, "");
            PreferenceStorage.saveFilterToDate(this, "");
            PreferenceStorage.saveFilterFromDate(this, "");

        }

        switch (v.getId()) {
            case R.id.btnselectdate:

                ((Button) findViewById(R.id.btnfrom)).setText("DD-MM-YYYY");
                ((Button) findViewById(R.id.btnto)).setText("DD-MM-YYYY");
                if (!datePressed) {
                    findViewById(R.id.btnselectdate).setBackgroundResource(R.drawable.bg_advance_filter_orange);
                    findViewById(R.id.btntomorrow).setBackgroundResource(R.drawable.bg_advanced_filter_properties);
                    findViewById(R.id.btntoday).setBackgroundResource(R.drawable.bg_advanced_filter_properties);
                    ((Button) findViewById(R.id.btntoday)).setTextColor(getResources().getColor(R.color.appColorBase));
                    ((Button) findViewById(R.id.btntomorrow)).setTextColor(getResources().getColor(R.color.appColorBase));
                    ((Button) findViewById(R.id.btnselectdate)).setTextColor(getResources().getColor(R.color.white));
                    final DatePickerDialog.OnDateSetListener singledate = new DatePickerDialog.OnDateSetListener() {

                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            //Log.e("Singledate", "singleDate : " + singleDate);
                            if (isdoneclick) {
                                ((Button) findViewById(R.id.btnselectdate)).setText(formatDate(year, month, day));
                                singleDate = formatDateServer(year, month, day);
                            } else {
                                Log.e("Close", "Close");
                                ((Button) findViewById(R.id.btnselectdate)).setText("DD-MM-YYYY");
                                ((Button) findViewById(R.id.btnselectdate)).setTextColor(getResources().getColor(R.color.appColorBase));
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
                    dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Close", new DialogInterface.OnClickListener() {
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
                    ((Button) findViewById(R.id.btntoday)).setTextColor(getResources().getColor(R.color.appColorBase));
                    ((Button) findViewById(R.id.btntomorrow)).setTextColor(getResources().getColor(R.color.appColorBase));
                    ((Button) findViewById(R.id.btnselectdate)).setTextColor(getResources().getColor(R.color.appColorBase));
                    singleDate = "";
                    datePressed = false;
                    todayPressed = false;
                    tomorrowPressed = false;
                }
                break;

            case R.id.btntomorrow:

                ((Button) findViewById(R.id.btnfrom)).setText("DD-MM-YYYY");
                ((Button) findViewById(R.id.btnto)).setText("DD-MM-YYYY");
                if (!tomorrowPressed) {

                    findViewById(R.id.btnselectdate).setBackgroundResource(R.drawable.bg_advanced_filter_properties);
                    findViewById(R.id.btntomorrow).setBackgroundResource(R.drawable.bg_advance_filter_orange);
                    findViewById(R.id.btntoday).setBackgroundResource(R.drawable.bg_advanced_filter_properties);
                    ((Button) findViewById(R.id.btntoday)).setTextColor(getResources().getColor(R.color.appColorBase));
                    ((Button) findViewById(R.id.btntomorrow)).setTextColor(getResources().getColor(R.color.white));
                    ((Button) findViewById(R.id.btnselectdate)).setTextColor(getResources().getColor(R.color.appColorBase));
                    final Calendar c = Calendar.getInstance();
                    c.add(Calendar.DATE, 1);
                    final int currentYear = c.get(Calendar.YEAR);
                    final int currentMonth = c.get(Calendar.MONTH);
                    final int currentDay = (c.get(Calendar.DAY_OF_MONTH));
                    singleDate = formatDateServer(currentYear, currentMonth, currentDay);

                    Log.e("Singledate", "singleDate : " + singleDate);
                    tomorrowPressed = true;
                } else {

                    findViewById(R.id.btnselectdate).setBackgroundResource(R.drawable.bg_advanced_filter_properties);
                    findViewById(R.id.btntomorrow).setBackgroundResource(R.drawable.bg_advanced_filter_properties);
                    findViewById(R.id.btntoday).setBackgroundResource(R.drawable.bg_advanced_filter_properties);
                    ((Button) findViewById(R.id.btntoday)).setTextColor(getResources().getColor(R.color.appColorBase));
                    ((Button) findViewById(R.id.btntomorrow)).setTextColor(getResources().getColor(R.color.appColorBase));
                    ((Button) findViewById(R.id.btnselectdate)).setTextColor(getResources().getColor(R.color.appColorBase));
                    singleDate = "";
                    tomorrowPressed = false;
                    datePressed = false;
                    todayPressed = false;
                }
                ((Button) findViewById(R.id.btnselectdate)).setText("DD-MM-YYYY");
                break;

            case R.id.btntoday:

                ((Button) findViewById(R.id.btnfrom)).setText("DD-MM-YYYY");
                ((Button) findViewById(R.id.btnto)).setText("DD-MM-YYYY");
                ((Button) findViewById(R.id.btnselectdate)).setText("DD-MM-YYYY");
                if (!todayPressed) {
                    findViewById(R.id.btnselectdate).setBackgroundResource(R.drawable.bg_advanced_filter_properties);
                    findViewById(R.id.btntomorrow).setBackgroundResource(R.drawable.bg_advanced_filter_properties);
                    findViewById(R.id.btntoday).setBackgroundResource(R.drawable.bg_advance_filter_orange);
                    ((Button) findViewById(R.id.btntoday)).setTextColor(getResources().getColor(R.color.white));
                    ((Button) findViewById(R.id.btntomorrow)).setTextColor(getResources().getColor(R.color.appColorBase));
                    ((Button) findViewById(R.id.btnselectdate)).setTextColor(getResources().getColor(R.color.appColorBase));
                    final Calendar c1 = Calendar.getInstance();
                    final int currentYear1 = c1.get(Calendar.YEAR);
                    final int currentMonth1 = c1.get(Calendar.MONTH);
                    final int currentDay1 = (c1.get(Calendar.DAY_OF_MONTH));
                    singleDate = formatDateServer(currentYear1, currentMonth1, currentDay1);

                    Log.e("Singledate", "singleDate : " + singleDate);
                    todayPressed = true;
                } else {
                    findViewById(R.id.btnselectdate).setBackgroundResource(R.drawable.bg_advanced_filter_properties);
                    findViewById(R.id.btntomorrow).setBackgroundResource(R.drawable.bg_advanced_filter_properties);
                    findViewById(R.id.btntoday).setBackgroundResource(R.drawable.bg_advanced_filter_properties);
                    ((Button) findViewById(R.id.btntoday)).setTextColor(getResources().getColor(R.color.appColorBase));
                    ((Button) findViewById(R.id.btntomorrow)).setTextColor(getResources().getColor(R.color.appColorBase));
                    ((Button) findViewById(R.id.btnselectdate)).setTextColor(getResources().getColor(R.color.appColorBase));
                    singleDate = "";
                    todayPressed = false;
                    datePressed = false;
                    tomorrowPressed = false;
                }
                break;

            case R.id.btnapply:

                findViewById(R.id.btnapply).setBackgroundResource(R.drawable.button_sign_in);
                if (valdite()) {
                    startActivity(new Intent(AdvanceFilterActivity.this, AdvancedFilterResultActivity.class));
                }
                else {
                    if (!lalala) {
                        AlertDialogHelper.showSimpleAlertDialog(this, "Select at least one criteria");
                    }
                }

                break;
        }
    }

    private boolean valdite() {
        String eventTypeStr = etEventTypeList.getText().toString();
        String eventPreferenceStr = etPreferenceList.getText().toString();
        String eventPreferenceIdStr = "";
        if (eventPreferenceStr.equalsIgnoreCase("")) {
            eventPreferenceIdStr = "";
        } else {
            eventPreferenceIdStr = sb1.toString();
        }
        String city = etCityList.getText().toString();
        if (mSelectedPriceRange > 0.00) {
            range = "0.00-" + mSelectedPriceRange + "0";
        } else {
            range = "";
        }
//                String price = etPriceList.getText().toString();
//                String city = spincity.getSelectedItem().toString();
        String eventCategoryStr = etEventCategoryList.getText().toString();
        String fromdate = ((Button) findViewById(R.id.btnfrom)).getText().toString();
        String todate = ((Button) findViewById(R.id.btnto)).getText().toString();
        if (!singleDate.equalsIgnoreCase("") ||
                (!city.isEmpty() || !eventCategoryStr.isEmpty()) ||
                (!eventTypeStr.isEmpty() || !range.isEmpty() || !eventPreferenceIdStr.isEmpty())) {
            PreferenceStorage.saveFilterSingleDate(this, singleDate);

            if (!city.equalsIgnoreCase("Select your city")) {
                PreferenceStorage.saveFilterCity(this, city);
            } else {
                PreferenceStorage.saveFilterCity(this, "");
            }
            if (!range.isEmpty()) {
                PreferenceStorage.saveFilterRange(this, range);
            } else {
                PreferenceStorage.saveFilterRange(this, "");
            }
            if (!eventTypeStr.isEmpty()) {
                PreferenceStorage.saveFilterEventType(this, eventTypeStr);
            } else {
                PreferenceStorage.saveFilterEventType(this, "");
            }
            if (!eventTypeStr.isEmpty()) {
                PreferenceStorage.saveFilterEventCategory(this, eventCategoryStr);
            } else {
                PreferenceStorage.saveFilterEventCategory(this, "");
            }
            if (!eventPreferenceIdStr.isEmpty()) {
                PreferenceStorage.saveFilterPreference(this, eventPreferenceIdStr);
            } else {
                PreferenceStorage.saveFilterPreference(this, "");
            }
            return true;
        } else if ((!fromdate.equalsIgnoreCase("DD-MM-YYYY") ||
                !todate.equalsIgnoreCase("DD-MM-YYYY"))) {
            if (!fromdate.equalsIgnoreCase("DD-MM-YYYY")) {
                if (todate.equalsIgnoreCase("DD-MM-YYYY")) {
                    AlertDialogHelper.showSimpleAlertDialog(this, "You need select both from and to dates");
                    lalala = true;
                    return false;
                } else {
                    PreferenceStorage.saveFilterToDate(this, todate);
                    PreferenceStorage.saveFilterFromDate(this, fromdate);
                    return true;
                }
            }
            if (!todate.equalsIgnoreCase("DD-MM-YYYY")) {
                if (fromdate.equalsIgnoreCase("DD-MM-YYYY")) {
                    AlertDialogHelper.showSimpleAlertDialog(this, "You need select both from and to dates");
                    lalala = true;
                    return false;
                } else {
                    PreferenceStorage.saveFilterToDate(this, todate);
                    PreferenceStorage.saveFilterFromDate(this, fromdate);
                    return true;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    private void showEventTypeList() {
        Log.d(TAG, "Show event type list");
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.gender_header_layout, null);
        TextView header = (TextView) view.findViewById(R.id.gender_header);
        header.setText("Select Event Type");
        builderSingle.setCustomTitle(view);

        builderSingle.setAdapter(eventTypeAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = eventTypeList.get(which);
                        etEventTypeList.setText(strName);
                    }
                });
        builderSingle.show();
    }

    private void showEventCategoryList() {
        Log.d(TAG, "Show event category list");
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.gender_header_layout, null);
        TextView header = (TextView) view.findViewById(R.id.gender_header);
        header.setText("Select Event Category");
        builderSingle.setCustomTitle(view);

        builderSingle.setAdapter(eventCategoryAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = eventCategoryList.get(which);
                        etEventCategoryList.setText(strName);
                    }
                });
        builderSingle.show();
    }

    private void DatePickerSelection() {
        final Calendar c = Calendar.getInstance();
        final int currentYear = c.get(Calendar.YEAR);
        final int currentMonth = c.get(Calendar.MONTH);
        final int currentDay = c.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog.OnDateSetListener fromdate = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int month, int day) {
                Log.d(TAG, "From selected");
                if (isdoneclick) {
                    ((Button) findViewById(R.id.btnfrom)).setText(formatDate(year, month, day));
                    mFromDateVal = formatDateServer(year, month, day);
                } else {
                    Log.e("Close", "Close");
                    ((Button) findViewById(R.id.btnfrom)).setText("DD-MM-YYYY");
                    mFromDateVal = "DD-MM-YYYY";
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
                ((Button) findViewById(R.id.btnselectdate)).setTextColor(getResources().getColor(R.color.appColorBase));

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
                mFromDatePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isdoneclick = false;
                        ((Button) findViewById(R.id.btnfrom)).setText("DD-MM-YYYY");
                        mFromDatePickerDialog.dismiss();
                    }
                });
                mFromDatePickerDialog.show();
//                String fromDt = ((Button) findViewById(R.id.btnfrom)).getText().toString();
//                if (!fromDt.isEmpty()) {
//                    DatePicker dP = mFromDatePickerDialog.getDatePicker();
//                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.UK);
//                    Date date = null;
//                    try {
//                        date = sdf.parse(fromDt);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    long millis = date.getTime();
//                    dP.setMaxDate(millis);
//                }
            }
        });

        final DatePickerDialog.OnDateSetListener todate = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int month, int day) {
                if (isdoneclick) {
                    ((Button) findViewById(R.id.btnto)).setText(formatDate(year, month, day));
                    mTodateVal = formatDateServer(year, month, day);
                } else {
                    Log.e("Clear", "Clear");
                    ((Button) findViewById(R.id.btnto)).setText("DD-MM-YYYY");
                    mTodateVal = "DD-MM-YYYY";
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
                ((Button) findViewById(R.id.btnselectdate)).setTextColor(getResources().getColor(R.color.appColorBase));
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
                dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isdoneclick = false;
                        ((Button) findViewById(R.id.btnto)).setText("DD-MM-YYYY");
                        dpd.dismiss();
                    }
                });
                dpd.show();
//                String fromDt = ((Button) findViewById(R.id.btnfrom)).getText().toString();
//                if (!fromDt.isEmpty()) {
//                    DatePicker dP = dpd.getDatePicker();
//                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.UK);
//                    Date date = null;
//                    try {
//                        date = sdf.parse(fromDt);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    long millis = date.getTime();
//                    dP.setMinDate(millis - 1000);
//                }


            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
//        PreferenceStorage.saveFilterCitySelection(this, position);
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

    private void createPreferenceList() {

        AlertDialog.Builder builder = new AlertDialog.Builder(AdvanceFilterActivity.this);
        sb = new StringBuilder();
        sb1 = new StringBuilder();
        sb1.append(" ");
        // String array for alert dialog multi choice items

        ddialog = new AlertDialog.Builder(this)
                .setTitle("Select Preference")
                .setAdapter(mPreferenceAdapter, null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //fetch all the selected category'
                        int ival = 0;
//                        boolean checkFirst = true;
                        for (Integer i : mSelectedPreferenceList) {
                            String name = PreferenceList.get(i);
                            String id = PreferenceIdList.get(i);
                            if (ival == 0) {
                                sb = sb.append(name);
                                sb1 = sb1.append(id + ",");
                            } else {
                                sb = sb.append("," + name);
                                sb1 = sb1.append(id + ",");
                            }
                            ival++;
                        }
                        if (!sb1.toString().equalsIgnoreCase("") || sb1.toString() != null) {
                            sb1.setLength(sb1.length() - 1);
                        }
                        etPreferenceList.setText(sb.toString());
                        preferenceId = sb1.toString();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();

        ddialog.getListView().setItemsCanFocus(false);
        ddialog.getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ddialog.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.d(TAG, "Item clicked");

            }
        });

    }

    private void showPreferenceList() {
        ddialog.show();
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

            String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.ALL_EVENT_CITY_LIST;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), url);


        } else {
            AlertDialogHelper.showSimpleAlertDialog(this, "No Network connection");
        }
    }

    private void showPriceList() {

        Log.d(TAG, "Show price range list");
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.gender_header_layout, null);
        TextView header = (TextView) view.findViewById(R.id.gender_header);
        header.setText("Select Price Range");
//        builderSingle.setCustomTitle(view);
//
//        builderSingle.setAdapter(mCityAdapter,
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        StoreCity cty = cityList.get(which);
//                        etCityList.setText(cty.getCityName());
//                        cityId = cty.getCityId();
//                    }
//                });
//        builderSingle.show();
    }

    private void GetPriceRange() {

        checkState = "price";

        if (CommonUtils.isNetworkAvailable(this)) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(getApplicationContext()));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.EVENT_PRICE_LIST;
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
                        etCityList.setText(cty.getCityName());
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
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<Category>>() {
                    }.getType();
                    ArrayList<Category> arrayList = gson.fromJson(getData.toString(), listType);
                    PreferenceList.clear();
                    PreferenceIdList.clear();
                    mSelectedPreferenceList.clear();
                    for (Category category : arrayList) {
                        PreferenceList.add(category.getCategory());
                        PreferenceIdList.add(category.getId());
                    }
                    createPreferenceList();
                    GetEventCities();

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

                    GetPriceRange();

                } else if (checkState.equalsIgnoreCase("price")) {

                    firstTime = true;
                    String maxRange = response.getString("Pricerange");
                    startRange = Double.parseDouble(maxRange);
                    int endPoint = (int) startRange;
                    etPriceList.setMax(endPoint);
                    endRangeText.setText("S$" + endPoint);
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

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mSelectedPriceRange = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Toast.makeText(getApplicationContext(), "Price range: Rs.0 - Rs." + seekBar.getProgress(), Toast.LENGTH_SHORT).show();
    }
}

