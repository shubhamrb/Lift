package com.liftPlzz.model.appupdate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AppUpdateModel implements Serializable {

    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("new_version")
    @Expose
    private int new_version;

    @SerializedName("update_message")
    @Expose
    private String update_message;

    @SerializedName("date")
    @Expose
    private long date;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getNew_version() {
        return new_version;
    }

    public void setNew_version(int new_version) {
        this.new_version = new_version;
    }

    public String getUpdate_message() {
        return update_message;
    }

    public void setUpdate_message(String update_message) {
        this.update_message = update_message;
    }

    @Override
    public String toString() {
        return "{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", new_version=" + new_version +
                ", update_message='" + update_message + '\'' +
                ", date=" + date +
                "}";
    }
}