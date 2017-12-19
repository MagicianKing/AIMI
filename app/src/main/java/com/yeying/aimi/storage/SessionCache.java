/**
 *
 */
package com.yeying.aimi.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.yeying.aimi.protocol.impl.Header;


/**
 * @author sparrow
 */
public class SessionCache extends BasicStorage {

    private static final long valid_time = 1000 * 60 * 60;// an hour
    public String username;//


    public String userId;
    public long timestamp;


    public String phone;
    public String nickname;
    public String headimgUrl = "";
    public String token;
    public String sex;   //1是男,2是女
    public String sessionId;
    public String type;
    public String locationX;
    public String locationY;
    public String address;
    public String search_history1;
    public String search_history2;
    public String search_history3;
    public String barId;
    public String barName;
    public String time;
    public String bar_desk;//桌号
    public String autograph;//签名
    public String region;//	地区
    public String birthday;//	生日
    public String constellation;
    public boolean isOnlyFriend;//隐私策略
    public int trendsNum;//动态数
    public int followNum;//关注数
    public int fansNum;//粉丝数
    public int capNum;//瓶盖数
    public long dan_time;
    public int first;
    public int two;
    public int three;
    public int four;
    public int five;
    public int dataNeedComplete;
    public String picPath;
    public int sendMoney;
    public String activityUrl;
    public int giftCheck;
    public int isLookChuiNiu;//是否看过吹牛提示
    public int lastPayType;
    public String card_no;//	支付宝账号
    public String real_name;//	真实姓名
    public int zhichiIsDisplay;//是否在粉丝团中显示头像	  0显示，1不显示
    public int isCloseTopActivity;//是否关闭过酒吧列表顶部活动  0未关闭，1关闭
    public String placardName;//酒吧列表顶部活动名称
    public String imgUrl;//图片加载头
    public int dingwei; //是否能获取定位信息
    public boolean isAdmin;
    public boolean isSeeFootMark;
    private static SessionCache mCache;

    /**
     * @param ctx
     */
    private SessionCache(Context ctx) {
        super(ctx);

    }

    public static SessionCache getInstance(Context ctx) {
        if(mCache==null){
            mCache = new SessionCache(ctx);
        }
        mCache.load();
        return mCache;
    }

    @Override
    public void ser(SharedPreferences sp) {
        //
        sp.edit().putString("username", username)
                .putString("nickName", nickname)
                .putString("sex", sex)
                .putString("token", token)
                .putString("userId", userId)
                .putString("locationX", locationX)
                .putString("locationY", locationY)
                .putLong("timestamp", System.currentTimeMillis())
                .putString("headimgUrl", headimgUrl)
                .putBoolean("isAdmin", isAdmin)
                .putBoolean("isSeeFootMark",isSeeFootMark)
                .putString("phone", phone)
                .putString("address", address)
                .putString("sessionId", sessionId)
                .putString("type", type)
                .putString("search_history1", search_history1)
                .putString("search_history2", search_history2)
                .putString("search_history3", search_history3)
                .putString("barId", barId)
                .putString("time", time)
                .putString("bar_desk", bar_desk)
                .putString("barName", barName)
                .putString("autograph", autograph)
                .putString("region", region)
                .putString("birthday", birthday)
                .putBoolean("isOnlyFriend", isOnlyFriend)
                .putInt("trendsNum", trendsNum)
                .putInt("first", first)
                .putInt("dataNeedComplete", dataNeedComplete)
                .putInt("two", two)
                .putInt("three", three)
                .putInt("four", four)
                .putInt("five", five)
                .putInt("followNum", followNum)
                .putInt("fansNum", fansNum)
                .putInt("capNum", capNum)
                .putLong("dan_time", dan_time)
                .putString("picPath", picPath)
                .putInt("sendMoney", sendMoney)
                .putString("activityUrl", activityUrl)
                .putInt("giftCheck", giftCheck)
                .putInt("isLookChuiNiu", isLookChuiNiu)
                .putInt("lastPayType", lastPayType)
                .putString("card_no", card_no)
                .putString("real_name", real_name)
                .putInt("zhichiIsDisplay", zhichiIsDisplay)
                .putInt("isCloseTopActivity", isCloseTopActivity)
                .putString("placardName", placardName)
                .putString("imgUrl", imgUrl)
                .putInt("Set", dingwei)
                .putString("constellation",constellation)
                .commit();
    }

