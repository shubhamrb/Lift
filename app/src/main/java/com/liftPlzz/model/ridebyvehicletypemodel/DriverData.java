package com.liftPlzz.model.ridebyvehicletypemodel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverData {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("total_point")
    @Expose
    private Float total_point;
    @SerializedName("lift_id")
    @Expose
    private Integer liftId;
    @SerializedName("from_lift_id")
    @Expose
    private String fromLiftId;
    @SerializedName("request_already_send")
    @Expose
    private Integer requestAlreadySend;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("paid_seats")
    @Expose
    private Integer paidSeats;
    @SerializedName("rate_per_km")
    @Expose
    private Integer ratePerKm;

    @SerializedName("lift_date")
    @Expose
    private String liftDate;
    @SerializedName("lfit_time")
    @Expose
    private String lfit_time;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("total_review")
    @Expose
    private String total_review;
    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("start_point_distance")
    @Expose
    private Double startPointDistance;
    @SerializedName("end_point_distance")
    @Expose
    private Double endPointDistance;

    public Float getTotal_point() {
        return total_point;
    }

    public void setTotal_point(Float total_point) {
        this.total_point = total_point;
    }

    public String getLfit_time() {
        return lfit_time;
    }

    public void setLfit_time(String lfit_time) {
        this.lfit_time = lfit_time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLiftId() {
        return liftId;
    }

    public void setLiftId(Integer liftId) {
        this.liftId = liftId;
    }

    public String getFromLiftId() {
        return fromLiftId;
    }

    public void setFromLiftId(String fromLiftId) {
        this.fromLiftId = fromLiftId;
    }

    public Integer getRequestAlreadySend() {
        return requestAlreadySend;
    }

    public void setRequestAlreadySend(Integer requestAlreadySend) {
        this.requestAlreadySend = requestAlreadySend;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }



    public Integer getPaidSeats() {
        return paidSeats;
    }

    public void setPaidSeats(Integer paidSeats) {
        this.paidSeats = paidSeats;
    }

    public String getLiftDate() {
        return liftDate;
    }

    public void setLiftDate(String liftDate) {
        this.liftDate = liftDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTotal_review() {
        return total_review;
    }

    public void setTotal_review(String total_review) {
        this.total_review = total_review;
    }

    public Double getStartPointDistance() {
        return startPointDistance;
    }

    public void setStartPointDistance(Double startPointDistance) {
        this.startPointDistance = startPointDistance;
    }

    public Double getEndPointDistance() {
        return endPointDistance;
    }

    public void setEndPointDistance(Double endPointDistance) {
        this.endPointDistance = endPointDistance;
    }

    public Integer getRatePerKm() {
        return ratePerKm;
    }

    public void setRatePerKm(Integer ratePerKm) {
        this.ratePerKm = ratePerKm;
    }
}