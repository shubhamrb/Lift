package com.liftPlzz.provider;

import com.liftPlzz.base.BaseActivity;
import com.liftPlzz.fragments.AddVehicleFragment;
import com.liftPlzz.fragments.ChatUserFragment;
import com.liftPlzz.fragments.ContactsFragment;
import com.liftPlzz.fragments.CreateProfileFragment;
import com.liftPlzz.fragments.ELearningFragment;
import com.liftPlzz.fragments.FaqFragment;
import com.liftPlzz.fragments.FeedbackFragment;
import com.liftPlzz.fragments.HomeFragment;
import com.liftPlzz.fragments.LoginFragment;
import com.liftPlzz.fragments.MyRidesFragment;
import com.liftPlzz.fragments.MyVehicleFragment;
import com.liftPlzz.fragments.NotificationFragment;
import com.liftPlzz.fragments.OTpFragment;
import com.liftPlzz.fragments.ProfileFragment;
import com.liftPlzz.fragments.SettingFragment;
import com.liftPlzz.fragments.UpdateProfileFragment;
import com.liftPlzz.navigator.AppNavigator;

public abstract class AppNavigationProvider extends BaseActivity implements AppNavigator {

    @Override
    public void openLoginFragment(PerformFragment performFragment) {
        LoginFragment loginFragment = new LoginFragment();
        openFragment(loginFragment, LoginFragment.class.getName(), performFragment, false);
    }


    @Override
    public void openCreateProfileFragment(PerformFragment performFragment) {
        CreateProfileFragment createProfileFragment = new CreateProfileFragment();
        openFragment(createProfileFragment, CreateProfileFragment.class.getName(), performFragment, false);
    }

    @Override
    public void openMyChatFragment(PerformFragment performFragment) {
        ChatUserFragment chatUserFragment = new ChatUserFragment();
        openFragment(chatUserFragment, ChatUserFragment.class.getName(), performFragment, true);
    }

    @Override
    public void openOTPFragment(PerformFragment performFragment) {
        OTpFragment oTpFragment = new OTpFragment();
        openFragment(oTpFragment, LoginFragment.class.getName(), performFragment, false);
    }

    @Override
    public void openHomeFragment(PerformFragment performFragment) {
        HomeFragment homeFragment = new HomeFragment();
        openFragment(homeFragment, HomeFragment.class.getName(), performFragment, false);
    }

    @Override
    public void openFeedbackFragment(PerformFragment performFragment) {
        FeedbackFragment feedbackFragment = new FeedbackFragment();
        openFragment(feedbackFragment, FeedbackFragment.class.getName(), performFragment, true);
    }

    @Override
    public void openLearningFragment(PerformFragment performFragment) {
        ELearningFragment eLearningFragment = new ELearningFragment();
        openFragment(eLearningFragment, ELearningFragment.class.getName(), performFragment, true);
    }

    @Override
    public void openAddVehicleFragment(PerformFragment performFragment) {
        AddVehicleFragment addVehicleFragment = new AddVehicleFragment();
        openFragment(addVehicleFragment, AddVehicleFragment.class.getName(), performFragment, true);
    }

    @Override
    public void openMyRidesFragment(PerformFragment performFragment) {
        MyRidesFragment myRidesFragment = new MyRidesFragment();
        openFragment(myRidesFragment, MyRidesFragment.class.getName(), performFragment, true);
    }

    @Override
    public void openContactsFragment(PerformFragment performFragment) {
        ContactsFragment contactsFragment = new ContactsFragment();
        openFragment(contactsFragment, ContactsFragment.class.getName(), performFragment, true);
    }

    @Override
    public void openProfileFragment(PerformFragment performFragment) {
        ProfileFragment profileFragment = new ProfileFragment();
        openFragment(profileFragment, ProfileFragment.class.getName(), performFragment, true);
    }

    @Override
    public void openUpdateProfileFragment(PerformFragment performFragment) {
        UpdateProfileFragment profileFragment = new UpdateProfileFragment();
        openFragment(profileFragment, UpdateProfileFragment.class.getName(), performFragment, true);
    }

    @Override
    public void openMyVehicleFragment(PerformFragment performFragment) {
        MyVehicleFragment myVehicleFragment = new MyVehicleFragment();
        openFragment(myVehicleFragment, MyVehicleFragment.class.getName(), performFragment, true);
    }

    @Override
    public void openNotificationFragment(PerformFragment performFragment) {
        NotificationFragment notificationFragment = new NotificationFragment();
        openFragment(notificationFragment, NotificationFragment.class.getName(), performFragment, true);
    }

//    @Override
//    public void openDriverListFragment(PerformFragment performFragment) {
//        SettingFragment driverListFragment = new SettingFragment();
//        openFragment(driverListFragment, SettingFragment.class.getName(), performFragment, true);
//    }

    @Override
    public void openSettingFragment(PerformFragment performFragment) {
        SettingFragment settingFragment = new SettingFragment();
        openFragment(settingFragment, SettingFragment.class.getName(), performFragment, true);
    }

    @Override
    public void openFaqFragment(PerformFragment performFragment) {
        FaqFragment faqFragment = new FaqFragment();
        openFragment(faqFragment, FaqFragment.class.getName(), performFragment, true);
    }
}
