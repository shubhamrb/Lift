package com.liftPlzz.presenter;

import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BasePresenter;
import com.liftPlzz.model.getNotification.ResponseNotification;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.NotificationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationPresenter extends BasePresenter<NotificationView> {


    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public void getAllNotification(String token) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<ResponseNotification> call = api.getNotificationList(Constants.API_KEY, Constants.ANDROID, token);
        call.enqueue(new Callback<ResponseNotification>() {
            @Override
            public void onResponse(Call<ResponseNotification> call, Response<ResponseNotification> response) {
                view.hideLoader();
                if (response.body() != null) {
                    if (response.code() == 200) {
                        if (response.body().getData() != null) {
                            view.setNotificationData(response.body().getData());
                        } else {
                            view.showMessage(response.body().getMessage());
                        }

                    } else {
                        view.showMessage(response.body().getMessage());
                    }
                }
            }


            @Override
            public void onFailure(Call<ResponseNotification> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage("Check your internet connection");
            }
        });
    }

}
