package com.example.scansayapplication;

public class Responsedata {
    String otp,userMobNum;

    public Responsedata(String otp , String number){
        this.otp = otp;
        this.userMobNum = userMobNum;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getNumber() {
        return userMobNum;
    }

    public void setNumber(String userMobNum) {
        this.userMobNum = userMobNum;
    }

}
