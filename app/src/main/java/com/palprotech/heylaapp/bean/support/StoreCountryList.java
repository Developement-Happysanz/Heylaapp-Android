package com.palprotech.heylaapp.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StoreCountryList {

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("Bookinghistory")
    @Expose
    private ArrayList<StoreCountry> bookingHistories = new ArrayList<StoreCountry>();

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
    public ArrayList<StoreCountry> getBookingHistories() {
        return bookingHistories;
    }

    /**
     * @param bookingHistories The bookingHistories
     */
    public void setEvents(ArrayList<StoreCountry> bookingHistories) {
        this.bookingHistories = bookingHistories;
    }
}
