package com.yeying.aimi.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.bean.FollBean;

import java.util.List;

/**
 * 我的关注缓存
 */
public class AttendCache extends BasicStorage {
    List<FollBean> attend;

    public AttendCache(Context ctx) {
        super(ctx);
    }

    public static AttendCache getAttendCache(Context ctx) {
        AttendCache cache = new AttendCache(ctx);
        cache.load();
        return cache;
    }

    public void ser(SharedPreferences sp) {
        String jsonorder = JSON.toJSONString(attend);
        sp.edit().putString("attend", jsonorder).commit();
    }

    @Override
    public void unSer(SharedPreferences sp) {
        attend = JSON.parseArray(sp.getString("attend", "[]"), FollBean.class);
    }

    @Override
    public void del(SharedPreferences sp) {
        sp.edit().remove("attend").commit();

    }

    @Override
    public String getIdentifer() {
        // TODO Auto-generated method stub
        return DrinkCache.class.getName();
    }

    public List<FollBean> getFoll() {
        return attend;
    }

    public void setFoll(List<FollBean> attend) {
        this.attend = attend;
    }

}
