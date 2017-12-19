package com.yeying.aimi.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tanchengkeji on 2017/10/27.
 */

public class GameTopBean implements Serializable{


    /**
     * topUserList : [{"headImg":"http://47.94.164.180:10032/hi8_files//headimg/38/170929152741434933820170929152756.png","money":20,"nickName":"啦咯啦咯啦咯啦咯啦咯啦咯","userId":"1709291527414349338"},{"headImg":"http://47.94.164.180:10032/hi8_files//headimg/96/170927120229463909620171017111523.png","money":10,"nickName":"等你","userId":"1709271202294639096"},{"headImg":"http://47.94.164.180:10032/hi8_files//headimg/67/170929184927211916720171027171028.png","money":5,"nickName":"JavaDevelop","userId":"1709291849272119167"},{"headImg":"http://47.94.164.180:10032/hi8_files//headimg/98/171016181316457969820171025154532.png","money":0,"nickName":"what","userId":"1710161813164579698"},{"headImg":"http://47.94.164.180:10032/hi8_files//headimg/38/17101215303422123820171025140618.png","money":0,"nickName":"啦啦啦","userId":"171012153034221238"}]
     * money : 0
     * season : Runway
     * ranking : 4
     * barName : 院
     * sendTime : 2017-10-27 21:24:36
     */

    private int money;
    private String season;
    private int ranking;
    private String barName;
    private String sendTime;
    private List<TopUserBean> topUserList;

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

    public List<TopUserBean> getTopUserList() {
        return topUserList;
    }

    public void setTopUserList(List<TopUserBean> topUserList) {
        this.topUserList = topUserList;
    }
}
