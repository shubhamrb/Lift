package com.liftPlzz.fragments;


import com.liftPlzz.R;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.presenter.WalkTrough1Presenter;
import com.liftPlzz.views.WalkTrough1View;

public class WalkTrough3Fragment extends BaseFragment<WalkTrough1Presenter, WalkTrough1View>
        implements WalkTrough1View {

    @Override
    protected int createLayout() {
        return R.layout.fragment_walktrough3;
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
