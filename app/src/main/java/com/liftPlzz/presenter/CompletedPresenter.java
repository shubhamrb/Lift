package com.liftPlzz.presenter;

import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BaseActivity;
import com.liftPlzz.base.BasePresenter;
import com.liftPlzz.dialog.EditLiftDaiFragment;
import com.liftPlzz.model.completedLift.ResponseCompletedLift;
import com.liftPlzz.model.upcomingLift.Lift;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.CompletedView;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompletedPresenter extends BasePresenter<CompletedView> {


    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public void getCompletedLift(String token) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<ResponseCompletedLift> call = api.getCompletedLift(Constants.API_KEY, Constants.ANDROID, token);
        call.enqueue(new Callback<ResponseCompletedLift>() {
            @Override
            public void onResponse(Call<ResponseCompletedLift> call, Response<ResponseCompletedLift> response) {
                view.hideLoader();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        if (response.body().getLifts() != null) {
                            view.setLiftData(response.body().getLifts());
                        } else {
                            view.showMessage(response.body().getMessage());
                        }
                    } else {
                        view.showMessage(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseCompletedLift> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage("Check your internet connection");
            }
        });
    }

    public void getDeleteLift(String token, int liftId) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<ResponseBody> call = api.deleteMyLift(Constants.API_KEY, Constants.ANDROID, token, liftId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                view.hideLoader();

                if (response.body() != null && response.code() == 200) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        boolean status = jsonObject.optBoolean("status");
                        String message = jsonObject.optString("message");
                        view.deleteLiftData(message);
                        view.showMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    view.showMessage("Something went wrong");
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage("Check your internet connection");
            }
        });
    }

    public void openAddLift(Lift completeLiftData, EditLiftDaiFragment.UpdateRecordListiner listinerUpdate, String edit) {
        navigator.openEditLift(BaseActivity.PerformFragment.REPLACE, completeLiftData, listinerUpdate, edit);

    }
}
