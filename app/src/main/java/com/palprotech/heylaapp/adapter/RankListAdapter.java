package com.palprotech.heylaapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.bean.support.Rank;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RankListAdapter extends BaseAdapter {

    //    private final Transformation transformation;
    private Context context;
    private ArrayList<Rank> rank;
    private boolean mSearching = false;
    private boolean mAnimateSearch = false;
    private ArrayList<Integer> mValidSearchIndices = new ArrayList<Integer>();

    public RankListAdapter(Context context, ArrayList<Rank> rank) {
        this.context = context;
        this.rank = rank;

//        transformation = new RoundedTransformationBuilder()
//                .cornerRadiusDp(0)
//                .oval(false)
//                .build();
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
            return rank.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (mSearching) {
            return rank.get(mValidSearchIndices.get(position));
        } else {
            return rank.get(position);
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
            convertView = inflater.inflate(R.layout.point_table_list_item, parent, false);

            holder = new ViewHolder();
            holder.txtUserName = (TextView) convertView.findViewById(R.id.txtExamSubject);
            holder.txtUserPic = (ImageView) convertView.findViewById(R.id.user_img);
            holder.txtUserRank = (TextView) convertView.findViewById(R.id.txtSubTotalMark);
            holder.txtUserPoints = (TextView) convertView.findViewById(R.id.txtSubTotalGrade);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mSearching) {
            position = mValidSearchIndices.get(position);
        } else {
            Log.d("Event List Adapter", "getview pos called" + position);
        }

        Rank ranks = rank.get(position);
        if(!ranks.getName().isEmpty()){
            holder.txtUserName.setText(ranks.getName());
        } else {
            holder.txtUserName.setText(ranks.getUserName());
        }
        holder.txtUserRank.setText(""+(position+1));
        holder.txtUserPoints.setText(ranks.getTotal_points());

        String url = ranks.getUser_picture();

        if (((url != null) && !(url.isEmpty()))) {
            Picasso.with(context).load(url).placeholder(R.drawable.ic_default_profile).error(R.drawable.ic_default_profile).into(holder.txtUserPic);
        }
        else {
            String s = "1";
        }

        return convertView;
    }

    public void startSearch(String eventName) {
        mSearching = true;
        mAnimateSearch = false;
        Log.d("EventListAdapter", "serach for event" + eventName);
        mValidSearchIndices.clear();
        for (int i = 0; i < rank.size(); i++) {
            String homeWorkTitle = rank.get(i).getId();
            if ((homeWorkTitle != null) && !(homeWorkTitle.isEmpty())) {
                if (homeWorkTitle.toLowerCase().contains(eventName.toLowerCase())) {
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
        public TextView txtUserName, txtUserPoints, txtUserRank;
        public ImageView txtUserPic;
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
