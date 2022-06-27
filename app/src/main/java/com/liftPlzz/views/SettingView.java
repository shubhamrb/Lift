package com.liftPlzz.views;

import com.liftPlzz.base.RootView;
import com.liftPlzz.model.getsetting.Datum;

import java.util.List;

public interface SettingView extends RootView {
    void setSettings(List<Datum> datum);
}
