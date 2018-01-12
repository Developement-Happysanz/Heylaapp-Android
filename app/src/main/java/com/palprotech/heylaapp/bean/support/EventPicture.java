package com.palprotech.heylaapp.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Admin on 12-01-2018.
 */

public class EventPicture implements Serializable {

    @SerializedName("gallery_id")
    @Expose
    private String galleryId;

    @SerializedName("event_id")
    @Expose
    private String eventId;

    @SerializedName("event_banner")
    @Expose
    private String eventBanner;

    /**
     * @return The galleryId
     */
    public String getGalleryId() {
        return galleryId;
    }

    /**
     * @param galleryId The galleryId
     */
    public void setGalleryId(String galleryId) {
        this.galleryId = galleryId;
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
     * @return The eventBanner
     */
    public String getEventBanner() {
        return eventBanner;
    }

    /**
     * @param eventBanner The eventBanner
     */
    public void setEventBanner(String eventBanner) {
        this.eventBanner = eventBanner;
    }
}
