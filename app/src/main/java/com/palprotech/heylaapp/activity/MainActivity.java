package com.palprotech.heylaapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.palprotech.heylaapp.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //boolean flag to know if main FAB is in open or closed state.
    private boolean fabExpanded = false;
//    private FloatingActionButton fabView;

    //Linear layout holding the Save submenu
    private LinearLayout layoutFabMapview;

    //Linear layout holding the Edit submenu
    private LinearLayout layoutFabNearby;
    private LinearLayout layoutFabListView;

    private TextView sample;

    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolBar();

//        fabView = (FloatingActionButton) this.findViewById(R.id.viewOptions);

//        layoutFabListView = (LinearLayout) this.findViewById(R.id.layoutFabListView);
//        layoutFabNearby = (LinearLayout) this.findViewById(R.id.layoutFabNearby);
//        layoutFabMapview = (LinearLayout) this.findViewById(R.id.layoutFabMapView);

//        sample = (TextView) findViewById(R.id.sampleViewChanger);
//        sample.setText("List View");

        //When main Fab (Settings) is clicked, it expands if not expanded already.
        //Collapses if main FAB was open already.
        //This gives FAB (Settings) open/close behavior
        /*fabView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabExpanded) {
                    closeSubMenusFab();
                } else {
                    openSubMenusFab();
                }
            }
        });*/

/*        layoutFabListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabExpanded) {
                    sample.setText("List View");
                }
            }
        });

        layoutFabMapview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabExpanded) {
                    sample.setText("Map View");
                }
            }
        });

        layoutFabNearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabExpanded) {
                    sample.setText("Nearby View");
                }
            }
        });*/

        //Only main FAB is visible in the beginning
        closeSubMenusFab();

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
//        changeFragment(0);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.action_favorites:
//                                changeFragment(0);
//                                fabView.setVisibility(View.VISIBLE);
                                break;

                            case R.id.action_popular:
//                                changeFragment(1);
//                                fabView.setVisibility(View.VISIBLE);
                                break;
                            case R.id.action_hotspot:
//                                changeFragment(2);
//                                fabView.setVisibility(View.VISIBLE);
                                break;
                            case R.id.action_leaderboard:
//                                changeFragment(3);
//                                fabView.setVisibility(View.INVISIBLE);
//                                closeSubMenusFab();
                                break;

                        }
                        return true;
                    }
                });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_landing, menu);
        return true;
    }

   /* private void changeFragment(int position) {

        Fragment newFragment = null;

        if (position == 0) {
            newFragment = new FavouriteFragment();
        } else if (position == 1) {
            newFragment = new PopularFragment();
        } else if (position == 2) {
            newFragment = new HotspotFragment();
        } else if (position == 3) {
            newFragment = new LeaderboardFragment();
        }

        getFragmentManager().beginTransaction().replace(
                R.id.fragmentContainer, newFragment)
                .commit();
    }*/


//        final String token = SharedPrefManager.getInstance(this).getDeviceToken();
//        String ok = token;

    //closes FAB submenus
    private void closeSubMenusFab() {
//        layoutFabListView.setVisibility(View.INVISIBLE);
//        layoutFabNearby.setVisibility(View.INVISIBLE);
//        layoutFabMapview.setVisibility(View.INVISIBLE);
//        fabView.setImageResource(R.drawable.ic_plus);
//        fabExpanded = false;
    }

    //Opens FAB submenus
    private void openSubMenusFab() {
//        layoutFabListView.setVisibility(View.VISIBLE);
//        layoutFabNearby.setVisibility(View.VISIBLE);
//        layoutFabMapview.setVisibility(View.VISIBLE);
        //Change settings icon to 'X' icon
//        fabView.setImageResource(R.drawable.ic_close);
//        fabExpanded = true;
    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.activity_toolbar);

        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_side_menu);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent homeIntent = new Intent(getApplicationContext(), MenuActivity.class);
                        startActivity(homeIntent);
                    }
                }
        );
    }

    @Override
    public void onClick(View view) {

    }

}
