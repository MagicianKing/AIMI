package com.yeying.aimi.bean;

import java.io.Serializable;

public class BarInfoBean implements Serializable {
    private String barId;
    private int hasActivity;
    private String activityUrl;
    private String chatGroupId;
    private String barName;

    public String getBarId() {
        return barId;
    }

    public void setBarId(String barId) {
        this.barId = barId;
    }

    public int getHasActivity() {
        return hasActivity;
    }

    public void setHasActivity(int hasActivity) {
        this.hasActivity = hasActivity;
    }

    public String getActivityUrl() {
        return activityUrl;
    }

    public void setActivityUrl(String activityUrl) {
        this.activityUrl = activityUrl;
    }

    public String getChatGroupId() {
        return chatGroupId;
    }

    public void setChatGroupId(String chatGroupId) {
        this.chatGroupId = chatGroupId;
    }

    public String getBarName() {
        return barName;
    }

    public void setBarName(String barName) {
        this.barName = barName;
    }

}
