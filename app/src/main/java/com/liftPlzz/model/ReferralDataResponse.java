package com.liftPlzz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReferralDataResponse implements Serializable {

    @SerializedName("status")
    @Expose
    private boolean status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private ReferralModel data;


    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ReferralModel getData() {
        return data;
    }

    public void setData(ReferralModel data) {
        this.data = data;
    }

    public static class ReferralModel implements Serializable {
        @SerializedName("reffer_id")
        @Expose
        private String reffer_id;

        @SerializedName("title")
        @Expose
        private String title;

        @SerializedName("description")
        @Expose
        private String description;

        @SerializedName("points_parent")
        @Expose
        private String points_parent;

        @SerializedName("points_child")
        @Expose
        private String points_child;

        @SerializedName("wallet_point")
        @Expose
        private String wallet_point;

        public String getReffer_id() {
            return reffer_id;
        }

        public void setReffer_id(String reffer_id) {
            this.reffer_id = reffer_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPoints_parent() {
            return points_parent;
        }

        public void setPoints_parent(String points_parent) {
            this.points_parent = points_parent;
        }

        public String getPoints_child() {
            return points_child;
        }

        public void setPoints_child(String points_child) {
            this.points_child = points_child;
        }

        public String getWallet_point() {
            return wallet_point;
        }

        public void setWallet_point(String wallet_point) {
            this.wallet_point = wallet_point;
        }
    }
}