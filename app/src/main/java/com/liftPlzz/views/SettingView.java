package com.liftPlzz.views;

import com.google.gson.JsonObject;
import com.liftPlzz.base.RootView;
import com.liftPlzz.model.createProfile.Response;
import com.liftPlzz.model.getsetting.SettingModel;

public interface SettingView extends RootView {
    void setSettings(SettingModel body);

    void setProfileData(Response datum);

    void onSuccessReset(JsonObject datum);

    void onSuccessAccountSuspend(JsonObject datum);
}
