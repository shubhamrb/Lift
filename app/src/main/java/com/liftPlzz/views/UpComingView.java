package com.liftPlzz.views;


import com.liftPlzz.base.RootView;
import com.liftPlzz.model.upcomingLift.Lift;

import java.util.List;

public interface UpComingView extends RootView {
    void setLiftData(List<Lift> lifts);

    void setCancelLiftData(String response);
}
