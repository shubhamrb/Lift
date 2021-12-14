package com.liftPlzz.navigator;


import com.liftPlzz.base.BaseActivity;

public interface HomeNavigator {

    void openHomeFragment(BaseActivity.PerformFragment performFragment);

    void openFeedbackFragment(BaseActivity.PerformFragment performFragment);

    void openLearningFragment(BaseActivity.PerformFragment performFragment);

    void openAddVehicleFragment(BaseActivity.PerformFragment performFragment);

    void openMyRidesFragment(BaseActivity.PerformFragment performFragment);

    void openContactsFragment(BaseActivity.PerformFragment performFragment);

    void openProfileFragment(BaseActivity.PerformFragment performFragment);

    void openMyVehicleFragment(BaseActivity.PerformFragment performFragment);

    void openMyChatFragment(BaseActivity.PerformFragment performFragment);

    void openNotificationFragment(BaseActivity.PerformFragment performFragment);

    void openUpdateProfileFragment(BaseActivity.PerformFragment performFragment);

    //    void openDriverListFragment(BaseActivity.PerformFragment performFragment);
    void openSettingFragment(BaseActivity.PerformFragment performFragment);

    void openFaqFragment(BaseActivity.PerformFragment performFragment);

}
