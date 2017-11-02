package com.palprotech.heylaapp.bean.support;

/**
 * Created by Admin on 02-11-2017.
 */

public class StoreState {

    private String stateId;
    private String stateName;

    public StoreState(String stateId, String stateName) {
        this.stateId = stateId;
        this.stateName = stateName;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return stateName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StoreState) {
            StoreState c = (StoreState) obj;
            if (c.getStateName().equals(stateName) && c.getStateId() == stateId) return true;
        }

        return false;
    }
}
