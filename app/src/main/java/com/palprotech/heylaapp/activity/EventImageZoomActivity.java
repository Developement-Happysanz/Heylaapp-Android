package com.palprotech.heylaapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.bean.support.EventPicture;
import com.squareup.picasso.Picasso;

/**
 * Created by Narendar on 05/03/18.
 */

public class EventImageZoomActivity extends AppCompatActivity {
    TouchImageView touchZoom;
    EventPicture eventPicture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_gallery_zoom);
        touchZoom = findViewById(R.id.img_zoom);
        eventPicture = (EventPicture) getIntent().getSerializableExtra("eventObj");
        String url = eventPicture.getEventBanner();
        if (((url != null) && !(url.isEmpty()))) {
            Picasso.get().load(url).error(R.drawable.event_img).into(touchZoom);
        }
    }
}
