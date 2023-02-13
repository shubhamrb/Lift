package com.liftPlzz.provider;

import android.os.Bundle;

import com.liftPlzz.base.BaseActivity;
import com.liftPlzz.dialog.EditLiftDaiFragment;
import com.liftPlzz.fragments.AddAccountFragment;
import com.liftPlzz.fragments.AddVehicleFragment;
import com.liftPlzz.fragments.BlockFragment;
import com.liftPlzz.fragments.ChatUserFragment;
import com.liftPlzz.fragments.ContactsFragment;
import com.liftPlzz.fragments.CreateProfileFragment;
import com.liftPlzz.fragments.DriverListFragment;
import com.liftPlzz.fragments.ELearningFragment;
import com.liftPlzz.fragments.FaqFragment;
import com.liftPlzz.fragments.FeedbackFragment;
import com.liftPlzz.fragments.FeedbackSuggestionFragment;
import com.liftPlzz.fragments.GoGreenFragment;
import com.liftPlzz.fragments.HomeFragment;
import com.liftPlzz.fragments.LoginFragment;
import com.liftPlzz.fragments.MatchingRideFragment;
import com.liftPlzz.fragments.MyRidesFragment;
import com.liftPlzz.fragments.MyVehicleFragment;
import com.liftPlzz.fragments.NotificationFragment;
import com.liftPlzz.fragments.OTpFragment;
import com.liftPlzz.fragments.PointRedemptionFragment;
import com.liftPlzz.fragments.PointWalletFragment;
import com.liftPlzz.fragments.ProfileFragment;
import com.liftPlzz.fragments.ReferFragment;
import com.liftPlzz.fragments.ReviewsFragment;
import com.liftPlzz.fragments.RideRequestFragment;
import com.liftPlzz.fragments.SettingFragment;
import com.liftPlzz.fragments.SettingOptionFragment;
import com.liftPlzz.fragments.UpdateProfileFragment;
import com.liftPlzz.fragments.UsersFragment;
import com.liftPlzz.fragments.VideosFragment;
import com.liftPlzz.model.upcomingLift.Lift;
import com.liftPlzz.navigator.AppNavigator;
import com.liftPlzz.utils.Constants;

public abstract class AppNavigationProvider extends BaseActivity implements AppNavigator {

    private PointWalletFragment pointWalletFragment;

    @Override
    public void openLoginFragment(PerformFragment performFragment, String referral_id) {
        Bundle bundle = new Bundle();
        bundle.putString("referral_id", referral_id);
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setArguments(bundle);
        openFragment(loginFragment, LoginFragment.class.getName(), performFragment, false);
    }


    @Override
    public void openCreateProfileFragment(PerformFragment performFragment, String referral_id) {
        Bundle bundle = new Bundle();
        bundle.putString("referral_id", referral_id);
        CreateProfileFragment createProfileFragment = new CreateProfileFragment();
        createProfileFragment.setArguments(bundle);
        openFragment(createProfileFragment, CreateProfileFragment.class.getName(), performFragment, false);
    }

    @Override
    public void openMyChatFragment(PerformFragment performFragment) {
        ChatUserFragment chatUserFragment = new ChatUserFragment();
        openFragment(chatUserFragment, ChatUserFragment.class.getName(), performFragment, true);
    }

    @Override
    public void openReferFragment(PerformFragment performFragment) {
        ReferFragment referFragment = new ReferFragment();
        openFragment(referFragment, ReferFragment.class.getName(), performFragment, true);
    }

    @Override
    public void openOTPFragment(PerformFragment performFragment, String referral_id) {
        Bundle bundle = new Bundle();
        bundle.putString("referral_id", referral_id);
        OTpFragment oTpFragment = new OTpFragment();
        oTpFragment.setArguments(bundle);
        openFragment(oTpFragment, LoginFragment.class.getName(), performFragment, false);
    }

    @Override
    public void openHomeFragment(PerformFragment performFragment) {
        HomeFragment homeFragment = new HomeFragment();
        openFragment(homeFragment, HomeFragment.class.getName(), performFragment, false);
    }

