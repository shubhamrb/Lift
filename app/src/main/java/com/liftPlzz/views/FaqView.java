package com.liftPlzz.views;

import com.liftPlzz.base.RootView;
import com.liftPlzz.model.getFaq.FaqData;

import java.util.List;

public interface FaqView extends RootView {
    void setFAQ(List<FaqData> faqData);
}
