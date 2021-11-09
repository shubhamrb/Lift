package com.liftPlzz.views;

import com.liftPlzz.base.RootView;
import com.liftPlzz.model.sendotp.SendOtpResponse;

public interface LoginView extends RootView {

    void setLoginData(SendOtpResponse response);
}
