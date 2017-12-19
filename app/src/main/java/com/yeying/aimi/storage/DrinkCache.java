package com.yeying.aimi.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.bean.DrinkListBean;

import java.util.List;


public class DrinkCache extends BasicStorage {
    List<DrinkListBean> drink;

    public DrinkCache(Context ctx) {
        super(ctx);
    }

    public static DrinkCache getDrinkCache(Context ctx) {
        DrinkCache cache = new DrinkCache(ctx);
        cache.load();
        return cache;
    }

    public void ser(SharedPreferences sp) {
        String jsonorder = JSON.toJSONString(drink);
        sp.edit().putString("drink", jsonorder).commit();
    }

    @Override
    public void unSer(SharedPreferences sp) {
        drink = JSON.parseArray(sp.getString("drink", "[]"), DrinkListBean.class);
    }

    @Override
    public void del(SharedPreferences sp) {
        sp.edit().remove("drink").commit();

    }

    @Override
    public String getIdentifer() {
        // TODO Auto-generated method stub
        return DrinkCache.class.getName();
    }

    public List<DrinkListBean> getDrink() {
        return drink;
    }

    public void setDrink(List<DrinkListBean> drink) {
        this.drink = drink;
    }


}
