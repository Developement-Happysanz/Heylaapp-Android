package com.palprotech.heylaapp.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.activity.LeaderboardStatistics;
import com.palprotech.heylaapp.activity.ProfileActivity;
import com.palprotech.heylaapp.customview.CircleImageView;

/**
 * Created by Narendar on 28/10/17.
 */

public class LeaderboardFragment extends Fragment implements View.OnClickListener {

    View rootView;
    CircleImageView profileImg;
    Button follow;
    TextView viewFullStatistics;
    RelativeLayout login, share, check_in, booking, reviews;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        initializeViews();
        setHasOptionsMenu(true);

        return rootView;
    }

    protected void initializeViews() {

        login = rootView.findViewById(R.id.login_layout);
        login.setOnClickListener(this);

        share = rootView.findViewById(R.id.sharing_layout);
        share.setOnClickListener(this);

        check_in = rootView.findViewById(R.id.check_in_layout);
        check_in.setOnClickListener(this);

        reviews = rootView.findViewById(R.id.review_layout);
        reviews.setOnClickListener(this);

        booking = rootView.findViewById(R.id.booking_layout);
        booking.setOnClickListener(this);
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
}
