package com.liftPlzz.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.liftPlzz.R;
import com.liftPlzz.activity.ChatActivity;
import com.liftPlzz.activity.HomeActivity;
import com.liftPlzz.activity.TicketListActivity;
import com.liftPlzz.activity.YoutubeActivity;
import com.liftPlzz.adapter.VideosAdapter;
import com.liftPlzz.base.BaseActivity;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.chatuser.ChatUser;
import com.liftPlzz.model.videos.upcomingLift.VideosResponse;
import com.liftPlzz.presenter.VideosPresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.VideosView;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideosFragment extends BaseFragment<VideosPresenter, VideosView> implements VideosView, VideosAdapter.ItemListener {

    SharedPreferences sharedPreferences;
    @BindView(R.id.recyclerViewUpcoming)
    RecyclerView recyclerViewUpcoming;
    @BindView(R.id.toolBarTitle)
    AppCompatTextView toolBarTitle;
    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    @Override
    protected int createLayout() {
        return R.layout.fragment_videos;
    }

    @Override
    protected void setPresenter() {
        presenter = new VideosPresenter();
    }


    @Override
    protected VideosView createView() {
        return this;
    }

    @Override
    protected void bindData() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        toolBarTitle.setText("Help Videos");
        presenter.getVideos();
    }

    @OnClick({R.id.imageViewBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageViewBack:
                getActivity().onBackPressed();
                break;
        }
    }


    @Override
    public void setVideosData(VideosResponse response) {
        if (response.getData().size() > 0) {
            recyclerViewUpcoming.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerViewUpcoming.setAdapter(new VideosAdapter(getContext(), response.getData(), this));

        }
    }


    @Override
    public void onItemClick(String url) {
        Intent intent = new Intent(getActivity(), YoutubeActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

}
