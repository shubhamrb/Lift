package com.liftPlzz.views;


import com.liftPlzz.base.RootView;
import com.liftPlzz.model.completedLift.CompleteLiftData;

import java.util.List;

public interface CompletedView extends RootView {
    void setLiftData(List<CompleteLiftData> lifts);

    void deleteLiftData(String msg);
}
