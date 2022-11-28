package com.liftPlzz.views;

import com.liftPlzz.base.RootView;
import com.liftPlzz.model.resendOtp.Response;
import com.liftPlzz.model.sendotp.SendOtpResponse;

public interface OtpView extends RootView {

    void setLoginData(SendOtpResponse response);

    void setResendData(Response response);
}
