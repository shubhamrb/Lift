package com.liftPlzz.fragments;


import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.adapter.CompletedLiftAdapter;
import com.liftPlzz.adapter.MyUpcomingLiftAdapter;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.completedLift.CompleteLiftData;
import com.liftPlzz.model.upcomingLift.Lift;
import com.liftPlzz.presenter.CompletedPresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.CompletedView;

import java.util.List;

import butterknife.BindView;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompletedFragment extends BaseFragment<CompletedPresenter, CompletedView> implements CompletedView, MyUpcomingLiftAdapter.ItemListener {

    SharedPreferences sharedPreferences;
    @BindView(R.id.recyclerViewUpcoming)
    RecyclerView recyclerViewUpcoming;


    @Override
    protected int createLayout() {
        return R.layout.fragment_upcoming;
    }

    @Override
    protected void setPresenter() {
        presenter = new CompletedPresenter();
    }


    @Override
    protected CompletedView createView() {
        return this;
    }

    @Override
    protected void bindData() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        presenter.getCompletedLift(sharedPreferences.getString(Constants.TOKEN, ""));
    }

    @Override
    public void setLiftData(List<CompleteLiftData> lifts) {
        if (lifts.size() > 0) {
            recyclerViewUpcoming.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerViewUpcoming.setAdapter(new CompletedLiftAdapter(getContext(), lifts));
        }
    }

    @Override
    public void onMatchClick(Lift lift) {

    }

    @Override
    public void onRequestClick(Lift lift) {

    }

    @Override
    public void onPartnetDetails(Lift lift) {

    }

    @Override
    public void onDriverProfile(Lift lift) {

    }

    @Override
    public void onCancelClick(int Id) {

    }
}
