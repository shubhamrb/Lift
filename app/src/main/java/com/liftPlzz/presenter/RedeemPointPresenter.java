package com.liftPlzz.presenter;

import com.google.gson.JsonObject;
import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BaseActivity;
import com.liftPlzz.base.BasePresenter;
import com.liftPlzz.dialog.EditLiftDaiFragment;
import com.liftPlzz.model.upcomingLift.Lift;
import com.liftPlzz.views.RedeemPointView;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RedeemPointPresenter extends BasePresenter<RedeemPointView> {


    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public void openPointRedemption() {
        navigator.openPointRedemption(BaseActivity.PerformFragment.REPLACE);

    }

    public void rechargeRequest(RequestBody api_key, RequestBody device, RequestBody token, MultipartBody.Part transImg, RequestBody amount, RequestBody desc) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<JsonObject> call = api.rechargeRequest(api_key, device, token, transImg, amount, desc);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                view.hideLoader();

                if (response.body() != null) {
                    if (response.body().get("status").getAsBoolean()) {
                        view.rechargeRequestSubmit(response.body().get("message").getAsString());
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

}
