package com.liftPlzz.model.on_going;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Lift {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("user_id")
    @Expose
    private int user_id;

    @SerializedName("vehicle_id")
    @Expose
    private int vehicle_id;

    @SerializedName("rate_per_km")
    @Expose
    private String rate_per_km;

    @SerializedName("total_km")
    @Expose
    private String total_km;

    @SerializedName("lift_type")
    @Expose
    private String lift_type;

    @SerializedName("lift_date")
    @Expose
    private String lift_date;

    @SerializedName("free_seats")
    @Expose
    private int free_seats;

    @SerializedName("paid_seats")
    @Expose
    private int paid_seats;

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("is_booked")
    @Expose
    private int is_booked;

    @SerializedName("total_request")
    @Expose
    private int total_request;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("start_point")
    @Expose
    private StartPoint start_point;

    @SerializedName("end_point")
    @Expose
    private EndPoint end_point;

    @SerializedName("lift_users")
    @Expose
    private List<LiftUsers> lift_users;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(int vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getRate_per_km() {
        return rate_per_km;
    }

    public void setRate_per_km(String rate_per_km) {
        this.rate_per_km = rate_per_km;
    }

    public String getTotal_km() {
        return total_km;
    }

    public void setTotal_km(String total_km) {
        this.total_km = total_km;
    }

    public String getLift_type() {
        return lift_type;
    }

    public void setLift_type(String lift_type) {
        this.lift_type = lift_type;
    }

    public String getLift_date() {
        return lift_date;
    }

    public void setLift_date(String lift_date) {
        this.lift_date = lift_date;
    }

    public int getFree_seats() {
        return free_seats;
    }

    public void setFree_seats(int free_seats) {
        this.free_seats = free_seats;
    }

    public int getPaid_seats() {
        return paid_seats;
    }

    public void setPaid_seats(int paid_seats) {
        this.paid_seats = paid_seats;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIs_booked() {
        return is_booked;
    }

    public void setIs_booked(int is_booked) {
        this.is_booked = is_booked;
    }

    public int getTotal_request() {
        return total_request;
    }

    public void setTotal_request(int total_request) {
        this.total_request = total_request;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public StartPoint getStart_point() {
        return start_point;
    }

    public void setStart_point(StartPoint start_point) {
        this.start_point = start_point;
    }

    public EndPoint getEnd_point() {
        return end_point;
    }

    public void setEnd_point(EndPoint end_point) {
        this.end_point = end_point;
    }

    public List<LiftUsers> getLift_users() {
        return lift_users;
    }

    public void setLift_users(List<LiftUsers> lift_users) {
        this.lift_users = lift_users;
    }
}

