package com.liftPlzz.presenter;

import android.util.Log;

import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BaseActivity;
import com.liftPlzz.base.BasePresenter;
import com.liftPlzz.model.FindLiftResponse;
import com.liftPlzz.model.createLift.CreateLiftResponse;
import com.liftPlzz.model.editlift.GetVehicleEditResponse;
import com.liftPlzz.model.getVehicle.GetVehicleListMainResponse;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.EditLiftView;
import com.liftPlzz.views.HomeView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditLiftPresenter extends BasePresenter<EditLiftView> {


    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public void openFeedback() {
        navigator.openFeedbackFragment(BaseActivity.PerformFragment.REPLACE);
    }

    public void openELearning() {
        navigator.openLearningFragment(BaseActivity.PerformFragment.REPLACE);
    }

    public void getVehicle(String token) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<GetVehicleListMainResponse> call = api.get_vehicle_list(Constants.API_KEY, "android", token);
        call.enqueue(new Callback<GetVehicleListMainResponse>() {
            @Override
            public void onResponse(Call<GetVehicleListMainResponse> call, Response<GetVehicleListMainResponse> response) {
                view.hideLoader();
                if (response.body() != null) {
                    if (response.body().getResponse().getStatus()) {
                        view.setVehicle(response.body().getResponse().getData());
                    } else {
                        view.showMessage(response.body().getResponse().getMessage());
                    }


                }
            }


            @Override
            public void onFailure(Call<GetVehicleListMainResponse> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage(throwable.getMessage());
            }
        });

    }

    public void updateToken(String token, String FCMToken) {
        ApiService api = RetroClient.getApiService();
        Call<FindLiftResponse> call = api.update_push_notification(Constants.API_KEY, "android", token, FCMToken);
        call.enqueue(new Callback<FindLiftResponse>() {
            @Override
            public void onResponse(Call<FindLiftResponse> call, Response<FindLiftResponse> response) {
            }


            @Override
            public void onFailure(Call<FindLiftResponse> call, Throwable throwable) {
            }
        });

    }

    public void findLift(String token, String title, String requir_seats, String start_point, String end_point, String lift_date,
                         String lift_id,String liftTime) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();

        Call<FindLiftResponse> call = api.findUpdate_lift(Constants.API_KEY, "android", token, title, requir_seats, start_point, end_point, lift_date,
                lift_id,liftTime);
        call.enqueue(new Callback<FindLiftResponse>() {
            @Override
            public void onResponse(Call<FindLiftResponse> call, Response<FindLiftResponse> response) {
                view.hideLoader();
                Log.e("response on","jjjjjjjjjjjjjjj");
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        Log.e("find update response ","true");
                        view.setFindRideData(response.body());
                    } else {
                        //  view.hideLoader();
                        view.showMessage(response.body().getMessage());
                    }
                }
            }


            @Override
            public void onFailure(Call<FindLiftResponse> call, Throwable throwable) {
                view.hideLoader();
                Log.e("response onFailure","fffffffffffff");
                view.showMessage(throwable.getMessage());
            }

        });
    }

    public void getEditVehicle(String token,String lift_id) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<GetVehicleEditResponse> call = api.get_lift_detail(Constants.API_KEY, "android",lift_id, token);
        call.enqueue(new Callback<GetVehicleEditResponse>() {
            @Override
            public void onResponse(Call<GetVehicleEditResponse> call, Response<GetVehicleEditResponse> response) {
                view.hideLoader();
                if (response.body() != null) {
                   if (response.body().getStatus()) {
                        view.getLiftDetail(response.body().getData());
                    } else {
                        view.showMessage("Error getting lift detail");
                    }
                }
            }


            @Override
            public void onFailure(Call<GetVehicleEditResponse> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage(throwable.getMessage());
            }
        });

    }


    public void createLift(String token, String vehicle_id, String lift_type, String free_seats, String paid_seats, String start_ponit, String end_point, String checkpoints, String lift_date,
                           String lift_id,String liftTime) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<CreateLiftResponse> call = api.offerUpdate_lift(Constants.API_KEY, "android", token, vehicle_id, lift_type, free_seats, paid_seats, start_ponit, end_point, checkpoints, lift_date,lift_id, liftTime);
        call.enqueue(new Callback<CreateLiftResponse>() {
            @Override
            public void onResponse(Call<CreateLiftResponse> call, Response<CreateLiftResponse> response) {
                view.hideLoader();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        view.setCreateRideData(response.body());
                    } else {
                        //  view.hideLoader();
                        view.showMessage(response.body().getMessage());
                    }


                }
            }


            @Override
            public void onFailure(Call<CreateLiftResponse> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage(throwable.getMessage());
            }
        });
    }

    public void openContacts() {
        navigator.openContactsFragment(BaseActivity.PerformFragment.REPLACE);
    }


    public void openMyVehicle() {
        navigator.openMyVehicleFragment(BaseActivity.PerformFragment.REPLACE);
    }

    public void openNotification() {
        navigator.openNotificationFragment(BaseActivity.PerformFragment.REPLACE);
    }

//    public void openDriverList() {
//        navigator.openDriverListFragment(BaseActivity.PerformFragment.REPLACE);
//    }


}
