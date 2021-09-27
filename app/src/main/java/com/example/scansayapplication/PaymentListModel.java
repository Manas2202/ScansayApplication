package com.example.scansayapplication;

public class PaymentListModel {
    String senderName,userName,userNumber,paymentApp,paymentAmount,paymentDate,paymentTime;

    PaymentListModel(String senderName,String userName,String userNumber,String paymentApp,String paymentAmount,String paymentDate,String paymentTime){
        this.senderName = senderName;
        this.userName = userName;
        this.userNumber = userNumber;
        this.paymentApp = paymentApp;
        this.paymentAmount = paymentAmount;
        this.paymentDate = paymentDate;
        this.paymentTime = paymentTime;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getPaymentApp() {
        return paymentApp;
    }

    public void setPaymentApp(String paymentApp) {
        this.paymentApp = paymentApp;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }
}
