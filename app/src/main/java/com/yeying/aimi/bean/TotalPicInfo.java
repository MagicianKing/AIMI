package com.yeying.aimi.bean;

public class TotalPicInfo {
    private String barName;
    private int height;
    private int width;
    private String albid = "";
    private String msg = "";
    private String isrc = "";
    private String Messgeid;
    private String ditance;

    public String getBarName() {
        return barName;
    }

    public void setBarName(String barName) {
        this.barName = barName;
    }

    public String getDitance() {
        return ditance;
    }

    public void setDitance(String ditance) {
        this.ditance = ditance;
    }

    public String getMessgeid() {
        return Messgeid;
    }

    public void setMessgeid(String messgeid) {
        Messgeid = messgeid;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getAlbid() {
        return albid;
    }

    public void setAlbid(String albid) {
        this.albid = albid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getIsrc() {
        return isrc;
    }

    public void setIsrc(String isrc) {
        this.isrc = isrc;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }


}
