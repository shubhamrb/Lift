package com.liftPlzz.model.chatuser;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.liftPlzz.model.completedLift.CompleteLiftData;

import java.util.List;

public class ResponseChatUser {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<ChatUser> data = null;
    @SerializedName("totam_record")
    @Expose
    private String totamrecord;

    //

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

    public List<ChatUser> getData() {
        return data;
    }

    public void setData(List<ChatUser> data) {
        this.data = data;
    }

    public String getTotamrecord() {
        return totamrecord;
    }

    public void setTotamrecord(String totamrecord) {
        this.totamrecord = totamrecord;
    }
}
