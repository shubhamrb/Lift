package com.liftPlzz.views;

import com.liftPlzz.base.RootView;
import com.liftPlzz.model.ReferralDataResponse;

import java.util.List;

public interface ReferView extends RootView {
    void setData(ReferralDataResponse.ReferralModel datum);
}
