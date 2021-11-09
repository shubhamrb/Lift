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
    @SerializedName("sub_message")
    @Expose
    private String subMessage;

    @SerializedName("lift_details")
    @Expose
    private LiftDetails liftDetails;

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
