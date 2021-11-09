package com.liftPlzz.model.vehiclesubcategory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleSubCategoryData {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("category")
@Expose
private String category;
@SerializedName("type")
@Expose
private String type;
@SerializedName("status")
@Expose
private Integer status;

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public String getCategory() {
return category;
}

public void setCategory(String category) {
this.category = category;
}

public String getType() {
return type;
}

public void setType(String type) {
this.type = type;
}

public Integer getStatus() {
return status;
}

public void setStatus(Integer status) {
this.status = status;
}

}