package com.example.scansayapplication;

public class LoginResponseData {
    String userName,userMobNum,shopname,address;

    public LoginResponseData(String userName , String userMobNum , String shopname , String address){
        this.userName = userName;
        this.userMobNum = userMobNum;
        this.shopname = shopname;
        this.address = address;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMobNum() {
        return userMobNum;
    }

    public void setUserMobNum(String userMobNum) {
        this.userMobNum = userMobNum;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
