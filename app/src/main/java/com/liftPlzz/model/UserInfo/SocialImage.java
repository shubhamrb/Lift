package com.liftPlzz.model.UserInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SocialImage {

@SerializedName("image_id")
@Expose
private Integer imageId;
@SerializedName("image")
@Expose
private String image;

public Integer getImageId() {
return imageId;
}

public void setImageId(Integer imageId) {
this.imageId = imageId;
}

public String getImage() {
return image;
}

public void setImage(String image) {
this.image = image;
}

}