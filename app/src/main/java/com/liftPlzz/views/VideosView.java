package com.liftPlzz.views;


import com.liftPlzz.base.RootView;
import com.liftPlzz.model.videos.upcomingLift.VideosResponse;

public interface VideosView extends RootView {
    void setVideosData(VideosResponse lifts);
}
