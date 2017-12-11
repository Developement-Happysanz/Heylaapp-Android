package com.palprotech.heylaapp.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.activity.LeaderboardStatistics;
import com.palprotech.heylaapp.activity.ProfileActivity;
import com.palprotech.heylaapp.bean.support.LeaderBoard;
import com.palprotech.heylaapp.customview.CircleImageView;
import com.palprotech.heylaapp.helper.AlertDialogHelper;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;
import com.palprotech.heylaapp.serviceinterfaces.IServiceListener;
import com.palprotech.heylaapp.utils.HeylaAppConstants;
import com.palprotech.heylaapp.utils.PreferenceStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Narendar on 28/10/17.
 */

public class LeaderboardFragment extends Fragment implements View.OnClickListener, IServiceListener {

    View rootView;
    CircleImageView profileImg;
    Context context;
    Button follow;
    Handler mHandler = new Handler();
    String lCount, lPpoint, cCount, cPoint, bCount, bPoint, sCount, sPoint, tPoint, rCount, rPoint;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    TextView viewFullStatistics, loginCount, loginPoints, shareCount, sharePoints;
    RelativeLayout login, share, check_in, booking, reviews;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        initializeHelpers();
        initializeViews();
        setHasOptionsMenu(true);

        return rootView;
    }

    protected void initializeViews() {

        login = rootView.findViewById(R.id.login_layout);
        login.setOnClickListener(this);
        loginCount = rootView.findViewById(R.id.login_count);

        loginPoints = rootView.findViewById(R.id.login_star_count);


        share = rootView.findViewById(R.id.sharing_layout);
        share.setOnClickListener(this);

        check_in = rootView.findViewById(R.id.check_in_layout);
        check_in.setOnClickListener(this);

        reviews = rootView.findViewById(R.id.review_layout);
        reviews.setOnClickListener(this);

        booking = rootView.findViewById(R.id.booking_layout);
        booking.setOnClickListener(this);

    }

    protected void initializeHelpers() {
        serviceHelper = new ServiceHelper(getActivity());
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(getActivity());
        makeLeaderBoardServiceCall();
    }

    private void makeLeaderBoardServiceCall() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(HeylaAppConstants.KEY_USER_ID, PreferenceStorage.getUserId(getActivity()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.LEADERBOARD;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    @Override
    public void onClick(View view) {
        if (view == viewFullStatistics) {
            Intent homeIntent = new Intent(getActivity(), LeaderboardStatistics.class);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            getActivity().finish();
        } else if (view == profileImg) {
            Intent homeIntent = new Intent(getActivity(), ProfileActivity.class);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            getActivity().finish();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (menu != null) {
            menu.findItem(R.id.action_filter).setVisible(false);
            menu.findItem(R.id.action_search_view).setVisible(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResponse(final JSONObject response) {

        progressDialogHelper.hideProgressDialog();
        try {
            final JSONArray getData = response.getJSONArray("Leaderboard");

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialogHelper.hideProgressDialog();
                    Gson gson = new Gson();
                    LeaderBoard leaderBoard = gson.fromJson(response.toString(), LeaderBoard.class);
                    loginCount.setText(leaderBoard.getLoginCount());

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(final String error) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                progressDialogHelper.hideProgressDialog();
                AlertDialogHelper.showSimpleAlertDialog(getApplicationContext(), error);
            }
        });

    }
}
