package com.liftPlzz.presenter;

import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BaseActivity;
import com.liftPlzz.base.BasePresenter;
import com.liftPlzz.dialog.EditLiftDaiFragment;
import com.liftPlzz.model.upcomingLift.Lift;
import com.liftPlzz.model.upcomingLift.UpcomingLiftResponse;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.UpComingView;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpComingPresenter extends BasePresenter<UpComingView> {


    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public void openRideRequests(boolean isLifter, Integer lift_id, boolean partner) {
        navigator.openRideRequests(BaseActivity.PerformFragment.REPLACE, isLifter, lift_id, partner);
    }

    public void openMatchingRide(boolean isFind, Integer lift_id, String vehicle_type, Integer vehicle_subcategory) {
        navigator.openMatchingRide(BaseActivity.PerformFragment.REPLACE, isFind, lift_id, vehicle_type, vehicle_subcategory);
    }

    public void openDriverList(boolean isFind, Integer lift_id, String vehicle_type, Integer vehicle_subcategory) {
        navigator.openDriverList(BaseActivity.PerformFragment.REPLACE, isFind, lift_id, vehicle_type, vehicle_subcategory);
    }

    public void openEditLift(Lift lift, EditLiftDaiFragment.UpdateRecordListiner listinerUpdate, String edit) {
        navigator.openEditLift(BaseActivity.PerformFragment.REPLACE, lift, listinerUpdate, edit);
    }

    public void getUpcomingLift(String token) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<UpcomingLiftResponse> call = api.my_upcoming_lifts(Constants.API_KEY, "android", token);
        call.enqueue(new Callback<UpcomingLiftResponse>() {
            @Override
            public void onResponse(Call<UpcomingLiftResponse> call, Response<UpcomingLiftResponse> response) {
                view.hideLoader();

                if (response.body() != null) {
                    if (response.code() == 200) {
                        view.setLiftData(response.body().getLifts());
                    } else {
                        view.showMessage(response.body().getMessage());
                    }
                } else {
                    view.showMessage("Something went wrong.");
                }
            }


            @Override
            public void onFailure(Call<UpcomingLiftResponse> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage("Check your internet connection");
            }
        });
    }

    public void getCancelLift(String token, int liftId) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<ResponseBody> call = api.cancelMyLift(Constants.API_KEY, Constants.ANDROID, token, liftId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                view.hideLoader();

                if (response.body() != null && response.code() == 200) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        boolean status = jsonObject.optBoolean("status");
                        String message = jsonObject.optString("message");
                        view.setCancelLiftData(message);
                        view.showMessage(message);
//                        if (status) {
//                            view.showMessage(message);
//                        } else {
//                            view.showMessage(message);
//                        }
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
}
