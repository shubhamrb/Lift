package com.liftPlzz.presenter;

import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BasePresenter;
import com.liftPlzz.model.completedLift.ResponseCompletedLift;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.CompletedView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompletedPresenter extends BasePresenter<CompletedView> {


    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public void getCompletedLift(String token) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<ResponseCompletedLift> call = api.getCompletedLift(Constants.API_KEY, Constants.ANDROID, token);
        call.enqueue(new Callback<ResponseCompletedLift>() {
            @Override
            public void onResponse(Call<ResponseCompletedLift> call, Response<ResponseCompletedLift> response) {
                view.hideLoader();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        if (response.body().getLifts() != null && response.body().getLifts().size() > 0) {
                            view.setLiftData(response.body().getLifts());
                        } else {
                            view.showMessage(response.body().getMessage());
                        }
                    } else {
                        view.showMessage(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseCompletedLift> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage(throwable.getMessage());
            }
        });
    }
}
