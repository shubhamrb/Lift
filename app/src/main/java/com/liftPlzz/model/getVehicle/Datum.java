package com.liftPlzz.model.getVehicle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("registration_no")
    @Expose
    private String registrationNo;
    @SerializedName("vehicle_image_front")
    @Expose
    private String vehicleImageFront;
    @SerializedName("vehicle_image_back")
    @Expose
    private String vehicleImageBack;
    @SerializedName("rc_image")
    @Expose
    private String rcImage;
    @SerializedName("insurance_date")
    @Expose
    private String insurance_date;

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("rate_per_km")
    @Expose
    private Integer ratePerKm;
    @SerializedName("is_default")
    @Expose
    private Integer isDefault;
    @SerializedName("vehicle_subcategory_id")
    @Expose
    private Integer vehicleSubcategoryId;
    @SerializedName("vehicle_subcategory")
    @Expose
    private String vehicleSubcategory;

    @SerializedName("vehicle_percentage")
    @Expose
    private Integer vehicle_percentage;
    @SerializedName("percentage_color")
    @Expose
    private String percentage_color;

    public Integer getVehicle_percentage() {
        return vehicle_percentage;
    }

    public void setVehicle_percentage(Integer vehicle_percentage) {
        this.vehicle_percentage = vehicle_percentage;
    }

    public String getPercentage_color() {
        return percentage_color;
    }

    public void setPercentage_color(String percentage_color) {
        this.percentage_color = percentage_color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getVehicleImageFront() {
        return vehicleImageFront;
    }

    public void setVehicleImageFront(String vehicleImageFront) {
        this.vehicleImageFront = vehicleImageFront;
    }

    public String getVehicleImageBack() {
        return vehicleImageBack;
    }

    public void setVehicleImageBack(String vehicleImageBack) {
        this.vehicleImageBack = vehicleImageBack;
    }

    public String getRcImage() {
        return rcImage;
    }

    public void setRcImage(String rcImage) {
        this.rcImage = rcImage;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public String getInsurance_date() {
        return insurance_date;
    }

    public void setInsurance_date(String insurance_date) {
        this.insurance_date = insurance_date;
    }

    public Integer getVehicleSubcategoryId() {
        return vehicleSubcategoryId;
    }

    public void setVehicleSubcategoryId(Integer vehicleSubcategoryId) {
        this.vehicleSubcategoryId = vehicleSubcategoryId;
    }

    public String getVehicleSubcategory() {
        return vehicleSubcategory;
    }

    public void setVehicleSubcategory(String vehicleSubcategory) {
        this.vehicleSubcategory = vehicleSubcategory;
    }

    public Integer getRatePerKm() {
        return ratePerKm;
    }

    public void setRatePerKm(Integer ratePerKm) {
        this.ratePerKm = ratePerKm;
    }
}