package com.liftPlzz.model.videos.upcomingLift;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideosResponse {
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("data")
    @Expose
    private List<VideosModel> data = null;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<VideosModel> getData() {
        return data;
    }

    public void setData(List<VideosModel> data) {
        this.data = data;
    }
}


