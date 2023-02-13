package com.liftPlzz.presenter;

import com.google.gson.JsonObject;
import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BaseActivity;
import com.liftPlzz.base.BasePresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.AddAccountView;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAccountPresenter extends BasePresenter<AddAccountView> {


    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public void addBankAccount(RequestBody api_key, RequestBody device, RequestBody token, MultipartBody.Part cancelCheque, MultipartBody.Part pan, RequestBody bankName, RequestBody account, RequestBody ifsc, RequestBody name) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<JsonObject> call = api.addBankAccount(api_key, device, token, cancelCheque, pan, bankName, account, ifsc, name);
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
