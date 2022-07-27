package com.liftPlzz.presenter;

import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BasePresenter;
import com.liftPlzz.model.getVehicle.getReview.GetReviewMainResponse;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.ReviewsView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewsPresenter extends BasePresenter<ReviewsView> {


    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public void getReview(String token, String user_id, String filter) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<GetReviewMainResponse> call = api.get_review(Constants.API_KEY, "android", token, user_id, filter);
        call.enqueue(new Callback<GetReviewMainResponse>() {
            @Override
            public void onResponse(Call<GetReviewMainResponse> call, Response<GetReviewMainResponse> response) {
                view.hideLoader();

                if (response.body() != null) {
                    if (response.code() == 200) {
                        view.setReviewData(response.body().getResponse());
                    } else {
                        //  view.hideLoader();
                        view.showMessage(response.body().getResponse().getMessage());
                    }


                }
            }


            @Override
            public void onFailure(Call<GetReviewMainResponse> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage("Check your internet connection");
            }
        });
    }

    public void likeReview(String token, int review_id) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<GetReviewMainResponse> call = api.like_review(Constants.API_KEY, "android", token, review_id);
        call.enqueue(new Callback<GetReviewMainResponse>() {
            @Override
            public void onResponse(Call<GetReviewMainResponse> call, Response<GetReviewMainResponse> response) {
                view.hideLoader();

                if (response.body() != null) {
                    if (response.code() == 200) {
                        view.onSuccessLikeDislike(response.body().getResponse());
                    } else {
                        //  view.hideLoader();
                        view.showMessage(response.body().getResponse().getMessage());
                    }


                }
            }


            @Override
            public void onFailure(Call<GetReviewMainResponse> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage("Check your internet connection");
            }
        });
    }

    public void dislikeReview(String token, int review_id) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<GetReviewMainResponse> call = api.dislike_review(Constants.API_KEY, "android", token, review_id);
        call.enqueue(new Callback<GetReviewMainResponse>() {
            @Override
            public void onResponse(Call<GetReviewMainResponse> call, Response<GetReviewMainResponse> response) {
                view.hideLoader();

                if (response.body() != null) {
                    if (response.code() == 200) {
                        view.onSuccessLikeDislike(response.body().getResponse());
                    } else {
                        //  view.hideLoader();
                        view.showMessage(response.body().getResponse().getMessage());
                    }


                }
            }


            @Override
            public void onFailure(Call<GetReviewMainResponse> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage("Check your internet connection");
            }
        });
    }

}
