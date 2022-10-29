package com.liftPlzz.model.getsetting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SettingModel implements Serializable {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private Boolean status;

    @SerializedName("title")
    @Expose
    private Map<String, String> title;

    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public Map<String, String> getTitle() {
        return title;
    }

    public void setTitle(Map<String, String> title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }
}
