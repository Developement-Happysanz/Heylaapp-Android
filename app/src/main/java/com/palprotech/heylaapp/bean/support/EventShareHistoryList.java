package com.palprotech.heylaapp.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Narendar on 12/01/18.
 */

public class EventShareHistoryList {

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("Eventgallery")
    @Expose
    private ArrayList<EventShareHistory> data = new ArrayList<EventShareHistory>();

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
     * @return The EventShareHistory
     */
    public ArrayList<EventShareHistory> getEventShareHistory() {
        return data;
    }

    /**
     * @param data The EventShareHistory
     */
    public void setEventShareHistory(ArrayList<EventShareHistory> data) {
        this.data = data;
    }
}
