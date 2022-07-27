package com.liftPlzz.model.getVehicle.getReview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("other_details")
    @Expose
    private OtherDetail other_details;
    @SerializedName("total_record")
    @Expose
    private Integer totalRecord;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    @SerializedName("filter_type")
    @Expose
    private List<String> filter_type = null;

    public List<String> getFilter_type() {
        return filter_type;
    }

    public void setFilter_type(List<String> filter_type) {
        this.filter_type = filter_type;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OtherDetail getOther_details() {
        return other_details;
    }

    public void setOther_details(OtherDetail other_details) {
        this.other_details = other_details;
    }

    public Integer getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(Integer totalRecord) {
        this.totalRecord = totalRecord;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class OtherDetail {
        @SerializedName("total_review")
        @Expose
        private int total_review;
        @SerializedName("rating")
        @Expose
        private String rating;
        @SerializedName("detail")
        @Expose
        private Detail detail;

        public int getTotal_review() {
            return total_review;
        }

        public void setTotal_review(int total_review) {
            this.total_review = total_review;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public Detail getDetail() {
            return detail;
        }

        public void setDetail(Detail detail) {
            this.detail = detail;
        }

    }

    public class Detail {
        @SerializedName("5 *")
        @Expose
        private int five_;
        @SerializedName("4 *")
        @Expose
        private int four_;
        @SerializedName("3 *")
        @Expose
        private int three_;
        @SerializedName("2 *")
        @Expose
        private int two_;
        @SerializedName("1 *")
        @Expose
        private int one_;

        public int getFive_() {
            return five_;
        }

        public void setFive_(int five_) {
            this.five_ = five_;
        }

        public int getFour_() {
            return four_;
        }

        public void setFour_(int four_) {
            this.four_ = four_;
        }

        public int getThree_() {
            return three_;
        }

        public void setThree_(int three_) {
            this.three_ = three_;
        }

        public int getTwo_() {
            return two_;
        }

        public void setTwo_(int two_) {
            this.two_ = two_;
        }

        public int getOne_() {
            return one_;
        }

        public void setOne_(int one_) {
            this.one_ = one_;
        }
    }

}