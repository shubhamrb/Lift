package com.liftPlzz.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.activity.DriverProfileActivity;
import com.liftPlzz.activity.MatchingRideActivity;
import com.liftPlzz.activity.RideRequestActivity;
import com.liftPlzz.adapter.MyUpcomingLiftAdapter;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.upcomingLift.Lift;
import com.liftPlzz.presenter.UpComingPresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.UpComingView;

import java.util.List;

import butterknife.BindView;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpComingFragment extends BaseFragment<UpComingPresenter, UpComingView> implements UpComingView, MyUpcomingLiftAdapter.ItemListener {

    SharedPreferences sharedPreferences;
    @BindView(R.id.recyclerViewUpcoming)
    RecyclerView recyclerViewUpcoming;
    String strToken = "";


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
            recyclerViewUpcoming.setAdapter(new MyUpcomingLiftAdapter(getContext(), lifts, UpComingFragment.this));
        }
    }

    @Override
    public void onMatchClick(Lift lift) {
        Intent intent = new Intent(getActivity(), MatchingRideActivity.class);
        intent.putExtra(Constants.LIFT_ID, lift.getId());
        startActivity(intent);
    }

    @Override
    public void onRequestClick(Lift lift) {
        Intent intent = new Intent(getActivity(), RideRequestActivity.class);
        intent.putExtra(Constants.LIFT_ID, lift.getId());
        intent.putExtra(Constants.PARTNER, false);
        startActivity(intent);
    }

    @Override
    public void onPartnetDetails(Lift lift) {
        Intent intent = new Intent(getActivity(), RideRequestActivity.class);
        intent.putExtra(Constants.LIFT_ID, lift.getId());
        intent.putExtra(Constants.PARTNER, true);
        startActivity(intent);
    }

    @Override
    public void onDriverProfile(Lift lift) {
        Intent intent = new Intent(getActivity(), DriverProfileActivity.class);
        intent.putExtra(Constants.USER_ID, lift.getUserId());
        startActivity(intent);
    }

    @Override
    public void onCancelClick(int id) {
        presenter.getCancelLift(strToken, id);
    }

    @Override
    public void setCancelLiftData(String response) {
        presenter.getUpcomingLift(strToken);
    }
}
