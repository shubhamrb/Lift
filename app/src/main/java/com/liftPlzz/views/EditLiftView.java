package com.liftPlzz.views;


import com.liftPlzz.base.RootView;
import com.liftPlzz.model.FindLiftResponse;
import com.liftPlzz.model.createLift.CreateLiftResponse;
import com.liftPlzz.model.editlift.EditVehicleData;
import com.liftPlzz.model.getVehicle.Datum;

import java.util.List;

public interface EditLiftView extends RootView {

    void setFindRideData(FindLiftResponse findRideData,String action);

    void setVehicle(List<Datum> data,String action);

    void setCreateRideData(CreateLiftResponse createRideData,String action);

    void getLiftDetail(EditVehicleData data);
}
