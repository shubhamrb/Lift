package com.liftPlzz.model.editlift;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.liftPlzz.model.upcomingLift.Lift;

import java.util.ArrayList;

public class EditVehicleData {

    @SerializedName("lift")
    @Expose
    private Lift lift;

    @SerializedName("start_point")
    @Expose
    private LiftLocationModel start_point;

    @SerializedName("end_point")
    @Expose
    private LiftLocationModel end_point;

    @SerializedName("check_point")
    @Expose
    private ArrayList<LiftLocationModel> check_point;

    public Lift getLift() {
        return lift;
    }

    public void setLift(Lift lift) {
        this.lift = lift;
    }

    public LiftLocationModel getStart_point() {
        return start_point;
    }

    public void setStart_point(LiftLocationModel start_point) {
        this.start_point = start_point;
    }

    public LiftLocationModel getEnd_point() {
        return end_point;
    }

    public void setEnd_point(LiftLocationModel end_point) {
        this.end_point = end_point;
    }

    public ArrayList<LiftLocationModel> getCheck_point() {
        return check_point;
    }

    public void setCheck_point(ArrayList<LiftLocationModel> check_point) {
        this.check_point = check_point;
    }
}

