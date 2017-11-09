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
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.bean.support.EventCities;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Admin on 05-11-2017.
 */

public class EventCitiesAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<EventCities> eventCities;
    private boolean mSearching = false;
    private boolean mAnimateSearch = false;
    private ArrayList<Integer> mValidSearchIndices = new ArrayList<Integer>();

    public EventCitiesAdapter(Context context, ArrayList<EventCities> eventCities) {
        this.context = context;
        this.eventCities = eventCities;
//        Collections.sort(classStudents, myComparator);
//        transformation = new RoundedTransformationBuilder().cornerRadiusDp(0).oval(false).build();
        mSearching = false;
    }

    @Override
    public int getCount() {
        if (mSearching) {
            if (!mAnimateSearch) {
                mAnimateSearch = true;
            }
            return mValidSearchIndices.size();

        } else {
            return eventCities.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (mSearching) {
            return eventCities.get(mValidSearchIndices.get(position));
        } else {
            return eventCities.get(position);
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
            convertView = inflater.inflate(R.layout.event_cities_list_item, parent, false);

            holder = new ViewHolder();
            holder.txtCityId = (TextView) convertView.findViewById(R.id.txtCityId);
            holder.txtCityName = (TextView) convertView.findViewById(R.id.txtCityName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mSearching) {
            position = mValidSearchIndices.get(position);

        } else {
            Log.d("Event List Adapter", "getview pos called" + position);
        }

        holder.txtCityId.setText(eventCities.get(position).getId());
        holder.txtCityName.setText(eventCities.get(position).getCityName());

        return convertView;
    }

    public void startSearch(String eventName) {
        mSearching = true;
        mAnimateSearch = false;
        Log.d("EventListAdapter", "serach for event" + eventName);
        mValidSearchIndices.clear();
        for (int i = 0; i < eventCities.size(); i++) {
            String eventCity = eventCities.get(i).getCityName();
            if ((eventCity != null) && !(eventCity.isEmpty())) {
                if (eventCity.toLowerCase().contains(eventName.toLowerCase())) {
                    mValidSearchIndices.add(i);
                }
            }
        }
        Log.d("Event List Adapter", "notify" + mValidSearchIndices.size());
    }

    public void exitSearch() {
        mSearching = false;
        mValidSearchIndices.clear();
        mAnimateSearch = false;
    }

    public void clearSearchFlag() {
        mSearching = false;
    }

    public class ViewHolder {
        public TextView txtCityName, txtCityId;
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
