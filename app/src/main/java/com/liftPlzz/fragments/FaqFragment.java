package com.liftPlzz.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.adapter.FAQAdapter;
import com.liftPlzz.adapter.SettingAdapter;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.getFaq.FaqData;
import com.liftPlzz.presenter.FaqPresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.FaqView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class FaqFragment extends BaseFragment<FaqPresenter, FaqView> implements FaqView {

    SharedPreferences sharedPreferences;
    String strToken = "";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    @BindView(R.id.toolBarTitle)
    AppCompatTextView toolBarTitle;
    @BindView(R.id.recyclerFAQ)
    RecyclerView recyclerViewFaq;

    @Override
    protected int createLayout() {
        return R.layout.fragment_faq;
    }

    @Override
    protected void setPresenter() {
        presenter = new FaqPresenter();
    }

    @Override
    protected FaqView createView() {
        return this;
    }

    @Override
    protected void bindData() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        toolBarTitle.setText(getResources().getString(R.string.txt_faq));
        strToken = sharedPreferences.getString(Constants.TOKEN, "");
        presenter.getFAQList();
    }

    @Override
    public void setFAQ(List<FaqData> faqData) {
        recyclerViewFaq.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewFaq.setAdapter(new FAQAdapter(getContext(), faqData));
    }

    @OnClick({R.id.imageViewBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageViewBack:
                getActivity().onBackPressed();
                break;
        }
    }
}
