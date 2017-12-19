package com.yeying.aimi.bean;

import java.io.Serializable;

public class MydetailBean implements Serializable {
    public String create_time;
    public int pic_height;
    public String pic_id;
    public String pic_url;
    public int pic_width;
    public String sort_index;
    public String user_id;
    public String wordsType;

    //新加
    public String message;
    public String message_id;
    public String imgUrl;


    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getPic_height() {
        return pic_height;
    }

    public void setPic_height(int pic_height) {
        this.pic_height = pic_height;
    }

    public String getPic_id() {
        return pic_id;
    }

    public void setPic_id(String pic_id) {
        this.pic_id = pic_id;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public int getPic_width() {
        return pic_width;
    }

    public void setPic_width(int pic_width) {
        this.pic_width = pic_width;
    }

    public String getSort_index() {
        return sort_index;
    }

    public void setSort_index(String sort_index) {
        this.sort_index = sort_index;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getWordsType() {
        return wordsType;
    }

    public void setWordsType(String wordsType) {
        this.wordsType = wordsType;
    }


}
