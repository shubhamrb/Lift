package com.liftPlzz.presenter;

import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BasePresenter;
import com.liftPlzz.model.createVehicle.CreateVehicleResponse;
import com.liftPlzz.views.BikeView;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BikePresenter extends BasePresenter<BikeView> {


    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public void createVehicle(RequestBody key, RequestBody client, RequestBody token, RequestBody type, RequestBody model, RequestBody register_number,
                              RequestBody ratePerKm, RequestBody insuranceDate, RequestBody seat, RequestBody is_default, RequestBody vehicleSubCategory, MultipartBody.Part front_image, MultipartBody.Part back_image, MultipartBody.Part rc_image) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<CreateVehicleResponse> call = api.create_vehicle(key, client, token, type, model, register_number,
                 insuranceDate,ratePerKm, seat, is_default, vehicleSubCategory, front_image, back_image, rc_image);
        call.enqueue(new Callback<CreateVehicleResponse>() {
            @Override
            public void onResponse(Call<CreateVehicleResponse> call, Response<CreateVehicleResponse> response) {
                view.hideLoader();
                if (response.body() != null) {
                    if (response.body().getResponse().getStatus()) {
                        view.createVehicle(response.body().getResponse());
                    } else {
                        view.showMessage(response.body().getResponse().getMessage());
                    }


                }
            }


            @Override
            public void onFailure(Call<CreateVehicleResponse> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage("Check your internet connection");            }
        });

    }

    public void updateVehicle(RequestBody key, RequestBody client, RequestBody token, RequestBody vehicle_id, RequestBody type, RequestBody model, RequestBody register_number,
                              RequestBody ratePerKm, RequestBody insuranceDate, RequestBody seat, RequestBody is_default, RequestBody vehicleSubCategory, MultipartBody.Part front_image, MultipartBody.Part back_image, MultipartBody.Part rc_image) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<CreateVehicleResponse> call = api.updateVehicle(key, client, token, vehicle_id, type, model, register_number,
                insuranceDate,ratePerKm, seat, is_default, vehicleSubCategory, front_image, back_image, rc_image);
        call.enqueue(new Callback<CreateVehicleResponse>() {
            @Override
            public void onResponse(Call<CreateVehicleResponse> call, Response<CreateVehicleResponse> response) {
                view.hideLoader();
                if (response.body() != null) {
                    if (response.body().getResponse().getStatus()) {
                        view.createVehicle(response.body().getResponse());
                    } else {
                        view.showMessage(response.body().getResponse().getMessage());
                    }


                }
            }


            @Override
            public void onFailure(Call<CreateVehicleResponse> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage(throwable.getMessage());
            }
        });

    }
}
