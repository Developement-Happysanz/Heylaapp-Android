package com.palprotech.heylaapp.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Nandha on 11-12-2016.
 */

public class BookPlan implements Serializable {

    @SerializedName("plan_name")
    @Expose
    private String plan_name;

    @SerializedName("seat_rate")
    @Expose
    private String seat_rate;

    @SerializedName("event_id")
    @Expose
    private String event_id;

    @SerializedName("plan_id")
    @Expose
    private String plan_id;

    @SerializedName("plan_time_id")
    @Expose
    private String plan_time_id;

    @SerializedName("show_date")
    @Expose
    private String show_date;

    @SerializedName("show_time")
    @Expose
    private String show_time;

    @SerializedName("seat_available")
    @Expose
    private String seat_available;

    public String getPlanName() {
        return plan_name;
    }

    public void setPlanName(String plan_name) {
        this.plan_name = plan_name;
    }

    public String getSeatRate() {
        return seat_rate;
    }

    public void setSeatRate(String seat_rate) {
        this.seat_rate = seat_rate;
    }

    public String getEventId() {
        return event_id;
    }

    public void setEventID(String event_id) {
        this.event_id = event_id;
    }

    public String getPlanId() {
        return plan_id;
    }

    public void setPlanId(String plan_id) {
        this.plan_id = plan_id;
    }

    public String getPlanTimeId() {
        return plan_time_id;
    }

    public void setPlanTimeId(String plan_time_id) {
        this.plan_time_id = plan_time_id;
    }


    public String getShowDate() {
        return show_date;
    }

    public void setShowDate(String show_date) {
        this.show_date = show_date;
    }

    public String getShowTime() {
        return show_time;
    }

    public void setShowTime(String show_time) {
        this.show_time = show_time;
    }

    public String getSeatAvailable() {
        return seat_available;
    }

    public void setSeatAvailable(String seat_available) {
        this.seat_available = seat_available;
    }
}
