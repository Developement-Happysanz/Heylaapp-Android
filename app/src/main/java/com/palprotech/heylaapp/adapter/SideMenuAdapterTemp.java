package com.palprotech.heylaapp.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.palprotech.heylaapp.bean.support.SideOptionView;

import java.util.ArrayList;

/**
 * Created by PSD on 13-04-17.
 */

public class SideMenuAdapterTemp extends BaseAdapter {
    private ArrayList<String> mOptions = new ArrayList<>();
    private ArrayList<SideOptionView> mOptionViews = new ArrayList<>();

    public SideMenuAdapterTemp(ArrayList<String> options) {
        mOptions = options;
    }

    @Override
    public int getCount() {
        return mOptions.size();
    }

    @Override
    public Object getItem(int position) {
        return mOptions.get(position);
    }

    public void setSelectView(int position, boolean selected) {

        // Looping through the options in the menu
        // Selecting the chosen option
        for (int i = 0; i < mOptionViews.size(); i++) {
            if (i == position) {
                mOptionViews.get(i).setSelected(selected);
            } else {
                mOptionViews.get(i).setSelected(!selected);
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String option = mOptions.get(position);

        // Using the SideOptionView to easily recreate the demo
        final SideOptionView optionView;
        if (convertView == null) {
            optionView = new SideOptionView(parent.getContext());
        } else {
            optionView = (SideOptionView) convertView;
        }

        // Using the SideOptionView's default selectors
        optionView.bind(option, null, null);

        // Adding the views to an array list to handle view selection
        mOptionViews.add(optionView);

        return optionView;
    }
}
