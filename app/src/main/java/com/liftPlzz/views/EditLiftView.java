package com.liftPlzz.views;


import com.liftPlzz.base.RootView;
import com.liftPlzz.model.FindLiftResponse;
import com.liftPlzz.model.createLift.CreateLiftResponse;
import com.liftPlzz.model.editlift.EditVehicleData;
import com.liftPlzz.model.getVehicle.Datum;

import java.util.List;

public interface EditLiftView extends RootView {

    void setFindRideData(FindLiftResponse findRideData);

    void setVehicle(List<Datum> data);

    void setCreateRideData(CreateLiftResponse createRideData);

    void getLiftDetail(EditVehicleData data);
}
