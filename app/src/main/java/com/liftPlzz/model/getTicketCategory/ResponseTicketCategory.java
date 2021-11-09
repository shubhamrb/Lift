package com.liftPlzz.model.getTicketCategory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseTicketCategory {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<TicketCategoryData> data = null;

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

    public List<TicketCategoryData> getData() {
        return data;
    }

    public void setData(List<TicketCategoryData> data) {
        this.data = data;
    }

}
