package com.liftPlzz.model.editlift;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.liftPlzz.model.getVehicle.Datum;

import java.util.List;

public class GetVehicleEditResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;

    @SerializedName("data")
    @Expose
    private EditVehicleData data = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public EditVehicleData getData() {
        return data;
    }

    public void setData(EditVehicleData data) {
        this.data = data;
    }
}

