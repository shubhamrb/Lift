package com.liftPlzz.fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.activity.DriverListActivity;
import com.liftPlzz.activity.DriverProfileActivity;
import com.liftPlzz.activity.HomeActivity;
import com.liftPlzz.activity.MatchingRideActivity;
import com.liftPlzz.activity.RideRequestActivity;
import com.liftPlzz.adapter.MyUpcomingLiftAdapter;
import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BaseActivity;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.dialog.EditLiftDaiFragment;
import com.liftPlzz.model.editlift.EditVehicleData;
import com.liftPlzz.model.editlift.GetVehicleEditResponse;
import com.liftPlzz.model.upcomingLift.Lift;
import com.liftPlzz.presenter.UpComingPresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.UpComingView;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpComingFragment extends BaseFragment<UpComingPresenter, UpComingView> implements UpComingView, MyUpcomingLiftAdapter.ItemListener {

    SharedPreferences sharedPreferences;
    @BindView(R.id.recyclerViewUpcoming)
    RecyclerView recyclerViewUpcoming;
    String strToken = "";
    private EditVehicleData editLiftData;


    @Override
    protected int createLayout() {
        return R.layout.fragment_upcoming;
    }

    @Override
    protected void setPresenter() {
        presenter = new UpComingPresenter();
    }


    @Override
    protected UpComingView createView() {
        return this;
    }

    @Override
    protected void bindData() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        strToken = sharedPreferences.getString(Constants.TOKEN, "");
        presenter.getUpcomingLift(strToken);
    }

    @Override
    public void setLiftData(List<Lift> lifts) {
        if (lifts.size() > 0) {
            recyclerViewUpcoming.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerViewUpcoming.setAdapter(new MyUpcomingLiftAdapter(getContext(), lifts, UpComingFragment.this, listinerUpdate));

        }
    }

    public void getupcomingLiftUpdate() {
        presenter.getUpcomingLift(strToken);
    }

    public EditLiftDaiFragment.UpdateRecordListiner listinerUpdate = new EditLiftDaiFragment.UpdateRecordListiner() {
        @Override
        public void done() {
            getupcomingLiftUpdate();
        }
    };

    @Override
    public void onMatchClick(Lift lift, boolean isFind) {
        Intent intent;
        if (isFind) {
            intent = new Intent(getActivity(), MatchingRideActivity.class);
        } else {
            intent = new Intent(getActivity(), DriverListActivity.class);
        }
        intent.putExtra(Constants.IS_FIND_LIFT, isFind);
        intent.putExtra(Constants.LIFT_ID, lift.getId());
        intent.putExtra(Constants.VEHICLE_TYPE, lift.getVehicle_type());
        intent.putExtra(Constants.SUB_CATEGORY_ID, lift.getVehicle_subcategory());
        startActivity(intent);

    }

    @Override
    public void onRequestClick(Lift lift, boolean isFind) {
        Intent intent = new Intent(getActivity(), RideRequestActivity.class);
        intent.putExtra("lifter", lift.getLiftType().equalsIgnoreCase("Offer Lift") && String.valueOf(lift.getUserId()).equals(sharedPreferences.getString(Constants.USER_ID, "")));
        intent.putExtra(Constants.LIFT_ID, lift.getId());
        intent.putExtra(Constants.PARTNER, false);
        startActivity(intent);
    }

    @Override
    public void onPartnetDetails(Lift lift) {
        Intent intent = new Intent(getActivity(), RideRequestActivity.class);
        intent.putExtra(Constants.LIFT_ID, lift.getId());
        intent.putExtra("lifter", lift.getLiftType().equalsIgnoreCase("Offer Lift") && String.valueOf(lift.getUserId()).equals(sharedPreferences.getString(Constants.USER_ID, "")));
        intent.putExtra(Constants.PARTNER, true);
        startActivity(intent);
    }

    @Override
    public void onDriverProfile(Lift lift) {
        Intent intent = new Intent(getActivity(), DriverProfileActivity.class);
        intent.putExtra(Constants.LIFT_ID, lift.getId());
        intent.putExtra(Constants.IS_DRIVER, true);
        startActivity(intent);
    }

    @Override
    public void onCancelClick(int id) {
        presenter.getCancelLift(strToken, id);
    }

    @Override
    public void showDialog(int id) {

        Constants.showLoader(getActivity());
        ApiService api = RetroClient.getApiService();
        Call<GetVehicleEditResponse> call = api.get_lift_detail(Constants.API_KEY, Constants.ANDROID, String.valueOf(id), strToken);
        call.enqueue(new Callback<GetVehicleEditResponse>() {
            @Override
            public void onResponse(Call<GetVehicleEditResponse> call, Response<GetVehicleEditResponse> response) {
                Constants.hideLoader();
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        editLiftData = response.body().getData();

                        Dialog dialog = new Dialog(getActivity());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.find_lift_dialog);
                        AppCompatButton buttonSubmit = dialog.findViewById(R.id.buttonSubmit);

                        buttonSubmit.setOnClickListener(v -> {
                            dialog.dismiss();
                            HomeActivity activity = (HomeActivity) getActivity();
                            if (activity != null && editLiftData != null) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("liftModel", editLiftData);
                                activity.openHomeFragment(BaseActivity.PerformFragment.REPLACE, bundle);
                            }
                        });
                        dialog.show();
                    } else {
                        editLiftData = null;
                    }
                }
            }

            @Override
            public void onFailure(Call<GetVehicleEditResponse> call, Throwable throwable) {
                Constants.hideLoader();
                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void setCancelLiftData(String response) {
        presenter.getUpcomingLift(strToken);
    }
}