    @Override
    public void openHomeFragment(PerformFragment performFragment, Bundle bundle) {
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(bundle);
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

    @Override
    public void openVideosFragment(PerformFragment performFragment) {
        VideosFragment videosFragment = new VideosFragment();
        openFragment(videosFragment, VideosFragment.class.getName(), performFragment, true);
    }

    @Override
    public void openPointWalletFragment(PerformFragment performFragment) {
        pointWalletFragment = new PointWalletFragment();
        openFragment(pointWalletFragment, PointWalletFragment.class.getName(), performFragment, true);
    }

    @Override
    public PointWalletFragment getPointWalletFragment() {
        return pointWalletFragment;
    }

    @Override
    public void openFeedbackSuggestionFragment(PerformFragment performFragment, String type) {
        FeedbackSuggestionFragment feedbackSuggestionFragment = new FeedbackSuggestionFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        feedbackSuggestionFragment.setArguments(bundle);
        openFragment(feedbackSuggestionFragment, FeedbackSuggestionFragment.class.getName(), performFragment, true);
    }

    @Override
    public void openReviewsFragment(PerformFragment performFragment) {
        ReviewsFragment reviewsFragment = new ReviewsFragment();
        openFragment(reviewsFragment, ReviewsFragment.class.getName(), performFragment, true);
    }

    @Override
    public void openBlockFragment(PerformFragment performFragment) {
        BlockFragment blockFragment = new BlockFragment();
        openFragment(blockFragment, BlockFragment.class.getName(), performFragment, true);
    }

    @Override
    public void openUsersFragment(PerformFragment performFragment) {
        UsersFragment blockFragment = new UsersFragment();
        openFragment(blockFragment, UsersFragment.class.getName(), performFragment, true);
    }

    @Override
    public void openSettingOptionFragment(PerformFragment performFragment, String title, int id) {
        SettingOptionFragment settingOptionFragment = new SettingOptionFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putInt("id", id);
        settingOptionFragment.setArguments(bundle);
        openFragment(settingOptionFragment, SettingOptionFragment.class.getName(), performFragment, true);
    }

    @Override
    public void openEditLift(PerformFragment performFragment, Lift lift, EditLiftDaiFragment.UpdateRecordListiner listinerUpdate, String edit) {
        EditLiftDaiFragment editLiftDaiFragment = new EditLiftDaiFragment(edit);
        editLiftDaiFragment.setLift(lift, listinerUpdate, edit);
        openFragment(editLiftDaiFragment, EditLiftDaiFragment.class.getName(), performFragment, true);
    }

    @Override
    public void openDriverList(PerformFragment performFragment, boolean isFind, Integer lift_id, String vehicle_type, Integer vehicle_subcategory) {
        DriverListFragment driverListFragment = new DriverListFragment();

        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.IS_FIND_LIFT, isFind);
        bundle.putInt(Constants.LIFT_ID, lift_id);
        bundle.putString(Constants.VEHICLE_TYPE, vehicle_type);
        bundle.putInt(Constants.SUB_CATEGORY_ID, vehicle_subcategory);
        driverListFragment.setArguments(bundle);
        openFragment(driverListFragment, DriverListFragment.class.getName(), performFragment, true);
    }

    @Override
    public void openMatchingRide(PerformFragment performFragment, boolean isFind, Integer lift_id, String vehicle_type, Integer vehicle_subcategory) {
        MatchingRideFragment matchingRideFragment = new MatchingRideFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.IS_FIND_LIFT, isFind);
        bundle.putInt(Constants.LIFT_ID, lift_id);
        bundle.putString(Constants.VEHICLE_TYPE, vehicle_type);
        if (vehicle_subcategory != null)
            bundle.putInt(Constants.SUB_CATEGORY_ID, vehicle_subcategory);
        matchingRideFragment.setArguments(bundle);
        openFragment(matchingRideFragment, MatchingRideFragment.class.getName(), performFragment, true);
    }

    @Override
    public void openMatchingRide(PerformFragment performFragment, Integer lift_id) {
        MatchingRideFragment matchingRideFragment = new MatchingRideFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.LIFT_ID, lift_id);
        matchingRideFragment.setArguments(bundle);
        openFragment(matchingRideFragment, MatchingRideFragment.class.getName(), performFragment, true);
    }

    @Override
    public void openRideRequests(PerformFragment performFragment, boolean isLifter, Integer lift_id, boolean partner, String from) {
        RideRequestFragment rideRequestFragment = new RideRequestFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("lifter", isLifter);
        bundle.putInt(Constants.LIFT_ID, lift_id);
        bundle.putBoolean(Constants.PARTNER, partner);
        bundle.putString("from", from);
        rideRequestFragment.setArguments(bundle);
        openFragment(rideRequestFragment, RideRequestFragment.class.getName(), performFragment, true);
    }

    @Override
    public void openGoGreenFragment(PerformFragment performFragment) {
        GoGreenFragment goGreenFragment = new GoGreenFragment();
        openFragment(goGreenFragment, GoGreenFragment.class.getName(), performFragment, true);
    }

    @Override
    public void openPointRedemption(PerformFragment performFragment) {
        PointRedemptionFragment pointRedemptionFragment = new PointRedemptionFragment();
        openFragment(pointRedemptionFragment, PointRedemptionFragment.class.getName(), performFragment, true);
    }

    @Override
    public void openAddAccount(PerformFragment performFragment) {
        AddAccountFragment addAccountFragment = new AddAccountFragment();
        openFragment(addAccountFragment, AddAccountFragment.class.getName(), performFragment, true);
    }
}
