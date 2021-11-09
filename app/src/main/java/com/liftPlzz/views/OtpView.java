package com.liftPlzz.views;

import com.liftPlzz.base.RootView;
import com.liftPlzz.model.resendOtp.Response;
import com.liftPlzz.model.sendotp.SendOtpResponse;
import com.liftPlzz.model.verifyOtp.VerifyOtpResponse;

public interface OtpView extends RootView {

    void setLoginData(SendOtpResponse response);

    void setResendData(Response response);
}
