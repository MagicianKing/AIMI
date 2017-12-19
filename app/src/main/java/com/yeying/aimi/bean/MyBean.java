package com.yeying.aimi.bean;

import java.io.Serializable;
import java.util.List;

public class MyBean implements Serializable {
    public String bar_id;
    public String create_time;
    public String distance;
    public String message;
    public String message_id;
    public List<MydetailBean> pic_urls;

    public String getBar_id() {
        return bar_id;
    }

    public void setBar_id(String bar_id) {
        this.bar_id = bar_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public List<MydetailBean> getPic_urls() {
        return pic_urls;
    }

    public void setPic_urls(List<MydetailBean> pic_urls) {
        this.pic_urls = pic_urls;
    }

}
