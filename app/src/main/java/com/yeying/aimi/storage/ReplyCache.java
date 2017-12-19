package com.yeying.aimi.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.bean.ReplyCacheBean;

import java.util.List;


/**
 * 我的动态通知缓存
 */
public class ReplyCache extends BasicStorage {
    List<ReplyCacheBean> replyList;

    public ReplyCache(Context ctx) {
        super(ctx);
    }

    public static ReplyCache getCommentCache(Context ctx) {
        ReplyCache cache = new ReplyCache(ctx);
        cache.load();
        return cache;
    }

    public void ser(SharedPreferences sp) {
        String jsonorder = JSON.toJSONString(replyList);
        sp.edit().putString("replyList", jsonorder).commit();
    }

    @Override
    public void unSer(SharedPreferences sp) {
        replyList = JSON.parseArray(sp.getString("replyList", "[]"), ReplyCacheBean.class);
    }

    @Override
    public void del(SharedPreferences sp) {
        sp.edit().remove("replyList").commit();

    }

    @Override
    public String getIdentifer() {
        // TODO Auto-generated method stub
        return ReplyCache.class.getName();
    }

    public List<ReplyCacheBean> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<ReplyCacheBean> replyList) {
        this.replyList = replyList;
    }


}
