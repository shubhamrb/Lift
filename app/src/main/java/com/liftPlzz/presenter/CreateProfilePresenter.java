package com.liftPlzz.presenter;

import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BaseActivity;
import com.liftPlzz.base.BasePresenter;
import com.liftPlzz.model.createProfile.CreateProfileMainResponse;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.CreateProfileView;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateProfilePresenter extends BasePresenter<CreateProfileView> {


    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public void updateProfile(String token, String name, String deg, String email, String mobile, String aboutme) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<CreateProfileMainResponse> call = api.create_profile(Constants.API_KEY, "android", token, name, email, mobile, deg, aboutme);
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
                view.showMessage("Check your internet connection");
            }
        });
    }

    public void updateProfile(RequestBody key, RequestBody client, RequestBody token, RequestBody name, RequestBody deg, RequestBody email, RequestBody mobile, RequestBody aboutme, MultipartBody.Part image) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<CreateProfileMainResponse> call = api.create_profile_image(key, client, token, name, email, mobile, deg, aboutme, image);
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

    public void openUpdateProfile() {
        navigator.openUpdateProfileFragment(BaseActivity.PerformFragment.REPLACE);
    }
}
