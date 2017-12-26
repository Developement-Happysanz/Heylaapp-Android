package com.palprotech.heylaapp.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Admin on 26-12-2017.
 */

public class BookingHistoryList {

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("Bookinghistory")
    @Expose
    private ArrayList<BookingHistory> bookingHistories = new ArrayList<BookingHistory>();

    /**
     * @return The count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count The count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return The events
     */
    public ArrayList<BookingHistory> getBookingHistories() {
        return bookingHistories;
    }

    /**
     * @param bookingHistories The bookingHistories
     */
    public void setEvents(ArrayList<BookingHistory> bookingHistories) {
        this.bookingHistories = bookingHistories;
    }
}
