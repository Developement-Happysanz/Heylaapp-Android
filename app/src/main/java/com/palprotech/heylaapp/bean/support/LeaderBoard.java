package com.palprotech.heylaapp.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Narendar on 09/12/17.
 */

public class LeaderBoard {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("login_count")
    @Expose
    private String loginCount;
    @SerializedName("login_points")
    @Expose
    private String loginPoints;
    @SerializedName("sharing_count")
    @Expose
    private String sharingCount;
    @SerializedName("sharing_points")
    @Expose
    private String sharingPoints;
    @SerializedName("checkin_count")
    @Expose
    private String checkinCount;
    @SerializedName("checkin_points")
    @Expose
    private String checkinPoints;
    @SerializedName("booking_count")
    @Expose
    private String bookingCount;
    @SerializedName("booking_points")
    @Expose
    private String bookingPoints;
    @SerializedName("review_count")
    @Expose
    private String reviewCount;
    @SerializedName("review_points")
    @Expose
    private String reviewPoints;
    @SerializedName("total_points")
    @Expose
    private String totalPoints;

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
     * @return The loginCount
     */
    public String getLoginCount() {
        return loginCount;
    }

    /**
     * @param loginCount The loginCount
     */
    public void setLoginCount(String loginCount) {
        this.loginCount = loginCount;
    }

    /**
     * @return The loginPoints
     */
    public String getLoginPoints() {
        return loginPoints;
    }

    /**
     * @param loginPoints The loginPoints
     */
    public void setLoginPoints(String loginPoints) {
        this.loginPoints = loginPoints;
    }

    /**
     * @return The sharingCount
     */
    public String getSharingCount() {
        return sharingCount;
    }

    /**
     * @param sharingCount The sharingCount
     */
    public void setSharingCount(String sharingCount) {
        this.sharingCount = sharingCount;
    }

    /**
     * @return The sharingPoints
     */
    public String getSharingPoints() {
        return sharingPoints;
    }

    /**
     * @param sharingPoints The sharingPoints
     */
    public void setSharingPoints(String sharingPoints) {
        this.sharingPoints = sharingPoints;
    }

    /**
     * @return The checkinCount
     */
    public String getCheckinCount() {
        return checkinCount;
    }

    /**
     * @param checkinCount The checkinCount
     */
    public void setCheckinCount(String checkinCount) {
        this.checkinCount = checkinCount;
    }

    /**
     * @return The checkinPoints
     */
    public String getCheckinPoints() {
        return checkinPoints;
    }

    /**
     * @param checkinPoints The checkinPoints
     */
    public void setCheckinPoints(String checkinPoints) {
        this.checkinPoints = checkinPoints;
    }

    /**
     * @return The bookingCount
     */
    public String getBookingCount() {
        return bookingCount;
    }

    /**
     * @param bookingCount The bookingCount
     */
    public void setBookingCount(String bookingCount) {
        this.bookingCount = bookingCount;
    }

    /**
     * @return The bookingPoints
     */
    public String getBookingPoints() {
        return bookingPoints;
    }

    /**
     * @param bookingPoints The bookingPoints
     */
    public void setBookingPoints(String bookingPoints) {
        this.bookingPoints = bookingPoints;
    }

    /**
     * @return The reviewCount
     */
    public String getReviewCount() {
        return reviewCount;
    }

    /**
     * @param reviewCount The reviewCount
     */
    public void setReviewCount(String reviewCount) {
        this.reviewCount = reviewCount;
    }

    /**
     * @return The reviewPoints
     */
    public String getReviewPoints() {
        return reviewPoints;
    }

    /**
     * @param reviewPoints The reviewPoints
     */
    public void setReviewPoints(String reviewPoints) {
        this.reviewPoints = reviewPoints;
    }

    /**
     * @return The totalPoints
     */
    public String getTotalPoints() {
        return totalPoints;
    }

    /**
     * @param totalPoints The totalPoints
     */
    public void setTotalPoints(String totalPoints) {
        this.totalPoints = totalPoints;
    }
}


