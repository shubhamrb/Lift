package com.liftPlzz.model.completedLift;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompleteLiftData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("vehicle_id")
    @Expose
    private Integer vehicleId;
    @SerializedName("lift_type")
    @Expose
    private String liftType;
    @SerializedName("free_seats")
    @Expose
    private Integer freeSeats;
    @SerializedName("paid_seats")
    @Expose
    private Integer paidSeats;
    @SerializedName("lift_date")
    @Expose
    private String liftDate;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("is_booked")
    @Expose
    private Integer isBooked;
    @SerializedName("total_request")
    @Expose
    private Integer totalRequest;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("start_latlong")
    @Expose
    private String startLatlong;
    @SerializedName("start_location")
    @Expose
    private String startLocation;
    @SerializedName("end_latlong")
    @Expose
    private String endLatlong;
    @SerializedName("end_location")
    @Expose
    private String endLocation;

    @SerializedName("total_distance")
    @Expose
    private String totalDistance;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getLiftType() {
        return liftType;
    }

    public void setLiftType(String liftType) {
        this.liftType = liftType;
    }

    public Integer getFreeSeats() {
        return freeSeats;
    }

    public void setFreeSeats(Integer freeSeats) {
        this.freeSeats = freeSeats;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsBooked() {
        return isBooked;
    }

    public void setIsBooked(Integer isBooked) {
        this.isBooked = isBooked;
    }

    public Integer getTotalRequest() {
        return totalRequest;
    }

    public void setTotalRequest(Integer totalRequest) {
        this.totalRequest = totalRequest;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStartLatlong() {
        return startLatlong;
    }

    public void setStartLatlong(String startLatlong) {
        this.startLatlong = startLatlong;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLatlong() {
        return endLatlong;
    }

    public void setEndLatlong(String endLatlong) {
        this.endLatlong = endLatlong;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public String getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        this.totalDistance = totalDistance;
    }
}
