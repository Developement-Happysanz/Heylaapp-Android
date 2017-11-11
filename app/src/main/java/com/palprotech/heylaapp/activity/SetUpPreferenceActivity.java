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
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.adapter.PreferenceListAdapter;
import com.palprotech.heylaapp.bean.support.Category;
import com.palprotech.heylaapp.bean.support.EventCities;
import com.palprotech.heylaapp.bean.support.Preference;
import com.palprotech.heylaapp.helper.AlertDialogHelper;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.interfaces.DialogClickListener;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;
import com.palprotech.heylaapp.serviceinterfaces.IServiceListener;
import com.palprotech.heylaapp.utils.CommonUtils;
import com.palprotech.heylaapp.utils.HeylaAppConstants;
import com.palprotech.heylaapp.utils.PreferenceStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Admin on 06-11-2017.
 */

public class SetUpPreferenceActivity extends AppCompatActivity implements IServiceListener,DialogClickListener,View.OnClickListener,PreferenceListAdapter.OnItemClickListener {

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
    private TextView txtGetStarted, txtSelect, txtSelectAll;
    HashSet<String> hashSet;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_preference);

        txtGetStarted = (TextView) findViewById(R.id.text_getStarted);
        txtGetStarted.setOnClickListener(this);
        txtSelect = (TextView) findViewById(R.id.text_select);
        txtSelectAll = (TextView) findViewById(R.id.checkBox);
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


        //mRecyclerView.setOnItemClickListener(this);
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

        /*SetCategory setCategory = new SetCategory();
        setCategory.setPreferences(preferences);
        setCategory.setFuncName("user_preference");
        setCategory.setUserId(PreferenceStorage.getUserId(this));
        setCategory.setUserType(PreferenceStorage.getUserType(this));
        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        Gson gson = new Gson();
        String json = gson.toJson(setCategory);
        categoryServiceHelper.makeSetCategoryServiceCall(json);*/
    }

    private void GetPreferences(){
        if (CommonUtils.isNetworkAvailable(this)) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(getApplicationContext()));

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
        String ok ="";
        progressDialogHelper.hideProgressDialog();
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Category>>() {
        }.getType();
        categoryArrayList = (ArrayList<Category>) gson.fromJson(response.toString(), listType);
        preferenceAdatper = new PreferenceListAdapter(this, categoryArrayList, this);
        mRecyclerView.setAdapter(preferenceAdatper);
    }

    @Override
    public void onError(String error) {
        progressDialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(this, error);
    }

    private SparseBooleanArray selectedItems;

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
                //alertDialogBuilder.setTitle("Registration Successful");
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
                    tag.setCategoryPreference("yes");
                    preferenceAdatper.notifyDataSetChanged();
//                    categoryArrayList.remove(pos);
//                    mRecyclerView.removeViewAt(pos);
//                    preferenceAdatper.notifyItemRemoved(pos);
//                    preferenceAdatper.notifyItemRangeChanged(pos, categoryArrayList.size());
                }
            } else {
                selval = false;
                for (pos = 0; pos < categoryArrayList.size(); pos++) {
                    Category tag = preferenceAdatper.getItem(pos);
                    tag.setCategoryPreference("no");
                    selectedList.removeAll(selectedList);
                    preferenceAdatper.notifyDataSetChanged();
                }
            }
            preferenceAdatper.notifyDataSetChanged();
//            Intent navigationIntent = new Intent(this, SelectPreferenceActivity.class);
//            navigationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(navigationIntent);

        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Category tag = preferenceAdatper.getItem(position);
        RelativeLayout textView = (RelativeLayout) view;

//        GradientDrawable bgShape = (GradientDrawable) textView.getBackground();
        if (tag.getCategoryPreference().equals("no")) {
//            bgShape.setColor(getResources().getColor(R.color.preference_orange));
//            textView.setBackgroundColor(getResources().getColor(R.color.preference_orange));
//            TextView mtextView = (TextView) view;
//            mtextView.setTextColor(getResources().getColor(R.color.preference_orange));
//            PrefSelect.setBackgroundResource(imgResource);
            tag.setCategoryPreference("yes");
            selectedList.add(tag);

            preferenceAdatper.notifyItemChanged(position);
        } else {
//            bgShape.setColor(getResources().getColor(R.color.white));
//            textView.setBackgroundColor(getResources().getColor(R.color.black));
//            TextView mtextView = (TextView) view;
            tag.setCategoryPreference("no");
            selectedList.remove(tag);
            preferenceAdatper.notifyItemChanged(position);
        }
//        preferenceAdatper.notifyItemChanged(position);
//        ImageView tickImage = (ImageView) view.findViewById(R.id.tickImage);
//        ImageView logoImage = (ImageView) view.findViewById(R.id.img_category);
//        if (tag.getCategoryPreference().equals("no")) {
//            tickImage.setVisibility(View.VISIBLE);
//            tag.setCategoryPreference("yes");
//            logoImage.setAlpha((float) 0.1);
//        } else {
//            tickImage.setVisibility(View.INVISIBLE);
//            tag.setCategoryPreference("no");
//            logoImage.setAlpha((float) 1);
//        }
//        if (selectedList.contains(tag)) {
//            selectedList.remove(tag);
//        } else {
//            selectedList.add(tag);
//        }
    }
}
