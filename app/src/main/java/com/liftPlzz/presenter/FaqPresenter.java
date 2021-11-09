package com.liftPlzz.presenter;

import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BasePresenter;
import com.liftPlzz.model.getFaq.ResponseFaq;
import com.liftPlzz.model.getsetting.SettingModel;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.FaqView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FaqPresenter extends BasePresenter<FaqView> {

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public void getFAQList() {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<ResponseFaq> call = api.getFAQ(Constants.API_KEY, Constants.ANDROID);
        call.enqueue(new Callback<ResponseFaq>() {
            @Override
            public void onResponse(Call<ResponseFaq> call, Response<ResponseFaq> response) {
                view.hideLoader();
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        view.setFAQ(response.body().getData());
                    } else {
                        view.showMessage("No Data Found");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseFaq> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage(throwable.getMessage());
            }
        });
    }
}
