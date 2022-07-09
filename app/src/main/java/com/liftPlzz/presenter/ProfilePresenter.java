package com.liftPlzz.presenter;

import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BaseActivity;
import com.liftPlzz.base.BasePresenter;
import com.liftPlzz.model.createProfile.CreateProfileMainResponse;
import com.liftPlzz.model.getVehicle.getReview.GetReviewMainResponse;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.ProfileView;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilePresenter extends BasePresenter<ProfileView> {


    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public void getProfile(String token) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<CreateProfileMainResponse> call = api.get_profile(Constants.API_KEY, "android", token);
        call.enqueue(new Callback<CreateProfileMainResponse>() {
            @Override
            public void onResponse(Call<CreateProfileMainResponse> call, Response<CreateProfileMainResponse> response) {
                view.hideLoader();

                if (response.body() != null) {
                    if (response.body().getResponse().getStatus()) {
                        view.setProfileData(response.body().getResponse());
                    } else {
                        //  view.hideLoader();
                        view.showMessage(response.body().getResponse().getMessage());
                    }


                }
            }


            @Override
            public void onFailure(Call<CreateProfileMainResponse> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage(throwable.getMessage());
            }
        });
    }

    public void getReview(String token) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<GetReviewMainResponse> call = api.get_review(Constants.API_KEY, "android", token);
        call.enqueue(new Callback<GetReviewMainResponse>() {
            @Override
            public void onResponse(Call<GetReviewMainResponse> call, Response<GetReviewMainResponse> response) {
                view.hideLoader();

                if (response.body() != null) {
                    if (response.code() == 200) {
                        view.setReviewData(response.body().getResponse().getData());
                    } else {
                        //  view.hideLoader();
                        view.showMessage(response.body().getResponse().getMessage());
                    }


                }
            }


            @Override
            public void onFailure(Call<GetReviewMainResponse> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage(throwable.getMessage());
            }
        });
    }

    public void delete_imag(String token, String id) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<GetReviewMainResponse> call = api.delete_eimag(Constants.API_KEY, "android", token, id);
        call.enqueue(new Callback<GetReviewMainResponse>() {
            @Override
            public void onResponse(Call<GetReviewMainResponse> call, Response<GetReviewMainResponse> response) {
                view.hideLoader();

                if (response.body() != null) {
                    if (response.code() == 200) {
                        view.setProfileImageData("success");
                    } else {
                        //  view.hideLoader();
                        view.showMessage(response.body().getResponse().getMessage());
                    }


                }
            }


            @Override
            public void onFailure(Call<GetReviewMainResponse> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage(throwable.getMessage());
            }
        });
    }


    public void openUpdateProfile() {
        navigator.openUpdateProfileFragment(BaseActivity.PerformFragment.REPLACE);
    }


    public void uploadImage(RequestBody api_key, RequestBody device, RequestBody token, MultipartBody.Part email) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<CreateProfileMainResponse> call = api.add_social_image(api_key, device, token, email);
        call.enqueue(new Callback<CreateProfileMainResponse>() {
            @Override
            public void onResponse(Call<CreateProfileMainResponse> call, Response<CreateProfileMainResponse> response) {
                view.hideLoader();

                if (response.body() != null) {
                    if (response.body().getResponse().getStatus()) {
                        view.setProfileData(response.body().getResponse());
                    } else {
                        //  view.hideLoader();
                        view.showMessage(response.body().getResponse().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<CreateProfileMainResponse> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage(throwable.getMessage());
            }
        });
    }

    public void uploadSelfie(RequestBody api_key, RequestBody device, RequestBody token, MultipartBody.Part email) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<CreateProfileMainResponse> call = api.add_selfie(api_key, device, token, email);
        call.enqueue(new Callback<CreateProfileMainResponse>() {
            @Override
            public void onResponse(Call<CreateProfileMainResponse> call, Response<CreateProfileMainResponse> response) {
                view.hideLoader();

                if (response.body() != null) {
                    if (response.body().getResponse().getStatus()) {
                        view.selfieUploaded(response.body().getResponse());
                    } else {
                        //  view.hideLoader();
                        view.showMessage(response.body().getResponse().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<CreateProfileMainResponse> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage(throwable.getMessage());
            }
        });
    }

    public void uploadId(RequestBody api_key, RequestBody device, RequestBody token, MultipartBody.Part email) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<CreateProfileMainResponse> call = api.add_Id(api_key, device, token, email);
        call.enqueue(new Callback<CreateProfileMainResponse>() {
            @Override
            public void onResponse(Call<CreateProfileMainResponse> call, Response<CreateProfileMainResponse> response) {
                view.hideLoader();

                if (response.body() != null) {
                    if (response.body().getResponse().getStatus()) {
                        view.idUploaded(response.body().getResponse());
                    } else {
                        //  view.hideLoader();
                        view.showMessage(response.body().getResponse().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<CreateProfileMainResponse> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage(throwable.getMessage());
            }
        });
    }

}
