package com.yeying.aimi.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.bean.ReplyCacheBean;

import java.util.List;


/**
 * 我的动态通知历史信息缓存
 */
public class ReplyHistoryCache extends BasicStorage {
    List<ReplyCacheBean> replyHistoryList;

    public ReplyHistoryCache(Context ctx) {
        super(ctx);
    }

    public static ReplyHistoryCache getCommentCache(Context ctx) {
        ReplyHistoryCache cache = new ReplyHistoryCache(ctx);
        cache.load();
        return cache;
    }

    public void ser(SharedPreferences sp) {
        String jsonorder = JSON.toJSONString(replyHistoryList);
        sp.edit().putString("replyhistoryList", jsonorder).commit();
    }

    @Override
    public void unSer(SharedPreferences sp) {
        replyHistoryList = JSON.parseArray(sp.getString("replyhistoryList", "[]"), ReplyCacheBean.class);
    }

    @Override
    public void del(SharedPreferences sp) {
        sp.edit().remove("replyhistoryList").commit();

    }

    @Override
    public String getIdentifer() {
        // TODO Auto-generated method stub
        return ReplyHistoryCache.class.getName();
    }

    public List<ReplyCacheBean> getReplyHistoryList() {
        return replyHistoryList;
    }

    public void setReplyHistoryList(List<ReplyCacheBean> replyHistoryList) {
        this.replyHistoryList = replyHistoryList;
    }


}
