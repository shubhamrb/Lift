package com.liftPlzz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AccountDataModel implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("bank_name")
    @Expose
    private String bank_name;

    @SerializedName("account_no")
    @Expose
    private String account_no;

    @SerializedName("account_name")
    @Expose
    private String account_name;

    @SerializedName("ifsc")
    @Expose
    private String ifsc;

    @SerializedName("pan_img")
    @Expose
    private String pan_img;

    @SerializedName("cheque_img")
    @Expose
    private String cheque_img;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("status_color")
    @Expose
    private String status_color;

    public String getStatus_color() {
        return status_color;
    }

    public void setStatus_color(String status_color) {
        this.status_color = status_color;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getAccount_no() {
        return account_no;
    }

    public void setAccount_no(String account_no) {
        this.account_no = account_no;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getPan_img() {
        return pan_img;
    }

    public void setPan_img(String pan_img) {
        this.pan_img = pan_img;
    }

    public String getCheque_img() {
        return cheque_img;
    }

    public void setCheque_img(String cheque_img) {
        this.cheque_img = cheque_img;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AccountDataModel{" +
                "id=" + id +
                ", bank_name='" + bank_name + '\'' +
                ", account_no='" + account_no + '\'' +
                ", account_name='" + account_name + '\'' +
                ", ifsc='" + ifsc + '\'' +
                ", pan_img='" + pan_img + '\'' +
                ", cheque_img='" + cheque_img + '\'' +
                ", status='" + status + '\'' +
                ", status_color='" + status_color + '\'' +
                '}';
    }
}