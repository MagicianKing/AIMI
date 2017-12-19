package com.yeying.aimi.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.bean.SimpleMsgBean;

import java.util.List;


/**
 * 临时消息缓存，用于处理非好友并且自己主动发过消息
 */
public class NormalMsgCache extends BasicStorage {
    List<SimpleMsgBean> normalmsg;

    public NormalMsgCache(Context ctx) {
        super(ctx);
    }

    public static NormalMsgCache getCache(Context ctx) {
        NormalMsgCache cache = new NormalMsgCache(ctx);
        cache.load();
        return cache;
    }

    public void ser(SharedPreferences sp) {
        String jsonorder = JSON.toJSONString(normalmsg);
        sp.edit().putString("normalmsg", jsonorder).commit();
    }

    @Override
    public void unSer(SharedPreferences sp) {
        normalmsg = JSON.parseArray(sp.getString("normalmsg", "[]"), SimpleMsgBean.class);
    }

    @Override
    public void del(SharedPreferences sp) {
        sp.edit().remove("normalmsg").commit();

    }

    @Override
    public String getIdentifer() {
        // TODO Auto-generated method stub
        return NormalMsgCache.class.getName();
    }

    public List<SimpleMsgBean> getNormalmsg() {
        return normalmsg;
    }

    public void setNormalmsg(List<SimpleMsgBean> normalmsg) {
        this.normalmsg = normalmsg;
    }


}
