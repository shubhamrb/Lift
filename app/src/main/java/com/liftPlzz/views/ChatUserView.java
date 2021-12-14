package com.liftPlzz.views;

import com.liftPlzz.base.RootView;
import com.liftPlzz.model.chatuser.ChatUser;

import java.util.List;

public interface ChatUserView extends RootView {
    void setChatUser(List<ChatUser> chatUserlist);
}
