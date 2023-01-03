package com.liftPlzz.presenter;

import com.google.gson.JsonObject;
import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BasePresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.GoGreenView;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoGreenPresenter extends BasePresenter<GoGreenView> {


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
        Call<JsonObject> call = api.get_go_green_data(Constants.API_KEY, "android", token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                view.hideLoader();

                if (response.body() != null) {
                    if (response.body().get("status").getAsBoolean()) {
                        view.setData(response.body());
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

    public void applyGoGreen(RequestBody api_key, RequestBody device, RequestBody token, MultipartBody.Part aadhar, MultipartBody.Part pan, RequestBody account, RequestBody ifsc, RequestBody name) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<JsonObject> call = api.applyGoGreen(api_key, device, token, aadhar, pan, account, ifsc, name);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                view.hideLoader();

                if (response.body() != null) {
                    if (response.body().get("status").getAsBoolean()) {
                        view.showMessage(response.body().get("message").getAsString());
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
