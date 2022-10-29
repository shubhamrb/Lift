package com.liftPlzz.presenter;

import com.google.gson.JsonObject;
import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BaseActivity;
import com.liftPlzz.base.BasePresenter;
import com.liftPlzz.model.createProfile.CreateProfileMainResponse;
import com.liftPlzz.model.getsetting.SettingModel;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.SettingView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingPresenter extends BasePresenter<SettingView> {


    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public void getSettingList(String token) {
//        view.setSettings();
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<SettingModel> call = api.getSettingList(Constants.API_KEY, Constants.ANDROID, token);
        call.enqueue(new Callback<SettingModel>() {
            @Override
            public void onResponse(Call<SettingModel> call, Response<SettingModel> response) {
                view.hideLoader();
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        view.setSettings(response.body());
                    } else {
                        view.showMessage(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<SettingModel> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage("Check your internet connection");
            }
        });
    }

    public void getProfile(String token) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<CreateProfileMainResponse> call = api.get_profile(Constants.API_KEY, "android", token);
        call.enqueue(new Callback<CreateProfileMainResponse>() {
            @Override
            public void onResponse(Call<CreateProfileMainResponse> call, Response<CreateProfileMainResponse> response) {
                view.hideLoader();

                if (response.body() != null) {
                    if (response.body().getResponse().getStatus()) {
                        view.setProfileData(response.body().getResponse());
                    } else {
                        //  view.hideLoader();
                        view.showMessage(response.body().getResponse().getMessage());
                    }


                }
            }


            @Override
            public void onFailure(Call<CreateProfileMainResponse> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage("Check your internet connection");
            }
        });
    }

    public void reset(String token) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<JsonObject> call = api.reset_setting(Constants.API_KEY, "android", token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                view.hideLoader();

                if (response.body() != null) {
                    if (response.body().get("status").getAsBoolean()) {
                        view.onSuccessReset(response.body());
                    } else {
                        //  view.hideLoader();
                        view.showMessage(response.body().get("message").getAsString());
                    }


                }
            }


            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage("Check your internet connection");
            }
        });
    }

    public void openUpdateProfile() {
        navigator.openUpdateProfileFragment(BaseActivity.PerformFragment.REPLACE);
    }

    public void openFaq() {
        navigator.openFaqFragment(BaseActivity.PerformFragment.REPLACE);
    }
    public void openHowToUse() {
        navigator.openVideosFragment(BaseActivity.PerformFragment.REPLACE);
    }

    public void openSettingOption(String title, int id) {
        navigator.openSettingOptionFragment(BaseActivity.PerformFragment.REPLACE, title, id);
    }

    public void openBlock() {
        navigator.openBlockFragment(BaseActivity.PerformFragment.REPLACE);
    }
}
