package com.liftPlzz.model.UserInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Settings {

    @SerializedName("chat")
    @Expose
    private Integer chat;
    @SerializedName("call")
    @Expose
    private Integer call;
    @SerializedName("profile_publicly")
    @Expose
    private Integer profilePublicly;

    public Integer getChat() {
        return chat;
    }

    public void setChat(Integer chat) {
        this.chat = chat;
    }

    public Integer getCall() {
        return call;
    }

    public void setCall(Integer call) {
        this.call = call;
    }

    public Integer getProfilePublicly() {
        return profilePublicly;
    }

    public void setProfilePublicly(Integer profilePublicly) {
        this.profilePublicly = profilePublicly;
    }

}