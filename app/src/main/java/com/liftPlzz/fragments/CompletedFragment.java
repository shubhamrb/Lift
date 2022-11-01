package com.liftPlzz.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.adapter.CompletedLiftAdapter;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.dialog.EditLiftDaiFragment;
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
public class CompletedFragment extends BaseFragment<CompletedPresenter, CompletedView> implements CompletedView, CompletedLiftAdapter.ItemListener {

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

    public EditLiftDaiFragment.UpdateRecordListiner listinerUpdate = new EditLiftDaiFragment.UpdateRecordListiner() {
        @Override
        public void done() {
//            getupcomingLiftUpdate();
        }
    };

    @Override
    public void setLiftData(List<CompleteLiftData> lifts) {
        if (lifts.size() > 0) {
            recyclerViewUpcoming.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerViewUpcoming.setAdapter(new CompletedLiftAdapter(getContext(), lifts, listinerUpdate, this));
            recyclerViewUpcoming.setVisibility(View.VISIBLE);
        } else {
            recyclerViewUpcoming.setVisibility(View.GONE);
        }
    }

    @Override
    public void deleteLiftData(String msg) {
        presenter.getCompletedLift(sharedPreferences.getString(Constants.TOKEN, ""));
    }

    @Override
    public void onMatchClick(CompleteLiftData model) {
//        EditLiftDaiFragment sheet = new EditLiftDaiFragment();
        Lift lif = new Lift();
        lif.setId(model.getId());
        lif.setTitle(model.getTitle());
        lif.setUserId(model.getUserId());
        lif.setVehicleId(model.getVehicleId());
        lif.setLiftType(model.getLiftType());
        lif.setLiftType(model.getLiftType());
        lif.setFreeSeats(model.getFreeSeats());
        lif.setPaidSeats(model.getPaidSeats());
        lif.setLiftDate(model.getLiftDate());
        lif.setStatus(model.getStatus());
        lif.setIsBooked(model.getIsBooked());
        lif.setTotalRequest(model.getTotalRequest());
        lif.setCreatedAt(model.getCreatedAt());
        lif.setUpdatedAt(model.getUpdatedAt());
        lif.setStartLatlong(model.getStartLatlong());
        lif.setStartLocation(model.getStartLocation());
        lif.setEndLatlong(model.getEndLatlong());
        lif.setEndLocation(model.getEndLocation());
        lif.setTotalDistance(model.getTotalDistance());
//        lif.setStart_time(model.get());
//        lif.setSameroutevehicle(model.get());
//        lif.setFindMatch(model.ge());


//        sheet.setLift(lif,listinerUpdate );
//        sheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.MyTheme);
//        sheet.show(((FragmentActivity)getActivity()).getSupportFragmentManager().beginTransaction(),"dialog");

    }

    @Override
    public void onDeleteClick(int id) {
        presenter.getDeleteLift(sharedPreferences.getString(Constants.TOKEN, ""), id);
    }
}
