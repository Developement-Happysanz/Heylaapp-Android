package com.palprotech.heylaapp.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Admin on 01-01-2018.
 */

public class ReviewList {

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("Reviewdetails")
    @Expose
    private ArrayList<Review> plans = new ArrayList<Review>();

    /**
     *
     * @return
     *     The count
     */
    public int getCount() {
        return count;
    }

    /**
     *
     * @param count
     *     The count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     *
     * @return
     *     The Review
     */
    public ArrayList<Review> getReviews() {
        return plans;
    }

    /**
     *
     * @param plans
     *     The Review
     */
    public void setReviews(ArrayList<Review> plans) {
        this.plans = plans;
    }
}
