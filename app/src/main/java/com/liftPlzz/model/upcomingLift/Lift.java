package com.liftPlzz.model.upcomingLift;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Lift implements Serializable {

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
    @SerializedName("vehicle_model")
    @Expose
    private String vehicle_model;
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
    @SerializedName("find_match")
    @Expose
    private Integer findMatch;

    @SerializedName("request_id")
    @Expose
    private Integer request_id;

    @SerializedName("is_driver_start")
    @Expose
    private Integer is_driver_start;

    @SerializedName("is_user_start")
    @Expose
    private Integer is_user_start;

    @SerializedName("ride_start")
    @Expose
    private Integer rideStart;


    @SerializedName("same_route_vehicle")
    @Expose
    private Integer sameroutevehicle;
    //

    @SerializedName("lift_time")
    @Expose
    private String lift_time;



    @SerializedName("start_time")
    @Expose
    private String start_time;

    @SerializedName("total_points")
    @Expose
    private String total_points;

    @SerializedName("rate_per_km")
    @Expose
    private String rate_per_km;
    @SerializedName("total_km")
    @Expose
    private String total_km;

    @SerializedName("driver_tracking_id")
    @Expose
    private Integer driver_tracking_id;

    public Integer getVehicle_subcategory() {
        return vehicle_subcategory;
    }

    public void setVehicle_subcategory(Integer vehicle_subcategory) {
        this.vehicle_subcategory = vehicle_subcategory;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    @SerializedName("vehicle_subcategory")
    @Expose
    private Integer vehicle_subcategory;

    @SerializedName("vehicle_type")
    @Expose
    private String vehicle_type;


    public Integer getIs_user_start() {
        return is_user_start;
    }

    public void setIs_user_start(Integer is_user_start) {
        this.is_user_start = is_user_start;
    }

    public Integer getRequest_id() {
        return request_id;
    }

    public void setRequest_id(Integer request_id) {
        this.request_id = request_id;
    }

    public Integer getIs_driver_start() {
        return is_driver_start;
    }

    public void setIs_driver_start(Integer is_driver_start) {
        this.is_driver_start = is_driver_start;
    }

    public String getTotal_km() {
        return total_km;
    }

    public void setTotal_km(String total_km) {
        this.total_km = total_km;
    }

    public Integer getDriver_tracking_id() {
        return driver_tracking_id;
    }

    public void setDriver_tracking_id(Integer driver_tracking_id) {
        this.driver_tracking_id = driver_tracking_id;
    }

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

    public Integer getFindMatch() {
        return findMatch;
    }

    public void setFindMatch(Integer findMatch) {
        this.findMatch = findMatch;
    }

    public Integer getRideStart() {
        return rideStart;
    }

    public void setRideStart(Integer rideStart) {
        this.rideStart = rideStart;
    }

    public Integer getSameroutevehicle() {
        return sameroutevehicle;
    }

    public void setSameroutevehicle(Integer sameroutevehicle) {
        this.sameroutevehicle = sameroutevehicle;
    }

    public String getLift_time() {
        return lift_time;
    }

    public void setLift_time(String lift_time) {
        this.lift_time = lift_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getTotal_points() {
        return total_points;
    }

    public void setTotal_points(String total_points) {
        this.total_points = total_points;
    }

    public String getRate_per_km() {
        return rate_per_km;
    }

    public void setRate_per_km(String rate_per_km) {
        this.rate_per_km = rate_per_km;
    }

    public String getVehicle_model() {
        return vehicle_model;
    }

    public void setVehicle_model(String vehicle_model) {
        this.vehicle_model = vehicle_model;
    }
}
