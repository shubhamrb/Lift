package com.liftPlzz.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
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
    private boolean isFind = true;

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
            isFind = getIntent().getBooleanExtra(Constants.IS_FIND_LIFT, true);
        }
        toolBarTitle.setText(getResources().getString(R.string.matches_list));
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        strToken = sharedPreferences.getString(Constants.TOKEN, "");
        driverListAdapter = new DriverListAdapter(this, arrayList, DriverListActivity.this, isFind);
        recyclerViewDriver.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDriver.setAdapter(driverListAdapter);

        getDriverList();
    }


    public void getDriverList() {
        Constants.showLoader(this);
        ApiService api = RetroClient.getApiService();
        Call<DriverByTypeReponse> call;
        if (isFind) {
            call = api.getRideByVehicleType(Constants.API_KEY, getResources().getString(R.string.android), strToken,
                    vehicleSubcategoryId, liftId, vehicleType);
        } else {
            call = api.getRideByDriver(Constants.API_KEY, getResources().getString(R.string.android), strToken,
                    vehicleSubcategoryId, liftId);
        }
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
        if (driverData.getRequestAlreadySend() == 0) {
            sendCancelInvitation(driverData.getLiftId(), Integer.parseInt(driverData.getFromLiftId()), driverData.getRequestAlreadySend(), null);
        } else {
            reasonDialog(driverData.getLiftId(), Integer.parseInt(driverData.getFromLiftId()), driverData.getRequestAlreadySend());
        }
    }

    public void sendCancelInvitation(int liftId, int fromLiftId, int requestAlreadySend, String reason) {
        Constants.showLoader(this);
        ApiService api = RetroClient.getApiService();
        Call<ResponseBody> call;
        if (requestAlreadySend == 0) {
            call = api.sendInvitation(Constants.API_KEY, getResources().getString(R.string.android), strToken,
                    liftId, fromLiftId);
        } else {
            call = api.cancelInvitation(Constants.API_KEY, getResources().getString(R.string.android), strToken,
                    liftId, reason);
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

    public void reasonDialog(int liftId, int fromLiftId, int requestAlreadySend) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.block_reason_dialog);
        AppCompatButton buttonSubmit = dialog.findViewById(R.id.buttonSubmit);
        TextView titleTxt = dialog.findViewById(R.id.titleTxt);
        EditText editTextPoints = dialog.findViewById(R.id.editTextPoints);

        titleTxt.setText("Reason to cancel?");

        buttonSubmit.setOnClickListener(v -> {
            if (editTextPoints.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Please enter the reason", Toast.LENGTH_SHORT).show();
            } else {
                sendCancelInvitation(liftId, fromLiftId, requestAlreadySend, editTextPoints.getText().toString());
                dialog.dismiss();
            }
        });
        dialog.show();
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

