package com.yeying.aimi.bean;

/**
 * 礼物实体类
 */
public class GiftListBean {
    private String giftId;//	礼物id
    private String giftName;//	礼物名
    private String imgUrl;//	礼物地址
    private String smallImgUrl;//	小图
    private int gift_score;//	礼物价值
    private double gift_price;//	礼物价格
    private int gift_type;//	礼物类型

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getGift_score() {
        return gift_score;
    }

    public void setGift_score(int gift_score) {
        this.gift_score = gift_score;
    }

    public double getGift_price() {
        return gift_price;
    }

    public void setGift_price(double gift_price) {
        this.gift_price = gift_price;
    }

    public int getGift_type() {
        return gift_type;
    }

    public void setGift_type(int gift_type) {
        this.gift_type = gift_type;
    }

    public String getSmallImgUrl() {
        return smallImgUrl;
    }

    public void setSmallImgUrl(String smallImgUrl) {
        this.smallImgUrl = smallImgUrl;
    }


}
