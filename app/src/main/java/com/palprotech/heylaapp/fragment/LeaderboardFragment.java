package com.palprotech.heylaapp.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.activity.LeaderboardStatistics;
import com.palprotech.heylaapp.customview.CircleImageView;

/**
 * Created by Narendar on 28/10/17.
 */

public class LeaderboardFragment extends Fragment implements View.OnClickListener {

    View rootView;
    CircleImageView profileImg;
    Button follow;
    TextView viewFullStatistics;
    ProgressBar share, engagement, checkin, booking, friends;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        initializeViews();
        setHasOptionsMenu(true);

        return rootView;
    }

    protected void initializeViews() {
        share = rootView.findViewById(R.id.progress_sharing);
        share.setProgress(0);
        engagement = rootView.findViewById(R.id.progress_engagement);
        engagement.setProgress(0);
        checkin = rootView.findViewById(R.id.progress_checkin);
        checkin.setProgress(0);
        booking = rootView.findViewById(R.id.progress_booking);
        booking.setProgress(0);
        friends = rootView.findViewById(R.id.progress_friends);
        friends.setProgress(0);
        profileImg = rootView.findViewById(R.id.profile_img);
        profileImg.setOnClickListener(this);
        follow = rootView.findViewById(R.id.follow);
        follow.setOnClickListener(this);
        viewFullStatistics = rootView.findViewById(R.id.view_statistics);
        viewFullStatistics.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == viewFullStatistics) {
            Intent homeIntent = new Intent(getActivity(), LeaderboardStatistics.class);
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
