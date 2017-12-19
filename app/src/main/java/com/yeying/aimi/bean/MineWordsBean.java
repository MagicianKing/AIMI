package com.yeying.aimi.bean;

import java.util.Date;
import java.util.List;

/**
 * Created by tanchengkeji on 2017/9/21.
 */

public class MineWordsBean {
    private String message_id; //动态id
    private List<PictureBean> pic_urls; //图片地址
    private Date create_time;  //创建时间
    private int reply_number; //回复数
    private String message; //动态内容
    private String barId; //酒吧id
    private String barName;//酒吧名称
    private double distance;//距离

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public List<PictureBean> getPic_urls() {
        return pic_urls;
    }

    public void setPic_urls(List<PictureBean> pic_urls) {
        this.pic_urls = pic_urls;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public int getReply_number() {
        return reply_number;
    }

    public void setReply_number(int reply_number) {
        this.reply_number = reply_number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
