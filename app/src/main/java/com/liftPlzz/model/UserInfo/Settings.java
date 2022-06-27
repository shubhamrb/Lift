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

    @SerializedName("lift_for_whome")
    @Expose
    private Integer lift_for_whome;

    @SerializedName("age_between")
    @Expose
    private Integer age_between;

    @SerializedName("rating_selection")
    @Expose
    private Integer rating_selection;

    @SerializedName("professional_status")
    @Expose
    private Integer professional_status;

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

    public Integer getLift_for_whome() {
        return lift_for_whome;
    }

    public void setLift_for_whome(Integer lift_for_whome) {
        this.lift_for_whome = lift_for_whome;
    }

    public Integer getAge_between() {
        return age_between;
    }

    public void setAge_between(Integer age_between) {
        this.age_between = age_between;
    }

    public Integer getRating_selection() {
        return rating_selection;
    }

    public void setRating_selection(Integer rating_selection) {
        this.rating_selection = rating_selection;
    }

    public Integer getProfessional_status() {
        return professional_status;
    }

    public void setProfessional_status(Integer professional_status) {
        this.professional_status = professional_status;
    }
}