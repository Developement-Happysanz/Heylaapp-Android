package com.palprotech.heylaapp.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Narendar on 13/01/18.
 */

public class EventCheckInHistory implements Serializable {

    @SerializedName("user_id")
    @Expose
    private String userId;

    @SerializedName("rule_id")
    @Expose
    private String ruleId;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("event_name")
    @Expose
    private String eventName;

    @SerializedName("event_id")
    @Expose
    private String eventId;

    @SerializedName("event_venue")
    @Expose
    private String eventVenue;

    /**
     * @return The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId The userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }


    /**
     * @return The ruleId
     */
    public String getRuleId() {
        return ruleId;
    }

    /**
     * @param ruleId The ruleId
     */
    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    /**
     * @return The date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date The date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return The eventId
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * @param eventId The eventId
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * @return The eventName
     */
    public String getEventName() { return eventName; }

    /**
     * @param eventName The eventName
     */
    public void setEventName(String eventName) { this.eventName = eventName; }

    /**
     * @return The eventVenue
     */
    public String getEventVenue() {
        return eventVenue;
    }

    /**
     * @param eventVenue The eventVenue
     */
    public void setEventVenue(String eventVenue) {
        this.eventVenue = eventVenue;
    }

}
