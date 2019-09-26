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
import com.palprotech.heylaapp.bean.support.BookingHistory;
import com.palprotech.heylaapp.helper.HeylaAppHelper;
import com.squareup.picasso.Transformation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static androidx.core.util.Preconditions.checkArgument;

/**
 * Created by Admin on 26-12-2017.
 */

public class BookingHistoryListAdapter extends BaseAdapter {

    private static final String TAG = BookingHistoryListAdapter.class.getName();
    private final Transformation transformation;
    private Context context;
    private ArrayList<BookingHistory> bookingHistories;
    private boolean mSearching = false;
    private boolean mAnimateSearch = false;
    private ArrayList<Integer> mValidSearchIndices = new ArrayList<Integer>();
    private ImageLoader imageLoader = AppController.getInstance().getUniversalImageLoader();

    static String[] suffixes =
            //    0     1     2     3     4     5     6     7     8     9
            {"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                    //    10    11    12    13    14    15    16    17    18    19
                    "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
                    //    20    21    22    23    24    25    26    27    28    29
                    "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                    //    30    31
                    "th", "st"};

    public BookingHistoryListAdapter(Context context, ArrayList<BookingHistory> bookingHistories) {
        this.context = context;
        this.bookingHistories = bookingHistories;

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
            return bookingHistories.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (mSearching) {
            return bookingHistories.get(mValidSearchIndices.get(position));
        } else {
            return bookingHistories.get(position);
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
            convertView = inflater.inflate(R.layout.booking_history_list_item, parent, false);

            holder = new BookingHistoryListAdapter.ViewHolder();
            holder.txtEventName = convertView.findViewById(R.id.txt_event_name);
            holder.txtEventBookedDate1 = convertView.findViewById(R.id.txt_event_booked_date1);
            holder.txtEventBookedDate2 = convertView.findViewById(R.id.txt_event_booked_date2);
            holder.txtEventBookedDate3 = convertView.findViewById(R.id.txt_event_booked_date3);
            holder.txtEventBookedTime = convertView.findViewById(R.id.txt_event_booked_time);
            holder.txtEventLocation = convertView.findViewById(R.id.txt_event_location);
            holder.txtTicketCount = convertView.findViewById(R.id.ticket_count);

            convertView.setTag(holder);
        } else {
            holder = (BookingHistoryListAdapter.ViewHolder) convertView.getTag();
        }

        BookingHistory bookingHistory = bookingHistories.get(position);

        holder.txtEventName.setText(bookingHistories.get(position).getEventName());

        String bookingDate = HeylaAppHelper.getDate(bookingHistories.get(position).getBookingDate());

        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
            Date date = (Date) formatter.parse(bookingDate);
            SimpleDateFormat month_date = new SimpleDateFormat("MMM");
            String month_name = month_date.format(date.getTime());
            SimpleDateFormat event_date = new SimpleDateFormat("dd");
            String date_name = event_date.format(date.getTime());
            SimpleDateFormat event_year = new SimpleDateFormat("yyyy");
            String year_name = event_year.format(date.getTime());

            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            Date dt1 = format1.parse(bookingDate);
            DateFormat format2 = new SimpleDateFormat("EEEE");
            String finalDay = format2.format(dt1);

            int day = Integer.parseInt(date_name);
            String dayStr = day + suffixes[day];

            if ((bookingDate != null)) {
                holder.txtEventBookedDate1.setText(" " + month_name );
                holder.txtEventBookedDate2.setText(" " + dayStr );
                holder.txtEventBookedDate3.setText(" " + finalDay);
            } else {
                holder.txtEventBookedDate1.setText("N/A");
                holder.txtEventBookedDate2.setText("N/A");
                holder.txtEventBookedDate3.setText("N/A");
            }

            holder.txtEventBookedTime.setText(bookingHistories.get(position).getPlanTime());
            holder.txtEventLocation.setText(bookingHistories.get(position).getEventVenue());
            holder.txtTicketCount.setText(bookingHistories.get(position).getNumberOfSeats());

        } catch (final ParseException e) {
            e.printStackTrace();
        }

        return convertView;
    }


    public void startSearch(String eventName) {
        mSearching = true;
        mAnimateSearch = false;
        Log.d("BookingHistoryList", "serach for event" + eventName);
        mValidSearchIndices.clear();
        for (int i = 0; i < bookingHistories.size(); i++) {
            String eventname = bookingHistories.get(i).getEventName();
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
        public TextView txtEventName, txtEventBookedDate1,txtEventBookedDate2,txtEventBookedDate3, txtEventBookedTime, txtEventLocation, txtTicketCount;
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
