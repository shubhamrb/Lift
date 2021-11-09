package com.liftPlzz.views;


import com.liftPlzz.base.RootView;
import com.liftPlzz.model.getVehicle.Datum;

import java.util.List;

public interface MyVehicleView extends RootView {


    void setVehicle(List<Datum> data);

    void setVehicleDelete(String message);
}
