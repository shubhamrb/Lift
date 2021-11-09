package com.liftPlzz.views;


import com.liftPlzz.base.RootView;
import com.liftPlzz.model.getNotification.NotificationData;

import java.util.List;

public interface NotificationView extends RootView {
    void setNotificationData(List<NotificationData> notificationData);
}
