package com.liftPlzz.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.liftPlzz.R;
import com.liftPlzz.adapter.TicketListAdapter;
import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.model.getTicketList.ResponseTicketList;
import com.liftPlzz.model.getTicketList.TicketListData;
import com.liftPlzz.utils.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketListActivity extends AppCompatActivity implements TicketListAdapter.ItemListener {

    TicketListAdapter ticketListAdapter;
    SharedPreferences sharedPreferences;
    @BindView(R.id.rel_driver)
    RelativeLayout relativeLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    @BindView(R.id.toolBarTitle)
    AppCompatTextView toolBarTitle;
    @BindView(R.id.rvTicketList)
    RecyclerView rvTicketList;
    @BindView(R.id.btn_add)
    FloatingActionButton floatingButtonAdd;
    private String strToken = "", vehicleType;
    private ArrayList<TicketListData> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_ticket_list);
        ButterKnife.bind(this);
        toolBarTitle.setText(getResources().getString(R.string.ticket_list));
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        strToken = sharedPreferences.getString(Constants.TOKEN, "");
        ticketListAdapter = new TicketListAdapter(this, arrayList, TicketListActivity.this);
        rvTicketList.setLayoutManager(new LinearLayoutManager(this));
        rvTicketList.setAdapter(ticketListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTicketList();
    }

    public void getTicketList() {
        Constants.showLoader(this);
        ApiService api = RetroClient.getApiService();
        Call<ResponseTicketList> call = api.getTicketList(Constants.API_KEY, getResources().getString(R.string.android), strToken);
        call.enqueue(new Callback<ResponseTicketList>() {
            @Override
            public void onResponse(Call<ResponseTicketList> call, Response<ResponseTicketList> response) {
                Constants.hideLoader();
                if (response.body() != null) {
                    if (response.body().getStatus() && response.body().getData() != null) {
                        arrayList.clear();
                        arrayList.addAll(response.body().getData());
                        ticketListAdapter.notifyDataSetChanged();
                    } else {
                        Constants.showMessage(TicketListActivity.this, response.body().getMessage(), relativeLayout);
                    }
                } else {
                    Constants.showMessage(TicketListActivity.this, getResources().getString(R.string.something_went_wrong), relativeLayout);
                }
            }


            @Override
            public void onFailure(Call<ResponseTicketList> call, Throwable throwable) {
                Constants.hideLoader();
                Constants.showMessage(TicketListActivity.this, throwable.getMessage(), relativeLayout);
            }
        });

    }

//    @Override
//    public void onSendButtonClick(DriverData driverData) {
////        if (driverData.getRequestAlreadySend() == 0) {
////            sendInvitation(driverData.getLiftId(), Integer.parseInt(driverData.getFromLiftId()));
////        } else {
////            //cancel
////            sendInvitation(driverData.getLiftId(), Integer.parseInt(driverData.getFromLiftId()));
////        }
//        sendCancelInvitation(driverData.getLiftId(), Integer.parseInt(driverData.getFromLiftId()), driverData.getRequestAlreadySend());
//    }

//    public void sendCancelInvitation(int liftId, int fromLiftId, int requestAlreadySend) {
//        Constants.showLoader(this);
//        ApiService api = RetroClient.getApiService();
//        Call<ResponseBody> call = null;
//        if (requestAlreadySend == 0) {
//            //if requestAlradySend is 0 than sendrequest will be called
//            call = api.sendInvitation(Constants.API_KEY, getResources().getString(R.string.android), strToken,
//                    liftId, fromLiftId);
//        } else {
//            //if requestAlradySend is 1 than cancelRequest will be called
//            call = api.cancelInvitation(Constants.API_KEY, getResources().getString(R.string.android), strToken,
//                    liftId);
//        }
//
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                Constants.hideLoader();
//                if (response.code() == 200) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response.body().string());
//                        boolean status = jsonObject.optBoolean("status");
//                        String message = jsonObject.optString("message");
//                        Toast.makeText(TicketListActivity.this, message, Toast.LENGTH_SHORT).show();
//                        if (status) {
//                            Intent intent = new Intent(TicketListActivity.this, HomeActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
//                            finish();
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
//                        String message = jsonObject.optString("message");
//                        Toast.makeText(TicketListActivity.this, message, Toast.LENGTH_SHORT).show();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
//                Constants.hideLoader();
//                Constants.showMessage(TicketListActivity.this, throwable.getMessage(), relativeLayout);
//            }
//        });
//
//    }

    @Override
    public void onItemClick(int id) {
        Intent intent = new Intent(TicketListActivity.this, DriverProfileActivity.class);
        intent.putExtra(Constants.USER_ID, id);
        startActivity(intent);
    }


    @OnClick({R.id.imageViewBack, R.id.btn_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageViewBack:
                onBackPressed();
                break;

            case R.id.btn_add:
                Intent intent = new Intent(TicketListActivity.this, CreateTicketActivity.class);
                startActivity(intent);
                break;
        }
    }
}

