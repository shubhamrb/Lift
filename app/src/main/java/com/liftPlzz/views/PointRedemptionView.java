package com.liftPlzz.views;

import com.google.gson.JsonObject;
import com.liftPlzz.base.RootView;

public interface PointRedemptionView extends RootView {
    void setHistory(JsonObject datum);

    void setAccounts(JsonObject datum);

    void onSubmit(String message);
}
