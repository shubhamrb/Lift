package com.liftPlzz.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.adapter.SettingAdapter;
import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.getsetting.Datum;
import com.liftPlzz.model.getsetting.SettingModel;
import com.liftPlzz.presenter.SettingPresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.SettingView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    String strToken = "";

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
        presenter.getSettingList(strToken);
    }

    @OnClick(R.id.imageViewBack)
    public void onClick() {
        getActivity().onBackPressed();
    }

    @Override
    public void setSettings(List<Datum> settingData) {
        recyclerViewSetting.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSetting.setAdapter(new SettingAdapter(getContext(), settingData, SettingFragment.this));
    }

    @Override
    public void onToggleClick(int settingId, int inputValue) {
        updateSetting(strToken, settingId, inputValue);
    }

    public void updateSetting(String token, int settingId, int inputValue) {
        Constants.showLoader(getActivity());
        ApiService api = RetroClient.getApiService();
        Call<SettingModel> call = api.updateUserSetting(Constants.API_KEY, Constants.ANDROID, token, settingId, inputValue);
        call.enqueue(new Callback<SettingModel>() {
            @Override
            public void onResponse(Call<SettingModel> call, Response<SettingModel> response) {
                Constants.hideLoader();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
//                        view.setSettings(response.body().getData());
                        presenter.getSettingList(strToken);
                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SettingModel> call, Throwable throwable) {
                Constants.hideLoader();
                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
