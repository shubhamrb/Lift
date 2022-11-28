package com.liftPlzz.views;


import com.liftPlzz.base.RootView;
import com.liftPlzz.model.getVehicle.getReview.Response;

public interface ReviewsView extends RootView {

    void setReviewData(Response response);

    void onSuccessLikeDislike(Response response);
}
