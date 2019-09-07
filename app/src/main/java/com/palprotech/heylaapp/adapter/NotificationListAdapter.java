package com.palprotech.heylaapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.app.AppController;
import com.palprotech.heylaapp.bean.support.Event;
import com.palprotech.heylaapp.bean.support.Notification;
import com.palprotech.heylaapp.helper.HeylaAppHelper;
import com.palprotech.heylaapp.utils.HeylaAppValidator;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NotificationListAdapter extends BaseAdapter {

    private static final String TAG = NotificationListAdapter.class.getName();
    private final Transformation transformation;
    private Context context;
    private ArrayList<Notification> notificationArrayList;
    String className;
    private boolean mSearching = false;
    private boolean mAnimateSearch = false;
    private ArrayList<Integer> mValidSearchIndices = new ArrayList<Integer>();
    private ImageLoader imageLoader = AppController.getInstance().getUniversalImageLoader();

    public NotificationListAdapter(Context context, ArrayList<Notification> notifications) {
        this.context = context;
        this.notificationArrayList = notifications;
        this.className = className;

        transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(0)
                .oval(false)

                .build();
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

            return notificationArrayList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (mSearching) {
            return notificationArrayList.get(mValidSearchIndices.get(position));
        } else {
            return notificationArrayList.get(position);
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
            convertView = inflater.inflate(R.layout.notification_list_item, parent, false);

            holder = new ViewHolder();
            holder.txtEventName = (TextView) convertView.findViewById(R.id.txt_event_name);
            holder.txtDate = (TextView) convertView.findViewById(R.id.txt_event_date);
            holder.imageView = (ImageView) convertView.findViewById(R.id.img_logo);
            holder.txtEventDetail = convertView.findViewById(R.id.txt_event_detail);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mSearching) {

            position = mValidSearchIndices.get(position);

        } else {
            Log.d("Event List Adapter", "getview pos called" + position);
        }

        holder.txtEventName.setText(notificationArrayList.get(position).getTemplate_name());

        if (HeylaAppValidator.checkNullString(notificationArrayList.get(position).getTemplate_pic())) {
            Picasso.get().load(notificationArrayList.get(position).getTemplate_pic()).fit().transform(this.transformation).placeholder(R.drawable.heyla_logo_transparent).error(R.drawable.heyla_logo_transparent).into(holder.imageView);
        } else {
            holder.imageView.setVisibility(View.GONE);
        }
        String start = HeylaAppHelper.getDate(notificationArrayList.get(position).getCreated_at());

        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
            Date date = (Date) formatter.parse(start);
            SimpleDateFormat event_date = new SimpleDateFormat("dd MMM yyyy");
            String date_name = event_date.format(date.getTime());
            if ((start != null) ) {
                holder.txtDate.setText(date_name);
            } else {
                holder.txtDate.setText("N/A");
            }
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        holder.txtEventDetail.setText(notificationArrayList.get(position).getTemplate_content());

        return convertView;
    }

    public void startSearch(String eventName) {
        mSearching = true;
        mAnimateSearch = false;
        Log.d("EventListAdapter", "serach for event" + eventName);
        mValidSearchIndices.clear();
        for (int i = 0; i < notificationArrayList.size(); i++) {
            String eventname = notificationArrayList.get(i).getTemplate_name();
            if ((eventname != null) && !(eventname.isEmpty())) {
                if (eventname.toLowerCase().contains(eventName.toLowerCase())) {
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
        // notifyDataSetChanged();
    }

    public void clearSearchFlag() {
        mSearching = false;
    }

    public class ViewHolder {
        public TextView txtEventName, txtDate, txtEventDetail;
        public ImageView imageView;
        public boolean status;
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
