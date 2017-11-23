package com.palprotech.heylaapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.adapter.PreferenceListAdapter;
import com.palprotech.heylaapp.bean.support.Category;
import com.palprotech.heylaapp.bean.support.Preference;
import com.palprotech.heylaapp.bean.support.SetCategory;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by Admin on 06-11-2017.
 */

public class SetUpPreferenceActivity extends AppCompatActivity implements IServiceListener, DialogClickListener, View.OnClickListener, PreferenceListAdapter.OnItemClickListener {

    private static final String TAG = SetUpPreferenceActivity.class.getName();
    private RecyclerView mRecyclerView;
    private PreferenceListAdapter preferenceAdatper;
    private ArrayList<Category> categoryArrayList, selectedList;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private MenuItem menuSet;
    private GridLayoutManager mLayoutManager;
    private boolean selval = false;
    private ImageView PrefSelect;
    int pos;
    private TextView txtGetStarted, txtSelect;
    private CheckBox txtSelectAll;
    HashSet<String> hashSet;
    private String responseActivity = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_preference);

        txtGetStarted = (TextView) findViewById(R.id.text_getStarted);
        txtGetStarted.setOnClickListener(this);
        txtSelect = (TextView) findViewById(R.id.text_select);
        txtSelectAll = findViewById(R.id.checkBox);
        txtSelectAll.setOnClickListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.listView_categories);
        PrefSelect = (ImageView) findViewById(R.id.pref_tick);
        mLayoutManager = new GridLayoutManager(this, 6);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (preferenceAdatper.getItemViewType(position) > 0) {
                    return preferenceAdatper.getItemViewType(position);
                } else {
                    return 4;
                }
                //return 2;
            }
        });
        mRecyclerView.setLayoutManager(mLayoutManager);

        selectedList = new ArrayList<>();
        hashSet = new HashSet<String>();

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        GetPreferences();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select, menu);
        menuSet = (MenuItem) menu.findItem(R.id.action_set);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_set) {
            setPreferences();
            return true;
        } else if (id == R.id.action_skip) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setPreferences() {
        //save preferences selected
        Log.d(TAG, "size of selected preferences" + selectedList.size());
        PreferenceStorage.savePreferencesSelected(this, true);
        ArrayList<Preference> preferences = new ArrayList<>();
        for (Category category : selectedList) {
            Preference preference = new Preference();
            Log.d(TAG, "add category id" + category.getId());
            preference.setCategoryId(category.getId());

            preferences.add(preference);
        }

        SetCategory setCategory = new SetCategory();
        setCategory.setPreferences(preferences);
        Gson gson = new Gson();
        String json = gson.toJson(preferences);

        String removeSquareBracketOpen = "[";
        String removeSquareBracketClose = "]";
        String removeEtcCharSet1 = "{\"category_id\":\"";
        String removeEtcCharSet2 = "\"}";

        String new_string = json.replace(removeSquareBracketOpen, "");
        String new_string1 = new_string.replace(removeSquareBracketClose, "");
        String new_string2 = new_string1.replace(removeEtcCharSet1, "");
        String new_string3 = new_string2.replace(removeEtcCharSet2, "");

        String[] strArray = new_string3.split(",");
        final int[] animalsArray = new int[strArray.length];
        for (int i = 0; i < strArray.length; i++) {
            animalsArray[i] = Integer.parseInt(strArray[i]);
        }

        System.out.println(Arrays.toString(animalsArray));
        final int[] buckets = new int[1001];
        for (final int i : animalsArray) {
            buckets[i]++;
        }
        final int[] unique = new int[animalsArray.length];
        int count = 0;
        for (int i = 0; i < buckets.length; ++i) {
            if (buckets[i] > 0) {
                unique[count++] = i;
            }
        }
        final int[] compressed = new int[count];
        System.arraycopy(unique, 0, compressed, 0, count);
        System.out.println(Arrays.toString(compressed));

        String newOk = Arrays.toString(compressed);

        String newOk3 = newOk.replace(removeSquareBracketOpen, "");
        String newOk4 = newOk3.replace(removeSquareBracketClose, "");


        if (CommonUtils.isNetworkAvailable(this)) {
            responseActivity = "update";
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(getApplicationContext()));
                jsonObject.put(HeylaAppConstants.KEY_USER_TYPE, PreferenceStorage.getUserType(getApplicationContext()));
                jsonObject.put(HeylaAppConstants.KEY_CATEGORIES_ID, newOk4);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
            String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.USER_PREFERENCES_UPDATE;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), url);


        } else {
            AlertDialogHelper.showSimpleAlertDialog(this, "No Network connection");
        }
    }

    private void GetPreferences() {
        if (CommonUtils.isNetworkAvailable(this)) {
            responseActivity = "view";
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(getApplicationContext()));
                jsonObject.put(HeylaAppConstants.KEY_USER_TYPE, PreferenceStorage.getUserType(getApplicationContext()));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
            String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.USER_PREFERENCES_LIST;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), url);


        } else {
            AlertDialogHelper.showSimpleAlertDialog(this, "No Network connection");
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
        progressDialogHelper.hideProgressDialog();
        if (validateSignInResponse(response)) {
            if (responseActivity.equalsIgnoreCase("view")) {
                try {
                    JSONArray getData = response.getJSONArray("Categories");
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<Category>>() {
                    }.getType();
                    categoryArrayList = (ArrayList<Category>) gson.fromJson(getData.toString(), listType);
                    preferenceAdatper = new PreferenceListAdapter(this, categoryArrayList, this);
                    mRecyclerView.setAdapter(preferenceAdatper);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (responseActivity.equalsIgnoreCase("update")) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onError(String error) {
        progressDialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(this, error);
    }

    public void onCategorySelected(int position) {
        Log.d(TAG, "selected category position" + position);
        if (selectedList != null) {
            Category category = (Category) preferenceAdatper.getItem(position);
            Log.d(TAG, "id" + category.getId());
            selectedList.add(category);
        }
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
    public void onClick(View v) {
        if (v == txtGetStarted) {
            if (selectedList.size() >= 4) {

                setPreferences();
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Not enough categories selected");
                alertDialogBuilder.setMessage("Please select atleast four categories");
                alertDialogBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        } else if (v == txtSelectAll) {
            if (!selval) {
                selval = true;
                for (pos = 0; pos < categoryArrayList.size(); pos++) {
                    Category tag = preferenceAdatper.getItem(pos);
                    selectedList.add(tag);
                    tag.setCategoryPreference("Y");
                    preferenceAdatper.notifyDataSetChanged();
                }

            } else {
                selval = false;
                for (pos = 0; pos < categoryArrayList.size(); pos++) {
                    Category tag = preferenceAdatper.getItem(pos);
                    tag.setCategoryPreference("N");
                    selectedList.removeAll(selectedList);
                    preferenceAdatper.notifyDataSetChanged();
                }
            }
            preferenceAdatper.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Category tag = preferenceAdatper.getItem(position);
        if (tag.getCategoryPreference().equals("N")) {

            tag.setCategoryPreference("Y");
            selectedList.add(tag);

            preferenceAdatper.notifyItemChanged(position);
        } else {
            tag.setCategoryPreference("N");
            selectedList.remove(tag);
            preferenceAdatper.notifyItemChanged(position);
        }
    }
}
