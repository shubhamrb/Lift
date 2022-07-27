package com.liftPlzz.presenter;

import com.google.gson.JsonObject;
import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BaseActivity;
import com.liftPlzz.base.BasePresenter;
import com.liftPlzz.model.BlockUserResponse;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.BlockView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlockPresenter extends BasePresenter<BlockView> {


    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public void getBlockedUsers(String token) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<BlockUserResponse> call = api.getBlockedUsers(Constants.API_KEY, Constants.ANDROID, token);
        call.enqueue(new Callback<BlockUserResponse>() {
            @Override
            public void onResponse(Call<BlockUserResponse> call, Response<BlockUserResponse> response) {
                view.hideLoader();
                if (response.body() != null) {
                    if (response.code() == 200) {
                        if (response.body().getData() != null) {
                            view.setBlockData(response.body().getData());
                        } else {
                            view.showMessage(response.body().getMessage());
                        }

                    } else {
                        view.showMessage(response.body().getMessage());
                    }
                }
            }


            @Override
            public void onFailure(Call<BlockUserResponse> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage("Check your internet connection");
            }
        });
    }

    public void getBlockUser(String token, int user_id, String reason) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<JsonObject> call = api.blockUser(Constants.API_KEY, Constants.ANDROID, token, user_id, reason);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                view.hideLoader();
                if (response.body() != null) {
                    if (response.code() == 200) {
                        if (response.body().get("status").getAsBoolean()) {
                            view.onSuccessBlock();
                        } else {
                            view.showMessage(response.body().get("message").getAsString());
                        }

                    } else {
                        view.showMessage("Something went wrong");
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

    public void openUsersFragment() {
        navigator.openUsersFragment(BaseActivity.PerformFragment.REPLACE);
    }
}
