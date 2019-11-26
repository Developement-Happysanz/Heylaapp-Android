package com.palprotech.heylaapp.activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.adapter.RankListAdapter;
import com.palprotech.heylaapp.bean.support.Rank;
import com.palprotech.heylaapp.bean.support.RankList;
import com.palprotech.heylaapp.customview.CircleImageView;
import com.palprotech.heylaapp.helper.AlertDialogHelper;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.interfaces.DialogClickListener;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;
import com.palprotech.heylaapp.serviceinterfaces.IServiceListener;
import com.palprotech.heylaapp.utils.CommonUtils;
import com.palprotech.heylaapp.utils.HeylaAppConstants;
import com.palprotech.heylaapp.utils.PreferenceStorage;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PointTableActivity extends AppCompatActivity implements IServiceListener, DialogClickListener {

    private static final String TAG = "ExamMarksActivity";
    ListView loadMoreListView;
    RankListAdapter rankListAdapter;
    private ServiceHelper serviceHelper;
    ArrayList<Rank> rankArrayList;
    RankList rankList = new RankList();
    int totalCount = 0;
    private ProgressDialogHelper progressDialogHelper;
    protected boolean isLoadingForFirstTime = true;
    Handler mHandler = new Handler();
    private Rank ranks;
    String ExamId, IsInternalExternal;
    TextView txtTotal;
    LinearLayout layout_all;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_table);
//        loadMoreListView = (ListView) findViewById(R.id.listView_ranks);
        layout_all = (LinearLayout) findViewById(R.id.layout_member_list);
        rankArrayList = new ArrayList<>();
        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);
//        txtTotal = (TextView) findViewById(R.id.txtTotal);

        callGetRankListViewService();
        ImageView bckbtn = (ImageView) findViewById(R.id.back_res);
        bckbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void callGetRankListViewService() {
       if (rankArrayList != null)
           rankArrayList.clear();

        if (CommonUtils.isNetworkAvailable(this)) {
            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
            new HttpAsyncTask().execute("");
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

    private boolean validateSignInResponse(JSONObject response) {
        boolean signInsuccess = false;
        if ((response != null)) {
            try {
                String status = response.getString("status");
                String msg = response.getString(HeylaAppConstants.PARAM_MESSAGE);
                Log.d(TAG, "status val" + status + "msg" + msg);

                if ((status != null)) {
                    if (((status.equalsIgnoreCase("activationError")) || (status.equalsIgnoreCase("alreadyRegistered")) ||
                            (status.equalsIgnoreCase("notRegistered")) || (status.equalsIgnoreCase("error")))) {
                        signInsuccess = false;
                        Log.d(TAG, "Show error dialog");
                        AlertDialogHelper.showSimpleAlertDialog(this, msg);

                    } else {
                        signInsuccess = true;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return signInsuccess;
    }

    @Override
    public void onResponse(final JSONObject response) {
        if (validateSignInResponse(response)) {
            Log.d("ajazFilterresponse : ", response.toString());
//            try {
//                String totalMark = response.getString("user_points");
//                txtTotal.setText(totalMark);
//
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialogHelper.hideProgressDialog();

                    Gson gson = new Gson();
                    rankList = gson.fromJson(response.toString(), RankList.class);
                    if (rankList.getRankDetails() != null && rankList.getRankDetails().size() > 0) {
                        totalCount = rankList.getRankDetails().size();
                        isLoadingForFirstTime = false;
//                        updateListAdapter(rankList.getRankDetails());
                        loadMembersList(totalCount);
                    }
                }
            });
        } else {
            Log.d(TAG, "Error while sign In");
        }
    }

    protected void updateListAdapter(ArrayList<Rank> rankViewArrayList) {
        this.rankArrayList.addAll(rankViewArrayList);
        if (rankListAdapter == null) {
            rankListAdapter = new RankListAdapter(this, this.rankArrayList);
            loadMoreListView.setAdapter(rankListAdapter);
        } else {
            rankListAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onError(final String error) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                progressDialogHelper.hideProgressDialog();
                AlertDialogHelper.showSimpleAlertDialog(PointTableActivity.this, error);
            }
        });
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... urls) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(getApplicationContext()));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
            String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.LEADER_BOARD_POINTS;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), url);

            return null;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Void result) {
            progressDialogHelper.cancelProgressDialog();
        }
    }

    private void loadMembersList(int memberCount) {

        try {

            for (int c1 = 0; c1 < memberCount; c1++) {

                RelativeLayout cell = new RelativeLayout(this);
                cell.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120));
                cell.setPadding(0, 0, 0, 0);
                cell.setBackgroundColor(Color.parseColor("#e5e6e9"));

//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 100);
//                params.setMargins(01, 01, 0, 01);
//
//                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(250, 100);
//                params1.setMargins(01, 01, 01, 01);
//
//                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(140, 100);
//                params2.setMargins(01, 01, 0, 01);


                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120);
                params.setMargins(0,0,0,0);

                if(c1 == 0) {
                    params.addRule(RelativeLayout.LEFT_OF, R.id.crown);
                } else {
                    params.addRule(RelativeLayout.LEFT_OF, R.id.user_points_txt);
                }
                params.addRule(RelativeLayout.RIGHT_OF, R.id.user_img);


                RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(160, 120);
                params1.setMargins(0, 0, 0, 0);
