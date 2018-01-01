package com.palprotech.heylaapp.bean.support;

/**
 * Created by Narendar on 04/11/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Event implements Serializable {

    @SerializedName("event_id")
    @Expose
    private String id;

    @SerializedName("popularity")
    @Expose
    private String popularity;

    @SerializedName("category_id")
    @Expose
    private String CategoryId;

    @SerializedName("event_name")
    @Expose
    private String eventName;

    @SerializedName("event_venue")
    @Expose
    private String eventVenue;

    @SerializedName("event_address")
    @Expose
    private String eventAddress;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("start_date")
    @Expose
    private String startDate;

    @SerializedName("end_date")
    @Expose
    private String endDate;

    @SerializedName("start_time")
    @Expose
    private String startTime;

    @SerializedName("end_time")
    @Expose
    private String endTime;

    @SerializedName("event_banner")
    @Expose
    private String eventBanner;

    @SerializedName("event_latitude")
    @Expose
    private String eventLatitude;

    @SerializedName("event_longitude")
    @Expose
    private String eventLongitude;

    @SerializedName("event_country")
    @Expose
    private String eventCountry;

    @SerializedName("event_city")
    @Expose
    private String eventCity;

    @SerializedName("primary_contact_no")
    @Expose
    private String primaryContactNo;

    @SerializedName("secondary_contact_no")
    @Expose
    private String secondaryContactNo;

    @SerializedName("contact_person")
    @Expose
    private String contactPerson;

    @SerializedName("contact_email")
    @Expose
    private String contactEmail;

    @SerializedName("event_type")
    @Expose
    private String eventType;

    @SerializedName("adv_status")
    @Expose
    private String advStatus;

    @SerializedName("booking_status")
    @Expose
    private String bookingStatus;

    @SerializedName("hotspot_status")
    @Expose
    private String hotspotStatus;

    @SerializedName("event_colour_scheme")
    @Expose
    private String eventColourScheme;

    @SerializedName("event_status")
    @Expose
    private String eventStatus;

    @SerializedName("created_by")
    @Expose
    private String createdBy;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("updated_by")
    @Expose
    private String updatedBy;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;


    public String getContactMail() {
        return contactEmail;
    }

    public void setContactMail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getEventBookingStatus() {
        return bookingStatus;
    }

    public void setEventBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getIsAd() {
        return advStatus;
    }

    public void setIsAd(String advStatus) {
        this.advStatus = advStatus;
    }

    public String getEventLatitude() {
        return eventLatitude;
    }

    public void setEventLatitude(String eventLatitude) {
        this.eventLatitude = eventLatitude;
    }

    public String getEventLongitude() {
        return eventLongitude;
    }

    public void setEventLongitude(String eventLongitude) {
        this.eventLongitude = eventLongitude;
    }

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
     * @return The popularity
     */
    public String getPopularity() {
        return popularity;
    }

    /**
     * @param popularity The popularity
     */
    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    /**
     * @return The eventCategoryId
     */
    public String getEventCategoryId() {
        return CategoryId;
    }

    /**
     * @param CategoryId The CategoryId
     */
    public void setEventCategoryId(String CategoryId) {
        this.CategoryId = CategoryId;
    }

    /**
     * @return The eventName
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * @param eventName The event_name
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * @return The eventVenue
     */
    public String getEventVenue() {
        return eventVenue;
    }

    /**
     * @param eventVenue The event_venue
     */
    public void setEventVenue(String eventVenue) {
        this.eventVenue = eventVenue;
    }

    /**
     * @return The eventVenue
     */
    public String getEventAddress() {
        return eventAddress;
    }

    /**
     * @param eventAddress The event_venue
     */
    public void setEventAddress(String eventAddress) {
        this.eventAddress = eventAddress;
    }

    /**
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return The startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate The start_date
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * @return The endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * @param endDate The end_date
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * @return The startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * @param startTime The startTime
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * @return The endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * @param endTime The endTime
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * @return The primaryContactNo
     */
    public String getPrimaryContactNo() {
        return primaryContactNo;
    }

    /**
     * @param primaryContactNo The primaryContactNo
     */
    public void setPrimaryContactNo(String primaryContactNo) {
        this.primaryContactNo = primaryContactNo;
    }

    /**
     * @return The secondaryContactNo
     */
    public String getSecondaryContactNo() {
        return secondaryContactNo;
    }

    /**
     * @param secondaryContactNo The secondaryContactNo
     */
    public void setSecondaryContactNo(String secondaryContactNo) {
        this.secondaryContactNo = secondaryContactNo;
    }

    /**
     * @return The contact
     */
    public String getContactPerson() {
        return contactPerson;
    }

    /**
     * @param contactPerson The contactPerson
     */
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    /**
     * @return The eventCountry
     */
    public String getEventCountry() {
        return eventCountry;
    }

    /**
     * @param eventCountry The eventCountry
     */
    public void setEventCountry(String eventCountry) {
        this.eventCountry = eventCountry;
    }

    /**
     * @return The eventCity
     */
    public String getEventCity() {
        return eventCity;
    }

    /**
     * @param eventCity The eventCity
     */
    public void setEventCity(String eventCity) {
        this.eventCity = eventCity;
    }


    /**
     * @return The eventType
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * @param eventType The eventType
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    /**
     * @return The hotspotStatus
     */
    public String getHotspotStatus() {

        return hotspotStatus;
    }

    /**
     * @param hotspotStatus The hotspotStatus
     */
    public void setBookingStatus(String hotspotStatus) {
        this.hotspotStatus = hotspotStatus;
    }

    /**
     * @return The eventColourScheme
     */
    public String getEventColourScheme() {

        return eventColourScheme;
    }

    /**
     * @param eventColourScheme The eventColourScheme
     */
    public void setEventColourScheme(String eventColourScheme) {
        this.eventColourScheme = eventColourScheme;
    }

    /**
     * @return The eventStatus
     */
    public String getEventStatus() {

        return eventStatus;
    }

    /**
     * @param eventStatus The eventStatus
     */
    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    /**
     * @return The createdBy
     */
    public String getCreatedBy() {

        return createdBy;
    }

    /**
     * @param createdBy The createdBy
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return The createdAt
     */
    public String getCreatedAt() {

        return createdAt;
    }

    /**
     * @param createdAt The createdAt
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return The updatedBy
     */
    public String getUpdatedBy() {

        return updatedBy;
    }

    /**
     * @param updatedBy The updatedBy
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * @return The updatedAt
     */
    public String getUpdatedAt() {

        return updatedAt;
    }

    /**
     * @param updatedAt The updatedAt
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * @return The eventBanner
     */
    public String getEventBanner() {
        return eventBanner;
    }

    /**
     * @param eventBanner The event_banner
     */
    public void setEventBanner(String eventBanner) {
        this.eventBanner = eventBanner;
    }

}
