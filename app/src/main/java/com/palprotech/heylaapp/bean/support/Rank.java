package com.palprotech.heylaapp.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rank {

    @SerializedName("user_name")
    @Expose
    private String user_name;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("email_id")
    @Expose
    private String email_id;

    @SerializedName("user_picture")
    @Expose
    private String user_picture;

    @SerializedName("total_points")
    @Expose
    private String total_points;

    /**
     * @return The user_name
     */
    public String getUserName() {
        return user_name;
    }

    /**
     * @param user_name The user_name
     */
    public void setUserName(String user_name) {
        this.user_name = user_name;
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
     * @return The email_id
     */
    public String getEmail_id() {
        return email_id;
    }

    /**
     * @param email_id The email_id
     */
    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    /**
     * @return The user_picture
     */
    public String getUser_picture() {
        return user_picture;
    }

    /**
     * @param user_picture The user_picture
     */
    public void setUser_picture(String user_picture) {
        this.user_picture = user_picture;
    }

    /**
     * @return The total_points
     */
    public String getTotal_points() {
        return total_points;
    }

    /**
     * @param total_points The total_points
     */
    public void setTotal_points(String total_points) {
        this.total_points = total_points;
    }
}