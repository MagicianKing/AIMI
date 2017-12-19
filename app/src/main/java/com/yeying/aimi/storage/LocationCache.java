package com.yeying.aimi.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.service.bean.PositionBean;

public class LocationCache extends BasicStorage {

    private static LocationCache cache;
    public PositionBean positionbean;

    /**
     * @param ctx
     */
    public LocationCache(Context ctx) {
        super(ctx);

    }

    public static synchronized LocationCache getLocationCache(Context ctx) {

        if (cache == null) {
            LocationCache cache1 = new LocationCache(ctx);
            cache1.load();
            cache = cache1;
        }
        return cache;
    }

    @Override
    public void ser(SharedPreferences sp) {
        // TODO Auto-generated method stub
        String jsonorder = JSON.toJSONString(positionbean);
        sp.edit().putString("positionbean", jsonorder).commit();
    }

    @Override
    public void unSer(SharedPreferences sp) {
        // TODO Auto-generated method stub
        positionbean = JSON.parseObject(sp.getString("positionbean", "{}"), PositionBean.class);
    }

    @Override
    public void del(SharedPreferences sp) {
        // TODO Auto-generated method stub
        sp.edit().remove("positionbean").commit();
    }

    @Override
    public String getIdentifer() {
        // TODO Auto-generated method stub
        return LocationCache.class.getName();
    }

}
