package com.liftPlzz.views;

import com.google.gson.JsonObject;
import com.liftPlzz.base.RootView;
import com.liftPlzz.model.createProfile.Response;
import com.liftPlzz.model.getsetting.Datum;

import java.util.List;

public interface SettingView extends RootView {
    void setSettings(List<Datum> datum);

    void setProfileData(Response datum);

    void onSuccessReset(JsonObject datum);
}
