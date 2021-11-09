package com.liftPlzz.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.adapter.MyVehicleListAdapter;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.getVehicle.Datum;
import com.liftPlzz.presenter.MyVehiclePresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.MyVehicleView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyVehicleFragment extends BaseFragment<MyVehiclePresenter, MyVehicleView> implements MyVehicleView, MyVehicleListAdapter.ItemListener {


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolBarTitle)
    AppCompatTextView toolBarTitle;
    SharedPreferences sharedPreferences;
    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    @BindView(R.id.textViewNovVehicle)
    AppCompatTextView textViewNovVehicle;
    @BindView(R.id.imageViewNoVehicle)
    GifImageView imageViewNoVehicle;
    @BindView(R.id.textViewAddVehicle)
    AppCompatTextView textViewAddVehicle;
    @BindView(R.id.layoutAddVehicle)
    LinearLayout layoutAddVehicle;
    @BindView(R.id.recyclerViewMyVehicle)
    RecyclerView recyclerViewMyVehicle;

    @Override
    protected int createLayout() {
        return R.layout.fragment_my_vehicle;
    }

    @Override
    protected void setPresenter() {
        presenter = new MyVehiclePresenter();
    }


    @Override
    protected MyVehicleView createView() {
        return this;
    }

    @Override
    protected void bindData() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        toolBarTitle.setText("My Vehicles");
        presenter.getVehicle(sharedPreferences.getString(Constants.TOKEN, ""));
    }


    @OnClick({R.id.imageViewBack, R.id.textViewAddVehicle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageViewBack:
                getActivity().onBackPressed();
                break;
            case R.id.textViewAddVehicle:
                AddVehicleFragment.setIsEdit(false);
                presenter.openAddVehicle();
                break;
        }
    }

    @Override
    public void setVehicle(List<Datum> data) {
        if (data.size() > 0) {
            recyclerViewMyVehicle.setVisibility(View.VISIBLE);
            textViewNovVehicle.setVisibility(View.GONE);
            imageViewNoVehicle.setVisibility(View.GONE);
            recyclerViewMyVehicle.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerViewMyVehicle.setAdapter(new MyVehicleListAdapter(getContext(), data, MyVehicleFragment.this));
        } else {
            recyclerViewMyVehicle.setVisibility(View.GONE);
            textViewNovVehicle.setVisibility(View.VISIBLE);
            imageViewNoVehicle.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void setVehicleDelete(String message) {
        presenter.getVehicle(sharedPreferences.getString(Constants.TOKEN, ""));
    }

    @Override
    public void onclick(int s) {

    }

    @Override
    public void onDeleteClick(int s) {
        presenter.deleteVehicle(sharedPreferences.getString(Constants.TOKEN, ""), String.valueOf(s));
    }

    @Override
    public void onEditClick(Datum s) {
        AddVehicleFragment.setIsEdit(true);
        AddVehicleFragment.setVehicleData(s);
        presenter.openAddVehicle();
    }
}
