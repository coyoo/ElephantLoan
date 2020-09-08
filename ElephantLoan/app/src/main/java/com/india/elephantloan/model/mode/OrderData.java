package com.india.elephantloan.model.mode;

public class OrderData {
    private String UserIcon;
    private String UserName;
    private String UserQuota;
    private String UserInterest;
    private String UserTime;
    private String UserStage;
    private String IntentUrl;
    private String ProductId;

    public OrderData(String UserIcon, String UserName, String UserQuota,String UserInterest,String UserTime,String UserStage,String IntentUrl,String ProductId) {
        this.UserIcon=UserIcon;
        this.UserName=UserName;
        this.UserQuota=UserQuota;
        this.UserInterest=UserInterest;
        this.UserTime=UserTime;
        this.UserStage=UserStage;
        this.IntentUrl=IntentUrl;
        this.ProductId=ProductId;
    }


    public String getUserIcon() {
        return UserIcon;
    }

    public void setUserIcon(String userIcon) {
        UserIcon = userIcon;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserQuota() {
        return UserQuota;
    }

    public void setUserQuota(String userQuota) {
        UserQuota = userQuota;
    }

    public String getUserInterest() {
        return UserInterest;
    }

    public void setUserInterest(String userInterest) {
        UserInterest = userInterest;
    }

    public String getUserTime() {
        return UserTime;
    }

    public void setUserTime(String userTime) {
        UserTime = userTime;
    }

    public String getUserStage() {
        return UserStage;
    }

    public void setUserStage(String userStage) {
        UserStage = userStage;
    }

    public String getIntentUrl() {
        return IntentUrl;
    }

    public void setIntentUrl(String intentUrl) {
        IntentUrl = intentUrl;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }
}
