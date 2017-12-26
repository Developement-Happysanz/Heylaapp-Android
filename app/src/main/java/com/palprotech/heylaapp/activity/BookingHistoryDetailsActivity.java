package com.palprotech.heylaapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.bean.support.BookingHistory;
import com.palprotech.heylaapp.bean.support.Event;
import com.palprotech.heylaapp.helper.HeylaAppHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Admin on 26-12-2017.
 */

public class BookingHistoryDetailsActivity extends AppCompatActivity {

    private BookingHistory bookingHistory;
    private TextView txtEventName, txtEventDate, txtEventTime, txtEventAddress, txtEventAttendees, txtEventTicktClass;
    static String[] suffixes =
            //    0     1     2     3     4     5     6     7     8     9
            {"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                    //    10    11    12    13    14    15    16    17    18    19
                    "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
                    //    20    21    22    23    24    25    26    27    28    29
                    "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                    //    30    31
                    "th", "st"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history_details);
        bookingHistory = (BookingHistory) getIntent().getSerializableExtra("bookingObj");
        loadUI();
    }

    private void loadUI() {
        txtEventName = findViewById(R.id.txt_event_name);
        txtEventDate = findViewById(R.id.txt_event_booked_date);
        txtEventTime = findViewById(R.id.txt_event_booked_time);
        txtEventAddress = findViewById(R.id.txt_event_location);
        txtEventAttendees = findViewById(R.id.txt_event_attendees_count);
        txtEventTicktClass = findViewById(R.id.txt_event_booking_plan);

        txtEventName.setText(bookingHistory.getEventName());

        String bookingDate = HeylaAppHelper.getDate(bookingHistory.getBookingDate());

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
                txtEventDate.setText(finalDay + " " + month_name + " " + dayStr + " " + year_name);
            } else {
                txtEventDate.setText("N/A");
            }

        } catch (final ParseException e) {
            e.printStackTrace();
        }

        txtEventAttendees.setText(bookingHistory.getNumberOfSeats());
        txtEventTicktClass.setText(bookingHistory.getPlanName() + " - " + bookingHistory.getNumberOfSeats() + " tickets");

    }
}
