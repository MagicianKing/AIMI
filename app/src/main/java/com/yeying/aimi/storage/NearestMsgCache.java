package com.yeying.aimi.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.bean.ChatBean;

import java.util.List;


/**
 * 最近的聊天缓存
 */
public class NearestMsgCache extends BasicStorage {
    List<ChatBean> msgList;

    public NearestMsgCache(Context ctx) {
        super(ctx);
    }

    public static NearestMsgCache getCache(Context ctx) {
        NearestMsgCache cache = new NearestMsgCache(ctx);
        cache.load();
        return cache;
    }

    public void ser(SharedPreferences sp) {
        String jsonorder = JSON.toJSONString(msgList);
        sp.edit().putString("NearestMsg", jsonorder).commit();
    }

    @Override
    public void unSer(SharedPreferences sp) {
        msgList = JSON.parseArray(sp.getString("NearestMsg", "[]"), ChatBean.class);
    }

    @Override
    public void del(SharedPreferences sp) {
        sp.edit().remove("NearestMsg").commit();

    }

    @Override
    public String getIdentifer() {
        // TODO Auto-generated method stub
        return NearestMsgCache.class.getName();
    }

    public List<ChatBean> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<ChatBean> msgList) {
        this.msgList = msgList;
    }


}
