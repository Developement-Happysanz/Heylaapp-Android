package com.palprotech.heylaapp.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Admin on 12-01-2018.
 */

public class EventPictureList {

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("Eventgallery")
    @Expose
    private ArrayList<EventPicture> data = new ArrayList<EventPicture>();

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
     * @return The EventPicture
     */
    public ArrayList<EventPicture> getEventPicture() {
        return data;
    }

    /**
     * @param data The EventPicture
     */
    public void setEventPicture(ArrayList<EventPicture> data) {
        this.data = data;
    }
}
