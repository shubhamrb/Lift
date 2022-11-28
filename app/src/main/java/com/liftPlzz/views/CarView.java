package com.liftPlzz.views;


import com.liftPlzz.base.RootView;
import com.liftPlzz.model.createVehicle.Response;
import com.liftPlzz.model.vehiclesubcategory.SubCategoryResponse;

public interface CarView extends RootView {
    void createVehicle(Response response);

    void getSubCategory(SubCategoryResponse response);
}
