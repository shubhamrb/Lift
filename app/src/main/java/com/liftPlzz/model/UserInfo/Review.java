package com.liftPlzz.model.UserInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review {

@SerializedName("review_id")
@Expose
private Integer reviewId;
@SerializedName("user_image")
@Expose
private String userImage;
@SerializedName("from_user")
@Expose
private String fromUser;
@SerializedName("rating")
@Expose
private Integer rating;
@SerializedName("feedback")
@Expose
private String feedback;
@SerializedName("review_date")
@Expose
private String reviewDate;

public Integer getReviewId() {
return reviewId;
}

public void setReviewId(Integer reviewId) {
this.reviewId = reviewId;
}

public String getUserImage() {
return userImage;
}

public void setUserImage(String userImage) {
this.userImage = userImage;
}

public String getFromUser() {
return fromUser;
}

public void setFromUser(String fromUser) {
this.fromUser = fromUser;
}

public Integer getRating() {
return rating;
}

public void setRating(Integer rating) {
this.rating = rating;
}

public String getFeedback() {
return feedback;
}

public void setFeedback(String feedback) {
this.feedback = feedback;
}

public String getReviewDate() {
return reviewDate;
}

public void setReviewDate(String reviewDate) {
this.reviewDate = reviewDate;
}

}