    @Override
    public void unSer(SharedPreferences sp) {
        //
        search_history1 = sp.getString("search_history1", "");
        search_history2 = sp.getString("search_history2", "");
        search_history3 = sp.getString("search_history3", "");
        barId = sp.getString("barId", "");

        time = sp.getString("time", null);

        username = sp.getString("username", null);
        nickname = sp.getString("nickName", null);

        sex = sp.getString("sex", null);

        token = sp.getString("token", null);
        userId = sp.getString("userId", null);
        timestamp = sp.getLong("timestamp", 0);
        headimgUrl = sp.getString("headimgUrl", "");

        phone = sp.getString("phone", null);
        sessionId = sp.getString("sessionId", null);
        type = sp.getString("type", "");
        locationX = sp.getString("locationX", "");
        locationY = sp.getString("locationY", "");
        address = sp.getString("address", null);
        bar_desk = sp.getString("bar_desk", "");
        barName = sp.getString("barName", "");
        autograph = sp.getString("autograph", "");
        region = sp.getString("region", "");
        birthday = sp.getString("birthday", "");
        isOnlyFriend = sp.getBoolean("isOnlyFriend", false);
        trendsNum = sp.getInt("trendsNum", 0);
        followNum = sp.getInt("followNum", 0);
        fansNum = sp.getInt("fansNum", 0);
        capNum = sp.getInt("capNum", 0);
        dan_time = sp.getLong("dan_time", 0);
        first = sp.getInt("first", 0);
        two = sp.getInt("two", 0);
        three = sp.getInt("three", three);
        four = sp.getInt("four", four);
        five = sp.getInt("five", five);
        dataNeedComplete = sp.getInt("dataNeedComplete", 0);
        picPath = sp.getString("picPath", "");
        sendMoney = sp.getInt("sendMoney", 0);
        activityUrl = sp.getString("activityUrl", "");
        giftCheck = sp.getInt("giftCheck", 0);
        isLookChuiNiu = sp.getInt("isLookChuiNiu", 0);
        lastPayType = sp.getInt("lastPayType", 0);
        real_name = sp.getString("real_name", "");
        card_no = sp.getString("card_no", "");
        zhichiIsDisplay = sp.getInt("zhichiIsDisplay", 0);
        isCloseTopActivity = sp.getInt("isCloseTopActivity", 0);
        placardName = sp.getString("placardName", "");
        imgUrl = sp.getString("imgUrl", imgUrl);
        dingwei = sp.getInt("Set", dingwei);
        isAdmin = sp.getBoolean("isAdmin", false);
        constellation =sp.getString("constellation","");
        isSeeFootMark = sp.getBoolean("isSeeFootMark",true);
    }

    @Override
    public void del(SharedPreferences sp) {
        //
        sp.edit().remove("username")
                .remove("nickName")
                .remove("birthday")
                .remove("sex")
                .remove("userId")
                .remove("timestamp")
                .remove("token")
                .remove("headimgUrl")
                .remove("type")
                .remove("sessionId")
                .remove("locationX")
                .remove("locationY")
                .remove("phone")
                .remove(address)
                .remove(barId)
                .remove("barName")
                .remove(time)
                .remove(search_history1)
                .remove(search_history2)
                .remove(search_history3)
                .remove(bar_desk)
                .remove("autograph")
                .remove("region")
                .remove("birthday")
                .remove("isOnlyFriend")
                .remove("trendsNum")
                .remove("followNum")
                .remove("dataNeedComplete")
                .remove("fansNum")
                .remove("capNum")
                .remove("dan_time")
                .remove("first")
                .remove("two")
                .remove("three")
                .remove("four")
                .remove("five")
                .remove("picPath")
                .remove("sendMoney")
                .remove("activityUrl")
                .remove("giftCheck")
                .remove("isLookChuiNiu")
                .remove("lastPayType")
                .remove("card_no")
                .remove("real_name")
                .remove("zhichiIsDisplay")
                .remove("isCloseTopActivity")
                .remove("placardName")
                .remove("imgUrl")
                .remove("Set")
                .remove("constellation")
                .commit();

    }

    public boolean isValid() {
        return System.currentTimeMillis() - timestamp > valid_time;
    }

    @Override
    public String getIdentifer() {
        //
        return SessionCache.class.getName();
    }

    public void initHeader(Header header) {
//		header.key = this.key;
//		header.mobile = this.mobile;
//		header.sessionid = this.sessionid;
        header.userid = this.userId;
        header.terminalid = "";
    }

}
