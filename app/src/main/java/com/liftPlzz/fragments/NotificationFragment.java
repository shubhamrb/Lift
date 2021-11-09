package com.liftPlzz.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.adapter.NotificationListAdapter;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.getNotification.NotificationData;
import com.liftPlzz.presenter.NotificationPresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.NotificationView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends BaseFragment<NotificationPresenter, NotificationView> implements NotificationView {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolBarTitle)
    AppCompatTextView toolBarTitle;
    SharedPreferences sharedPreferences;
    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    @BindView(R.id.recyclerViewNotification)
    RecyclerView recyclerViewNotification;

    @Override
    protected int createLayout() {
        return R.layout.fragment_notification;
    }

    @Override
    protected void setPresenter() {
        presenter = new NotificationPresenter();
    }


    @Override
    protected NotificationView createView() {
        return this;
    }

    @Override
    protected void bindData() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        toolBarTitle.setText("Notification");
        presenter.getAllNotification(sharedPreferences.getString(Constants.TOKEN, ""));

    }


    @OnClick(R.id.imageViewBack)
    public void onViewClicked() {
        getActivity().onBackPressed();
    }

    @Override
    public void setNotificationData(List<NotificationData> notificationData) {
        recyclerViewNotification.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewNotification.setAdapter(new NotificationListAdapter(getContext(), notificationData));
    }

}
