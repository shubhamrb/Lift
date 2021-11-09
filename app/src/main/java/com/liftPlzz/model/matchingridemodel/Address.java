package com.liftPlzz.model.matchingridemodel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Address {

@SerializedName("start_points")
@Expose
private List<String> startPoints = null;
@SerializedName("end_points")
@Expose
private List<String> endPoints = null;
@SerializedName("start_location")
@Expose
private String startLocation;
@SerializedName("end_location")
@Expose
private String endLocation;

public List<String> getStartPoints() {
return startPoints;
}

public void setStartPoints(List<String> startPoints) {
this.startPoints = startPoints;
}

public List<String> getEndPoints() {
return endPoints;
}

public void setEndPoints(List<String> endPoints) {
this.endPoints = endPoints;
}

public String getStartLocation() {
return startLocation;
}

public void setStartLocation(String startLocation) {
this.startLocation = startLocation;
}

public String getEndLocation() {
return endLocation;
}

public void setEndLocation(String endLocation) {
this.endLocation = endLocation;
}

}