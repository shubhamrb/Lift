package com.liftPlzz.presenter;

import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BasePresenter;
import com.liftPlzz.model.chatuser.ResponseChatUser;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.ChatUserView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatUserPresenter extends BasePresenter<ChatUserView> {

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public void getChatUser(String token) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<ResponseChatUser> call = api.getChatUser(Constants.API_KEY, "android", token);
        call.enqueue(new Callback<ResponseChatUser>() {
            @Override
            public void onResponse(Call<ResponseChatUser> call, Response<ResponseChatUser> response) {
                view.hideLoader();

                if (response.body() != null) {
                    if (response.code() == 200) {
                        view.setChatUser(response.body().getData());
                    } else {
                        //  view.hideLoader();
                        view.showMessage(response.body().getMessage());
                    }


                }
            }


            @Override
            public void onFailure(Call<ResponseChatUser> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage("Check your internet connection");
            }
        });
    }

}
