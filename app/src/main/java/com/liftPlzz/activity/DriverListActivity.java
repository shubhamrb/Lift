package com.liftPlzz.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.adapter.DriverListAdapter;
import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.model.ridebyvehicletypemodel.DriverByTypeReponse;
import com.liftPlzz.model.ridebyvehicletypemodel.DriverData;
import com.liftPlzz.utils.Constants;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverListActivity extends AppCompatActivity implements DriverListAdapter.ItemListener {

    DriverListAdapter driverListAdapter;
    SharedPreferences sharedPreferences;
    @BindView(R.id.rel_driver)
    RelativeLayout relativeLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    @BindView(R.id.toolBarTitle)
    AppCompatTextView toolBarTitle;
    @BindView(R.id.recyclerViewDriver)
    RecyclerView recyclerViewDriver;
    private String strToken = "", vehicleType;
    private int vehicleSubcategoryId = -1, liftId = -1;
    private ArrayList<DriverData> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_driver_list);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            liftId = getIntent().getIntExtra(Constants.LIFT_ID, -1);
            vehicleSubcategoryId = getIntent().getIntExtra(Constants.SUB_CATEGORY_ID, -1);
            vehicleType = getIntent().getStringExtra(Constants.VEHICLE_TYPE);
        }
        toolBarTitle.setText(getResources().getString(R.string.matches_list));
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        strToken = sharedPreferences.getString(Constants.TOKEN, "");
        driverListAdapter = new DriverListAdapter(this, arrayList, DriverListActivity.this);
        recyclerViewDriver.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDriver.setAdapter(driverListAdapter);

        getDriverList();
    }


    public void getDriverList() {
        Constants.showLoader(this);
        ApiService api = RetroClient.getApiService();
        Call<DriverByTypeReponse> call = api.getRideByVehicleType(Constants.API_KEY, getResources().getString(R.string.android), strToken,
                vehicleSubcategoryId, liftId, vehicleType);
        call.enqueue(new Callback<DriverByTypeReponse>() {
            @Override
            public void onResponse(Call<DriverByTypeReponse> call, Response<DriverByTypeReponse> response) {
                Constants.hideLoader();
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        arrayList.clear();
                        arrayList.addAll(response.body().getData());
                        driverListAdapter.notifyDataSetChanged();
                    } else {
                        Constants.showMessage(DriverListActivity.this, response.body().getMessage(), relativeLayout);
                    }
                }
            }


            @Override
            public void onFailure(Call<DriverByTypeReponse> call, Throwable throwable) {
                Constants.hideLoader();
                Constants.showMessage(DriverListActivity.this, throwable.getMessage(), relativeLayout);
            }
        });

    }

    @Override
    public void onSendButtonClick(DriverData driverData) {
//        if (driverData.getRequestAlreadySend() == 0) {
//            sendInvitation(driverData.getLiftId(), Integer.parseInt(driverData.getFromLiftId()));
//        } else {
//            //cancel
//            sendInvitation(driverData.getLiftId(), Integer.parseInt(driverData.getFromLiftId()));
//        }
        sendCancelInvitation(driverData.getLiftId(), Integer.parseInt(driverData.getFromLiftId()), driverData.getRequestAlreadySend());
    }

    public void sendCancelInvitation(int liftId, int fromLiftId, int requestAlreadySend) {
        Constants.showLoader(this);
        ApiService api = RetroClient.getApiService();
        Call<ResponseBody> call = null;
        if (requestAlreadySend == 0) {
            //if requestAlradySend is 0 than sendrequest will be called
            call = api.sendInvitation(Constants.API_KEY, getResources().getString(R.string.android), strToken,
                    liftId, fromLiftId);
        } else {
            //if requestAlradySend is 1 than cancelRequest will be called
            call = api.cancelInvitation(Constants.API_KEY, getResources().getString(R.string.android), strToken,
                    liftId);
        }


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Constants.hideLoader();
                if (response.code() == 200) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        boolean status = jsonObject.optBoolean("status");
                        String message = jsonObject.optString("message");
                        Toast.makeText(DriverListActivity.this, message, Toast.LENGTH_SHORT).show();
                        if (status) {
                            getDriverList();
//                            Intent intent = new Intent(DriverListActivity.this, HomeActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
//                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.optString("message");
                        Toast.makeText(DriverListActivity.this, message, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Constants.hideLoader();
                Constants.showMessage(DriverListActivity.this, throwable.getMessage(), relativeLayout);
            }
        });

    }

    @Override
    public void onUserImageClick(int id) {
        Intent intent = new Intent(DriverListActivity.this, DriverProfileActivity.class);
        intent.putExtra(Constants.USER_ID, id);
        startActivity(intent);
    }


    @OnClick({R.id.imageViewBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageViewBack:
                onBackPressed();
                break;
        }
    }
}

