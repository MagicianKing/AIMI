package com.yeying.aimi.database;

import org.litepal.crud.DataSupport;

/**
 * Created by tanchengkeji on 2017/9/28.
 */

public class HBData extends DataSupport{

    private String bar_id;
    private String imgUrl;
    private String red_pack_id;
    private String redpack_choose;
    private String create_time;
    private String user_id;
    private String user_name;
    private String invalid_time;
    private String remark;
    private String type;
    private String limit_sex;
    private String status;
    
    public HBData() {
    }
    public String getBar_id() {
        return bar_id;
    }

    public void setBar_id(String bar_id) {
        this.bar_id = bar_id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getRed_pack_id() {
        return red_pack_id;
    }

    public void setRed_pack_id(String red_pack_id) {
        this.red_pack_id = red_pack_id;
    }

    public String getRedpack_choose() {
        return redpack_choose;
    }

    public void setRedpack_choose(String redpack_choose) {
        this.redpack_choose = redpack_choose;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getInvalid_time() {
        return invalid_time;
    }

    public void setInvalid_time(String invalid_time) {
        this.invalid_time = invalid_time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLimit_sex() {
        return limit_sex;
    }

    public void setLimit_sex(String limit_sex) {
        this.limit_sex = limit_sex;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
