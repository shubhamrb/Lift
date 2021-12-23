package com.liftPlzz.model.editlift;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LiftLocationModel {

    @SerializedName("LatLng")
    @Expose
    private String latLng;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("time") @Expose private String time;

    @SerializedName("city") @Expose private String city;
    @SerializedName("state") @Expose private String state;
    @SerializedName("country") @Expose private String country;




    @SerializedName("location")
    @Expose
    private String location;

    public String getLatLng() {
        return latLng;
    }

    public void setLatLng(String latLng) {
        this.latLng = latLng;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}

