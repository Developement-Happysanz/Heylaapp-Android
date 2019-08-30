package com.palprotech.heylaapp.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NotificationList {

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("Notification")
    @Expose
    private ArrayList<Notification> data = new ArrayList<Notification>();

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
    public ArrayList<Notification> getData() {
        return data;
    }

    /**
     * @param data The EventShareHistory
     */
    public void setEventShareHistory(ArrayList<Notification> data) {
        this.data = data;
    }
}

