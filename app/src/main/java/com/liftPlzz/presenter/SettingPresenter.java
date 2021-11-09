package com.liftPlzz.presenter;

import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BasePresenter;
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
                        view.setSettings(response.body().getData());
                    } else {
                        view.showMessage(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<SettingModel> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage(throwable.getMessage());
            }
        });

    }
}
