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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.app.AppController;
import com.palprotech.heylaapp.bean.support.Event;
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

/**
 * Created by zahid.r on 10/30/2015.
 */
public class EventsListAdapter extends BaseAdapter {

    private static final String TAG = EventsListAdapter.class.getName();
    private final Transformation transformation;
    private Context context;
    private ArrayList<Event> events;
    String className;
    private boolean mSearching = false;
    private boolean mAnimateSearch = false;
    private ArrayList<Integer> mValidSearchIndices = new ArrayList<Integer>();
    private ImageLoader imageLoader = AppController.getInstance().getUniversalImageLoader();

    public EventsListAdapter(Context context, ArrayList<Event> events, String className) {
        this.context = context;
        this.events = events;
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

            return events.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (mSearching) {
            return events.get(mValidSearchIndices.get(position));
        } else {
            return events.get(position);
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
            convertView = inflater.inflate(R.layout.event_list_item, parent, false);

            holder = new ViewHolder();
            holder.txtEventName = (TextView) convertView.findViewById(R.id.txt_event_name);
            holder.txtDate = (TextView) convertView.findViewById(R.id.txt_event_date);
            holder.txtEndDate = (TextView) convertView.findViewById(R.id.txt_event_end_date);
            holder.imageView = (ImageView) convertView.findViewById(R.id.img_logo);
            holder.paidBtn = (Button) convertView.findViewById(R.id.event_paid_btn);
            holder.txtPrice = convertView.findViewById(R.id.txt_event_price);
            holder.txtPromo = convertView.findViewById(R.id.ad_name);
            holder.norEvent = convertView.findViewById(R.id.nor_event);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mSearching) {

            position = mValidSearchIndices.get(position);

        } else {
            Log.d("Event List Adapter", "getview pos called" + position);
        }

        Event event = events.get(position);

        holder.txtEventName.setText(events.get(position).getEventName());

        String[] aux = events.get(position).getEventVenue().toString().split(",\\s*");
        String result = "";
        if (aux.length > 2) {
            result = aux[aux.length - 2];
        } else {
            result = aux[aux.length - 1];
        }

        if(events.get(position).getIsAd().equalsIgnoreCase("Y")){
            if(events.get(position).getAdvertisement().equalsIgnoreCase("y")){
                holder.txtPromo.setVisibility(View.VISIBLE);
                holder.norEvent.setVisibility(View.GONE);
            } else {
                holder.txtPromo.setVisibility(View.GONE);
                holder.norEvent.setVisibility(View.VISIBLE);
            }

        } else {
            holder.txtPromo.setVisibility(View.GONE);
            holder.norEvent.setVisibility(View.VISIBLE);
        }

        String paidBtnVal = event.getEventType();
        if (paidBtnVal != null) {
            holder.txtPrice.setText(paidBtnVal);
            if (paidBtnVal.equalsIgnoreCase("invite")) {
                holder.paidBtn.setTextColor(context.getResources().getColor(R.color.white)); //Blue
                holder.txtPrice.setBackgroundColor(ContextCompat.getColor(context, R.color.Paid));
            } else if (paidBtnVal.equalsIgnoreCase("free")) {
                holder.paidBtn.setTextColor(context.getResources().getColor(R.color.white)); //Green
                holder.txtPrice.setText(paidBtnVal);
                holder.txtPrice.setBackgroundColor(ContextCompat.getColor(context, R.color.Free));
            } else if (paidBtnVal.equalsIgnoreCase("paid")) {
                holder.paidBtn.setTextColor(context.getResources().getColor(R.color.white)); //rounder_button
                holder.txtPrice.setBackgroundColor(ContextCompat.getColor(context, R.color.Paid));
            }
        }

        if (HeylaAppValidator.checkNullString(events.get(position).getEventBanner())) {
            Picasso.with(this.context).load(events.get(position).getEventBanner()).fit().transform(this.transformation).placeholder(R.drawable.heyla_logo_transparent).error(R.drawable.heyla_logo_transparent).into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.heyla_logo_transparent);
        }
        String start = HeylaAppHelper.getDate(events.get(position).getStartDate());
        String end = HeylaAppHelper.getDate(events.get(position).getEndDate());

        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
            Date date = (Date) formatter.parse(start);
            Date date1 = (Date) formatter.parse(end);
            SimpleDateFormat event_date = new SimpleDateFormat("dd MMM yyyy");
            String date_name = event_date.format(date.getTime());
            String date_end_name = event_date.format(date1.getTime());
            if ((start != null) && (end != null)) {
                if (events.get(position).getHotspotStatus().equalsIgnoreCase("N")) {
                    holder.txtDate.setTextColor(Color.parseColor("#676767"));
                    holder.txtDate.setText(date_name + " - ");
                    holder.txtEndDate.setTextColor(Color.parseColor("#676767"));
                    holder.txtEndDate.setText(date_end_name);
                } else {
//                    holder.txtDate.setVisibility(View.GONE);
                    holder.txtDate.setText(events.get(position).getEventVenue());
                    holder.txtDate.setTextColor(Color.parseColor("#676767"));
                    holder.txtEndDate.setVisibility(View.GONE);
                }
            } else {
                holder.txtDate.setText("N/A");
            }
        } catch (final ParseException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public void startSearch(String eventName) {
        mSearching = true;
        mAnimateSearch = false;
        Log.d("EventListAdapter", "serach for event" + eventName);
        mValidSearchIndices.clear();
        for (int i = 0; i < events.size(); i++) {
            String eventname = events.get(i).getEventName();
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
        public TextView txtEventName, txtDate, txtPrice, txtEndDate, txtPromo;
        public ImageView imageView;
        public Button paidBtn;
        public LinearLayout norEvent;
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
