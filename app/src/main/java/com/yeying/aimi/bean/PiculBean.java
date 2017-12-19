package com.yeying.aimi.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PiculBean implements Serializable {
    private String pic_url;
    private int pic_width;
    private int pic_height;
    private int wordsType;//	类型	0图片1是视频

    public int getWordsType() {
        return wordsType;
    }

    public void setWordsType(int wordsType) {
        this.wordsType = wordsType;
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

    public int getPic_height() {
        return pic_height;
    }

    public void setPic_height(int pic_height) {
        this.pic_height = pic_height;
    }

}
