package com.liftPlzz.model.vehiclesubcategory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleSubCategoryModel {

@SerializedName("response")
@Expose
private SubCategoryResponse response;

public SubCategoryResponse getResponse() {
return response;
}

public void setResponse(SubCategoryResponse response) {
this.response = response;
}

}