package com.palprotech.heylaapp.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Admin on 06-11-2017.
 */

public class EventCitiesListAll {

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("Cities")
    @Expose
    private ArrayList<EventCities> data = new ArrayList<EventCities>();

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
     * @return The EventCities
     */
    public ArrayList<EventCities> getEventCities() {
        return data;
    }

    /**
     * @param data The EventCities
     */
    public void setEventCities(ArrayList<EventCities> data) {
        this.data = data;
    }
}
