package com.liftPlzz.presenter;

import com.google.gson.JsonObject;
import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BaseActivity;
import com.liftPlzz.base.BasePresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.PointRedemptionView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PointRedemptionPresenter extends BasePresenter<PointRedemptionView> {


    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public void getHistory(String token) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<JsonObject> call = api.get_redemption_history(Constants.API_KEY, "android", token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                view.hideLoader();

                if (response.body() != null) {
                    if (response.body().get("response").getAsJsonObject().get("status").getAsBoolean()) {
                        view.setHistory(response.body().get("response").getAsJsonObject());
                    } else {
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

    public void getAccounts(String token) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<JsonObject> call = api.get_redemption_data(Constants.API_KEY, "android", token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                view.hideLoader();

                if (response.body() != null) {
                    if (response.body().get("response").getAsJsonObject().get("status").getAsBoolean()) {
                        view.setAccounts(response.body().get("response").getAsJsonObject());
                    } else {
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

    public void redeemRequest(String token, String points, String desc, String account) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<JsonObject> call = api.redeemRequest(Constants.API_KEY, Constants.ANDROID, token, points, desc, account);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                view.hideLoader();

                if (response.body() != null) {
                    if (response.body().get("status").getAsBoolean()) {
                        view.onSubmit(response.body().get("message").getAsString());
                    } else {
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

    public void openAddAccount() {
        navigator.openAddAccount(BaseActivity.PerformFragment.REPLACE);

    }
}
