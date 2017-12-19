package com.yeying.aimi.protocol.impl;


import com.yeying.aimi.utils.Tools;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author sparrow
 */
public class Header implements Serializable {
    public String token;
    public String userid;
    public String transid;
    public String version;//版本
    public String terminalid = "android"; //系统 android  ios
    public String time;
    public String content;
    public String source;    // 3 是android  4 是 ios
    public String sign;
    public String appVer;


    public Header() {
        super();
        version = Tools.getClientVersion();
        appVer = Tools.getClientVersion();
        terminalid = "android";
        source = "3";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date now = new Date();
        time = sdf.format(now);
        //6228480018522545278
    }

}
