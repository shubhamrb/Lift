package com.liftPlzz.model.resendOtp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

@SerializedName("status")
@Expose
private Boolean status;
@SerializedName("message")
@Expose
private String message;
@SerializedName("otp")
@Expose
private String otp;

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

public String getOtp() {
return otp;
}

public void setOtp(String otp) {
this.otp = otp;
}

}