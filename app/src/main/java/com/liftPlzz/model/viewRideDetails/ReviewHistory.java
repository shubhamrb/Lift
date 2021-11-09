package com.liftPlzz.model.viewRideDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewHistory {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("from_user")
@Expose
private Integer fromUser;
@SerializedName("to_user")
@Expose
private Integer toUser;
@SerializedName("review_category")
@Expose
private Integer reviewCategory;
@SerializedName("review_question")
@Expose
private Integer reviewQuestion;
@SerializedName("rating")
@Expose
private Integer rating;
@SerializedName("feedback")
@Expose
private String feedback;
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

public Integer getFromUser() {
return fromUser;
}

public void setFromUser(Integer fromUser) {
this.fromUser = fromUser;
}

public Integer getToUser() {
return toUser;
}

public void setToUser(Integer toUser) {
this.toUser = toUser;
}

public Integer getReviewCategory() {
return reviewCategory;
}

public void setReviewCategory(Integer reviewCategory) {
this.reviewCategory = reviewCategory;
}

public Integer getReviewQuestion() {
return reviewQuestion;
}

public void setReviewQuestion(Integer reviewQuestion) {
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