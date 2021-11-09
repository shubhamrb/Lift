package com.liftPlzz.fragments;


import androidx.appcompat.widget.AppCompatImageView;

import com.liftPlzz.R;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.presenter.WalkTrough1Presenter;
import com.liftPlzz.views.WalkTrough1View;

import butterknife.BindView;

public class WalkTrough1Fragment extends BaseFragment<WalkTrough1Presenter, WalkTrough1View>
        implements WalkTrough1View {

    @BindView(R.id.imageViewCall2)
    AppCompatImageView imageViewCall2;

    @Override
    protected int createLayout() {
        return R.layout.fragment_walktrough1;
    }

    @Override
    protected void setPresenter() {
        presenter = new WalkTrough1Presenter();
    }

    @Override
    protected WalkTrough1View createView() {
        return this;
    }

    @Override
    protected void bindData() {}
}
