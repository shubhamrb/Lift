package com.liftPlzz.model.getVehicle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetVehicleListMainResponse {

    @SerializedName("response")
    @Expose
    private GetVehicleListResponse response;

    public GetVehicleListResponse getResponse() {
        return response;
    }

    public void setResponse(GetVehicleListResponse response) {
        this.response = response;
    }

}