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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.activity.EventSharingPointActivity;
import com.palprotech.heylaapp.activity.LeaderboardStatistics;
import com.palprotech.heylaapp.activity.LoginPointsActivity;
import com.palprotech.heylaapp.activity.ProfileActivity;
import com.palprotech.heylaapp.customview.CircleImageView;
import com.palprotech.heylaapp.helper.AlertDialogHelper;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;
import com.palprotech.heylaapp.serviceinterfaces.IServiceListener;
import com.palprotech.heylaapp.utils.HeylaAppConstants;
import com.palprotech.heylaapp.utils.PreferenceStorage;
import com.squareup.picasso.Picasso;

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
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    TextView viewFullStatistics, loginCount, loginPoints, shareCount, sharePoints, checkinCount;
    TextView checkinPoints, bookingCount, bookingPoints, reviewCount, reviewPoints, totalPoints;
    TextView name, username;
    RelativeLayout login, share, check_in, booking, reviews;
    ImageView userPic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        initializeHelpers();
        initializeViews();
        setHasOptionsMenu(true);

        return rootView;
    }

    protected void initializeViews() {

        name = rootView.findViewById(R.id.name);
        if (PreferenceStorage.getFullName(getActivity()) == null) {
            name.setText("Name");
        } else {
            name.setText(PreferenceStorage.getFullName(getActivity()));
        }
        username = rootView.findViewById(R.id.username);
        if (PreferenceStorage.getUsername(getActivity()) == null) {
            username.setText("Username");
        } else {
            username.setText(PreferenceStorage.getUsername(getActivity()));
        }
        userPic = rootView.findViewById(R.id.leaderboard_profile_img);
        userPic.setOnClickListener(this);
        String url = PreferenceStorage.getUserPicture(getActivity());
        if (((url != null) && !(url.isEmpty()))) {
            Picasso.with(getActivity()).load(url).placeholder(R.drawable.ic_default_profile).error(R.drawable.ic_default_profile).into(userPic);
        }

        login = rootView.findViewById(R.id.login_layout);
        login.setOnClickListener(this);
        loginCount = rootView.findViewById(R.id.login_count);
        loginPoints = rootView.findViewById(R.id.login_star_count);


        share = rootView.findViewById(R.id.sharing_layout);
        share.setOnClickListener(this);
        shareCount = rootView.findViewById(R.id.sharing_count);
        sharePoints = rootView.findViewById(R.id.share_star_count);


        check_in = rootView.findViewById(R.id.check_in_layout);
        check_in.setOnClickListener(this);
        checkinCount = rootView.findViewById(R.id.check_in_count);
        checkinPoints = rootView.findViewById(R.id.check_in_star_count);

        reviews = rootView.findViewById(R.id.review_layout);
        reviews.setOnClickListener(this);
        reviewCount = rootView.findViewById(R.id.review_count);
        reviewPoints = rootView.findViewById(R.id.review_star_count);

        booking = rootView.findViewById(R.id.booking_layout);
        booking.setOnClickListener(this);
        bookingCount = rootView.findViewById(R.id.booking_count);
        bookingPoints = rootView.findViewById(R.id.booking_star_count);

        totalPoints = rootView.findViewById(R.id.total_points);
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

        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.LEADER_BOARD;
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
        } else if (view == login) {
            Intent homeIntent = new Intent(getActivity(), LoginPointsActivity.class);
//            homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
//            getActivity().finish();
        } else if (view==share){
            Intent homeIntent = new Intent(getActivity(), EventSharingPointActivity.class);
//            homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
//            getActivity().finish();
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

            String lCount, lPoint, cCount, cPoint, bCount, bPoint, sCount, sPoint, tPoint, rCount, rPoint;

            final JSONArray getData = response.getJSONArray("Leaderboard");

            lCount = getData.getJSONObject(0).getString("login_count");
            lPoint = getData.getJSONObject(0).getString("login_points");
            cCount = getData.getJSONObject(0).getString("checkin_count");
            cPoint = getData.getJSONObject(0).getString("checkin_points");
            bCount = getData.getJSONObject(0).getString("booking_count");
            bPoint = getData.getJSONObject(0).getString("booking_points");
            sCount = getData.getJSONObject(0).getString("sharing_count");
            sPoint = getData.getJSONObject(0).getString("sharing_points");
            tPoint = getData.getJSONObject(0).getString("total_points");
            rCount = getData.getJSONObject(0).getString("review_count");
            rPoint = getData.getJSONObject(0).getString("review_points");

            loginCount.setText("(" + lCount + ")");
            loginPoints.setText(lPoint);
            shareCount.setText("(" + sCount + ")");
            sharePoints.setText(sPoint);
            checkinCount.setText("(" + cCount + ")");
            checkinPoints.setText(cPoint);
            reviewCount.setText("(" + rCount + ")");
            reviewPoints.setText(rPoint);
            bookingCount.setText("(" + bCount + ")");
            bookingPoints.setText(bPoint);
            totalPoints.setText("(" + tPoint + ")");

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
