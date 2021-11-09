package com.liftPlzz.model.partnerdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {

    @SerializedName("id")
    @Expose
    private Integer id;
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
}