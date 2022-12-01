package com.liftPlzz.presenter;

import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BasePresenter;
import com.liftPlzz.model.BlockUserResponse;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.FeedbackSuggestionView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackSuggestionPresenter extends BasePresenter<FeedbackSuggestionView> {

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public void getSubmitFeedback(String token, String message) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<BlockUserResponse> call = api.submitFeedback(Constants.API_KEY, Constants.ANDROID, token, message);
        call.enqueue(new Callback<BlockUserResponse>() {
            @Override
            public void onResponse(Call<BlockUserResponse> call, Response<BlockUserResponse> response) {
                view.hideLoader();
                if (response.body() != null) {
                    if (response.code() == 200) {
                        view.setSubmitData();
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

    public void getSubmitSuggestion(String token, String message) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<BlockUserResponse> call = api.submitSuggestion(Constants.API_KEY, Constants.ANDROID, token, message);
        call.enqueue(new Callback<BlockUserResponse>() {
            @Override
            public void onResponse(Call<BlockUserResponse> call, Response<BlockUserResponse> response) {
                view.hideLoader();
                if (response.body() != null) {
                    if (response.code() == 200) {
                        view.setSubmitData();
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

}
