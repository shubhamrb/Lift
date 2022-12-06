package com.liftPlzz.presenter;

import com.liftPlzz.base.BaseActivity;
import com.liftPlzz.base.BasePresenter;
import com.liftPlzz.views.AddVehicleView;

public class AddVehiclePresenter extends BasePresenter<AddVehicleView> {


    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public void openHome() {
        navigator.openHomeFragment(BaseActivity.PerformFragment.REPLACE);
    }
}
