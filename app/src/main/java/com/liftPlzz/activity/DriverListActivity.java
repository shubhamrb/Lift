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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refresh_layout;
    private String strToken = "", vehicleType;
    private int vehicleSubcategoryId = -1, liftId = -1;
    private ArrayList<DriverData> arrayList = new ArrayList<>();
    private boolean isFind = true;
    private String seat = "0";

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

        refresh_layout.setOnRefreshListener(this::getDriverList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDriverList();
    }

    public void getDriverList() {
        Constants.showLoader(this);
        ApiService api = RetroClient.getApiService();
        Call<DriverByTypeReponse> call;
        if (isFind) {
            call = api.getRideByVehicleType(Constants.API_KEY, getResources().getString(R.string.android), strToken, vehicleSubcategoryId, liftId, vehicleType);
        } else {
            call = api.getRideByDriver(Constants.API_KEY, getResources().getString(R.string.android), strToken, vehicleSubcategoryId, liftId);
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
            if (driverData.getApplied_seats() > driverData.getVacant_seats()) {
                updateSeatDialog(driverData.getVacant_seats(), driverData.getFromLiftId());
            } else {
                sendCancelInvitation(driverData.getLiftId(), Integer.parseInt(driverData.getFromLiftId()), driverData.getRequestAlreadySend(), null);
            }
        } else {
            reasonDialog(driverData.getLiftId(), Integer.parseInt(driverData.getFromLiftId()), driverData.getRequestAlreadySend());
        }
    }

    public void sendCancelInvitation(int liftId, int fromLiftId, int requestAlreadySend, String reason) {
        Constants.showLoader(this);
        ApiService api = RetroClient.getApiService();
        Call<ResponseBody> call;
        if (requestAlreadySend == 0) {
            call = api.sendInvitation(Constants.API_KEY, getResources().getString(R.string.android), strToken, liftId, fromLiftId);
        } else {
            call = api.cancelInvitation(Constants.API_KEY, getResources().getString(R.string.android), strToken, liftId, reason);
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

    public void updateSeatDialog(Integer vacant_seats, String liftId) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.update_seat_dialog);
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        AppCompatButton buttonSubmit = dialog.findViewById(R.id.buttonSubmit);
        ImageView seat1 = dialog.findViewById(R.id.seat1);
        ImageView seat2 = dialog.findViewById(R.id.seat2);
        ImageView seat3 = dialog.findViewById(R.id.seat3);
        ImageView seat4 = dialog.findViewById(R.id.seat4);
        ImageView seat5 = dialog.findViewById(R.id.seat5);
        ImageView seat6 = dialog.findViewById(R.id.seat6);
        ImageView seat7 = dialog.findViewById(R.id.seat7);


        switch (vacant_seats) {
            case 1:
                seat1.setVisibility(View.VISIBLE);
                break;
            case 2:
                seat1.setVisibility(View.VISIBLE);
                seat2.setVisibility(View.VISIBLE);
                break;
            case 3:
                seat1.setVisibility(View.VISIBLE);
                seat2.setVisibility(View.VISIBLE);
                seat3.setVisibility(View.VISIBLE);
                break;
            case 4:
                seat1.setVisibility(View.VISIBLE);
                seat2.setVisibility(View.VISIBLE);
                seat3.setVisibility(View.VISIBLE);
                seat4.setVisibility(View.VISIBLE);
                break;
            case 5:
                seat1.setVisibility(View.VISIBLE);
                seat2.setVisibility(View.VISIBLE);
                seat3.setVisibility(View.VISIBLE);
                seat4.setVisibility(View.VISIBLE);
                seat5.setVisibility(View.VISIBLE);
                break;
            case 6:
                seat1.setVisibility(View.VISIBLE);
                seat2.setVisibility(View.VISIBLE);
                seat3.setVisibility(View.VISIBLE);
                seat4.setVisibility(View.VISIBLE);
                seat5.setVisibility(View.VISIBLE);
                seat6.setVisibility(View.VISIBLE);
                break;
            case 7:
                seat1.setVisibility(View.VISIBLE);
                seat2.setVisibility(View.VISIBLE);
                seat3.setVisibility(View.VISIBLE);
                seat4.setVisibility(View.VISIBLE);
                seat5.setVisibility(View.VISIBLE);
                seat6.setVisibility(View.VISIBLE);
                seat7.setVisibility(View.VISIBLE);
                break;
        }

        seat1.setOnClickListener(v -> {
            seat = "1";
            seat1.setImageResource(R.drawable.seat_filled);
            seat2.setImageResource(R.drawable.seat_outline);
            seat3.setImageResource(R.drawable.seat_outline);
            seat4.setImageResource(R.drawable.seat_outline);
            seat5.setImageResource(R.drawable.seat_outline);
            seat6.setImageResource(R.drawable.seat_outline);
            seat7.setImageResource(R.drawable.seat_outline);
        });
        seat2.setOnClickListener(v -> {
            seat = "2";
            seat1.setImageResource(R.drawable.seat_filled);
            seat2.setImageResource(R.drawable.seat_filled);
            seat3.setImageResource(R.drawable.seat_outline);
            seat4.setImageResource(R.drawable.seat_outline);
            seat5.setImageResource(R.drawable.seat_outline);
            seat6.setImageResource(R.drawable.seat_outline);
            seat7.setImageResource(R.drawable.seat_outline);
        });
        seat3.setOnClickListener(v -> {
            seat = "3";
            seat1.setImageResource(R.drawable.seat_filled);
            seat2.setImageResource(R.drawable.seat_filled);
            seat3.setImageResource(R.drawable.seat_filled);
            seat4.setImageResource(R.drawable.seat_outline);
            seat5.setImageResource(R.drawable.seat_outline);
            seat6.setImageResource(R.drawable.seat_outline);
            seat7.setImageResource(R.drawable.seat_outline);
        });
        seat4.setOnClickListener(v -> {
            seat = "4";
            seat1.setImageResource(R.drawable.seat_filled);
            seat2.setImageResource(R.drawable.seat_filled);
            seat3.setImageResource(R.drawable.seat_filled);
            seat4.setImageResource(R.drawable.seat_filled);
            seat5.setImageResource(R.drawable.seat_outline);
            seat6.setImageResource(R.drawable.seat_outline);
            seat7.setImageResource(R.drawable.seat_outline);
        });
        seat5.setOnClickListener(v -> {
            seat = "5";
            seat1.setImageResource(R.drawable.seat_filled);
            seat2.setImageResource(R.drawable.seat_filled);
            seat3.setImageResource(R.drawable.seat_filled);
            seat4.setImageResource(R.drawable.seat_filled);
            seat5.setImageResource(R.drawable.seat_filled);
            seat6.setImageResource(R.drawable.seat_outline);
            seat7.setImageResource(R.drawable.seat_outline);
        });
        seat6.setOnClickListener(v -> {
            seat = "6";
            seat1.setImageResource(R.drawable.seat_filled);
            seat2.setImageResource(R.drawable.seat_filled);
            seat3.setImageResource(R.drawable.seat_filled);
            seat4.setImageResource(R.drawable.seat_filled);
            seat5.setImageResource(R.drawable.seat_filled);
            seat6.setImageResource(R.drawable.seat_filled);
            seat7.setImageResource(R.drawable.seat_outline);
        });
        seat7.setOnClickListener(v -> {
            seat = "7";
            seat1.setImageResource(R.drawable.seat_filled);
            seat2.setImageResource(R.drawable.seat_filled);
            seat3.setImageResource(R.drawable.seat_filled);
            seat4.setImageResource(R.drawable.seat_filled);
            seat5.setImageResource(R.drawable.seat_filled);
            seat6.setImageResource(R.drawable.seat_filled);
            seat7.setImageResource(R.drawable.seat_filled);
        });

        buttonSubmit.setOnClickListener(v -> {

            if (seat.equals("0")) {
                Toast.makeText(this, "Please select the seat", Toast.LENGTH_SHORT).show();
            } else {
                updateSeats(liftId, seat);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void updateSeats(String liftId, String seat) {
        Constants.showLoader(this);
        ApiService api = RetroClient.getApiService();
        Call<ResponseBody> call = api.updateSeat(Constants.API_KEY, getResources().getString(R.string.android), strToken, liftId, Integer.parseInt(seat));


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
        dialog.setContentView(R.layout.request_cancel_reason_dialog);
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        RadioGroup reason_group = dialog.findViewById(R.id.reason_group);
        RadioButton radio1 = dialog.findViewById(R.id.radio1);
        RadioButton radio2 = dialog.findViewById(R.id.radio2);
        RadioButton radio3 = dialog.findViewById(R.id.radio3);
        LinearLayout layoutEditText = dialog.findViewById(R.id.layoutEditText);


        AppCompatButton buttonSubmit = dialog.findViewById(R.id.buttonSubmit);
        TextView titleTxt = dialog.findViewById(R.id.titleTxt);
        EditText editTextPoints = dialog.findViewById(R.id.editTextPoints);

        titleTxt.setText("Reason to cancel?");

        reason_group.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.radio4) {
                layoutEditText.setVisibility(View.VISIBLE);
            } else {
                layoutEditText.setVisibility(View.GONE);
            }
        });

        buttonSubmit.setOnClickListener(v -> {
            String reason;
            if (reason_group.getCheckedRadioButtonId() == R.id.radio1) {
                reason = radio1.getText().toString();
            } else if (reason_group.getCheckedRadioButtonId() == R.id.radio2) {
                reason = radio2.getText().toString();
            } else if (reason_group.getCheckedRadioButtonId() == R.id.radio3) {
                reason = radio3.getText().toString();
            } else {
                reason = editTextPoints.getText().toString();
            }

            if (reason.trim().equals("")) {
                Toast.makeText(this, "Please select the reason", Toast.LENGTH_SHORT).show();
            } else {
                sendCancelInvitation(liftId, fromLiftId, requestAlreadySend, reason);
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

