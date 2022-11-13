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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.JsonObject;
import com.liftPlzz.R;
import com.liftPlzz.adapter.PartnerAdapter;
import com.liftPlzz.adapter.RideRquestAdapter;
import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.model.partnerdetails.Example;
import com.liftPlzz.model.partnerdetails.User;
import com.liftPlzz.model.riderequestmodel.RideRequestData;
import com.liftPlzz.model.riderequestmodel.RideRequestResponse;
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

public class RideRequestActivity extends AppCompatActivity implements RideRquestAdapter.ItemListener, PartnerAdapter.ItemListener {

    SharedPreferences sharedPreferences;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    @BindView(R.id.toolBarTitle)
    AppCompatTextView toolBarTitle;
    @BindView(R.id.recycler_rquest)
    RecyclerView recyclerRequest;
    @BindView(R.id.relative_ride)
    RelativeLayout relative;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refresh_layout;
    RideRquestAdapter rideRquestAdapter;
    PartnerAdapter partnerAdapter;
    ArrayList<RideRequestData> arrayListRide = new ArrayList<>();
    ArrayList<User> arrayListPartner = new ArrayList<>();
    private String strToken = "";
    private int liftId, seats;
    private boolean isPartner;
    private boolean isLifter;
    private String from;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_request);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            liftId = getIntent().getIntExtra(Constants.LIFT_ID, -1);
            isPartner = getIntent().getBooleanExtra(Constants.PARTNER, false);
            isLifter = getIntent().getBooleanExtra("lifter", false);
            from = getIntent().getStringExtra("from");
        }
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        strToken = sharedPreferences.getString(Constants.TOKEN, "");
        recyclerRequest.setLayoutManager(new LinearLayoutManager(this));

        refresh_layout.setOnRefreshListener(this::loadData);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        if (isPartner) {
            partnerAdapter = new PartnerAdapter(this, arrayListPartner, isLifter, liftId, this);
            recyclerRequest.setAdapter(partnerAdapter);
            getPartnerDetails();
            toolBarTitle.setText(getResources().getString(R.string.partnet_details));
        } else {
            rideRquestAdapter = new RideRquestAdapter(this, arrayListRide, RideRequestActivity.this, isLifter);
            recyclerRequest.setAdapter(rideRquestAdapter);
            getRideRequestApi();
            toolBarTitle.setText(getResources().getString(R.string.ride_request));
        }
    }

    public void getRideRequestApi() {
        Constants.showLoader(this);
        ApiService api = RetroClient.getApiService();
        Call<RideRequestResponse> call = api.getRideRequest(Constants.API_KEY, getResources().getString(R.string.android), strToken,
                liftId);
        call.enqueue(new Callback<RideRequestResponse>() {
            @Override
            public void onResponse(Call<RideRequestResponse> call, Response<RideRequestResponse> response) {
                Constants.hideLoader();
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        arrayListRide.clear();
                        arrayListRide.addAll(response.body().getData());
                        rideRquestAdapter.notifyDataSetChanged();

                    } else {
                        Constants.showMessage(RideRequestActivity.this, response.body().getMessage(), relative);
                    }
                }
            }


            @Override
            public void onFailure(Call<RideRequestResponse> call, Throwable throwable) {
                Constants.hideLoader();
                Constants.showMessage(RideRequestActivity.this, throwable.getMessage(), relative);
            }
        });
    }

    public void getPartnerDetails() {
        Constants.showLoader(this);
        ApiService api = RetroClient.getApiService();
        Call<Example> call = api.getPartnerDetails(Constants.API_KEY,
                getResources().getString(R.string.android), strToken,
                liftId);
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                Constants.hideLoader();
                if (response.body() != null) {
                    if (response.body().getResponse().getStatus()) {
                        arrayListPartner.clear();
                        arrayListPartner.addAll(response.body().getResponse().getUser());
                        partnerAdapter.notifyDataSetChanged();

                    } else {
                        Constants.showMessage(RideRequestActivity.this, response.body().getResponse().getMessage(), relative);
                    }


                }
            }


            @Override
            public void onFailure(Call<Example> call, Throwable throwable) {
                Constants.hideLoader();
                Constants.showMessage(RideRequestActivity.this, throwable.getMessage(), relative);
            }
        });
    }

    @Override
    public void onAcceptClick(int position, RideRequestData rideRequestData) {
        updateRequestApi(rideRequestData.getId(), 1);
    }

    @Override
    public void onRejectClick(int position, RideRequestData rideRequestData) {
        updateRequestApi(rideRequestData.getId(), 2);
    }

    @Override
    public void onBlockClick(int position, Integer user_id) {
        Constants.showLoader(this);
        ApiService api = RetroClient.getApiService();
        Call<JsonObject> call = api.blockUser(Constants.API_KEY, Constants.ANDROID, strToken, user_id, "");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Constants.hideLoader();
                if (response.body() != null) {
                    if (response.code() == 200) {
                        if (response.body().get("status").getAsBoolean()) {
                            loadData();
                        } else {
                            Constants.showMessage(getApplicationContext(), response.body().get("message").getAsString(), recyclerRequest);
                        }

                    } else {
                        Constants.showMessage(getApplicationContext(), "Something went wrong", recyclerRequest);
                    }
                }
            }


            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                Constants.hideLoader();
                Constants.showMessage(getApplicationContext(), "Check your internet connection", recyclerRequest);
            }
        });
    }

    public void updateRequestApi(int inviteId, int status) {
        Constants.showLoader(this);
        ApiService api = RetroClient.getApiService();
        Call<ResponseBody> call = api.updateInvitationStatus(Constants.API_KEY, getResources().getString(R.string.android), strToken,
                inviteId, status);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Constants.hideLoader();
                if (response.code() == 200) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        boolean status = jsonObject.optBoolean("status");
                        String message = jsonObject.optString("message");
                        Toast.makeText(RideRequestActivity.this, message, Toast.LENGTH_SHORT).show();
                        if (status) {
                            if (from == null) {
                                finish();
                            } else {
                                Intent intent = new Intent(RideRequestActivity.this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.optString("message");
                        Toast.makeText(RideRequestActivity.this, message, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Constants.hideLoader();
                Constants.showMessage(RideRequestActivity.this, throwable.getMessage(), relative);
            }
        });
    }


    @OnClick({R.id.imageViewBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageViewBack:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onDeleteClick(Integer request_id, int lift_id) {
        reasonDialog(request_id, lift_id);
    }

    public void reasonDialog(Integer request_id, int liftId) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.block_reason_dialog);
        AppCompatButton buttonSubmit = dialog.findViewById(R.id.buttonSubmit);
        EditText editTextPoints = dialog.findViewById(R.id.editTextPoints);
        TextView titleTxt = dialog.findViewById(R.id.titleTxt);

        titleTxt.setText("Reason");

        buttonSubmit.setOnClickListener(v -> {
            if (editTextPoints.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Please enter the reason", Toast.LENGTH_SHORT).show();
            } else {
                cancelRide(request_id, liftId, editTextPoints.getText().toString());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void cancelRide(Integer request_id, int liftId, String reason) {
        Constants.showLoader(this);
        ApiService api = RetroClient.getApiService();
        Call<ResponseBody> call = api.cancelPartnerRide(Constants.API_KEY, getResources().getString(R.string.android), strToken,
                request_id, liftId, reason);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Constants.hideLoader();
                if (response.code() == 200) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        boolean status = jsonObject.optBoolean("status");
                        String message = jsonObject.optString("message");
                        Toast.makeText(RideRequestActivity.this, message, Toast.LENGTH_SHORT).show();
                        if (status) {
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.optString("message");
                        Toast.makeText(RideRequestActivity.this, message, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Constants.hideLoader();
                Constants.showMessage(RideRequestActivity.this, throwable.getMessage(), relative);
            }
        });
    }
}