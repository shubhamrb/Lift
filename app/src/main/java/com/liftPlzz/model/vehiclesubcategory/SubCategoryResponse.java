package com.liftPlzz.model.vehiclesubcategory;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubCategoryResponse {

@SerializedName("status")
@Expose
private Boolean status;
@SerializedName("message")
@Expose
private String message;
@SerializedName("data")
@Expose
private List<VehicleSubCategoryData> data = null;

public Boolean getStatus() {
return status;
}

public void setStatus(Boolean status) {
this.status = status;
}

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public List<VehicleSubCategoryData> getData() {
return data;
}

public void setData(List<VehicleSubCategoryData> data) {
this.data = data;
}

}