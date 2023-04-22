package com.liftPlzz.model.on_going;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LiftUsers {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("request_id")
    @Expose
    private int request_id;

    @SerializedName("user_lift_id")
    @Expose
    private int user_lift_id;

    @SerializedName("lift_id")
    @Expose
    private int lift_id;

    @SerializedName("user_id")
    @Expose
    private int user_id;

    @SerializedName("from_lift_id")
    @Expose
    private int from_lift_id;

    @SerializedName("seats")
    @Expose
    private int seats;

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("ride_confirm")
    @Expose
    private int ride_confirm;

    @SerializedName("ride_start_latlong")
    @Expose
    private String ride_start_latlong;

    @SerializedName("ride_end_latlong")
    @Expose
    private String ride_end_latlong;

    @SerializedName("ride_status")
    @Expose
    private int ride_status;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("mobile")
    @Expose
    private String mobile;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("sos")
    @Expose
    private String sos;

    @SerializedName("email_verified_at")
    @Expose
    private String email_verified_at;

    @SerializedName("otp")
    @Expose
    private String otp;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("role_id")
    @Expose
    private int role_id;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("designation")
    @Expose
    private String designation;

    @SerializedName("about_me")
    @Expose
    private String about_me;

    @SerializedName("notification_id")
    @Expose
    private String notification_id;

    @SerializedName("liftee_joined")
    @Expose
    private int liftee_joined;

    @SerializedName("api_token")
    @Expose
    private String api_token;

    public int getLiftee_joined() {
        return liftee_joined;
    }

    public void setLiftee_joined(int liftee_joined) {
        this.liftee_joined = liftee_joined;
    }

    public int getRequest_id() {
        return request_id;
    }

    public void setRequest_id(int request_id) {
        this.request_id = request_id;
    }

    public int getUser_lift_id() {
        return user_lift_id;
    }

    public void setUser_lift_id(int user_lift_id) {
        this.user_lift_id = user_lift_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLift_id() {
        return lift_id;
    }

    public void setLift_id(int lift_id) {
        this.lift_id = lift_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getFrom_lift_id() {
        return from_lift_id;
    }

    public void setFrom_lift_id(int from_lift_id) {
        this.from_lift_id = from_lift_id;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRide_confirm() {
        return ride_confirm;
    }

    public void setRide_confirm(int ride_confirm) {
        this.ride_confirm = ride_confirm;
    }

    public String getRide_start_latlong() {
        return ride_start_latlong;
    }

    public void setRide_start_latlong(String ride_start_latlong) {
        this.ride_start_latlong = ride_start_latlong;
    }

    public String getRide_end_latlong() {
        return ride_end_latlong;
    }

    public void setRide_end_latlong(String ride_end_latlong) {
        this.ride_end_latlong = ride_end_latlong;
    }

    public int getRide_status() {
        return ride_status;
    }

    public void setRide_status(int ride_status) {
        this.ride_status = ride_status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getSos() {
        return sos;
    }

    public void setSos(String sos) {
        this.sos = sos;
    }

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public void setEmail_verified_at(String email_verified_at) {
        this.email_verified_at = email_verified_at;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getAbout_me() {
        return about_me;
    }

    public void setAbout_me(String about_me) {
        this.about_me = about_me;
    }

    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }
}

