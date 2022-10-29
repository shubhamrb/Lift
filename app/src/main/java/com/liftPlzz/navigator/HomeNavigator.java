package com.liftPlzz.navigator;


import android.os.Bundle;

import com.liftPlzz.base.BaseActivity;
import com.liftPlzz.fragments.PointWalletFragment;
import com.liftPlzz.model.getsetting.SettingModel;

public interface HomeNavigator {

    void openHomeFragment(BaseActivity.PerformFragment performFragment);

    void openHomeFragment(BaseActivity.PerformFragment performFragment, Bundle bundle);

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

    void openSettingOptionFragment(BaseActivity.PerformFragment performFragment, String title, int id);

    void openBlockFragment(BaseActivity.PerformFragment performFragment);

    void openUsersFragment(BaseActivity.PerformFragment performFragment);

    void openReviewsFragment(BaseActivity.PerformFragment performFragment);

    void openSettingFragment(BaseActivity.PerformFragment performFragment);

    void openFaqFragment(BaseActivity.PerformFragment performFragment);

    void openPointWalletFragment(BaseActivity.PerformFragment performFragment);

    PointWalletFragment getPointWalletFragment();

    void openFeedbackSuggestionFragment(BaseActivity.PerformFragment performFragment, String type);

    void openVideosFragment(BaseActivity.PerformFragment performFragment);

}
