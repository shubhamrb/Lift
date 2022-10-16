package com.liftPlzz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PercentageModel {

    @SerializedName("percantage")
    @Expose
    private int percantage;

    @SerializedName("color")
    @Expose
    private String color;

    public int getPercantage() {
        return percantage;
    }

    public void setPercantage(int percantage) {
        this.percantage = percantage;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
