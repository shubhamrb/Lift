package com.liftPlzz.views;

import com.google.gson.JsonObject;
import com.liftPlzz.base.RootView;

public interface GoGreenView extends RootView {
    void setData(JsonObject datum);

    void onSubmit(String message);
}
