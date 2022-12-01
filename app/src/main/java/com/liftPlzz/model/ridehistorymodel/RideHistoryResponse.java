package com.liftPlzz.model.ridehistorymodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RideHistoryResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("start_data")
    @Expose
    private List<Data> start_data;
    @SerializedName("end_data")
    @Expose
    private List<Data> end_data;

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

    public List<Data> getStart_data() {
        return start_data;
    }

    public void setStart_data(List<Data> start_data) {
        this.start_data = start_data;
    }

    public List<Data> getEnd_data() {
        return end_data;
    }

    public void setEnd_data(List<Data> end_data) {
        this.end_data = end_data;
    }
}

