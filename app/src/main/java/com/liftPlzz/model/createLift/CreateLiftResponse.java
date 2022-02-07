package com.liftPlzz.model.createLift;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateLiftResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("lift_id")
    @Expose
    private String lift_id;
    @SerializedName("matches_count")
    @Expose
    private Integer matches_count;
    @SerializedName("sub_message")
    @Expose
    private String subMessage;

    @SerializedName("lift_details")
    @Expose
    private LiftDetails liftDetails;

    public String getLift_id() {
        return lift_id;
    }

    public void setLift_id(String lift_id) {
        this.lift_id = lift_id;
    }

    public Integer getMatches_count() {
        return matches_count;
    }

    public void setMatches_count(Integer matches_count) {
        this.matches_count = matches_count;
    }

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

    public LiftDetails getLiftDetails() {
        return liftDetails;
    }

    public void setLiftDetails(LiftDetails liftDetails) {
        this.liftDetails = liftDetails;
    }

    public String getSubMessage() {
        return subMessage;
    }

    public void setSubMessage(String subMessage) {
        this.subMessage = subMessage;
    }
}
