package com.liftPlzz.model.getVehicle.getReview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

@SerializedName("review_id")
@Expose
private Integer reviewId;
@SerializedName("user_image")
@Expose
private String userImage;
@SerializedName("from_user")
@Expose
private String fromUser;
@SerializedName("to_user")
@Expose
private String toUser;
@SerializedName("review_category")
@Expose
private String reviewCategory;
@SerializedName("review_question")
@Expose
private String reviewQuestion;
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

public String getToUser() {
return toUser;
}

public void setToUser(String toUser) {
this.toUser = toUser;
}

public String getReviewCategory() {
return reviewCategory;
}

public void setReviewCategory(String reviewCategory) {
this.reviewCategory = reviewCategory;
}

public String getReviewQuestion() {
return reviewQuestion;
}

public void setReviewQuestion(String reviewQuestion) {
this.reviewQuestion = reviewQuestion;
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