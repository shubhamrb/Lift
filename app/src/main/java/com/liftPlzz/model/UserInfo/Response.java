package com.liftPlzz.model.UserInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

@SerializedName("status")
@Expose
private Boolean status;
@SerializedName("message")
@Expose
private String message;
@SerializedName("user")
@Expose
private User user;

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

public User getUser() {
return user;
}

public void setUser(User user) {
this.user = user;
}

}