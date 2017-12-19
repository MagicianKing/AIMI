package com.yeying.aimi.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by king .
 * 公司:业英众娱
 * 2017/9/18 下午1:42
 */

public class BarPhoneList implements Serializable{
    String      bar_phone_id; //酒吧电话id
    String      bar_id; //酒吧id
    String      phone;  //酒吧电话
    Date create_time; //创建时间

    public String getBar_phone_id() {
        return bar_phone_id;
    }

    public void setBar_phone_id(String bar_phone_id) {
        this.bar_phone_id = bar_phone_id;
    }

    public String getBar_id() {
        return bar_id;
    }

    public void setBar_id(String bar_id) {
        this.bar_id = bar_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }
}
