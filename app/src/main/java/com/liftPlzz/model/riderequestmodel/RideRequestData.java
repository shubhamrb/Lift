package com.liftPlzz.model.riderequestmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.liftPlzz.model.PercentageModel;

public class RideRequestData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("seats")
    @Expose
    private Integer seats;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("start_point")
    @Expose
    private String startPoint;
    @SerializedName("end_point")
    @Expose
    private String endPoint;
    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("profile_percentage")
    @Expose
    private PercentageModel profile_percentage;

    @SerializedName("vehicle_percentage")
    @Expose
    private PercentageModel vehicle_percentage;

    @SerializedName("total_km")
    @Expose
    private String total_km;

    @SerializedName("total_point")
    @Expose
    private String total_point;
    @SerializedName("rate_per_km")
    @Expose
    private String rate_per_km;

    @SerializedName("location")
    @Expose
    private DriveLocation driveLocation;
    @SerializedName("is_contact_public")
    @Expose
    private int is_contact_public;

    @SerializedName("is_user_blocked")
    @Expose
    private int is_user_blocked;

    public int getIs_user_blocked() {
        return is_user_blocked;
    }

    public void setIs_user_blocked(int is_user_blocked) {
        this.is_user_blocked = is_user_blocked;
    }

    public int getIs_contact_public() {
        return is_contact_public;
    }

    public void setIs_contact_public(int is_contact_public) {
        this.is_contact_public = is_contact_public;
    }

    public String getRate_per_km() {
        return rate_per_km;
    }

    public void setRate_per_km(String rate_per_km) {
        this.rate_per_km = rate_per_km;
    }

    public DriveLocation getDriveLocation() {
        return driveLocation;
    }

    public void setDriveLocation(DriveLocation driveLocation) {
        this.driveLocation = driveLocation;
    }

    public DriveLocation getLocation() {
        return driveLocation;
    }

    public void setLocation(DriveLocation driveLocation) {
        this.driveLocation = driveLocation;
    }

    public PercentageModel getProfile_percentage() {
        return profile_percentage;
    }

    public void setProfile_percentage(PercentageModel profile_percentage) {
        this.profile_percentage = profile_percentage;
    }

    public PercentageModel getVehicle_percentage() {
        return vehicle_percentage;
    }

    public void setVehicle_percentage(PercentageModel vehicle_percentage) {
        this.vehicle_percentage = vehicle_percentage;
    }

    public String getTotal_km() {
        return total_km;
    }

    public void setTotal_km(String total_km) {
        this.total_km = total_km;
    }

    public String getTotal_point() {
        return total_point;
    }

    public void setTotal_point(String total_point) {
        this.total_point = total_point;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public class DriveLocation {
        public String getEnd_city() {
            return end_city;
        }

        public String getEnd_latlong() {
            return end_latlong;
        }

        public String getEnd_location() {
            return end_location;
        }

        public String getStart_city() {
            return start_city;
        }

        public String getStart_latlong() {
            return start_latlong;
        }

        public String getStart_location() {
            return start_location;
        }

        public String getStart_time() {
            return start_time;
        }

        @SerializedName("end_city")
        @Expose
        String end_city;
        @SerializedName("end_latlong")
        @Expose
        String end_latlong;
        @SerializedName("end_location")
        @Expose
        String end_location;
        @SerializedName("start_city")
        @Expose
        String start_city;
        @SerializedName("start_latlong")
        @Expose
        String start_latlong;
        @SerializedName("start_location")
        @Expose
        String start_location;
        @SerializedName("start_time")
        @Expose
        String start_time;
    }
}