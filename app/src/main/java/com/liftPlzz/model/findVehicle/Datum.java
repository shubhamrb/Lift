package com.liftPlzz.model.findVehicle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Datum {

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
@SerializedName("paid_seats")
@Expose
private Integer paidSeats;
@SerializedName("lift_date")
@Expose
private String liftDate;
@SerializedName("start_point")
@Expose
private String startPoint;
@SerializedName("end_point")
@Expose
private String endPoint;
@SerializedName("name")
@Expose
private String name;
@SerializedName("mobile")
@Expose
private String mobile;
@SerializedName("username")
@Expose
private String username;
@SerializedName("start_point_distance")
@Expose
private Double startPointDistance;
@SerializedName("end_point_distance")
@Expose
private Double endPointDistance;
@SerializedName("start_point_data")
@Expose
private StartPointData startPointData;
@SerializedName("end_point_data")
@Expose
private EndPointData endPointData;
@SerializedName("check_point_data")
@Expose
private List<CheckPointDatum> checkPointData = null;

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

public Double getStartPointDistance() {
return startPointDistance;
}

public void setStartPointDistance(Double startPointDistance) {
this.startPointDistance = startPointDistance;
}

public Double getEndPointDistance() {
return endPointDistance;
}

public void setEndPointDistance(Double endPointDistance) {
this.endPointDistance = endPointDistance;
}

public StartPointData getStartPointData() {
return startPointData;
}

public void setStartPointData(StartPointData startPointData) {
this.startPointData = startPointData;
}

public EndPointData getEndPointData() {
return endPointData;
}

public void setEndPointData(EndPointData endPointData) {
this.endPointData = endPointData;
}

public List<CheckPointDatum> getCheckPointData() {
return checkPointData;
}

public void setCheckPointData(List<CheckPointDatum> checkPointData) {
this.checkPointData = checkPointData;
}

}