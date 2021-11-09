package com.liftPlzz.presenter;

import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BaseActivity;
import com.liftPlzz.base.BasePresenter;
import com.liftPlzz.model.deleteVehicle.DeleteVehicleMainResponse;
import com.liftPlzz.model.getVehicle.GetVehicleListMainResponse;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.MyVehicleView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyVehiclePresenter extends BasePresenter<MyVehicleView> {


    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

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
    public void deleteVehicle(String token,String vehicle_id) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<DeleteVehicleMainResponse> call = api.delete_vehicle(Constants.API_KEY, "android", token,vehicle_id);
        call.enqueue(new Callback<DeleteVehicleMainResponse>() {
            @Override
            public void onResponse(Call<DeleteVehicleMainResponse> call, Response<DeleteVehicleMainResponse> response) {
                view.hideLoader();
                if (response.body() != null) {
                    if (response.body().getResponse().getStatus()) {
                        view.setVehicleDelete(response.body().getResponse().getMessage());
                    } else {
                        view.showMessage(response.body().getResponse().getMessage());
                    }
                }
            }


            @Override
            public void onFailure(Call<DeleteVehicleMainResponse> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage(throwable.getMessage());
            }
        });

    }
    public void openAddVehicle() {
        navigator.openAddVehicleFragment(BaseActivity.PerformFragment.REPLACE);
    }
}
