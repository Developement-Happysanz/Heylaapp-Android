package com.palprotech.heylaapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.app.AppController;
import com.palprotech.heylaapp.bean.support.Attendees;
import com.palprotech.heylaapp.bean.support.BookingHistory;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

/**
 * Created by Admin on 27-12-2017.
 */

public class AttendeesListAdapter extends BaseAdapter {

    private static final String TAG = AttendeesListAdapter.class.getName();
    private final Transformation transformation;
    private Context context;
    private ArrayList<Attendees> attendees;
    private boolean mSearching = false;
    private boolean mAnimateSearch = false;
    private ArrayList<Integer> mValidSearchIndices = new ArrayList<Integer>();
    private ImageLoader imageLoader = AppController.getInstance().getUniversalImageLoader();

    public AttendeesListAdapter(Context context, ArrayList<Attendees> attendees) {
        this.context = context;
        this.attendees = attendees;

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
            return attendees.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (mSearching) {
            return attendees.get(mValidSearchIndices.get(position));
        } else {
            return attendees.get(position);
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
            convertView = inflater.inflate(R.layout.attendees_list_item, parent, false);

            holder = new ViewHolder();
            holder.txtAttendeesName = convertView.findViewById(R.id.txt_attendees_name);
            holder.txtAttendeesEmailId = convertView.findViewById(R.id.txt_attendees_email);
            holder.txtAttendeesMobile = convertView.findViewById(R.id.txt_attendees_mobile);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Attendees attendee = attendees.get(position);

        holder.txtAttendeesName.setText(attendees.get(position).getName());
        holder.txtAttendeesEmailId.setText(attendees.get(position).getEmailId());
        holder.txtAttendeesMobile.setText(attendees.get(position).getMobileNo());

        return convertView;
    }

    public void startSearch(String eventName) {
        mSearching = true;
        mAnimateSearch = false;
        Log.d("BookingHistoryList", "serach for event" + eventName);
        mValidSearchIndices.clear();
        for (int i = 0; i < attendees.size(); i++) {
            String eventname = attendees.get(i).getName();
            if ((eventname != null) && !(eventname.isEmpty())) {
                if (eventname.toLowerCase().contains(eventName.toLowerCase())) {
                    mValidSearchIndices.add(i);
                }

            }

        }
        Log.d("Event List Adapter", "notify" + mValidSearchIndices.size());
        //notifyDataSetChanged();

    }

    public void exitSearch() {
        mSearching = false;
        mValidSearchIndices.clear();
        mAnimateSearch = false;
        // notifyDataSetChanged();
    }

    public void clearSearchFlag() {
        mSearching = false;
    }

    public class ViewHolder {
        public TextView txtAttendeesName, txtAttendeesEmailId, txtAttendeesMobile;
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
