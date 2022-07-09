package com.liftPlzz.presenter;

import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BasePresenter;
import com.liftPlzz.model.upcomingLift.UpcomingLiftResponse;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.PurchasePointView;
import com.liftPlzz.views.UpComingView;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchasePointPresenter extends BasePresenter<PurchasePointView> {


    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

}
