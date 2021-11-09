package com.liftPlzz.model.sendotp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MainResponse {

    @SerializedName("response")
    @Expose
    private SendOtpResponse response;

    public SendOtpResponse getResponse() {
        return response;
    }

    public void setResponse(SendOtpResponse response) {
        this.response = response;
    }

}
