package com.liftPlzz.model;



public class PaymentHistoryModel {
    private String sstatus;
    private String amount;
    private String source;
    private String datetime;
    private String type;

    public PaymentHistoryModel(String sstatus, String amount, String source, String datetime, String type) {
        this.sstatus = sstatus;
        this.amount = amount;
        this.source = source;
        this.datetime = datetime;
        this.type = type;
    }

    public String getType() {
        return type;
    }
    public String getSstatus() {
        return sstatus;
    }

    public String getAmount() {
        return amount;
    }

    public String getSource() {
        return source;
    }

    public String getDatetime() {
        return datetime;
    }




}
