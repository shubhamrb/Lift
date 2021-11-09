package com.liftPlzz.model.viewRideDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("title")
@Expose
private Object title;
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
@SerializedName("created_at")
@Expose
private String createdAt;
@SerializedName("updated_at")
@Expose
private String updatedAt;
@SerializedName("name")
@Expose
private String name;
@SerializedName("mobile")
@Expose
private String mobile;
@SerializedName("username")
@Expose
private String username;
@SerializedName("review_history")
@Expose
private List<ReviewHistory> reviewHistory = null;
@SerializedName("rating")
@Expose
private Double rating;
@SerializedName("start_point")
@Expose
private StartPoint startPoint;
@SerializedName("end_point")
@Expose
private EndPoint endPoint;
@SerializedName("checkpoints")
@Expose
private List<Checkpoint> checkpoints = null;

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public Object getTitle() {
return title;
}

public void setTitle(Object title) {
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

public List<ReviewHistory> getReviewHistory() {
return reviewHistory;
}

public void setReviewHistory(List<ReviewHistory> reviewHistory) {
this.reviewHistory = reviewHistory;
}

public Double getRating() {
return rating;
}

public void setRating(Double rating) {
this.rating = rating;
}

public StartPoint getStartPoint() {
return startPoint;
}

public void setStartPoint(StartPoint startPoint) {
this.startPoint = startPoint;
}

public EndPoint getEndPoint() {
return endPoint;
}

public void setEndPoint(EndPoint endPoint) {
this.endPoint = endPoint;
}

public List<Checkpoint> getCheckpoints() {
return checkpoints;
}

public void setCheckpoints(List<Checkpoint> checkpoints) {
this.checkpoints = checkpoints;
}

}