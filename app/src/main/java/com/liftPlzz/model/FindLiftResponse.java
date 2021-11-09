package com.liftPlzz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FindLiftResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("sub_message")
    @Expose
    private String subMessage;
    @SerializedName("matches_count")
    @Expose
    private Integer matchesCount;
    @SerializedName("lift_id")
    @Expose
    private Integer liftId;


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

    public String getSubMessage() {
        return subMessage;
    }

    public void setSubMessage(String subMessage) {
        this.subMessage = subMessage;
    }

    public Integer getMatchesCount() {
        return matchesCount;
    }

    public void setMatchesCount(Integer matchesCount) {
        this.matchesCount = matchesCount;
    }

    public Integer getLiftId() {
        return liftId;
    }

    public void setLiftId(Integer liftId) {
        this.liftId = liftId;
    }
}