package com.liftPlzz.model.verifyOtp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.liftPlzz.model.sendotp.SendOtpResponse;

public class VerifyOtpMainResponse {

    @SerializedName("response")
    @Expose
    private VerifyOtpResponse response;

    public VerifyOtpResponse getResponse() {
        return response;
    }

    public void setResponse(VerifyOtpResponse response) {
        this.response = response;
    }

}
