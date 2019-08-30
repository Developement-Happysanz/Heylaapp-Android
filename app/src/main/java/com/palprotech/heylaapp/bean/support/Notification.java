package com.palprotech.heylaapp.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Notification implements Serializable {

    @SerializedName("template_name")
    @Expose
    private String template_name;

    @SerializedName("template_content")
    @Expose
    private String template_content;

    @SerializedName("view_status")
    @Expose
    private String view_status;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("template_pic")
    @Expose
    private String template_pic;

    /**
     * @return The template_name
     */
    public String getTemplate_name() {
        return template_name;
    }

    /**
     * @param template_name The template_name
     */
    public void setTemplate_name(String template_name) {
        this.template_name = template_name;
    }


    /**
     * @return The template_content
     */
    public String getTemplate_content() {
        return template_content;
    }

    /**
     * @param template_content The template_content
     */
    public void setTemplate_content(String template_content) {
        this.template_content = template_content;
    }

    /**
     * @return The view_status
     */
    public String getView_status() {
        return view_status;
    }

    /**
     * @param view_status The view_status
     */
    public void setView_status(String view_status) {
        this.view_status = view_status;
    }

    /**
     * @return The created_at
     */
    public String getCreated_at() {
        return created_at;
    }

    /**
     * @param created_at The created_at
     */
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    /**
     * @return The template_pic
     */
    public String getTemplate_pic() {
        return template_pic;
    }

    /**
     * @param template_pic The template_pic
     */
    public void setTemplate_pic(String template_pic) {
        this.template_pic = template_pic;
    }
}