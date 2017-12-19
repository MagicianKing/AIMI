package com.yeying.aimi.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.bean.NewSystemMsgBean;

import java.util.List;

/**
 * 系统推送过来的系统消息缓存
 * 2.0.0版本调整	(系统消息)
 */
public class SystemMessageCache extends BasicStorage {
    List<NewSystemMsgBean> msgList;

    public SystemMessageCache(Context ctx) {
        super(ctx);
    }

    public static SystemMessageCache getMsgCache(Context ctx) {
        SystemMessageCache cache = new SystemMessageCache(ctx);
        cache.load();
        return cache;
    }

    public void ser(SharedPreferences sp) {
        String jsonorder = JSON.toJSONString(msgList);
        sp.edit().putString("SystemMessage", jsonorder).commit();
    }

    @Override
    public void unSer(SharedPreferences sp) {
        msgList = JSON.parseArray(sp.getString("SystemMessage", "[]"),
                NewSystemMsgBean.class);
    }

    @Override
    public void del(SharedPreferences sp) {
        sp.edit().remove("SystemMessage").commit();

    }

    @Override
    public String getIdentifer() {
        // TODO Auto-generated method stub
        return SystemMessageCache.class.getName();
    }

    public List<NewSystemMsgBean> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<NewSystemMsgBean> msgList) {
        this.msgList = msgList;
    }

}
