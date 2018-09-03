package com.palprotech.heylaapp.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RankList {

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("user_points")
    @Expose
    private ArrayList<Rank> rankDetails = new ArrayList<Rank>();

    /**
     * @return The count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count The count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return The rankDetails
     */
    public ArrayList<Rank> getRankDetails() {
        return rankDetails;
    }

    /**
     * @param rankDetails The rankDetails
     */
    public void setRankDetails(ArrayList<Rank> rankDetails) {
        this.rankDetails = rankDetails;
    }
}