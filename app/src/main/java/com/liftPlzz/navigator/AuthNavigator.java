package com.liftPlzz.navigator;

import com.liftPlzz.base.BaseActivity;

public interface AuthNavigator {

    void openLoginFragment(BaseActivity.PerformFragment performFragment);

    void openOTPFragment(BaseActivity.PerformFragment performFragment);
    void openCreateProfileFragment(BaseActivity.PerformFragment performFragment);
}
