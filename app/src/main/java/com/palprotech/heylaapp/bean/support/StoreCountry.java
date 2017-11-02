package com.palprotech.heylaapp.bean.support;

/**
 * Created by Admin on 01-11-2017.
 */

public class StoreCountry {

    private String countryId;
    private String countryName;

    public StoreCountry(String countryId, String countryName) {
        this.countryId = countryId;
        this.countryName = countryName;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return countryName;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof StoreCountry){
            StoreCountry c = (StoreCountry )obj;
            if(c.getCountryName().equals(countryName) && c.getCountryId()==countryId ) return true;
        }

        return false;
    }
}
