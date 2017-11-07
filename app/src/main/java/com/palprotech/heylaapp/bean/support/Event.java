//package com.palprotech.heylaapp.bean.support;
//
///**
// * Created by Narendar on 04/11/17.
// */
//
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//
//import java.io.Serializable;
//
//public class Event implements Serializable {
//
//    @SerializedName("id")
//    @Expose
//    private String id;
//
//    @SerializedName("category_id")
//    @Expose
//    private String CategoryId;
//
//    @SerializedName("event_name")
//    @Expose
//    private String eventName;
//
//    @SerializedName("event_venue")
//    @Expose
//    private String eventVenue;
//
//    @SerializedName("event_address")
//    @Expose
//    private String eventAddress;
//
//    @SerializedName("description")
//    @Expose
//    private String description;
//
//    @SerializedName("start_date")
//    @Expose
//    private String startDate;
//
//    @SerializedName("end_date")
//    @Expose
//    private String endDate;
//
//    @SerializedName("start_time")
//    @Expose
//    private String startTime;
//
//    @SerializedName("end_time")
//    @Expose
//    private String endTime;
//
//    @SerializedName("event_banner")
//    @Expose
//    private String eventBanner;
//
//    @SerializedName("event_latitude")
//    @Expose
//    private String eventLatitude;
//
//    @SerializedName("event_longitude")
//    @Expose
//    private String eventLongitude;
//
//    @SerializedName("event_country")
//    @Expose
//    private String eventCountry;
//
//    @SerializedName("event_city")
//    @Expose
//    private String eventCity;
//
//    @SerializedName("primary_contact_no")
//    @Expose
//    private String primaryContactNo;
//
//    @SerializedName("secondary_contact_no")
//    @Expose
//    private String secondaryContactNo;
//
//    @SerializedName("contact_person")
//    @Expose
//    private String contactPerson;
//
//    @SerializedName("contact_email")
//    @Expose
//    private String contactEmail;
//
//    @SerializedName("event_type")
//    @Expose
//    private String eventType;
//
//    @SerializedName("adv_status")
//    @Expose
//    private String advStatus;
//
//    @SerializedName("booking_status")
//    @Expose
//    private String bookingStatus;
//
//    @SerializedName("hotspot_status")
//    @Expose
//    private String hotspotStatus;
//
//    @SerializedName("event_colour_scheme")
//    @Expose
//    private String eventColourScheme;
//
//    @SerializedName("event_status")
//    @Expose
//    private String eventStatus;
//
//    @SerializedName("created_by")
//    @Expose
//    private String createdBy;
//
//    @SerializedName("created_at")
//    @Expose
//    private String createdAt;
//
//    @SerializedName("updated_by")
//    @Expose
//    private String updatedBy;
//
//    @SerializedName("updated_at")
//    @Expose
//    private String updatedAt;
//
//
//
//    public String getContactMail() {
//        return contactEmail;
//    }
//
//    public void setContactMail(String contactEmail) {
//        this.contactEmail = contactEmail;
//    }
//
//    public String getEventBookingStatus() {
//        return bookingStatus;
//    }
//
//    public void setEventBookingStatus(String bookingStatus) {
//        this.bookingStatus = bookingStatus;
//    }
//
//    public String getIsAd() {
//        return advStatus;
//    }
//
//    public void setIsAd(String advStatus) {
//        this.advStatus = advStatus;
//    }
//
//    public String getEventLatitude() {
//        return eventLatitude;
//    }
//
//    public void setEventLatitude(String eventLatitude) {
//        this.eventLatitude = eventLatitude;
//    }
//
//    public String getEventLongitude() {
//        return eventLongitude;
//    }
//
//    public void setEventLongitude(String eventLongitude) {
//        this.eventLongitude = eventLongitude;
//    }
//
//    /**
//     * @return The id
//     */
//    public String getId() {
//        return id;
//    }
//
//    /**
//     * @param id The id
//     */
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    /**
//     * @return The eventCategoryId
//     */
//    public String getEventCategoryId() {
//        return CategoryId;
//    }
//
//    /**
//     * @param CategoryId The CategoryId
//     */
//    public void setEventCategoryId(String CategoryId) {
//        this.CategoryId = CategoryId;
//    }
//
//    /**
//     * @return The eventName
//     */
//    public String getEventName() {
//        return eventName;
//    }
//
//    /**
//     * @param eventName The event_name
//     */
//    public void setEventName(String eventName) {
//        this.eventName = eventName;
//    }
//
//    /**
//     * @return The eventVenue
//     */
//    public String getEventVenue() {
//        return eventVenue;
//    }
//
//    /**
//     * @param eventVenue The event_venue
//     */
//    public void setEventVenue(String eventVenue) {
//        this.eventVenue = eventVenue;
//    }
//
//    /**
//     * @return The eventVenue
//     */
//    public String getEventAddress() {
//        return eventAddress;
//    }
//
//    /**
//     * @param eventAddress The event_venue
//     */
//    public void setEventAddress(String eventAddress) {
//        this.eventAddress = eventAddress;
//    }
//
//    /**
//     * @return The description
//     */
//    public String getDescription() {
//        return description;
//    }
//
//    /**
//     * @param description The description
//     */
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    /**
//     * @return The startDate
//     */
//    public String getStartDate() {
//        return startDate;
//    }
//
//    /**
//     * @param startDate The start_date
//     */
//    public void setStartDate(String startDate) {
//        this.startDate = startDate;
//    }
//
//    /**
//     * @return The endDate
//     */
//    public String getEndDate() {
//        return endDate;
//    }
//
//    /**
//     * @param endDate The end_date
//     */
//    public void setEndDate(String endDate) {
//        this.endDate = endDate;
//    }
//
//    /**
//     * @return The primaryContactNo
//     */
//    public String getPrimaryContactNo() {
//        return primaryContactNo;
//    }
//
//    /**
//     * @param primaryContactNo The primaryContactNo
//     */
//    public void setPrimaryContactNo(String primaryContactNo) {
//        this.primaryContactNo = primaryContactNo;
//    }
//
//    /**
//     * @return The secondaryContactNo
//     */
//    public String getSecondaryContactNo() {
//        return secondaryContactNo;
//    }
//
//    /**
//     * @param secondaryContactNo The secondaryContactNo
//     */
//    public void setSecondaryContactNo(String secondaryContactNo) {
//        this.secondaryContactNo = secondaryContactNo;
//    }
//
//    /**
//     * @return The contact
//     */
//    public String getContactPerson() {
//        return contactPerson;
//    }
//
//    /**
//     * @param contactPerson The contactPerson
//     */
//    public void setContactPerson(String contactPerson) {
//        this.contactPerson = contactPerson;
//    }
//
//    /**
//     * @return The c
//     */
//    public String getCategoryName() {
//        return categoryName;
//    }
//
//    /**
//     * @param categoryName The category_name
//     */
//    public void setCategoryName(String categoryName) {
//        this.categoryName = categoryName;
//    }
//
//    /**
//     * @return The categoryImg
//     */
//    public String getCategoryImg() {
//        return categoryImg;
//    }
//
//    /**
//     * @param categoryImg The category_img
//     */
//    public void setCategoryImg(String categoryImg) {
//        this.categoryImg = categoryImg;
//    }
//
//
//    /**
//     * @return The eventLogo
//     */
//    public String getEventLogo() {
//        return eventLogo;
//    }
//    /**
//     * @param eventLogo The event_logo
//     */
//    public void setEventLogo(String eventLogo) {
//        this.eventLogo = eventLogo;
//    }
//
//    /**
//     * @return The eventLogo_1
//     */
//    public String getEventLogo_1() {
//
//        return eventLogo_1;
//    }
//    /**
//     * @param eventLogo_1 The event_logo_1
//     */
//    public void setEventLogo_1(String eventLogo_1) {
//        this.eventLogo_1 = eventLogo_1;
//    }
//
//    /**
//     * @return The eventLogo_2
//     */
//    public String getEventLogo_2() {
//
//        return eventLogo_2;
//    }
//    /**
//     * @param eventLogo_2 The event_logo_2
//     */
//    public void setEventLogo_2(String eventLogo_2) {
//        this.eventLogo_2 = eventLogo_2;
//    }
//
//    /**
//     * @return The eventLogo_3
//     */
//    public String getEventLogo_3() {
//
//        return eventLogo_3;
//    }
//    /**
//     * @param eventLogo_3 The eventLogo_3
//     */
//    public void setEventLogo_3(String eventLogo_3) {
//        this.eventLogo_3 = eventLogo_3;
//    }
//
//    /**
//     * @return The eventLogo_4
//     */
//    public String getEventLogo_4() {
//
//        return eventLogo_4;
//    }
//    /**
//     * @param eventLogo_4 The eventLogo_4
//     */
//    public void setEventLogo_4(String eventLogo_4) {
//        this.eventLogo_4 = eventLogo_4;
//    }
//
//    /**
//     * @return The eventBanner
//     */
//    public String getEventBanner() {
//        return eventBanner;
//    }
//
//    /**
//     * @param eventBanner The event_banner
//     */
//    public void setEventBanner(String eventBanner) {
//        this.eventBanner = eventBanner;
//    }
//
//}
