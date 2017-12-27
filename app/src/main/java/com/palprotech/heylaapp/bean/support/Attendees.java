package com.palprotech.heylaapp.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Admin on 27-12-2017.
 */

public class Attendees implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("email_id")
    @Expose
    private String email_id;

    @SerializedName("mobile_no")
    @Expose
    private String mobile_no;

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
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The email_id
     */
    public String getEmailId() {
        return email_id;
    }

    /**
     * @param email_id The email_id
     */
    public void setEmailId(String email_id) {
        this.email_id = email_id;
    }

    /**
     * @return The mobile_no
     */
    public String getMobileNo() {
        return mobile_no;
    }

    /**
     * @param mobile_no The mobile_no
     */
    public void setMobileNo(String mobile_no) {
        this.mobile_no = mobile_no;
    }
}
