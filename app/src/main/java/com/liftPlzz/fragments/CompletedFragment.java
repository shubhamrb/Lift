package com.liftPlzz.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.liftPlzz.R;
import com.liftPlzz.adapter.CompletedLiftAdapter;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.dialog.EditLiftDaiFragment;
import com.liftPlzz.model.completedLift.CompleteLiftData;
import com.liftPlzz.model.upcomingLift.Lift;
import com.liftPlzz.presenter.CompletedPresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.CompletedView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompletedFragment extends BaseFragment<CompletedPresenter, CompletedView> implements CompletedView, CompletedLiftAdapter.ItemListener {

    SharedPreferences sharedPreferences;
    @BindView(R.id.recyclerViewUpcoming)
    RecyclerView recyclerViewUpcoming;
    String strToken;

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
        strToken = sharedPreferences.getString(Constants.TOKEN, "");
        presenter.getCompletedLift(strToken);
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
    public void showBill(int request_id) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST, "https://charpair.com/api/get-invoice", response -> {
            try {
                JSONObject jObject = new JSONObject(response);
                JSONObject data = jObject.getJSONObject("data");
                JSONObject st = data.getJSONObject("start_point");
                String stlocation = st.getString("location");
                JSONObject end = data.getJSONObject("end_point");
                String etlocation = end.getString("location");
                String date = data.getString("date");
                String distance = data.getString("distance");
                Integer perkm = data.getInt("per_km_point");
                String totalpoint = data.getString("total_point");

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                dialogBuilder.setTitle("Invoice");
                dialogBuilder.setCancelable(true);
                View dialogView = inflater.inflate(R.layout.invoicelayout, null);
                dialogBuilder.setView(dialogView);
                TextView pickuplocation = dialogView.findViewById(R.id.pickuplocationn);
                TextView droplocation = dialogView.findViewById(R.id.droplocationn);
                TextView datee = dialogView.findViewById(R.id.datee);
                TextView distancee = dialogView.findViewById(R.id.distance);
                TextView kmpoint = dialogView.findViewById(R.id.kmpoint);
                TextView totalpointt = dialogView.findViewById(R.id.totalpoint);
                Button paybtn = dialogView.findViewById(R.id.pay);
                Button okayBtn = dialogView.findViewById(R.id.okayBtn);
                paybtn.setVisibility(View.GONE);
                okayBtn.setVisibility(View.VISIBLE);

                pickuplocation.setText("Pickup Location : " + stlocation);
                droplocation.setText("Drop Location : " + etlocation);
                datee.setText("Date : " + date);
                distancee.setText("Distance : " + distance);
                kmpoint.setText("Per KM Point : " + perkm);
                totalpointt.setText("Total Point : " + totalpoint);
                AlertDialog alertDialog = dialogBuilder.create();

                okayBtn.setOnClickListener(view -> alertDialog.dismiss());

                alertDialog.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", Constants.API_KEY);
                params.put("client", Constants.ANDROID);
                params.put("token", strToken);
                params.put("request_id", String.valueOf(request_id));
                return params;
            }
        };
        queue.add(sr);
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
    public void onAddClick(Lift completeLiftData, EditLiftDaiFragment.UpdateRecordListiner listinerUpdate, String edit) {
        presenter.openAddLift(completeLiftData, listinerUpdate, edit);

    }

    @Override
    public void onDeleteClick(int id) {
        presenter.getDeleteLift(sharedPreferences.getString(Constants.TOKEN, ""), id);
    }
}
