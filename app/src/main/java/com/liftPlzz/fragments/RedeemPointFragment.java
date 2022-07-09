package com.liftPlzz.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.adapter.FuelCardRechargeHistoryAdapter;
import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.pointsRedemption.CardModel;
import com.liftPlzz.model.pointsRedemption.PointsModel;
import com.liftPlzz.model.recharge.RechargeFuelCardHistory;
import com.liftPlzz.model.recharge.RechargeFuelCardHistoryResponse;
import com.liftPlzz.presenter.RedeemPointPresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.RedeemPointView;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RedeemPointFragment extends BaseFragment<RedeemPointPresenter, RedeemPointView> implements RedeemPointView {

    SharedPreferences sharedPreferences;

    private String strToken;
    @BindView(R.id.buttonRecharge)
    AppCompatButton btnRecharge;
    @BindView(R.id.buttonRequest)
    AppCompatButton btnRequest;
    @BindView(R.id.tv_points)
    AppCompatTextView tv_points;
    @BindView(R.id.recyclerViewpackage)
    RecyclerView recyclerViewpackage;
    private CardModel cardModel;
    private ArrayList<RechargeFuelCardHistory> rechargeFuelCardHistories;


    @Override
    protected int createLayout() {
        return R.layout.fragment_point_redeem;
    }

    @Override
    protected void setPresenter() {
        presenter = new RedeemPointPresenter();
    }


    @Override
    protected RedeemPointView createView() {
        return this;
    }

    @Override
    protected void bindData() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        strToken = sharedPreferences.getString(Constants.TOKEN, "");

        btnRecharge.setOnClickListener(view -> {
            showRechargeDialog();
        });

        btnRequest.setOnClickListener(view -> {
            showRequestDialog();
        });

        rechargeFuelCardHistories = new ArrayList<>();
        getPointsDetail();
    }

    private void getRechargeHistory() {
        ApiService api = RetroClient.getApiService();
        Call<RechargeFuelCardHistoryResponse> call = api.rechargeFuelCardHistory(Constants.API_KEY, "android", strToken);
        call.enqueue(new Callback<RechargeFuelCardHistoryResponse>() {
            @Override
            public void onResponse(Call<RechargeFuelCardHistoryResponse> call, Response<RechargeFuelCardHistoryResponse> response) {
                Constants.hideLoader();

                if (response.body() != null) {
                    if (response.code() == 200) {
                        rechargeFuelCardHistories.clear();
                        rechargeFuelCardHistories.addAll(response.body().getRechargeFuelCardHistories());
                        FuelCardRechargeHistoryAdapter adapter = new FuelCardRechargeHistoryAdapter(getActivity(), rechargeFuelCardHistories);
                        recyclerViewpackage.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerViewpackage.setAdapter(adapter);
                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<RechargeFuelCardHistoryResponse> call, Throwable throwable) {
                Constants.hideLoader();
                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void getPointsDetail() {
        Constants.showLoader(getActivity());
        ApiService api = RetroClient.getApiService();
        Call<PointsModel> call = api.cardDetail(Constants.API_KEY, Constants.ANDROID, strToken);
        call.enqueue(new Callback<PointsModel>() {
            @Override
            public void onResponse(Call<PointsModel> call, Response<PointsModel> response) {
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        cardModel = response.body().getCardModel();
                        if (cardModel != null) {
                            tv_points.setText(String.format(Locale.getDefault(), " %d", cardModel.getCurrent_point()));
                            getRechargeHistory();
                        }
                    } else {
                        Constants.hideLoader();
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PointsModel> call, Throwable throwable) {
                Constants.hideLoader();
                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Show Recharge dialog
     */
    public void showRechargeDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.recharge_dialog);
        AppCompatButton buttonSubmit = dialog.findViewById(R.id.buttonSubmit);
        EditText editTextPoints = dialog.findViewById(R.id.editTextPoints);

        if (cardModel != null && cardModel.getCurrent_point() > 0) {
            editTextPoints.setText("" + cardModel.getCurrent_point());
        }

        buttonSubmit.setOnClickListener(v -> {
            if (editTextPoints.getText().toString().trim().equals("")) {
                Toast.makeText(getActivity(), "Please enter redeem points", Toast.LENGTH_SHORT).show();
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
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.request_dialog);
        AppCompatButton buttonSubmit = dialog.findViewById(R.id.buttonSubmit);
        EditText editTextName = dialog.findViewById(R.id.editTextName);
        EditText editTextAddress = dialog.findViewById(R.id.editTextAddress);
        EditText editTextPoints = dialog.findViewById(R.id.editTextPoints);

        if (cardModel != null) {
            try {
                editTextName.setText(cardModel.getFull_name());
                editTextPoints.setText("" + cardModel.getCurrent_point());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        buttonSubmit.setOnClickListener(v -> {
            if (editTextName.getText().toString().trim().equals("")) {
                Toast.makeText(getActivity(), "Please enter your full name.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (editTextAddress.getText().toString().trim().equals("")) {
                Toast.makeText(getActivity(), "Please enter your full address.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (editTextPoints.getText().toString().trim().equals("")) {
                Toast.makeText(getActivity(), "Please enter redeem points", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Integer.parseInt(editTextPoints.getText().toString()) < 500) {
                Toast.makeText(getActivity(), "Min. 500 points are required.", Toast.LENGTH_SHORT).show();
                return;
            }
            requestNewCard(strToken, editTextName.getText().toString().trim(), editTextAddress.getText().toString().trim(), editTextPoints.getText().toString().trim());
            dialog.dismiss();
        });
        dialog.show();
    }

    private void rechargeFuelCard(String token, String points) {
        Constants.showLoader(getActivity());
        ApiService api = RetroClient.getApiService();
        Call<PointsModel> call = api.rechargeFuelCard(Constants.API_KEY, Constants.ANDROID, token, points);
        call.enqueue(new Callback<PointsModel>() {
            @Override
            public void onResponse(Call<PointsModel> call, Response<PointsModel> response) {
                Constants.hideLoader();
                if (response.body() != null) {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    getPointsDetail();
                }
            }

            @Override
            public void onFailure(Call<PointsModel> call, Throwable throwable) {
                Constants.hideLoader();
                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestNewCard(String token, String name, String address, String points) {
        Constants.showLoader(getActivity());
        ApiService api = RetroClient.getApiService();
        Call<PointsModel> call = api.requestNewCard(Constants.API_KEY, Constants.ANDROID, token, name, address, points);
        call.enqueue(new Callback<PointsModel>() {
            @Override
            public void onResponse(Call<PointsModel> call, Response<PointsModel> response) {
                Constants.hideLoader();
                if (response.body() != null) {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    getPointsDetail();
                }
            }

            @Override
            public void onFailure(Call<PointsModel> call, Throwable throwable) {
                Constants.hideLoader();
                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
