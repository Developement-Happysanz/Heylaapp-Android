package com.palprotech.heylaapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.palprotech.heylaapp.R;

public class UserGuideActivity extends AppCompatActivity implements View.OnClickListener {
    Display display;
    Point size;
    Bitmap bmp;
    ImageView guideImg;
    TextView userExplain, skip, userExplainTitle;
    int imgCount = 1;
    Button next,previous;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_guide);

        /* adapt the image to the size of the display */
        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.user_guide_1), size.x, size.y, true);

        /* fill the background ImageView with the resized image */
        guideImg = (ImageView) findViewById(R.id.guide_img);
        guideImg.setImageBitmap(bmp);
        userExplain = (TextView) findViewById(R.id.explain);
        userExplainTitle = (TextView) findViewById(R.id.explain_heading);
        userExplainTitle.setText("Events that you like");
        userExplain.setText("Here we list the events based on your preferences. And you can change your preferences anytime.");
        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(this);
        previous = (Button) findViewById(R.id.previous);
        previous.setOnClickListener(this);
        skip = (TextView) findViewById(R.id.skip);
        skip.setOnClickListener(this);
    }

    private void changeImage(int i) {
        switch (i) {
            case 1:
                bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.user_guide_1), size.x, size.y, true);
                guideImg.setImageBitmap(bmp);
                userExplainTitle.setText("Events that you like");
                userExplain.setText("Events that you likeHere we list the events based on your preferences. \nAnd you can change your preferences anytime.");
                break;
            case 2:
                bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.user_guide_2), size.x, size.y, true);
                guideImg.setImageBitmap(bmp);
                userExplainTitle.setText("Events that are on trend");
                userExplain.setText("Popular events are those viewed by most");
                break;
            case 3:
                bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.user_guide_3), size.x, size.y, true);
                userExplainTitle.setText("Everything around you goes in here!");
                userExplain.setText("All events nearby your chosen location are listed here");
                guideImg.setImageBitmap(bmp);
                break;
            case 4:
                bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.user_guide_4), size.x, size.y, true);
                userExplainTitle.setText("Places that bustle!");
                userExplain.setText("Hotspot lists the most popular and exciting areas in Singapore");
                guideImg.setImageBitmap(bmp);
                break;
            case 5:
                bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.user_guide_5), size.x, size.y, true);
                userExplainTitle.setText("The board of bonus!");
                userExplain.setText("Leaderboard lists Heyla users with the most bonus points earned through various activities");
                guideImg.setImageBitmap(bmp);
                break;
            case 6:
                bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.user_guide_6), size.x, size.y, true);
                userExplainTitle.setText("All you need is beside here");
                userExplain.setText("Side menu boasts some of the app's important features");
                guideImg.setImageBitmap(bmp);
                break;
            case 7:
                bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.user_guide_7), size.x, size.y, true);
                userExplainTitle.setText("No need to scroll down!");
                userExplain.setText("Search for events by their names");
                guideImg.setImageBitmap(bmp);
                break;
            case 8:
                bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.user_guide_8), size.x, size.y, true);
                userExplainTitle.setText("Get what you're exactly looking for!");
                userExplain.setText("Advance Search enables you to narrow your search results");
                guideImg.setImageBitmap(bmp);
                break;
            case 9:
                bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.user_guide_9), size.x, size.y, true);
                userExplainTitle.setText("Events in your radius!");
                userExplain.setText("You can find events within a radius of your choice. \nMap View gives you a broader perspective of your location");
                guideImg.setImageBitmap(bmp);
                break;
            case 10:
                bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.user_guide_10), size.x, size.y, true);
                userExplainTitle.setText("Click it to view it!");
                userExplain.setText("Clicking on an event will give you its details");
                guideImg.setImageBitmap(bmp);
                break;
            default:
                skip.setText("Finish");
                next.setVisibility(View.GONE);
                previous.setVisibility(View.GONE);
                userExplainTitle.setVisibility(View.GONE);
                userExplain.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == next) {
            imgCount++;
            changeImage(imgCount);
        }
        if (v == previous) {
            if (imgCount != 1) {
                imgCount--;
                changeImage(imgCount);
            }
        }
        if (v == skip) {
            finish();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }
}
