package com.yeying.aimi.bean;

import java.util.List;


public class TotalBean {
    public String message_id;//动态id；
    public String message;
    public String barName;
    public List<TotalPic> pic_urls;
    public String distance;
    private String bar_id;

    public String getBar_id() {
        return bar_id;
    }

    public void setBar_id(String bar_id) {
        this.bar_id = bar_id;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBarName() {
        return barName;
    }

    public void setBarName(String barName) {
        this.barName = barName;
    }

    public List<TotalPic> getPic_urls() {
        return pic_urls;
    }

    public void setPic_urls(List<TotalPic> pic_urls) {
        this.pic_urls = pic_urls;
    }


}
