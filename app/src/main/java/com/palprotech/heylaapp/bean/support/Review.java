package com.palprotech.heylaapp.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Admin on 01-01-2018.
 */

public class Review implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("event_id")
    @Expose
    private String event_id;
    @SerializedName("event_name")
    @Expose
    private String event_name;
    @SerializedName("event_rating")
    @Expose
    private String event_rating;
    @SerializedName("comments")
    @Expose
    private String comments;
    @SerializedName("user_name")
    @Expose
    private String user_name;

    public String getReviewId() {
        return id;
    }

    public void setReviewId(String id) {
        this.id = id;
    }

    public String getEventId() {
        return event_id;
    }

    public void setEventId(String event_id) {
        this.event_id = event_id;
    }

    public String getEventName() {
        return event_name;
    }

    public void setEventName(String event_name) {
        this.event_name = event_name;
    }

    public String getEventRating() {
        return event_rating;
    }

    public void setEventRating(String event_rating) {
        this.event_rating = event_rating;
    }

    public String getEventComments() {
        return comments;
    }

    public void setEventComments(String comments) {
        this.comments = comments;
    }

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String user_name) {
        this.user_name = user_name;
    }
}
