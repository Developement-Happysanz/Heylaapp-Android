package com.palprotech.heylaapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.utils.PreferenceStorage;
import com.squareup.picasso.Picasso;

/**
 * Created by Narendar on 24/10/17.
 */

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MenuActivity.class.getName();
    private RelativeLayout vBooking;
    private RelativeLayout vCategory;
    private RelativeLayout vChangeCity;
    private RelativeLayout vWishList;
    private RelativeLayout vShare;
    private RelativeLayout vAboutUs;
    private RelativeLayout vRateUs;
    private RelativeLayout vSignOut;
    private ImageView vUserImage;
    private TextView profileName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        vBooking = (RelativeLayout) findViewById(R.id.booking_history_img);
        vBooking.setOnClickListener(this);
        vCategory = (RelativeLayout) findViewById(R.id.category_img);
        vCategory.setOnClickListener(this);
        vChangeCity = (RelativeLayout) findViewById(R.id.change_city_img);
        vChangeCity.setOnClickListener(this);
        vWishList = (RelativeLayout) findViewById(R.id.wishlist_img);
        vWishList.setOnClickListener(this);
        vShare = (RelativeLayout) findViewById(R.id.share_img);
        vShare.setOnClickListener(this);
        vAboutUs = (RelativeLayout) findViewById(R.id.about_us_img);
        vAboutUs.setOnClickListener(this);
        vRateUs = (RelativeLayout) findViewById(R.id.rate_us_img);
        vRateUs.setOnClickListener(this);
        vSignOut = (RelativeLayout) findViewById(R.id.sign_out_img);
        vSignOut.setOnClickListener(this);
        profileName = findViewById(R.id.profile_name);
        profileName.setText(PreferenceStorage.getFullName(this));
        vUserImage = (ImageView) findViewById(R.id.profile_img);
        vUserImage.setOnClickListener(this);
        String url = PreferenceStorage.getUserPicture(this);
        if (((url != null) && !(url.isEmpty()))) {
            Picasso.with(this).load(url).placeholder(R.drawable.ic_default_profile).error(R.drawable.ic_default_profile).into(vUserImage);
        }
        findViewById(R.id.menu_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == vBooking) {
            Intent homeIntent = new Intent(getApplicationContext(), BookingHistoryActivity.class);
            startActivity(homeIntent);
        }
        if (v == vCategory) {
            Intent homeIntent = new Intent(getApplicationContext(), SetUpPreferenceActivity.class);
            startActivity(homeIntent);
        }
        if (v == vChangeCity) {
            Intent homeIntent = new Intent(getApplicationContext(), SelectCityActivity.class);
            startActivity(homeIntent);
        }
        if (v == vWishList) {
//            Intent homeIntent = new Intent(getApplicationContext(), BookingActivity.class);
//            startActivity(homeIntent);
        }
        if (v == vShare) {
            Intent i = new Intent(android.content.Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share");
            i.putExtra(android.content.Intent.EXTRA_TEXT, "Hey! Get Heyla app and win some exciting rewards. https://goo.gl/JTmdEX");
            startActivity(Intent.createChooser(i, "Share via"));
        }
        if (v == vAboutUs) {
//            Intent homeIntent = new Intent(getApplicationContext(), BookingActivity.class);
//            startActivity(homeIntent);
        }
        if (v == vRateUs) {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.palprotech.heylaapp&hl=en");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        if (v == vSignOut) {
            doLogout();
        }
        if (v == vUserImage) {
            Intent homeIntent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(homeIntent);
        }
    }

    public void doLogout() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().clear().commit();
//        TwitterUtil.getInstance().resetTwitterRequestToken();

        Intent homeIntent = new Intent(this, SplashScreenActivity.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
        this.finish();
    }
}
