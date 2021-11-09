package com.liftPlzz.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.OnClick;
import com.liftPlzz.R;
import com.liftPlzz.activity.HomeActivity;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.presenter.FeedbackPresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.FeedbackView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedbackFragment extends BaseFragment<FeedbackPresenter, FeedbackView> implements FeedbackView {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imageViewHome)
    ImageView imageViewHome;
    @BindView(R.id.toolBarTitle)
    AppCompatTextView toolBarTitle;
    SharedPreferences sharedPreferences;

    @Override
    protected int createLayout() {
        return R.layout.fragment_feedback;
    }

    @Override
    protected void setPresenter() {
        presenter = new FeedbackPresenter();
    }


    @Override
    protected FeedbackView createView() {
        return this;
    }

    @Override
    protected void bindData() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        toolBarTitle.setText("Feedback");
    }


    @OnClick(R.id.imageViewHome)
    public void onViewClicked() {
        ((HomeActivity) getActivity()).openDrawer();
    }
}
