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
import com.liftPlzz.adapter.SectionPagerMyRidesAdapter;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.presenter.AddVehiclePresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.AddVehicleView;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyRidesFragment extends BaseFragment<AddVehiclePresenter, AddVehicleView> implements AddVehicleView {


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolBarTitle)
    AppCompatTextView toolBarTitle;
    SharedPreferences sharedPreferences;
    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    @BindView(R.id.text_upcoming)
    AppCompatTextView textViewCar;
    @BindView(R.id.layoutupcoming)
    LinearLayout layoutupcoming;
    @BindView(R.id.text_completed)
    AppCompatTextView textCompleted;
    @BindView(R.id.layoutcompleted)
    LinearLayout layoutcompleted;
    @BindView(R.id.viewpagermyride)
    ViewPager viewpagermyride;

    @Override
    protected int createLayout() {
        return R.layout.fragment_my_rides;
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
        toolBarTitle.setText(getResources().getString(R.string.my_lifts));
        SectionPagerMyRidesAdapter sectionPagerAdapter =
                new SectionPagerMyRidesAdapter(getChildFragmentManager(), 2);
        viewpagermyride.setAdapter(sectionPagerAdapter);
        viewpagermyride.setOffscreenPageLimit(1);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (viewpagermyride.getCurrentItem() == 0) {
            layoutupcoming.setBackground(getResources().getDrawable(R.drawable.rounded_bg_blue));
            layoutcompleted.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            textViewCar.setTextColor(getResources().getColor(R.color.colorWhite));
            textCompleted.setTextColor(getResources().getColor(R.color.colorBlack));
            viewpagermyride.setCurrentItem(0);
        } else {
            layoutcompleted.setBackground(getResources().getDrawable(R.drawable.rounded_bg_blue));
            layoutupcoming.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            textViewCar.setTextColor(getResources().getColor(R.color.colorBlack));
            textCompleted.setTextColor(getResources().getColor(R.color.colorWhite));
            viewpagermyride.setCurrentItem(1);
        }
    }

    @OnClick({R.id.imageViewBack, R.id.layoutupcoming, R.id.layoutcompleted})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageViewBack:
                getActivity().onBackPressed();
                break;
            case R.id.layoutupcoming:
                layoutupcoming.setBackground(getResources().getDrawable(R.drawable.rounded_bg_blue));
                layoutcompleted.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                textViewCar.setTextColor(getResources().getColor(R.color.colorWhite));
                textCompleted.setTextColor(getResources().getColor(R.color.colorBlack));
                viewpagermyride.setCurrentItem(0);
                break;
            case R.id.layoutcompleted:
                layoutcompleted.setBackground(getResources().getDrawable(R.drawable.rounded_bg_blue));
                layoutupcoming.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                textViewCar.setTextColor(getResources().getColor(R.color.colorBlack));
                textCompleted.setTextColor(getResources().getColor(R.color.colorWhite));
                viewpagermyride.setCurrentItem(1);
                break;
        }
    }
}
