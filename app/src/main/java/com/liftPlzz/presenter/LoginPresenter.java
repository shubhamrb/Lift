package com.liftPlzz.presenter;

import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BaseActivity;
import com.liftPlzz.base.BasePresenter;
import com.liftPlzz.model.sendotp.MainResponse;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.LoginView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenter extends BasePresenter<LoginView> {


    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public void openOTP() {
        navigator.openOTPFragment(BaseActivity.PerformFragment.REPLACE);
    }

    public void sendOtp(String mobile) {
         view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<MainResponse> call = api.sendOTP(Constants.API_KEY, "android", mobile);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                view.hideLoader();

                if (response.body() != null) {
                    if (response.body().getResponse().getStatus()) {
                            view.setLoginData(response.body().getResponse());

                    } else {
                        //  view.hideLoader();
                        view.showMessage(response.body().getResponse().getMessage());
                    }


                }
            }


            @Override
            public void onFailure(Call<MainResponse> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage(throwable.getMessage());
            }
        });
    }
}
