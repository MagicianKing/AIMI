package com.yeying.aimi.service.bean;

public class PositionBean {


    private double latd; //纬度
    private double lond; //经度

    private String cucity;

    private String district;

    private long time;

    private int set = 0;


    public int getSet() {
        return set;
    }

    public void setSet(int set) {
        this.set = set;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getLatd() {
        return latd;
    }

    public void setLatd(double latd) {
        this.latd = latd;
    }

    public double getLond() {
        return lond;
    }

    public void setLond(double lond) {
        this.lond = lond;
    }

    public String getCucity() {
        return cucity;
    }

    public void setCucity(String cucity) {
        this.cucity = cucity;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }


}
