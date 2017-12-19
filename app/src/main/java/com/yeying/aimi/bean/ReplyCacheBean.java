package com.yeying.aimi.bean;

import java.io.Serializable;
import java.util.Date;


/**
 * 我的动态通知实体类
 */
public class ReplyCacheBean implements Serializable {
    private String myUserId;//我的userid，用于区分不同登录
    private long theMsgTime;//环信消息时间
    private boolean isRead;//是否已读
    private String userId;//	评论人
    private String name;//	名称
    private String img;//	头像
    private int type;//	类型 1:关注 其他:评论
    private String content;//	内容
    private String id;//	动态id
    private Date time;//	时间
    private String pic_img;//	动态图片
    private int pic_type;//	动态类型
    private String msgId;//消息id
    private String userName;
    private String userHeadImg;
    private String userIdString;
    private int typeInt;
    private Date userTime;
    private String stauts;//缘分牌状态
    private String tempParaInfo;//缘分牌配对成功 用户信息
    private int rank;//霸屏排名

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getTempParaInfo() {
        return tempParaInfo;
    }

    public void setTempParaInfo(String tempParaInfo) {
        this.tempParaInfo = tempParaInfo;
    }

    public String getStauts() {
        return stauts;
    }

    public void setStauts(String stauts) {
        this.stauts = stauts;
    }

    public Date getUserTime() {
        return userTime;
    }

    public void setUserTime(Date userTime) {
        this.userTime = userTime;
    }

    public int getTypeInt() {
        return typeInt;
    }

    public void setTypeInt(int typeInt) {
        this.typeInt = typeInt;
    }

    public String getUserIdString() {
        return userIdString;
    }

    public void setUserIdString(String userIdString) {
        this.userIdString = userIdString;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserHeadImg() {
        return userHeadImg;
    }

    public void setUserHeadImg(String userHeadImg) {
        this.userHeadImg = userHeadImg;
    }

    public String getMyUserId() {
        return myUserId;
    }

    public void setMyUserId(String myUserId) {
        this.myUserId = myUserId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getPic_img() {
        return pic_img;
    }

    public void setPic_img(String pic_img) {
        this.pic_img = pic_img;
    }

    public int getPic_type() {
        return pic_type;
    }

    public void setPic_type(int pic_type) {
        this.pic_type = pic_type;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public long getTheMsgTime() {
        return theMsgTime;
    }

    public void setTheMsgTime(long theMsgTime) {
        this.theMsgTime = theMsgTime;
    }


}
