package com.yeying.aimi.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.bean.SimpleMsgBean;

import java.util.List;


/**
 * 删除的消息缓存
 */
public class CancleMsgCache extends BasicStorage {
    List<SimpleMsgBean> canclemsg;

    public CancleMsgCache(Context ctx) {
        super(ctx);
    }

    public static CancleMsgCache getCancleMsgCache(Context ctx) {
        CancleMsgCache cache = new CancleMsgCache(ctx);
        cache.load();
        return cache;
    }

    public void ser(SharedPreferences sp) {
        String jsonorder = JSON.toJSONString(canclemsg);
        sp.edit().putString("canclemsg", jsonorder).commit();
    }

    @Override
    public void unSer(SharedPreferences sp) {
        canclemsg = JSON.parseArray(sp.getString("canclemsg", "[]"), SimpleMsgBean.class);
    }

    @Override
    public void del(SharedPreferences sp) {
        sp.edit().remove("canclemsg").commit();

    }

    @Override
    public String getIdentifer() {
        // TODO Auto-generated method stub
        return CancleMsgCache.class.getName();
    }

    public List<SimpleMsgBean> getCanclemsg() {
        return canclemsg;
    }

    public void setCanclemsg(List<SimpleMsgBean> canclemsg) {
        this.canclemsg = canclemsg;
    }


}
