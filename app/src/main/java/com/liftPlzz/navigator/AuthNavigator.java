package com.liftPlzz.navigator;

import com.liftPlzz.base.BaseActivity;

public interface AuthNavigator {

    void openLoginFragment(BaseActivity.PerformFragment performFragment, String referral_id);

    void openOTPFragment(BaseActivity.PerformFragment performFragment, String referral_id);

    void openCreateProfileFragment(BaseActivity.PerformFragment performFragment,String referral_id);
}
