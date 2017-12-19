package com.yeying.aimi.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by king .
 * 公司:业英众娱
 * 2017/9/18 下午1:38
 */

public class ActiveList implements Serializable {
    String activity_id; // 活动唯一id
    String barId; //酒吧ID
    String activity_content; //活动内容
    int activityType; //活动类型
    String time_regular; // 活动时间规则
    int time_jiange; // 活动间隔时间
    String act_title;  // 标题
    Date start_time; // 开始时间
    Date end_time;  //  结束时间

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getBarId() {
        return barId;
    }

    public void setBarId(String barId) {
        this.barId = barId;
    }

    public String getActivityContent() {
        return activity_content;
    }

    public void setActivityContent(String activityContent) {
        this.activity_content = activityContent;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public String getTime_regular() {
        return time_regular;
    }

    public void setTime_regular(String time_regular) {
        this.time_regular = time_regular;
    }

    public int getTime_jiange() {
        return time_jiange;
    }

    public void setTime_jiange(int time_jiange) {
        this.time_jiange = time_jiange;
    }

    public String getAct_title() {
        return act_title;
    }

    public void setAct_title(String act_title) {
        this.act_title = act_title;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }
}
