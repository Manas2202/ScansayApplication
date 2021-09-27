package com.example.scansayapplication.ui.notifications;

public class list_item_model {
    private String appName , senderName , paymentAmount , paymentTime ;

    list_item_model(String appName, String senderName, String paymentAmount, String paymentTime){
        this.appName = appName;
        this.senderName = senderName;
        this.paymentAmount = paymentAmount;
        this.paymentTime = paymentTime;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }
}
