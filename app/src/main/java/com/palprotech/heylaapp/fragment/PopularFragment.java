package com.palprotech.heylaapp.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.palprotech.heylaapp.R;

/**
 * Created by Narendar on 28/10/17.
 */

public class PopularFragment extends Fragment {

    private static final String TAG = PopularFragment.class.getName();
    private View rootView;
    MapView mapView;
    GoogleMap map;
    //boolean flag to know if main FAB is in open or closed state.
    private boolean fabExpanded = false;
    private FloatingActionButton fabView;
    //Linear layout holding the Save submenu
    private LinearLayout layoutFabMapview;

    //Linear layout holding the Edit submenu
    private LinearLayout layoutFabNearby;
    private LinearLayout layoutFabListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_popular, container, false);
        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) rootView.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        fabView = (FloatingActionButton) rootView.findViewById(R.id.viewOptions);
        layoutFabListView = (LinearLayout) rootView.findViewById(R.id.layoutFabListView);
        layoutFabNearby = (LinearLayout) rootView.findViewById(R.id.layoutFabNearby);
        layoutFabMapview = (LinearLayout) rootView.findViewById(R.id.layoutFabMapView);

        //When main Fab (Settings) is clicked, it expands if not expanded already.
        //Collapses if main FAB was open already.
        //This gives FAB (Settings) open/close behavior
        fabView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabExpanded) {
                    closeSubMenusFab();
                } else {
                    openSubMenusFab();
                }
            }
        });

        //Only main FAB is visible in the beginning
        closeSubMenusFab();
        return rootView;
    }

    //closes FAB submenus
    private void closeSubMenusFab() {
        layoutFabListView.setVisibility(View.INVISIBLE);
        layoutFabNearby.setVisibility(View.INVISIBLE);
        layoutFabMapview.setVisibility(View.INVISIBLE);
        fabView.setImageResource(R.drawable.ic_plus);
        fabExpanded = false;
    }

    //Opens FAB submenus
    private void openSubMenusFab() {
        layoutFabListView.setVisibility(View.VISIBLE);
        layoutFabNearby.setVisibility(View.VISIBLE);
        layoutFabMapview.setVisibility(View.VISIBLE);
//        Change settings icon to 'X' icon
        fabView.setImageResource(R.drawable.ic_close);
        fabExpanded = true;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
