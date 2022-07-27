package com.liftPlzz.presenter;

import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BasePresenter;
import com.liftPlzz.model.videos.upcomingLift.VideosResponse;
import com.liftPlzz.views.VideosView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideosPresenter extends BasePresenter<VideosView> {


    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public void getVideos() {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<VideosResponse> call = api.getVideos();
        call.enqueue(new Callback<VideosResponse>() {
            @Override
            public void onResponse(Call<VideosResponse> call, Response<VideosResponse> response) {
                view.hideLoader();

                if (response.body() != null) {
                    if (response.code() == 200) {
                        view.setVideosData(response.body());
                    } else {
                        view.showMessage("Something went wrong.");
                    }
                } else {
                    view.showMessage("Something went wrong.");
                }
            }


            @Override
            public void onFailure(Call<VideosResponse> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage("Check your internet connection");
            }
        });
    }

}
