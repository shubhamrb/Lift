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
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("feedback")
    @Expose
    private String feedback;
    @SerializedName("review_date")
    @Expose
    private String reviewDate;
    @SerializedName("total_like")
    @Expose
    private Integer total_like;

    @SerializedName("total_dislike")
    @Expose
    private Integer total_dislike;

    @SerializedName("is_like")
    @Expose
    private boolean is_like;

    @SerializedName("is_dislike")
    @Expose
    private boolean is_dislike;

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

    public Integer getTotal_like() {
        return total_like;
    }

    public void setTotal_like(Integer total_like) {
        this.total_like = total_like;
    }

    public Integer getTotal_dislike() {
        return total_dislike;
    }

    public void setTotal_dislike(Integer total_dislike) {
        this.total_dislike = total_dislike;
    }

    public boolean isIs_like() {
        return is_like;
    }

    public void setIs_like(boolean is_like) {
        this.is_like = is_like;
    }

    public boolean isIs_dislike() {
        return is_dislike;
    }

    public void setIs_dislike(boolean is_dislike) {
        this.is_dislike = is_dislike;
    }
}