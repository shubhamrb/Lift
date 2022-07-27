package com.liftPlzz.views;


import com.liftPlzz.base.RootView;
import com.liftPlzz.model.getVehicle.getReview.Datum;
import com.liftPlzz.model.getVehicle.getReview.Response;

import java.util.List;

public interface ReviewsView extends RootView {

    void setReviewData(Response response);
    void onSuccessLikeDislike(Response response);
}
