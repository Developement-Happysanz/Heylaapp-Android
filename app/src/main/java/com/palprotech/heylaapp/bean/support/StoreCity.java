package com.palprotech.heylaapp.bean.support;

/**
 * Created by Admin on 02-11-2017.
 */

public class StoreCity {

    private String cityId;
    private String cityName;

    public StoreCity(String cityId, String cityName) {
        this.cityId = cityId;
        this.cityName = cityName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String stateId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return cityName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StoreCity) {
            StoreCity c = (StoreCity) obj;
            if (c.getCityName().equals(cityName) && c.getCityId() == cityId) return true;
        }

        return false;
    }
}
