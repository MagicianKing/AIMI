package com.yeying.aimi.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.bean.SimpleMsgBean;

import java.util.List;


/**
 * 消息免打扰缓存
 */
public class MianDaRaoMsgCache extends BasicStorage {
    List<SimpleMsgBean> msgList;

    public MianDaRaoMsgCache(Context ctx) {
        super(ctx);
    }

    public static MianDaRaoMsgCache getMsgCache(Context ctx) {
        MianDaRaoMsgCache cache = new MianDaRaoMsgCache(ctx);
        cache.load();
        return cache;
    }

    public void ser(SharedPreferences sp) {
        String jsonorder = JSON.toJSONString(msgList);
        sp.edit().putString("MianDaRaoMsg", jsonorder).commit();
    }

    @Override
    public void unSer(SharedPreferences sp) {
        msgList = JSON.parseArray(sp.getString("MianDaRaoMsg", "[]"), SimpleMsgBean.class);
    }

    @Override
    public void del(SharedPreferences sp) {
        sp.edit().remove("MianDaRaoMsg").commit();

    }

    @Override
    public String getIdentifer() {
        // TODO Auto-generated method stub
        return MianDaRaoMsgCache.class.getName();
    }

    public List<SimpleMsgBean> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<SimpleMsgBean> msgList) {
        this.msgList = msgList;
    }


}
