package com.liftPlzz.model.getsetting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Datum implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("setting_title_id")
    @Expose
    private Integer setting_title_id;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("option_type")
    @Expose
    private String optionType;

    @SerializedName("option_value")
    @Expose
    private List<String> option_value;

    @SerializedName("short_code")
    @Expose
    private String shortCode;
    @SerializedName("selected_value")
    @Expose
    private String selectedValue;


    public Integer getSetting_title_id() {
        return setting_title_id;
    }

    public void setSetting_title_id(Integer setting_title_id) {
        this.setting_title_id = setting_title_id;
    }

    public List<String> getOption_value() {
        return option_value;
    }

    public void setOption_value(List<String> option_value) {
        this.option_value = option_value;
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

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }

}