package com.liftPlzz.model.getFaq;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseFaq {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<FaqData> data = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<FaqData> getData() {
        return data;
    }

    public void setData(List<FaqData> data) {
        this.data = data;
    }
}
