package com.liftPlzz.presenter;

import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BaseActivity;
import com.liftPlzz.base.BasePresenter;
import com.liftPlzz.model.ReferralDataResponse;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.ReferView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReferPresenter extends BasePresenter<ReferView> {


    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public void getData(String token) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<ReferralDataResponse> call = api.get_referral_data(Constants.API_KEY, "android", token);
        call.enqueue(new Callback<ReferralDataResponse>() {
            @Override
            public void onResponse(Call<ReferralDataResponse> call, Response<ReferralDataResponse> response) {
                view.hideLoader();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        view.setData(response.body().getData());
                    } else {
                        view.showMessage(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ReferralDataResponse> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage("Check your internet connection");
            }
        });
    }

    public void openGoGreenFragment() {
        navigator.openGoGreenFragment(BaseActivity.PerformFragment.REPLACE);
    }
}
