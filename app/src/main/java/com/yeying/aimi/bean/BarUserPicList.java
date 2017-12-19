package com.yeying.aimi.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by king .
 * 公司:业英众娱
 * 2017/9/18 下午1:40
 */

public class BarUserPicList implements Serializable {
    String     photoId; //头像id
    String     messageId; //动态内容
    String     picUrl; //动态图片地址
    int        width; //图片宽度
    int        height; //图片高度
    Date createTime; // 创建类型
    int         picType; // 图片类型 1图片 2视频（暂时无视频）

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getPicType() {
        return picType;
    }

    public void setPicType(int picType) {
        this.picType = picType;
    }
}
