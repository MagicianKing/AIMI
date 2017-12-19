package com.yeying.aimi.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by king .
 * 公司:业英众娱
 * 2017/9/18 下午1:41
 */

public class YeMaoList implements Serializable{
    String      userHistoryId; //用户访问酒吧历史id
    String      userId; //用户id
    String      barId;  //酒吧id
    Date lastViewTime; //上次访问时间
    Date        createTime; //创建时间
    int         top;    //是否是酒吧推荐常客
    int         visitNumber; //参观次数
    Date        muteTime;// 禁言到的时间
    long        rankingScore; //排行积分
    int         isStaff; //是否是员工
    int         time; //时间
    String      nickName; //昵称
    String      gender; //性别
    String      headImg; //头像地址
    int         status; //状态 1在酒吧内（在线）0不在酒吧内(离线)

    public String getUserHistoryId() {
        return userHistoryId;
    }

    public void setUserHistoryId(String userHistoryId) {
        this.userHistoryId = userHistoryId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBarId() {
        return barId;
    }

    public void setBarId(String barId) {
        this.barId = barId;
    }

    public Date getLastViewTime() {
        return lastViewTime;
    }

    public void setLastViewTime(Date lastViewTime) {
        this.lastViewTime = lastViewTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getVisitNumber() {
        return visitNumber;
    }

    public void setVisitNumber(int visitNumber) {
        this.visitNumber = visitNumber;
    }

    public Date getMuteTime() {
        return muteTime;
    }

    public void setMuteTime(Date muteTime) {
        this.muteTime = muteTime;
    }

    public long getRankingScore() {
        return rankingScore;
    }

    public void setRankingScore(long rankingScore) {
        this.rankingScore = rankingScore;
    }

    public int getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(int isStaff) {
        this.isStaff = isStaff;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
