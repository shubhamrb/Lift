package com.liftPlzz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RedeemRequestModel implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("point")
    @Expose
    private int point;

    @SerializedName("comment")
    @Expose
    private String comment;

    @SerializedName("status")
    @Expose
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RedeemRequestModel{" +
                "id=" + id +
                ", point=" + point +
                ", comment='" + comment + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}