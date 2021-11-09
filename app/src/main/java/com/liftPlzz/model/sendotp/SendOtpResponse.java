package com.liftPlzz.model.sendotp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendOtpResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("otp")
    @Expose
    private Integer otp;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("new_user")
    @Expose
    private Integer newUser;
    @SerializedName("data")
    @Expose
    private SendOtpData data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getNewUser() {
        return newUser;
    }

    public void setNewUser(Integer newUser) {
        this.newUser = newUser;
    }

    public SendOtpData getData() {
        return data;
    }

    public void setData(SendOtpData data) {
        this.data = data;
    }

}
