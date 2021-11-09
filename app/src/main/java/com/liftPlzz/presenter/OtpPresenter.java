package com.liftPlzz.presenter;

import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BaseActivity;
import com.liftPlzz.base.BasePresenter;

import com.liftPlzz.model.resendOtp.ResendOtpResponse;
import com.liftPlzz.model.sendotp.MainResponse;
import com.liftPlzz.model.verifyOtp.VerifyOtpMainResponse;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.OtpView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpPresenter extends BasePresenter<OtpView> {


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

    public void ResendOtp(String mobile) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<ResendOtpResponse> call = api.resendOTP(Constants.API_KEY, "android", mobile);
        call.enqueue(new Callback<ResendOtpResponse>() {
            @Override
            public void onResponse(Call<ResendOtpResponse> call, Response<ResendOtpResponse> response) {
                view.hideLoader();

                if (response.body() != null) {
                    if (response.body().getResponse().getStatus()) {
                        view.setResendData(response.body().getResponse());

                    } else {
                        //  view.hideLoader();
                        view.showMessage(response.body().getResponse().getMessage());
                    }


                }
            }


            @Override
            public void onFailure(Call<ResendOtpResponse> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage(throwable.getMessage());
            }
        });
    }

    public void verifyOtp(String otp, String mobile) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<VerifyOtpMainResponse> call = api.verifyOTP(Constants.API_KEY, "android", otp, mobile);
        call.enqueue(new Callback<VerifyOtpMainResponse>() {
            @Override
            public void onResponse(Call<VerifyOtpMainResponse> call, Response<VerifyOtpMainResponse> response) {
                view.hideLoader();

                if (response.body() != null) {
                    if (response.body().getResponse().getStatus()) {
//                        view.setLoginData(response.body().getResponse());

                    } else {
                        //  view.hideLoader();
//                        view.showMessage(response.body().getResponse().getMessage());
                    }


                }
            }


            @Override
            public void onFailure(Call<VerifyOtpMainResponse> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage(throwable.getMessage());
            }
        });
    }

    public void openCreateProfile() {
        navigator.openCreateProfileFragment(BaseActivity.PerformFragment.REPLACE);
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
