package com.liftPlzz.model.completedLift;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseCompletedLift {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("lifts")
    @Expose
    private List<CompleteLiftData> lifts = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CompleteLiftData> getLifts() {
        return lifts;
    }

    public void setLifts(List<CompleteLiftData> lifts) {
        this.lifts = lifts;
    }

}
