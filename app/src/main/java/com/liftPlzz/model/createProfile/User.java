package com.liftPlzz.model.createProfile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.liftPlzz.model.SocialImage;

import java.util.List;

public class User {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("share_code")
    @Expose
    private String shareCode;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("email_verified_at")
    @Expose
    private String emailVerifiedAt;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("role_id")
    @Expose
    private Integer roleId;
    @SerializedName("designation")
    @Expose
    private String designation;
    @SerializedName("about_me")
    @Expose
    private String aboutMe;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("sos")
    @Expose
    private String sos;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("is_image")
    @Expose
    private boolean is_image;
    @SerializedName("is_govt_id")
    @Expose
    private boolean is_govt_id;
    @SerializedName("api_token")
    @Expose
    private String apiToken;
    @SerializedName("google_token")
    @Expose
    private String googleToken;
    @SerializedName("fb_token")
    @Expose
    private String fbToken;
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

    @SerializedName("profile_percentage")
    @Expose
    private Integer profile_percentage;


    public boolean isIs_image() {
        return is_image;
    }

    public void setIs_image(boolean is_image) {
        this.is_image = is_image;
    }

    public boolean isIs_govt_id() {
        return is_govt_id;
    }

    public void setIs_govt_id(boolean is_govt_id) {
        this.is_govt_id = is_govt_id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @SerializedName("company")
    @Expose
    private String company;

    @SerializedName("department")
    @Expose
    private String department;


    @SerializedName("is_contact_public")
    @Expose
    private Integer is_contact_public;
    @SerializedName("is_dob_public")
    @Expose
    private Integer is_dob_public;
    @SerializedName("is_email_public")
    @Expose
    private Integer is_email_public;


    public Integer getProfile_percentage() {
        return profile_percentage;
    }

    public void setProfile_percentage(Integer profile_percentage) {
        this.profile_percentage = profile_percentage;
    }

    @SerializedName("social_images")
    @Expose
    private List<SocialImage> socialImages = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSos() {
        return sos;
    }

    public void setSos(String sos) {
        this.sos = sos;
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

    public String getEmailVerifiedAt() {
        return emailVerifiedAt;
    }

    public void setEmailVerifiedAt(String emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
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

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getGoogleToken() {
        return googleToken;
    }

    public void setGoogleToken(String googleToken) {
        this.googleToken = googleToken;
    }

    public String getFbToken() {
        return fbToken;
    }

    public void setFbToken(String fbToken) {
        this.fbToken = fbToken;
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

    public String getShareCode() {
        return shareCode;
    }

    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
    }
}