package com.liftPlzz.model.partnerdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.liftPlzz.model.PercentageModel;

import java.util.List;

public class User {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("request_id")
    @Expose
    private Integer request_id;

    @SerializedName("lift_id")
    @Expose
    private Integer lift_id;
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("seats")
    @Expose
    private Integer seats;

    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("userType")
    @Expose
    private String userType;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("designation")
    @Expose
    private String designation;
    @SerializedName("about")
    @Expose
    private String about;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("total_lift")
    @Expose
    private Integer totalLift;
    @SerializedName("lift_giver")
    @Expose
    private Integer liftGiver;
    @SerializedName("lift_taker")
    @Expose
    private Integer liftTaker;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("total_review")
    @Expose
    private Integer totalReview;
    @SerializedName("driver_details")
    @Expose
    private String driverDetails;
    @SerializedName("social_images")
    @Expose
    private List<Object> socialImages = null;

    @SerializedName("profile_percentage")
    @Expose
    private PercentageModel profile_percentage;

    @SerializedName("vehicle_percentage")
    @Expose
    private PercentageModel vehicle_percentage;

    @SerializedName("rate_per_km")
    @Expose
    private String rate_per_km;

    @SerializedName("total_km")
    @Expose
    private String total_km;

    @SerializedName("total_point")
    @Expose
    private String total_point;
    @SerializedName("is_contact_public")
    @Expose
    private int is_contact_public;

    @SerializedName("is_block")
    @Expose
    private boolean is_block;

    public Integer getRequest_id() {
        return request_id;
    }

    public void setRequest_id(Integer request_id) {
        this.request_id = request_id;
    }

    public DriveLocation getLocation() {
        return driveLocation;
    }

    @SerializedName("location")
    @Expose
    private DriveLocation driveLocation;


    public boolean isIs_block() {
        return is_block;
    }

    public void setIs_block(boolean is_block) {
        this.is_block = is_block;
    }

    public int getIs_contact_public() {
        return is_contact_public;
    }

    public void setIs_contact_public(int is_contact_public) {
        this.is_contact_public = is_contact_public;
    }

    public Integer getLift_id() {
        return lift_id;
    }

    public void setLift_id(Integer lift_id) {
        this.lift_id = lift_id;
    }

    public void setProfile_percentage(PercentageModel profile_percentage) {
        this.profile_percentage = profile_percentage;
    }

    public void setTotal_km(String total_km) {
        this.total_km = total_km;
    }

    public void setTotal_point(String total_point) {
        this.total_point = total_point;
    }

    public DriveLocation getDriveLocation() {
        return driveLocation;
    }

    public void setDriveLocation(DriveLocation driveLocation) {
        this.driveLocation = driveLocation;
    }

    public void setEvery_seat(String every_seat) {
        this.every_seat = every_seat;
    }

    public void setVehicle_percentage(PercentageModel vehicle_percentage) {
        this.vehicle_percentage = vehicle_percentage;
    }

    public String getRate_per_km() {
        return rate_per_km;
    }

    public void setRate_per_km(String rate_per_km) {
        this.rate_per_km = rate_per_km;
    }

    public PercentageModel getProfile_percentage() {
        return profile_percentage;
    }

    public PercentageModel getVehicle_percentage() {
        return vehicle_percentage;
    }

    public String getTotal_km() {
        return total_km;
    }

    public String getTotal_point() {
        return total_point;
    }

    public String getEvery_seat() {
        return every_seat;
    }

    @SerializedName("every seat")
    @Expose
    private String every_seat;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getTotalLift() {
        return totalLift;
    }

    public void setTotalLift(Integer totalLift) {
        this.totalLift = totalLift;
    }

    public Integer getLiftGiver() {
        return liftGiver;
    }

    public void setLiftGiver(Integer liftGiver) {
        this.liftGiver = liftGiver;
    }

    public Integer getLiftTaker() {
        return liftTaker;
    }

    public void setLiftTaker(Integer liftTaker) {
        this.liftTaker = liftTaker;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Integer getTotalReview() {
        return totalReview;
    }

    public void setTotalReview(Integer totalReview) {
        this.totalReview = totalReview;
    }

    public String getDriverDetails() {
        return driverDetails;
    }

    public void setDriverDetails(String driverDetails) {
        this.driverDetails = driverDetails;
    }

    public List<Object> getSocialImages() {
        return socialImages;
    }

    public void setSocialImages(List<Object> socialImages) {
        this.socialImages = socialImages;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
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