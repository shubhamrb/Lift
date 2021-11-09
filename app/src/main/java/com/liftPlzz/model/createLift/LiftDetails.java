package com.liftPlzz.model.createLift;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LiftDetails {

@SerializedName("user_id")
@Expose
private Integer userId;
@SerializedName("vehicle_id")
@Expose
private Object vehicleId;
@SerializedName("lift_type")
@Expose
private String liftType;
@SerializedName("free_seats")
@Expose
private String freeSeats;
@SerializedName("paid_seats")
@Expose
private String paidSeats;
@SerializedName("lift_date")
@Expose
private String liftDate;
@SerializedName("status")
@Expose
private Integer status;
@SerializedName("updated_at")
@Expose
private String updatedAt;
@SerializedName("created_at")
@Expose
private String createdAt;
@SerializedName("id")
@Expose
private Integer id;

public Integer getUserId() {
return userId;
}

public void setUserId(Integer userId) {
this.userId = userId;
}

public Object getVehicleId() {
return vehicleId;
}

public void setVehicleId(Object vehicleId) {
this.vehicleId = vehicleId;
}

public String getLiftType() {
return liftType;
}

public void setLiftType(String liftType) {
this.liftType = liftType;
}

public String getFreeSeats() {
return freeSeats;
}

public void setFreeSeats(String freeSeats) {
this.freeSeats = freeSeats;
}

public String getPaidSeats() {
return paidSeats;
}

public void setPaidSeats(String paidSeats) {
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

public String getUpdatedAt() {
return updatedAt;
}

public void setUpdatedAt(String updatedAt) {
this.updatedAt = updatedAt;
}

public String getCreatedAt() {
return createdAt;
}

public void setCreatedAt(String createdAt) {
this.createdAt = createdAt;
}

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

}