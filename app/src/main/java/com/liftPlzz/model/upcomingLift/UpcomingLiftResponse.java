package com.liftPlzz.model.upcomingLift;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpcomingLiftResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("lifts")
    @Expose
    private List<Lift> lifts = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Lift> getLifts() {
        return lifts;
    }

    public void setLifts(List<Lift> lifts) {
        this.lifts = lifts;
    }
}


