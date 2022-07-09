package com.liftPlzz.model.recharge;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RechargeFuelCardHistoryResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<RechargeFuelCardHistory> rechargeFuelCardHistories = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<RechargeFuelCardHistory> getRechargeFuelCardHistories() {
        return rechargeFuelCardHistories;
    }

    public void setRechargeFuelCardHistories(List<RechargeFuelCardHistory> rechargeFuelCardHistories) {
        this.rechargeFuelCardHistories = rechargeFuelCardHistories;
    }
}


