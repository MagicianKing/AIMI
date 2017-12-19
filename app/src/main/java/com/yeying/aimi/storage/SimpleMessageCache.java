/**
 *
 */
package com.yeying.aimi.storage;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * 删除消息item操作记录以及记录只显示一次的提示
 */
public class SimpleMessageCache extends BasicStorage {

    public boolean isCancleDongtaiMsg;//动态消息
    public boolean isCancleVisitorMsg;//访客消息
    public boolean isCancleSystemMsg;//系统消息
    public boolean isCancleNearMsg;//招呼消息
    public boolean isNeedTiShi;//是否需要提示设置隐私
    public boolean isNewXiu;//是否显示秀场提示红点
    public boolean isNewMsg;//是否显示消息提示红点
    public boolean isNewMy;//是否显示我的提示红点
    public int newMsgNum;//新消息数
    public boolean isReadBalanceRule;//是否看过平衡球规则
    public boolean isReadLightRule;//是否看过荧光棒规则
    /**
     * @param ctx
     */
    public SimpleMessageCache(Context ctx) {
        super(ctx);

    }

    public static SimpleMessageCache getInstance(Context ctx) {
        SimpleMessageCache cache = new SimpleMessageCache(ctx);
        cache.load();
        return cache;
    }

    @Override
    public void ser(SharedPreferences sp) {
        //
        sp.edit().putBoolean("isCancleSystemMsg", isCancleSystemMsg).putBoolean("isCancleNearMsg", isCancleNearMsg)
                .putBoolean("isNeedTiShi", isNeedTiShi).putBoolean("isNewXiu", isNewXiu).putBoolean("isNewMsg", isNewMsg)
                .putBoolean("isNewMy", isNewMy).putBoolean("isCancleVisitorMsg", isCancleVisitorMsg).putBoolean("isCancleDongtaiMsg", isCancleDongtaiMsg)
                .putInt("newMsgNum", newMsgNum).putBoolean("isReadBalanceRule", isReadBalanceRule)
                .putBoolean("isReadLightRule", isReadLightRule).commit();


    }

    @Override
    public void unSer(SharedPreferences sp) {

        isCancleSystemMsg = sp.getBoolean("isCancleSystemMsg", false);
        isCancleNearMsg = sp.getBoolean("isCancleNearMsg", false);
        isNeedTiShi = sp.getBoolean("isNeedTiShi", true);
        isNewXiu = sp.getBoolean("isNewXiu", false);
        isNewMsg = sp.getBoolean("isNewMsg", false);
        isNewMy = sp.getBoolean("isNewMy", false);
        isCancleVisitorMsg = sp.getBoolean("isCancleVisitorMsg", false);
        isCancleDongtaiMsg = sp.getBoolean("isCancleDongtaiMsg", false);
        newMsgNum = sp.getInt("newMsgNum", 0);
        isReadBalanceRule = sp.getBoolean("isReadBalanceRule", false);
        isReadLightRule = sp.getBoolean("isReadLightRule", false);
    }

    @Override
    public void del(SharedPreferences sp) {
        //
        sp.edit().remove("isCancleSystemMsg").remove("isCancleNearMsg").remove("isNeedTiShi")
                .remove("isNewXiu").remove("isNewMsg").remove("isNewMy").remove("isCancleVisitorMsg")
                .remove("isCancleDongtaiMsg").remove("newMsgNum").remove("isReadBalanceRule")
                .remove("isReadLightRule").commit();

    }


    @Override
    public String getIdentifer() {
        //
        return SimpleMessageCache.class.getName();
    }


}
