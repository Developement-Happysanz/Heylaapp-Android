package com.palprotech.heylaapp.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Admin on 27-12-2017.
 */

public class AttendeesList {

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("Bookingattendees")
    @Expose
    private ArrayList<Attendees> attendees = new ArrayList<Attendees>();

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
     * @return The attendees
     */
    public ArrayList<Attendees> getAttendees() {
        return attendees;
    }

    /**
     * @param attendees The attendees
     */
    public void setEvents(ArrayList<Attendees> attendees) {
        this.attendees = attendees;
    }
}
