package com.liftPlzz.presenter;

import com.liftPlzz.base.BaseActivity;
import com.liftPlzz.base.BasePresenter;
import com.liftPlzz.views.MatchingRideView;

public class MatchingRidePresenter extends BasePresenter<MatchingRideView> {

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public void openDriverList(boolean isFind, Integer lift_id, String vehicle_type, Integer vehicle_subcategory) {
        navigator.openDriverList(BaseActivity.PerformFragment.REPLACE, isFind, lift_id, vehicle_type, vehicle_subcategory);
    }
}
