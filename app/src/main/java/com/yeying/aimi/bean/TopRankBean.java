package com.yeying.aimi.bean;

/**
 * Created by tanchengkeji on 2017/10/23.
 */

public class TopRankBean {


    /**
     * money : 100
     * season : 22
     * ranking : 1
     * barName : 院
     * sendTime : 2017-10-23 18:44:32
     */

    private int money;
    private String season;
    private int ranking;
    private String barName;
    private String sendTime;

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public String getBarName() {
        return barName;
    }

    public void setBarName(String barName) {
        this.barName = barName;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
}