//                params1.addRule(RelativeLayout.LEFT_OF, R.id.user_rank_txt);
                params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);


                RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(100, 120);
                params2.setMargins(0, 0, 0, 0);
                params2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                RelativeLayout.LayoutParams imgParams = new RelativeLayout.LayoutParams(100, 120);
                imgParams.addRule(RelativeLayout.RIGHT_OF, R.id.user_rank_txt);
                imgParams.setMargins(0,0,0,0);


                RelativeLayout.LayoutParams crownParams = new RelativeLayout.LayoutParams(100, ViewGroup.LayoutParams.MATCH_PARENT);
                crownParams.addRule(RelativeLayout.LEFT_OF, R.id.user_points_txt);
                crownParams.setMargins(0,0,0,0);
//                params.addRule(RelativeLayout.LEFT_OF, R.id.user_name_txt);
//                imgParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

//                TextView title = new TextView(this);
//                title.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
//                title.setTextColor(Color.BLACK);
//                title.setText("Attendee Details " + c1);
//                title.setLayoutParams(params2);

                TextView line1 = new TextView(this);
                line1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, 2.0f));

                line1.setText(rankList.getRankDetails().get(c1).getName());


                line1.setId(R.id.user_name_txt);
                line1.setHint("User Name");
                line1.requestFocusFromTouch();
                line1.setTextSize(14.0f);
                line1.setBackgroundColor(Color.parseColor("#e5e6e9"));
//                line1.setSingleLine(true);
                line1.setTextColor(Color.parseColor("#000000"));
                line1.setGravity(Gravity.CENTER_VERTICAL);
                line1.setPadding(15, 0, 15, 0);
                line1.setLayoutParams(params);

                TextView line2 = new TextView(this);
                line2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, 2.0f));

                line2.setText(rankList.getRankDetails().get(c1).getTotal_points());

                line2.setId(R.id.user_points_txt);
                line2.setHint("User Points");
                line2.requestFocusFromTouch();
                line2.setTextSize(14.0f);
                line2.setAllCaps(true);
                line2.setGravity(Gravity.CENTER);
                line2.setSingleLine(true);
                line2.setBackgroundColor(Color.parseColor("#e5e6e9"));
                line2.setTextColor(Color.parseColor("#000000"));
                line2.setPadding(15, 0, 15, 0);
                line2.setLayoutParams(params1);

                TextView line3 = new TextView(this);
                line3.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, 2.0f));


                line3.setId(R.id.user_rank_txt);
                line3.setBackgroundColor(Color.parseColor("#e5e6e9"));
                line3.requestFocusFromTouch();
                line3.setTextSize(14.0f);
                line3.setAllCaps(true);
                line3.setGravity(Gravity.CENTER);
                line3.setSingleLine(true);
                line3.setTextColor(Color.parseColor("#000000"));
                line3.setText(""+(c1+1));
                line3.setPadding(15, 0, 15, 0);
                line3.setLayoutParams(params2);

                CircleImageView line4 = new CircleImageView(this);
                line4.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, 2.0f));

                String url = rankList.getRankDetails().get(c1).getUser_picture();

                line4.setId(R.id.user_img);
                if (((url != null) && !(url.isEmpty()))) {
                    Picasso.get().load(url).placeholder(R.drawable.ic_default_profile).error(R.drawable.ic_default_profile).into(line4);
                } else {
                    Picasso.get().load("123").placeholder(R.drawable.ic_default_profile).error(R.drawable.ic_default_profile).into(line4);
                }

                line4.setBackgroundColor(Color.parseColor("#e5e6e9"));
                line4.requestFocusFromTouch();
                line4.setPadding(0, 25, 0, 25);
                line4.setLayoutParams(imgParams);
                line3.setGravity(Gravity.CENTER);

//                TextView border = new TextView(this);
//                border.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
//                border.setHeight(1);
//                border.setBackgroundColor(Color.BLACK);

                ImageView crown = new ImageView(this);
                crown.setLayoutParams(crownParams);
                crown.setId(R.id.crown);
                crown.setImageResource(R.drawable.ic_points_ranking);
                crown.setPadding(50, 0, 0, 10);

                cell.addView(line4);
                cell.addView(line1);
                cell.addView(line2);
                cell.addView(line3);
                if(c1 == 0) {
                    cell.addView(crown);
                }
//                cell.addView(border);

                layout_all.addView(cell);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
