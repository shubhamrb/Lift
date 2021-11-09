package com.liftPlzz.model.ridebyvehicletypemodel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverData {
    @SerializedName("id")
    @Expose
    private Integer id;
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
    @SerializedName("vehicle_id")
    @Expose
    private Integer vehicleId;
    @SerializedName("paid_seats")
    @Expose
    private Integer paidSeats;
    @SerializedName("rate_per_km")
    @Expose
    private Integer ratePerKm;

    @SerializedName("lift_date")
    @Expose
    private String liftDate;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("vehicle_total_seats")
    @Expose
    private Integer vehicleTotalSeats;
    @SerializedName("start_point_distance")
    @Expose
    private Double startPointDistance;
    @SerializedName("end_point_distance")
    @Expose
    private Double endPointDistance;

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

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getVehicleTotalSeats() {
        return vehicleTotalSeats;
    }

    public void setVehicleTotalSeats(Integer vehicleTotalSeats) {
        this.vehicleTotalSeats = vehicleTotalSeats;
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