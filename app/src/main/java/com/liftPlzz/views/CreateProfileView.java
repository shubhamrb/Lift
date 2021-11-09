package com.liftPlzz.views;


import com.liftPlzz.base.RootView;
import com.liftPlzz.model.createProfile.Response;

public interface CreateProfileView extends RootView {
    void setProfileData(Response response);
}
