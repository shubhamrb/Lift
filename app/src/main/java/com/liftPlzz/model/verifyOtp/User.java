package com.liftPlzz.model.verifyOtp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

@SerializedName("id")
@Expose
private Integer id;
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
@SerializedName("email_verified_at")
@Expose
private Object emailVerifiedAt;
@SerializedName("otp")
@Expose
private String otp;
@SerializedName("gender")
@Expose
private String gender;
@SerializedName("role_id")
@Expose
private Integer roleId;
@SerializedName("image")
@Expose
private Object image;
@SerializedName("designation")
@Expose
private String designation;
@SerializedName("dob")
@Expose
private String dob;
@SerializedName("about_me")
@Expose
private Object aboutMe;
@SerializedName("status")
@Expose
private String status;
@SerializedName("api_token")
@Expose
private String apiToken;
@SerializedName("google_token")
@Expose
private Object googleToken;
@SerializedName("fb_token")
@Expose
private Object fbToken;
@SerializedName("driver_details")
@Expose
private Object driverDetails;
@SerializedName("created_at")
@Expose
private String createdAt;
@SerializedName("updated_at")
@Expose
private String updatedAt;

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

public Object getEmailVerifiedAt() {
return emailVerifiedAt;
}

public void setEmailVerifiedAt(Object emailVerifiedAt) {
this.emailVerifiedAt = emailVerifiedAt;
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

public Integer getRoleId() {
return roleId;
}

public void setRoleId(Integer roleId) {
this.roleId = roleId;
}

public Object getImage() {
return image;
}

public void setImage(Object image) {
this.image = image;
}

public String getDesignation() {
return designation;
}

public void setDesignation(String designation) {
this.designation = designation;
}

public String getDob() {
return dob;
}

public void setDob(String dob) {
this.dob = dob;
}

public Object getAboutMe() {
return aboutMe;
}

public void setAboutMe(Object aboutMe) {
this.aboutMe = aboutMe;
}

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public String getApiToken() {
return apiToken;
}

public void setApiToken(String apiToken) {
this.apiToken = apiToken;
}

public Object getGoogleToken() {
return googleToken;
}

public void setGoogleToken(Object googleToken) {
this.googleToken = googleToken;
}

public Object getFbToken() {
return fbToken;
}

public void setFbToken(Object fbToken) {
this.fbToken = fbToken;
}

public Object getDriverDetails() {
return driverDetails;
}

public void setDriverDetails(Object driverDetails) {
this.driverDetails = driverDetails;
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

}