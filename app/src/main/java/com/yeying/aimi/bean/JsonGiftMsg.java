package com.yeying.aimi.bean;

/**
 * 通讯用礼物信息
 */
public class JsonGiftMsg {
    private String call_id;//	招呼id
    private int msg_type;//	消息类型	Int(0礼物、1心意、2普通招呼)
    private GiftListBean gift;//	礼物信息

    public String getCall_id() {
        return call_id;
    }

    public void setCall_id(String call_id) {
        this.call_id = call_id;
    }

    public GiftListBean getGift() {
        return gift;
    }

    public void setGift(GiftListBean gift) {
        this.gift = gift;
    }

    public int getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }
}
