package com.palprotech.heylaapp.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.adapter.EventCitiesAdapter;
import com.palprotech.heylaapp.bean.support.EventCities;
import com.palprotech.heylaapp.bean.support.EventCitiesList;
import com.palprotech.heylaapp.bean.support.StoreCountry;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Admin on 01-11-2017.
 */

public class SelectCityActivity extends AppCompatActivity implements LocationListener, View.OnClickListener, IServiceListener, DialogClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = SelectCityActivity.class.getName();

    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    ListView loadMoreListView;
    EventCitiesAdapter eventCitiesAdapter;
    ArrayList<EventCities> eventCitiesArrayList;
    Handler mHandler = new Handler();
    int pageNumber = 0, totalCount = 0;
    protected boolean isLoadingForFirstTime = true;
    private Spinner spnCountryList;
    protected LocationManager locationManager;
    RelativeLayout location;
    protected Double latitude, longitude;
    String address = "", resString = "", storeCountryId = "";
    TextView loc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);
        spnCountryList = (Spinner) findViewById(R.id.country_list_spinner);
//        location = (RelativeLayout) findViewById(R.id.location_layout);
//        location.setOnClickListener(this);
//        loc = (TextView) findViewById(R.id.currentloctxt);
        loadMoreListView = (ListView) findViewById(R.id.listView_events);
        loadMoreListView.setOnItemClickListener(this);
        eventCitiesArrayList = new ArrayList<>();

//        GetEventCountries();

        GetEventCities();

        spnCountryList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                StoreCountry classList = (StoreCountry) parent.getSelectedItem();

                if (eventCitiesArrayList != null) {
                    eventCitiesArrayList.clear();
                    loadMoreListView.setAdapter(eventCitiesAdapter);
                }
                storeCountryId = classList.getCountryId();
                GetEventCities();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void GetEventCities() {

        resString = "Cities";

        if (CommonUtils.isNetworkAvailable(this)) {

            JSONObject jsonObject = new JSONObject();
            try {
//                jsonObject.put(HeylaAppConstants.KEY_EVENT_COUNTRY_ID, storeCountryId);
//                jsonObject.put(HeylaAppConstants.KEY_EVENT_COUNTRY_ID, "99");
                jsonObject.put(HeylaAppConstants.KEY_EVENT_COUNTRY_ID, "195");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
            String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.EVENT_CITY_LIST;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), url);


        } else {
            AlertDialogHelper.showSimpleAlertDialog(this, "No Network connection");
        }
    }

    private void GetEventCountries() {

        resString = "Countries";

        if (CommonUtils.isNetworkAvailable(this)) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(getApplicationContext()));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
            String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.EVENT_COUNTRY_LIST;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), url);


        } else {
            AlertDialogHelper.showSimpleAlertDialog(this, "No Network connection");
        }
    }

    private void sendSelectedCity(EventCities eventCities) {

        resString = "select";

        if (CommonUtils.isNetworkAvailable(this)) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(getApplicationContext()));
                jsonObject.put(HeylaAppConstants.PARAMS_CITY_ID, PreferenceStorage.getUserId(getApplicationContext()));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
            String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.EVENT_COUNTRY_LIST;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), url);


        } else {
            AlertDialogHelper.showSimpleAlertDialog(this, "No Network connection");
        }
    }

    @Override
    public void onClick(View v) {
        if (v == location) {

        }
    }

    @Override
    public void onResponse(final JSONObject response) {

        progressDialogHelper.hideProgressDialog();
        if (validateSignInResponse(response)) {

            try {
                if (resString.equalsIgnoreCase("Cities")) {
                    JSONArray getData = response.getJSONArray("cities");
                    if (getData != null && getData.length() > 0) {

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressDialogHelper.hideProgressDialog();
                                Gson gson = new Gson();
                                EventCitiesList eventCitiesList = gson.fromJson(response.toString(), EventCitiesList.class);
                                if (eventCitiesList.getEventCities() != null && eventCitiesList.getEventCities().size() > 0) {
                                    totalCount = eventCitiesList.getCount();
                                    isLoadingForFirstTime = false;
                                    updateListAdapter(eventCitiesList.getEventCities());
//                                    try {
//                                        checkCurrentCity();
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
                                }
                            }
                        });
                    } else {
                        if (eventCitiesArrayList != null) {
                            eventCitiesArrayList.clear();
                            eventCitiesAdapter = new EventCitiesAdapter(this, this.eventCitiesArrayList);
                            loadMoreListView.setAdapter(eventCitiesAdapter);
                        }
                    }
                } else if (resString.equalsIgnoreCase("Countries")) {

                    JSONArray getData = response.getJSONArray("Countries");
                    JSONObject userData = getData.getJSONObject(0);
                    int getLength = getData.length();
                    String subjectName = null;
                    Log.d(TAG, "userData dictionary" + userData.toString());

                    String classId = "";
                    String className = "";
                    ArrayList<StoreCountry> classesList = new ArrayList<>();

                    for (int i = 0; i < getLength; i++) {

                        classId = getData.getJSONObject(i).getString("id");
                        className = getData.getJSONObject(i).getString("country_name");

                        classesList.add(new StoreCountry(classId, className));
                    }

                    //fill data in spinner
                    ArrayAdapter<StoreCountry> adapter = new ArrayAdapter<StoreCountry>(getApplicationContext(), R.layout.spinner_item_ns, classesList);
                    spnCountryList.setAdapter(adapter);
                } else if (resString.equalsIgnoreCase("select")) {

//                    Toast.makeText(getApplicationContext(), "You are in now " + eventCities.getCityName(), Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(this, SetUpPreferenceActivity.class);
//        intent.putExtra("eventObj", eventCities);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(final String error) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                progressDialogHelper.hideProgressDialog();
                AlertDialogHelper.showSimpleAlertDialog(SelectCityActivity.this, error);
            }
        });

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

    protected void updateListAdapter(ArrayList<EventCities> eventCitiesArrayList) {
        this.eventCitiesArrayList.addAll(eventCitiesArrayList);
        if (eventCitiesAdapter == null) {
            eventCitiesAdapter = new EventCitiesAdapter(this, this.eventCitiesArrayList);
            loadMoreListView.setAdapter(eventCitiesAdapter);
        } else {
            eventCitiesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onEvent list item click" + position);
        EventCities eventCities = null;
        if ((eventCitiesAdapter != null) && (eventCitiesAdapter.ismSearching())) {
            Log.d(TAG, "while searching");
            int actualindex = eventCitiesAdapter.getActualEventPos(position);
            Log.d(TAG, "actual index" + actualindex);
            eventCities = eventCitiesArrayList.get(actualindex);
        } else {
            eventCities = eventCitiesArrayList.get(position);
        }
        PreferenceStorage.saveEventCityId(getApplicationContext(), eventCities.getId());
        PreferenceStorage.saveEventCityName(getApplicationContext(), eventCities.getCityName());
        sendSelectedCity(eventCities);

    }

    private void checkCurrentCity() throws IOException {
        String selectCity = "";
        selectCity = PreferenceStorage.getEventCityName(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            address = addresses.get(0).getLocality(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            loc.setText(address);
        } else {
            showSettingsAlert();
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing the Settings button.
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
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


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
