package com.palprotech.heylaapp.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Admin on 05-11-2017.
 */

public class EventCities implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("city_name")
    @Expose
    private String city_name;

    @SerializedName("city_latitude")
    @Expose
    private String city_latitude;

    @SerializedName("city_longitude")
    @Expose
    private String city_longitude;

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
     * @return The city_name
     */
    public String getCityName() {
        return city_name;
    }

    /**
     * @param city_name The city_name
     */
    public void setCityName(String city_name) {
        this.city_name = city_name;
    }

    /**
     * @return The city_latitude
     */
    public String getCityLatitude() {
        return city_latitude;
    }

    /**
     * @param city_latitude The city_latitude
     */
    public void setCityLatitude(String city_latitude) {
        this.city_latitude = city_latitude;
    }

    /**
     * @return The city_longitude
     */
    public String getCityLongitude() {
        return city_longitude;
    }

    /**
     * @param city_longitude The city_longitude
     */
    public void setCityLongitude(String city_longitude) {
        this.city_longitude = city_longitude;
    }
}

