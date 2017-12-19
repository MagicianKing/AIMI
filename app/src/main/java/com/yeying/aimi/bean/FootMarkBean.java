package com.yeying.aimi.bean;

/**
 * Created by tanchengkeji on 2017/9/21.
 */

public class FootMarkBean {

    private String barId;  //酒吧id
    private String barName; //酒吧名称
    private String picUrl; //酒吧图片
    private double distance; //距离
    private int hotNumber; //夜猫数量

    public String getBarId() {
        return barId;
    }

    public void setBarId(String barId) {
        this.barId = barId;
    }

    public String getBarName() {
        return barName;
    }

    public void setBarName(String barName) {
        this.barName = barName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getHotNumber() {
        return hotNumber;
    }

    public void setHotNumber(int hotNumber) {
        this.hotNumber = hotNumber;
    }
}
