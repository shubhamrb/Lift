package com.liftPlzz.model.pointsRedemption;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CardModel {
    @SerializedName("card_number")
    @Expose
    private String card_number;

    @SerializedName("full_name")
    @Expose
    private String full_name;

    @SerializedName("current_point")
    @Expose
    private int current_point;

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public int getCurrent_point() {
        return current_point;
    }

    public void setCurrent_point(int current_point) {
        this.current_point = current_point;
    }
}
