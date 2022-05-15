package com.liftPlzz.model.on_going;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MainOnGoingResponse {

    @SerializedName("response")
    @Expose
    private InnerGoingResponse response;

    public InnerGoingResponse getResponse() {
        return response;
    }

    public void setResponse(InnerGoingResponse response) {
        this.response = response;
    }
}

