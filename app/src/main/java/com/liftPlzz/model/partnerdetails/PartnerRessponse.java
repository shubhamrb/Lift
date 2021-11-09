package com.liftPlzz.model.partnerdetails;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PartnerRessponse {

@SerializedName("status")
@Expose
private Boolean status;
@SerializedName("message")
@Expose
private String message;
@SerializedName("user")
@Expose
private ArrayList<User> user = null;

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

public ArrayList<User> getUser() {
return user;
}

public void setUser(ArrayList<User> user) {
this.user = user;
}

}