package com.palprotech.heylaapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.app.AppController;
import com.palprotech.heylaapp.bean.support.EventCheckInHistory;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

/**
 * Created by Narendar on 13/01/18.
 */

public class EventCheckInHistoryListAdapter extends BaseAdapter {

    private static final String TAG = EventCheckInHistoryListAdapter.class.getName();
    private final Transformation transformation;
    private Context context;
    private ArrayList<EventCheckInHistory> eventCheckInHistories;
    private boolean mSearching = false;
    private boolean mAnimateSearch = false;
    private ArrayList<Integer> mValidSearchIndices = new ArrayList<Integer>();
    private ImageLoader imageLoader = AppController.getInstance().getUniversalImageLoader();

    public EventCheckInHistoryListAdapter(Context context, ArrayList<EventCheckInHistory> eventCheckInHistories) {
        this.context = context;
        this.eventCheckInHistories = eventCheckInHistories;

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
            return eventCheckInHistories.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (mSearching) {
            return eventCheckInHistories.get(mValidSearchIndices.get(position));
        } else {
            return eventCheckInHistories.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EventCheckInHistoryListAdapter.ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.event_share_history_item, parent, false);

            holder = new EventCheckInHistoryListAdapter.ViewHolder();
            holder.eventName = (TextView) convertView.findViewById(R.id.share_event_name);
            holder.eventLocation = (TextView) convertView.findViewById(R.id.share_event_location);
            convertView.setTag(holder);
        } else {
            holder = (EventCheckInHistoryListAdapter.ViewHolder) convertView.getTag();
        }

        EventCheckInHistory eventShareHistory = eventCheckInHistories.get(position);

        holder.eventName.setText(eventCheckInHistories.get(position).getEventName());
        holder.eventName.setText(eventCheckInHistories.get(position).getEventVenue());

        return convertView;
    }

    public class ViewHolder {
        public TextView eventName;
        public TextView eventLocation;
    }
}
