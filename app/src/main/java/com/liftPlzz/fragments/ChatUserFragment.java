package com.liftPlzz.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.liftPlzz.R;
import com.liftPlzz.activity.ChatActivity;
import com.liftPlzz.activity.DriverProfileActivity;
import com.liftPlzz.adapter.ChatUserListAdapter;
import com.liftPlzz.adapter.FAQAdapter;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.chatuser.ChatUser;
import com.liftPlzz.model.getFaq.FaqData;
import com.liftPlzz.presenter.ChatUserPresenter;
import com.liftPlzz.presenter.FaqPresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.ChatUserView;
import com.liftPlzz.views.FaqView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ChatUserFragment extends BaseFragment<ChatUserPresenter, ChatUserView> implements ChatUserView {

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
        presenter = new ChatUserPresenter();
    }

    @Override
    protected ChatUserView createView() {
        return this;
    }

    @Override
    protected void bindData() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        toolBarTitle.setText("My Chat");
        strToken = sharedPreferences.getString(Constants.TOKEN, "");
        presenter.getChatUser(strToken);
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
    public void setChatUser(List<ChatUser> chatUserlist) {
        Log.e("call ","chat user");
        recyclerViewFaq.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewFaq.setAdapter(new ChatUserListAdapter(getContext(), chatUserlist, new ChatUserListAdapter.ItemListener() {
            @Override
            public void onSendButtonClick(ChatUser chatUser) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(Constants.USER_ID, String.valueOf(chatUser.getId()));
                intent.putExtra("charuser",new Gson().toJson(chatUser));
                startActivity(intent);
            }
            @Override
            public void onUserImageClick(int id) {

            }
        }));
    }
}
