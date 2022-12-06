package com.liftPlzz.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.liftPlzz.R;
import com.liftPlzz.adapter.MatchingRideAdapter;
import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.matchingridemodel.MatchingRideByCategoryResponse;
import com.liftPlzz.model.matchingridemodel.MatchingRideResponse;
import com.liftPlzz.presenter.MatchingRidePresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.MatchingRideView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchingRideFragment extends BaseFragment<MatchingRidePresenter, MatchingRideView> implements MatchingRideView,
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, MatchingRideAdapter.ItemListener {

    @BindView(R.id.recycler_matching)
    RecyclerView recyclerViewMatching;
    @BindView(R.id.tv_start_point)
    AppCompatTextView tvStartPoint;
    @BindView(R.id.tv_end_point)
    AppCompatTextView tvEndPoint;
    List<LatLng> pontos = new ArrayList<>();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolBarTitle)
    AppCompatTextView toolBarTitle;
    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    @BindView(R.id.layout_matching)
    LinearLayout linearLayout;

    @BindView(R.id.imageViewHome)
    ImageView imageViewHome;

    GoogleMap mGoogleMap;
    SupportMapFragment mapFragment;
    String origin;
    String destination, strToken = "";
    private int liftId;
    private MatchingRideAdapter matchingRideAdapter;
    private ArrayList<MatchingRideResponse> arrayList = new ArrayList<>();
    SharedPreferences sharedPreferences;
    private boolean isFind = true;

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }


    @Override
    protected int createLayout() {
        return R.layout.activity_matching_ride;
    }

    @Override
    protected void setPresenter() {
        presenter = new MatchingRidePresenter();
    }

    @Override
    protected MatchingRideView createView() {
        return this;
    }

    @Override
    protected void bindData() {
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            liftId = bundle.getInt(Constants.LIFT_ID, -1);
            isFind = bundle.getBoolean(Constants.IS_FIND_LIFT, true);

        }
        imageViewHome.setVisibility(View.VISIBLE);
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        toolBarTitle.setText(getResources().getString(R.string.driver_list));
        strToken = sharedPreferences.getString(Constants.TOKEN, "");
        mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map_matching);
        matchingRideAdapter = new MatchingRideAdapter(getActivity(), arrayList, MatchingRideFragment.this);
        recyclerViewMatching.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewMatching.setAdapter(matchingRideAdapter);
        getMachingRide();
    }

    public void getMachingRide() {
        Constants.showLoader(getActivity());
        ApiService api = RetroClient.getApiService();
        Call<MatchingRideByCategoryResponse> call = api.getMatchingRideByCategory(Constants.API_KEY, getResources().getString(R.string.android), strToken, liftId);
        call.enqueue(new Callback<MatchingRideByCategoryResponse>() {
            @Override
            public void onResponse(Call<MatchingRideByCategoryResponse> call, Response<MatchingRideByCategoryResponse> response) {
                Constants.hideLoader();
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        tvStartPoint.setText(response.body().getData().getAddress().getStartLocation());
                        tvEndPoint.setText(response.body().getData().getAddress().getEndLocation());
                        arrayList.clear();
                        arrayList.addAll(response.body().getData().getVehicleList());
                        matchingRideAdapter.notifyDataSetChanged();
                    } else {
                        Constants.showMessage(getActivity(), response.body().getMessage(), linearLayout);
                    }
                }
            }

            @Override
            public void onFailure(Call<MatchingRideByCategoryResponse> call, Throwable throwable) {
                Constants.hideLoader();
                Constants.showMessage(getActivity(), throwable.getMessage(), linearLayout);
            }
        });

    }

    @Override
    public void onCategoryClick(MatchingRideResponse matchingRideResponse) {
        presenter.openDriverList(isFind, liftId, matchingRideResponse.getType(), matchingRideResponse.getId());
    }


    @OnClick({R.id.imageViewBack, R.id.imageViewHome})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageViewBack:
                getActivity().onBackPressed();
                break;
            case R.id.imageViewHome:
                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
        }
    }
}