package com.liftPlzz.model;

public class PaymentPackageModel {
    private String amount;
    private String points;
    private String description;

    public PaymentPackageModel(String amount, String points, String description) {
        this.amount = amount;
        this.points = points;
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public String getPoints() {
        return points;
    }

    public String getDescription() {
        return description;
    }


}
