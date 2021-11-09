package com.liftPlzz.model.ridebyvehicletypemodel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverByTypeReponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("total_count")
    @Expose
    private Integer totalCount;
    @SerializedName("data")
    @Expose
    private List<DriverData> data = null;

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

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<DriverData> getData() {
        return data;
    }

    public void setData(List<DriverData> data) {
        this.data = data;
    }

}