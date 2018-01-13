package com.palprotech.heylaapp.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Narendar on 13/01/18.
 */

public class EventCheckInHistoryList {

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("Data")
    @Expose
    private ArrayList<EventCheckInHistory> data = new ArrayList<EventCheckInHistory>();

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
     * @return The EventCheckInHistory
     */
    public ArrayList<EventCheckInHistory> getCheckInHistory() {
        return data;
    }

    /**
     * @param data The EventCheckInHistory
     */
    public void setCheckInHistory(ArrayList<EventCheckInHistory> data) {
        this.data = data;
    }
}
