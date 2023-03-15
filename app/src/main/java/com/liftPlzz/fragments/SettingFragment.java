package com.liftPlzz.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.liftPlzz.R;
import com.liftPlzz.adapter.SettingAdapter;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.createProfile.User;
import com.liftPlzz.model.getsetting.SettingModel;
import com.liftPlzz.presenter.SettingPresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.SettingView;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingFragment extends BaseFragment<SettingPresenter, SettingView> implements SettingView, SettingAdapter.ItemListener {

    SharedPreferences sharedPreferences;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    @BindView(R.id.toolBarTitle)
    AppCompatTextView toolBarTitle;
    @BindView(R.id.recyclerViewSetting)
    RecyclerView recyclerViewSetting;

    @BindView(R.id.imageViewHome)
    ImageView imageViewHome;

    String strToken = "";
    String professionSelection = "";
    private User userData;

    @Override
    protected int createLayout() {
        return R.layout.setting_fragment;
    }

    @Override
    protected void setPresenter() {
        presenter = new SettingPresenter();
    }

    @Override
    protected SettingView createView() {
        return this;
    }

    @Override
    protected void bindData() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        toolBarTitle.setText(getResources().getString(R.string.setting));
        strToken = sharedPreferences.getString(Constants.TOKEN, "");
        imageViewHome.setVisibility(View.VISIBLE);
        loadSettings();
    }

    private void loadSettings() {
        presenter.getSettingList(strToken);
    }

    @OnClick({R.id.imageViewBack, R.id.imageViewHome})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageViewBack:
                getActivity().onBackPressed();
                break;
            case R.id.imageViewHome:
                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
        }
    }

    @Override
    public void setSettings(SettingModel model) {
        recyclerViewSetting.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSetting.setAdapter(new SettingAdapter(getContext(), model.getTitle(), SettingFragment.this));
    }

    @Override
    public void setProfileData(com.liftPlzz.model.createProfile.Response response) {
        userData = response.getUser();
        if (userData != null) {
            UpdateProfileFragment.setUser(userData);
        }
        presenter.openUpdateProfile();
    }

    @Override
    public void onSuccessReset(JsonObject jsonObject) {
        showMessage(jsonObject.get("message").getAsString());
        loadSettings();
    }

    @Override
    public void onSuccessAccountSuspend(JsonObject datum) {

    }

    @Override
    public void onSelectOption(String title, int id) {
        presenter.openSettingOption(title, id);
    }
}
