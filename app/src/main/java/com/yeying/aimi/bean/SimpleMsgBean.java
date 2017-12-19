package com.yeying.aimi.bean;

/**
 * 简单消息类
 */
public class SimpleMsgBean {
    private String myUserId;//我的userid，用于区分不同登录
    private String userId;
    private int num;//未读消息数

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getMyUserId() {
        return myUserId;
    }

    public void setMyUserId(String myUserId) {
        this.myUserId = myUserId;
    }

}
