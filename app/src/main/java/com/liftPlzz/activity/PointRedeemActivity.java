package com.liftPlzz.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.liftPlzz.R;
import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.model.pointsRedemption.CardModel;
import com.liftPlzz.model.pointsRedemption.PointsModel;
import com.liftPlzz.utils.Constants;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PointRedeemActivity extends AppCompatActivity {

    ImageView imageView;
    private String strToken;
    SharedPreferences sharedPreferences;
    private AppCompatButton btnRecharge, btnRequest;
    private AppCompatTextView tv_points;
    private CardModel cardModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_redeem);
        imageView = findViewById(R.id.imageViewBackrecharge);
        btnRecharge = findViewById(R.id.buttonRecharge);
        btnRequest = findViewById(R.id.buttonRequest);
        tv_points = findViewById(R.id.tv_points);
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        strToken = sharedPreferences.getString(Constants.TOKEN, "");

        imageView.setOnClickListener(view -> {
            Intent intent = new Intent(PointRedeemActivity.this, HomeActivity.class);
            startActivity(intent);
        });

        btnRecharge.setOnClickListener(view -> {
            showRechargeDialog();
        });

        btnRequest.setOnClickListener(view -> {
            showRequestDialog();
        });

        getPointsDetail();

    }

    private void getPointsDetail() {
        Constants.showLoader(this);
        ApiService api = RetroClient.getApiService();
        Call<PointsModel> call = api.cardDetail(Constants.API_KEY, Constants.ANDROID, strToken);
        call.enqueue(new Callback<PointsModel>() {
            @Override
            public void onResponse(Call<PointsModel> call, Response<PointsModel> response) {
                Constants.hideLoader();
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        cardModel = response.body().getCardModel();
                        if (cardModel != null) {
                            tv_points.setText(String.format(Locale.getDefault(), " %d", cardModel.getCurrent_point()));
                        }
                    } else {
                        Toast.makeText(PointRedeemActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PointsModel> call, Throwable throwable) {
                Constants.hideLoader();
                Toast.makeText(PointRedeemActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Show Recharge dialog
     */
    public void showRechargeDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.recharge_dialog);
        AppCompatButton buttonSubmit = dialog.findViewById(R.id.buttonSubmit);
        EditText editTextPoints = dialog.findViewById(R.id.editTextPoints);

        if (cardModel != null && cardModel.getCurrent_point() > 0) {
            editTextPoints.setText("" + cardModel.getCurrent_point());
        }

        buttonSubmit.setOnClickListener(v -> {
            if (editTextPoints.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Please enter redeem points", Toast.LENGTH_SHORT).show();
            } else {
                rechargeFuelCard(strToken, editTextPoints.getText().toString().trim());
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    /**
     * Show request dialog
     */
    public void showRequestDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.request_dialog);
        AppCompatButton buttonSubmit = dialog.findViewById(R.id.buttonSubmit);
        EditText editTextName = dialog.findViewById(R.id.editTextName);
        EditText editTextAddress = dialog.findViewById(R.id.editTextAddress);
        EditText editTextPoints = dialog.findViewById(R.id.editTextPoints);

        if (cardModel != null) {
            try {
                editTextName.setText(cardModel.getFull_name());
                editTextPoints.setText(""+cardModel.getCurrent_point());
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        buttonSubmit.setOnClickListener(v -> {
            if (editTextName.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Please enter your full name.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (editTextAddress.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Please enter your full address.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (editTextPoints.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Please enter redeem points", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Integer.parseInt(editTextPoints.getText().toString()) < 500) {
                Toast.makeText(this, "Min. 500 points are required.", Toast.LENGTH_SHORT).show();
                return;
            }
            requestNewCard(strToken, editTextName.getText().toString().trim(), editTextAddress.getText().toString().trim(), editTextPoints.getText().toString().trim());
            dialog.dismiss();
        });
        dialog.show();
    }

    private void rechargeFuelCard(String token, String points) {
        Constants.showLoader(this);
        ApiService api = RetroClient.getApiService();
        Call<PointsModel> call = api.rechargeFuelCard(Constants.API_KEY, Constants.ANDROID, token, points);
        call.enqueue(new Callback<PointsModel>() {
            @Override
            public void onResponse(Call<PointsModel> call, Response<PointsModel> response) {
                Constants.hideLoader();
                if (response.body() != null) {
                    Toast.makeText(PointRedeemActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PointsModel> call, Throwable throwable) {
                Constants.hideLoader();
                Toast.makeText(PointRedeemActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestNewCard(String token, String name, String address, String points) {
        Constants.showLoader(this);
        ApiService api = RetroClient.getApiService();
        Call<PointsModel> call = api.requestNewCard(Constants.API_KEY, Constants.ANDROID, token, name, address, points);
        call.enqueue(new Callback<PointsModel>() {
            @Override
            public void onResponse(Call<PointsModel> call, Response<PointsModel> response) {
                Constants.hideLoader();
                if (response.body() != null) {
                    Toast.makeText(PointRedeemActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PointsModel> call, Throwable throwable) {
                Constants.hideLoader();
                Toast.makeText(PointRedeemActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}