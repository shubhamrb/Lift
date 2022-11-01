package com.liftPlzz.model.UserInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.liftPlzz.model.PercentageModel;

import java.util.List;

public class User {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
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
    @SerializedName("share_code")
    @Expose
    private Integer shareCode;
    @SerializedName("driver_details")
    @Expose
    private String driverDetails;
    @SerializedName("social_images")
    @Expose
    private List<SocialImage> socialImages = null;
    @SerializedName("reviews")
    @Expose
    private List<Review> reviews = null;
    @SerializedName("settings")
    @Expose
    private Settings settings;

    public Integer getIs_contact_public() {
        return is_contact_public;
    }

    public void setIs_contact_public(Integer is_contact_public) {
        this.is_contact_public = is_contact_public;
    }

    public Integer getIs_dob_public() {
        return is_dob_public;
    }

    public void setIs_dob_public(Integer is_dob_public) {
        this.is_dob_public = is_dob_public;
    }

    public Integer getIs_email_public() {
        return is_email_public;
    }

    public void setIs_email_public(Integer is_email_public) {
        this.is_email_public = is_email_public;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @SerializedName("is_contact_public")
    @Expose
    private Integer is_contact_public;
    @SerializedName("is_dob_public")
    @Expose
    private Integer is_dob_public;
    @SerializedName("is_email_public")
    @Expose
    private Integer is_email_public;
    @SerializedName("department")
    @Expose
    private String department;

    @SerializedName("company")
    @Expose
    private String company;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @SerializedName("age")
    @Expose
    private String age;

    public PercentageModel getProfile_percentage() {
        return profile_percentage;
    }

    @SerializedName("profile_percentage")
    @Expose
    private PercentageModel profile_percentage;


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

    public List<SocialImage> getSocialImages() {
        return socialImages;
    }

    public void setSocialImages(List<SocialImage> socialImages) {
        this.socialImages = socialImages;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }


    public Integer getShareCode() {
        return shareCode;
    }

    public void setShareCode(Integer shareCode) {
        this.shareCode = shareCode;
    }
}