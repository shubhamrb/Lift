package com.liftPlzz.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.liftPlzz.R;
import com.liftPlzz.adapter.SectionPagerAddVehicleAdapter;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.getVehicle.Datum;
import com.liftPlzz.presenter.AddVehiclePresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.AddVehicleView;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddVehicleFragment extends BaseFragment<AddVehiclePresenter, AddVehicleView> implements AddVehicleView {


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolBarTitle)
    AppCompatTextView toolBarTitle;
    SharedPreferences sharedPreferences;
    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    @BindView(R.id.textViewCar)
    AppCompatTextView textViewCar;
    @BindView(R.id.layoutCar)
    LinearLayout layoutCar;
    @BindView(R.id.textViewBike)
    AppCompatTextView textViewBike;
    @BindView(R.id.layoutBike)
    LinearLayout layoutBike;
    @BindView(R.id.viewpagerAddVehicle)
    ViewPager viewpagerAddVehicle;

    public static boolean isEdit = false;
    public static Datum vehicleData;

    public static void setVehicleData(Datum vehicleData) {
        AddVehicleFragment.vehicleData = vehicleData;
    }

    public static void setIsEdit(boolean isEdit) {
        AddVehicleFragment.isEdit = isEdit;
    }

    @Override
    protected int createLayout() {
        return R.layout.fragment_add_vehicle;
    }

    @Override
    protected void setPresenter() {
        presenter = new AddVehiclePresenter();
    }


    @Override
    protected AddVehicleView createView() {
        return this;
    }

    @Override
    protected void bindData() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (!isEdit) {
            toolBarTitle.setText(getResources().getString(R.string.add_vehicle));
            SectionPagerAddVehicleAdapter sectionPagerAdapter =
                    new SectionPagerAddVehicleAdapter(getChildFragmentManager(), 2);
            viewpagerAddVehicle.setAdapter(sectionPagerAdapter);
            viewpagerAddVehicle.setOffscreenPageLimit(1);
        } else {
            toolBarTitle.setText(getResources().getString(R.string.update_vehicle));
            SectionPagerAddVehicleAdapter sectionPagerAdapter =
                    new SectionPagerAddVehicleAdapter(getChildFragmentManager(), 2, vehicleData);
            viewpagerAddVehicle.setAdapter(sectionPagerAdapter);
            viewpagerAddVehicle.setOffscreenPageLimit(1);
            if (vehicleData.getType().equalsIgnoreCase(getResources().getString(R.string.four_wheeler))) {
                viewpagerAddVehicle.setCurrentItem(0);
                CarFragment.setIsEdit(true);
                CarFragment.setVehicleData(vehicleData);
            } else {
                viewpagerAddVehicle.setCurrentItem(1);
                BikeFragment.setIsEdit(true);
                BikeFragment.setVehicleData(vehicleData);
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewpagerAddVehicle.getCurrentItem() == 0) {
            layoutCar.setBackground(getResources().getDrawable(R.drawable.rounded_bg_blue));
            layoutBike.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            textViewCar.setTextColor(getResources().getColor(R.color.colorWhite));
            textViewBike.setTextColor(getResources().getColor(R.color.colorBlack));
            textViewCar.setSelected(true);
            textViewBike.setSelected(false);
            viewpagerAddVehicle.setCurrentItem(0);
        } else {
            layoutBike.setBackground(getResources().getDrawable(R.drawable.rounded_bg_blue));
            layoutCar.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            textViewCar.setTextColor(getResources().getColor(R.color.colorBlack));
            textViewBike.setTextColor(getResources().getColor(R.color.colorWhite));
            textViewCar.setSelected(false);
            textViewBike.setSelected(true);
            viewpagerAddVehicle.setCurrentItem(1);
        }
    }

    @OnClick({R.id.imageViewBack, R.id.layoutCar, R.id.layoutBike})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageViewBack:
                getActivity().onBackPressed();
                break;
            case R.id.layoutCar:
                if (!isEdit) {
                    layoutCar.setBackground(getResources().getDrawable(R.drawable.rounded_bg_blue));
                    layoutBike.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    textViewCar.setTextColor(getResources().getColor(R.color.colorWhite));
                    textViewBike.setTextColor(getResources().getColor(R.color.colorBlack));
                    textViewCar.setSelected(true);
                    textViewBike.setSelected(false);
                    viewpagerAddVehicle.setCurrentItem(0);
                }
                break;
            case R.id.layoutBike:
                if (!isEdit) {
                    layoutBike.setBackground(getResources().getDrawable(R.drawable.rounded_bg_blue));
                    layoutCar.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    textViewCar.setTextColor(getResources().getColor(R.color.colorBlack));
                    textViewBike.setTextColor(getResources().getColor(R.color.colorWhite));
                    textViewCar.setSelected(false);
                    textViewBike.setSelected(true);
                    viewpagerAddVehicle.setCurrentItem(1);
                }
                break;
        }
    }
}
