package com.liftPlzz.model.matchingridemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("vehicle_list")
    @Expose
    private List<MatchingRideResponse> vehicleList = null;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<MatchingRideResponse> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(List<MatchingRideResponse> vehicleList) {
        this.vehicleList = vehicleList;
    }

}