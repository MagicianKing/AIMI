package com.yeying.aimi.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 回复内容实体类
 */
public class ReplyBean implements Serializable {
    private String reply_id;//	回复id
    private String content;//	回复内容
    private String user_id;//	留言人id
    private String user_name;//	留言人名
    private String reply_user_id;//	留言对象id
    private String reply_user_name;//	留言对象名
    private Date reply_time;//	留言时间
    private String user_url;

    public ReplyBean(String content, String user_name, String user_url) {
        super();
        this.content = content;

        this.user_name = user_name;

        this.user_url = user_url;
    }

    public ReplyBean() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getUser_url() {
        return user_url;
    }

    public void setUser_url(String user_url) {
        this.user_url = user_url;
    }

    public String getReply_id() {
        return reply_id;
    }

    public void setReply_id(String reply_id) {
        this.reply_id = reply_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getReply_user_id() {
        return reply_user_id;
    }

    public void setReply_user_id(String reply_user_id) {
        this.reply_user_id = reply_user_id;
    }

    public String getReply_user_name() {
        return reply_user_name;
    }

    public void setReply_user_name(String reply_user_name) {
        this.reply_user_name = reply_user_name;
    }

    public Date getReply_time() {
        return reply_time;
    }

    public void setReply_time(Date reply_time) {
        this.reply_time = reply_time;
    }


}
