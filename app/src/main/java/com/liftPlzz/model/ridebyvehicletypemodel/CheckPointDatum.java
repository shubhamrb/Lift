package com.liftPlzz.model.ridebyvehicletypemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckPointDatum {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("lift_id")
@Expose
private Integer liftId;
@SerializedName("LatLng")
@Expose
private String latLng;
@SerializedName("date")
@Expose
private String date;
@SerializedName("time")
@Expose
private String time;
@SerializedName("country")
@Expose
private String country;
@SerializedName("state")
@Expose
private String state;
@SerializedName("city")
@Expose
private String city;
@SerializedName("location")
@Expose
private String location;
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

public Integer getLiftId() {
return liftId;
}

public void setLiftId(Integer liftId) {
this.liftId = liftId;
}

public String getLatLng() {
return latLng;
}

public void setLatLng(String latLng) {
this.latLng = latLng;
}

public String getDate() {
return date;
}

public void setDate(String date) {
this.date = date;
}

public String getTime() {
return time;
}

public void setTime(String time) {
this.time = time;
}

public String getCountry() {
return country;
}

public void setCountry(String country) {
this.country = country;
}

public String getState() {
return state;
}

public void setState(String state) {
this.state = state;
}

public String getCity() {
return city;
}

public void setCity(String city) {
this.city = city;
}

public String getLocation() {
return location;
}

public void setLocation(String location) {
this.location = location;
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