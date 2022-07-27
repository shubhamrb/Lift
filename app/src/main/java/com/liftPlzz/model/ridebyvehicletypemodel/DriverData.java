package com.liftPlzz.model.ridebyvehicletypemodel;

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
    @SerializedName("vacant_seats")
    @Expose
    private Integer vacant_seats;
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

    @SerializedName("total_km")
    @Expose
    private String total_km;

    @SerializedName("start_location")
    @Expose
    private String start_location;

    @SerializedName("end_location")
    @Expose
    private String end_location;

    @SerializedName("price_per_seat")
    @Expose
    private String price_per_seat;

    @SerializedName("start_point_distance")
    @Expose
    private Double startPointDistance;
    @SerializedName("end_point_distance")
    @Expose
    private Double endPointDistance;

    public Integer getVacant_seats() {
        return vacant_seats;
    }

    public void setVacant_seats(Integer vacant_seats) {
        this.vacant_seats = vacant_seats;
    }

    public String getPrice_per_seat() {
        return price_per_seat;
    }

    public void setPrice_per_seat(String price_per_seat) {
        this.price_per_seat = price_per_seat;
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

    public String getTotal_km() {
        return total_km;
    }

    public void setTotal_km(String total_km) {
        this.total_km = total_km;
    }

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