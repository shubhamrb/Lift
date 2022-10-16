package com.liftPlzz.model.ridehistorymodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data implements Serializable {

    @SerializedName("start_point")
    @Expose
    private String start_point;

    @SerializedName("end_point")
    @Expose
    private String end_point;

    @SerializedName("start_location")
    @Expose
    private String start_location;

    @SerializedName("end_location")
    @Expose
    private String end_location;

    public String getStart_point() {
        return start_point;
    }

    public void setStart_point(String start_point) {
        this.start_point = start_point;
    }

    public String getEnd_point() {
        return end_point;
    }

    public void setEnd_point(String end_point) {
        this.end_point = end_point;
    }

    public String getStart_location() {
        return start_location;
    }

    public void setStart_location(String start_location) {
        this.start_location = start_location;
    }

    public String getEnd_location() {
        return end_location;
    }

    public void setEnd_location(String end_location) {
        this.end_location = end_location;
    }

    @Override
    public String toString() {
        return "Data{" +
                "start_point='" + start_point + '\'' +
                ", end_point='" + end_point + '\'' +
                ", start_location='" + start_location + '\'' +
                ", end_location='" + end_location + '\'' +
                '}';
    }
}