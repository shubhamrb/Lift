package com.liftPlzz.views;

public interface OtpReceivedInterface {
    void onOtpReceived(String otp);

    void onOtpTimeout();
}