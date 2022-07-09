package com.liftPlzz.model.recharge;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RechargeHistoryResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<RechargeHistory> rechargeHistories = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<RechargeHistory> getRechargeHistories() {
        return rechargeHistories;
    }

    public void setRechargeHistories(List<RechargeHistory> rechargeHistories) {
        this.rechargeHistories = rechargeHistories;
    }
}


