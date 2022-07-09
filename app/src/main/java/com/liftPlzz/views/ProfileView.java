package com.liftPlzz.views;


import com.liftPlzz.base.RootView;
import com.liftPlzz.model.createProfile.Response;
import com.liftPlzz.model.getVehicle.getReview.Datum;

import java.util.List;

public interface ProfileView extends RootView {


    void setProfileData(Response response);

    void selfieUploaded(Response response);
    void idUploaded(Response response);

    void setProfileImageData(String message);

    void setReviewData(List<Datum> data);
}
