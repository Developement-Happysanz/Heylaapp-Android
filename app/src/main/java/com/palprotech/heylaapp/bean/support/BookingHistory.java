package com.palprotech.heylaapp.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Admin on 26-12-2017.
 */

public class BookingHistory implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("order_id")
    @Expose
    private String order_id;

    @SerializedName("event_id")
    @Expose
    private String event_id;

    @SerializedName("plan_name")
    @Expose
    private String plan_name;

    @SerializedName("show_time")
    @Expose
    private String plan_time;

    @SerializedName("number_of_seats")
    @Expose
    private String number_of_seats;

    @SerializedName("show_date")
    @Expose
    private String booking_date;

    @SerializedName("total_amount")
    @Expose
    private String total_amount;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("category_name")
    @Expose
    private String category_name;

    @SerializedName("event_name")
    @Expose
    private String event_name;

    @SerializedName("event_venue")
    @Expose
    private String event_venue;

    @SerializedName("event_address")
    @Expose
    private String event_address;

    @SerializedName("event_colour_scheme")
    @Expose
    private String event_colour_scheme;

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The order_id
     */
    public String getOrderId() {
        return order_id;
    }

    /**
     * @param order_id The order_id
     */
    public void setOrderId(String order_id) {
        this.order_id = order_id;
    }

    /**
     * @return The event_id
     */
    public String getEventId() {
        return event_id;
    }

    /**
     * @param event_id The event_id
     */
    public void setEventId(String event_id) {
        this.event_id = event_id;
    }

    /**
     * @return The plan_name
     */
    public String getPlanName() {
        return plan_name;
    }

    /**
     * @param plan_name The plan_name
     */
    public void setPlanName(String plan_name) {
        this.plan_name = plan_name;
    }

    /**
     * @return The plan_time
     */
    public String getPlanTime() {
        return plan_time;
    }

    /**
     * @param plan_time The plan_time
     */
    public void setPlanTime(String plan_time) {
        this.plan_time = plan_time;
    }

    /**
     * @return The number_of_seats
     */
    public String getNumberOfSeats() {
        return number_of_seats;
    }

    /**
     * @param number_of_seats The number_of_seats
     */
    public void setNumberOfSeats(String number_of_seats) {
        this.number_of_seats = number_of_seats;
    }

    /**
     * @return The booking_date
     */
    public String getBookingDate() {
        return booking_date;
    }

    /**
     * @param booking_date The booking_date
     */
    public void setBookingDate(String booking_date) {
        this.booking_date = booking_date;
    }

    /**
     * @return The total_amount
     */
    public String getTotalAmount() {
        return total_amount;
    }

    /**
     * @param total_amount The total_amount
     */
    public void setTotalAmount(String total_amount) {
        this.total_amount = total_amount;
    }

    /**
     * @return The created_at
     */
    public String getCreatedAt() {
        return created_at;
    }

    /**
     * @param created_at The created_at
     */
    public void setCreatedAt(String created_at) {
        this.created_at = created_at;
    }

    /**
     * @return The category_name
     */
    public String getCategoryName() {
        return category_name;
    }

    /**
     * @param category_name The category_name
     */
    public void setCategoryName(String category_name) {
        this.category_name = category_name;
    }

    /**
     * @return The event_name
     */
    public String getEventName() {
        return event_name;
    }

    /**
     * @param event_name The event_name
     */
    public void setEventName(String event_name) {
        this.event_name = event_name;
    }

    /**
     * @return The event_venue
     */
    public String getEventVenue() {
        return event_venue;
    }

    /**
     * @param event_venue The event_venue
     */
    public void setEventVenue(String event_venue) {
        this.event_venue = event_venue;
    }

    /**
     * @return The event_address
     */
    public String getEventAddress() {
        return event_address;
    }

    /**
     * @param event_address The event_address
     */
    public void setEventAddress(String event_address) {
        this.event_address = event_address;
    }

    /**
     * @return The event_colour_scheme
     */
    public String getEventColourScheme() {
        return event_colour_scheme;
    }

    /**
     * @param event_colour_scheme The event_colour_scheme
     */
    public void setEventColourScheme(String event_colour_scheme) {
        this.event_colour_scheme = event_colour_scheme;
    }

}
