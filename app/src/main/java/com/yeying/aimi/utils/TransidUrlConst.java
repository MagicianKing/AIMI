package com.yeying.aimi.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * transid 和 后台phpserver段路径对应关系
 *
 * @author shahuaitao
 */
public class TransidUrlConst {

    private static Map<String, String> urlMap = new HashMap<String, String>();

    static {
        //urlMap.put("10001", "http://192.168.1.90:10030/hi8_mobile_web/servlet/main.cl");//短信发送
    }

    public static String getUrlByTransid(String transid) {
        return urlMap.get(transid);
    }

}
