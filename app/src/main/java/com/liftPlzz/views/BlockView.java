package com.liftPlzz.views;


import com.liftPlzz.base.RootView;
import com.liftPlzz.model.partnerdetails.User;

import java.util.List;

public interface BlockView extends RootView {
    void setBlockData(List<User> notificationData);

    void onSuccessBlock();
}
