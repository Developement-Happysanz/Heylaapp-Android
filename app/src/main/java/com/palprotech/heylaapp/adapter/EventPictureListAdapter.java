package com.palprotech.heylaapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.app.AppController;
import com.palprotech.heylaapp.bean.support.EventPicture;
import com.palprotech.heylaapp.utils.HeylaAppValidator;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

/**
 * Created by Admin on 12-01-2018.
 */

public class EventPictureListAdapter extends BaseAdapter {

    private static final String TAG = EventPictureListAdapter.class.getName();
    private final Transformation transformation;
    private Context context;
    private ArrayList<EventPicture> eventPictures;
    private boolean mSearching = false;
    private boolean mAnimateSearch = false;
    private ArrayList<Integer> mValidSearchIndices = new ArrayList<Integer>();
    private ImageLoader imageLoader = AppController.getInstance().getUniversalImageLoader();

    public EventPictureListAdapter(Context context, ArrayList<EventPicture> eventPictures) {
        this.context = context;
        this.eventPictures = eventPictures;

        transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(0)
                .oval(false)
                .build();
        mSearching = false;
    }

    @Override
    public int getCount() {
        if (mSearching) {
            // Log.d("Event List Adapter","Search count"+mValidSearchIndices.size());
            if (!mAnimateSearch) {
                mAnimateSearch = true;
            }
            return mValidSearchIndices.size();

        } else {
            // Log.d(TAG,"Normal count size");
            return eventPictures.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (mSearching) {
            return eventPictures.get(mValidSearchIndices.get(position));
        } else {
            return eventPictures.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.event_picture_list_item, parent, false);

            holder = new ViewHolder();
            holder.txtId = (TextView) convertView.findViewById(R.id.txt_id);
            holder.imageView = (ImageView) convertView.findViewById(R.id.img_logo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        EventPicture eventPicture = eventPictures.get(position);

        holder.txtId.setText(eventPictures.get(position).getGalleryId());

        if (HeylaAppValidator.checkNullString(eventPicture.getEventBanner())) {
            Picasso.get().load(eventPictures.get(position).getEventBanner()).fit().transform(this.transformation).placeholder(R.drawable.heyla_logo_transparent).error(R.drawable.heyla_logo_transparent).into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.heyla_logo_transparent);
        }

        return convertView;
    }

    public class ViewHolder {
        public TextView txtId;
        public ImageView imageView;
    }

    public boolean ismSearching() {
        return mSearching;
    }

    public int getActualEventPos(int selectedSearchpos) {
        if (selectedSearchpos < mValidSearchIndices.size()) {
            return mValidSearchIndices.get(selectedSearchpos);
        } else {
            return 0;
        }
    }

}
