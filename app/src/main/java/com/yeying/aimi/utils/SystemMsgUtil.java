package com.yeying.aimi.utils;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.yeying.aimi.bean.NewSystemMsgBean;
import com.yeying.aimi.bean.ReplyCacheBean;
import com.yeying.aimi.bean.SimpleMsgBean;
import com.yeying.aimi.bean.TheNewMsgBean;
import com.yeying.aimi.mode.HomeActivity;
import com.yeying.aimi.mode.bar_info.Activity_BaPing;
import com.yeying.aimi.storage.MianDaRaoMsgCache;
import com.yeying.aimi.storage.NormalMsgCache;
import com.yeying.aimi.storage.ReplyCache;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.storage.SimpleMessageCache;
import com.yeying.aimi.storage.SystemMessageCache;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import com.bugtags.library.obfuscated.cb;

/**
 * 系统消息处理帮助类
 */
public class SystemMsgUtil {
    /**
     * 处理系统发来的消息(2.0.0版本处理)
     */
    public static void dealSystemMessage(EMMessage message, Context appContext) {

        SessionCache session = SessionCache.getInstance(appContext);
        String transCode = message.getStringAttribute("transCode", "");
        String para = message.getStringAttribute("para", "");
        TheNewMsgBean bean = new TheNewMsgBean();
        bean.setMyUserId(session.userId);
        bean.setMsgId(message.getMsgId());
        bean.setTransCode(transCode);
        bean.setPara(para);
        bean.setFrom(message.getFrom());
        bean.setMsgTime(message.getMsgTime());
        if (transCode.equals("send10320")) {
            // 放入招呼消息缓存
            //addNearByMessageCache(bean, appContext);
//			addNewMsg(message,bean, appContext);
        } else if (Const.SystemTranscode.contains(transCode)) {
            // 放入系统消息缓存
            addSystemMessageCache(message, bean, appContext);
        } else if (transCode.equals("visitor10114")) {
            // 放入访客通知缓存
            //addVisitorCache(bean, appContext);
            addNewMsg(message, bean, appContext);
        } else if (transCode.equals("send10231")) {
            // 放入动态消息缓存
            addDongTaiCache(message, bean, appContext);
        } else if (transCode.equals("12201")) {
            // 处理好友设置通知
            dealOptionCache(bean, appContext);
        } else if (transCode.equals("cc0001")) {
            addNewMsg(message, bean, appContext);
        } else if (transCode.equals("cc0003")) {
            addNormalMsg(bean.getFrom(), appContext);
            addNewMsg(message, bean, appContext);
            refreashMsg();
        } else if (transCode.equals("cc0007")) {
            addNormalMsg(bean.getFrom(), appContext);
            addNewMsg(message, bean, appContext);
            refreashMsg();
        } else if (transCode.equals("send_10106")) {
            //处理赠送猫币通知
            //addSendMoney(bean, appContext);
        } else if (transCode.equals("send30000")) {
            //处理赠送抽奖卷通知
            //addSendCard(bean, appContext);
        } else if (transCode.equals("send30001")) {
            //处理抽奖开奖通知
            //addOpenPrize(bean, appContext);
        }

    }


    /*public static void addOpenPrize(TheNewMsgBean bean, Context appContext) {
        String para = bean.getPara();
        com.alibaba.fastjson.JSONObject paraObject = com.alibaba.fastjson.JSONObject
                .parseObject(para);

        String targetUserId = paraObject.getString("targetUserId");//	对方用户id
        int suc = paraObject.getIntValue("suc");//	是否中奖0是未中1是中奖
        String period = paraObject.getString("period");//	期号
        String prize_code = paraObject.getString("prize_code");//	开奖号码
        String prize_user_id = paraObject.getString("prize_user_id");// 	中奖用户
        String prize_user_name = paraObject.getString("prize_user_name");//用户名
        String prize_user_heading = paraObject.getString("prize_user_heading");//用户头像
        String activity_id = paraObject.getString("activity_id");//活动id
        Date prizeTime = paraObject.getDate("prize_time");//开奖时间
        String barId = paraObject.getString("bar_id");//酒吧id


        ChouJiangBean cBean = new ChouJiangBean();
        cBean.setMsgId(bean.getMsgId());
        cBean.setPara(bean.getPara());
        cBean.setTransCode(bean.getTransCode());
        cBean.setTargetUserId(targetUserId);
        cBean.setSuc(suc);
        cBean.setPeriod(period);
        cBean.setPrize_code(prize_code);
        cBean.setPrize_user_id(prize_user_id);
        cBean.setActivityId(activity_id);
        cBean.setPrizeUserName(prize_user_name);
        cBean.setPrizeUserHeading(prize_user_heading);
        cBean.setPrizeTime(prizeTime);
        cBean.setBarId(barId);

        ChouJiangCache cjCache = ChouJiangCache.getMsgCache(appContext);
        boolean isHaveData = false;
        for (int i = 0; i < cjCache.getChoujiangList().size(); i++) {
            ChouJiangBean cjBean = cjCache.getChoujiangList().get(i);
            if (cjBean.getMsgId().equals(cBean.getMsgId())) {
                isHaveData = true;
            }
        }
        if (!isHaveData) {
            cjCache.getChoujiangList().add(cBean);
            cjCache.save();

            SessionCache session = SessionCache.getInstance(appContext);
            if (HallActivity.activityInstance != null && !TextUtils.isEmpty(session.barId) && !TextUtils.isEmpty(barId) && session.barId.equals(barId)) {
                Message msg = new Message();
                msg.what = 5;
                //HallActivity.activityInstance.handler.sendMessage(msg);
            }
        }
    }*/

    /*public static void addSendCard(TheNewMsgBean bean, Context appContext) {
        String para = bean.getPara();
        com.alibaba.fastjson.JSONObject paraObject = com.alibaba.fastjson.JSONObject
                .parseObject(para);
        String targetUserId = paraObject.getString("targetUserId");//对方用户id
        String title = paraObject.getString("title");//标题
        String prize_name = paraObject.getString("prize_name");//奖品名称
        String prize_pic_url = paraObject.getString("prize_name");//奖品图片
        String people = paraObject.getString("people");//人数
        Date prize_time = paraObject.getDate("prize_time");//	开奖时间
        String number_code = paraObject.getString("number_code");//	编号
        String order_id = paraObject.getString("order_id");//	抽奖订单id
        String activity_id = paraObject.getString("activity_id");//活动id
        Date endTime = paraObject.getDate("end_time");//	结束时间

        ChouJiangBean cBean = new ChouJiangBean();
        cBean.setMsgId(bean.getMsgId());
        cBean.setPara(bean.getPara());
        cBean.setTransCode(bean.getTransCode());
        cBean.setTargetUserId(targetUserId);
        cBean.setTitle(title);
        cBean.setPrize_name(prize_name);
        cBean.setPrize_pic_url(prize_pic_url);
        cBean.setPeople(people);
        cBean.setPrize_time(prize_time);
        cBean.setNumber_code(number_code);
        cBean.setOrder_id(order_id);
        cBean.setActivityId(activity_id);
        cBean.setEndTime(endTime);

        ChouJiangCache cjCache = ChouJiangCache.getMsgCache(appContext);
        boolean isHaveData = false;
        for (int i = 0; i < cjCache.getChoujiangList().size(); i++) {
            ChouJiangBean cjBean = cjCache.getChoujiangList().get(i);
            if (cjBean.getMsgId().equals(cBean.getMsgId())) {
                isHaveData = true;
            }
        }
        if (!isHaveData) {
            cjCache.getChoujiangList().add(cBean);
            cjCache.save();

            if (HallActivity.activityInstance != null) {
                Message msg = new Message();
                msg.what = 4;
                // HallActivity.activityInstance.handler.sendMessage(msg);
            }
        }

    }*/


    /*public static void addSendMoney(TheNewMsgBean bean, Context appContext) {
        SessionCache session = SessionCache.getInstance(appContext);

        String para = bean.getPara();
        com.alibaba.fastjson.JSONObject paraObject = com.alibaba.fastjson.JSONObject
                .parseObject(para);
        String targetUserId = paraObject.getString("targetUserId");
        int money = paraObject.getIntValue("money");
        session.sendMoney = money;
        session.save();

        if (HallActivity.activityInstance != null) {
            Message msg = new Message();
            msg.what = 1;
            // HallActivity.activityInstance.handler.sendMessage(msg);
        }

    }*/

    public static void refreashMsg() {
        if (HomeActivity.activityInstance != null) {
            HomeActivity.activityInstance.toRefresh();
        }
        /*if (NewMessageActivity.activityInstance != null) {
            Message msg = new Message();
            msg.what = 0;
            NewMessageActivity.activityInstance.handler.sendMessage(msg);
        }*/
    }

    /***
     *放入临时消息缓存
     */
    public static void addNormalMsg(String userid, Context appContext) {
        //临时消息缓存
        NormalMsgCache cache = NormalMsgCache.getCache(appContext);
        boolean isHaveData = false;
        for (int i = 0; i < cache.getNormalmsg().size(); i++) {
            if (cache.getNormalmsg().get(i).getUserId().equals(userid)) {
                isHaveData = true;
                break;
            }
        }
        if (!isHaveData) {
            SimpleMsgBean bean = new SimpleMsgBean();
            bean.setUserId(userid);
            cache.getNormalmsg().add(bean);
            cache.save();
        }
    }

    /**
     * 提示有新消息
     */
    public static void addNewMsg(EMMessage emMessage, TheNewMsgBean bean1, Context appContext) {
        //boolean isInMianDaRao = isInMianDaRao(bean1.getFrom(), appContext);
        boolean isInMianDaRao = false;
        if (Activity_BaPing.activityInstance != null && Activity_BaPing.activityInstance.toChatUserId != null) {
            if (Activity_BaPing.activityInstance.toChatUserId.equals(bean1.getFrom())) {
                isInMianDaRao = true;
            }
        }

        if (!isInMianDaRao && emMessage.getChatType() == ChatType.Chat) {
            String tempPara = emMessage.getStringAttribute("para", "");
            String tempTransCode = emMessage.getStringAttribute("transCode", "");

            Log.i("", "新消息数增加1 : " + tempTransCode + " || " + tempPara + " || " + emMessage.toString());
            SimpleMessageCache simpleCache = SimpleMessageCache.getInstance(appContext);
            simpleCache.isNewMsg = true;
            simpleCache.newMsgNum = simpleCache.newMsgNum + 1;
            simpleCache.save();


            if (HomeActivity.activityInstance != null) {
                Message message1 = new Message();
                message1.what = 1;
                HomeActivity.activityInstance.mHandlerPlus.sendMessage(message1);
            }
        }
    }

    /**
     * 判断对方是否在我的消息免打扰中
     */
    public static boolean isInMianDaRao(String otherUserId, Context appContext) {
        boolean isInMianDaRao = false;
        SessionCache session = SessionCache.getInstance(appContext);
        MianDaRaoMsgCache mianCache = MianDaRaoMsgCache.getMsgCache(appContext);
        for (int i = 0; i < mianCache.getMsgList().size(); i++) {
            SimpleMsgBean bean = mianCache.getMsgList().get(i);
            if (bean.getMyUserId().equals(session.userId) && bean.getUserId().equals(otherUserId)) {
                isInMianDaRao = true;
            }
        }
        return isInMianDaRao;
    }

    /**
     * 查看我是否在对方的消息免打扰中
     * @param otherUserId
     * @param appContext
     * @return
     */
    /*public static boolean isMyInMianDaRao(String otherUserId, Context appContext) {
        boolean isInMianDaRao = false;
        SessionCache session = SessionCache.getInstance(appContext);
        MianDaRaoHaveMyCache mianCache = MianDaRaoHaveMyCache.getMsgCache(appContext);
        for (int i = 0; i < mianCache.getUserList().size(); i++) {
            OptionBean bean = mianCache.getUserList().get(i);
            if (bean.getMyUserId().equals(session.userId) && bean.getUserId().equals(otherUserId)) {
                isInMianDaRao = true;
            }
        }
        return isInMianDaRao;
    }*/

    /**
     * 查看我是否在对方的黑名单中
     */
    /*public static boolean isMyInBlack(String userId, Context appContext) {
        boolean isMyInBlack = false;
        SessionCache session = SessionCache.getInstance(appContext);
        BlackHaveMyCache blackCache = BlackHaveMyCache.getMsgCache(appContext);
        for (int i = 0; i < blackCache.getUserList().size(); i++) {
            OptionBean bean = blackCache.getUserList().get(i);
            if (bean.getMyUserId().equals(session.userId) && userId != null && bean.getUserId().equals(userId)) {
                isMyInBlack = true;
                Log.i("", "我在他的黑名单里");
                break;
            }
        }
        return isMyInBlack;
    }*/

    /**
     * 处理好友设置通知
     */
    public static void dealOptionCache(TheNewMsgBean bean1, Context appContext) {

        String para = bean1.getPara();
        com.alibaba.fastjson.JSONObject paraObject = com.alibaba.fastjson.JSONObject
                .parseObject(para);
        String targetUserId = paraObject.getString("targetUserId");
        if (targetUserId == null) {
            targetUserId = "";
        }
        //int black = paraObject.getIntValue("black");// 黑名单 -1不处理 0 默认 1黑名单
        //int interrupting = paraObject.getIntValue("interrupting");// 免打扰 -1不处理
        // 0默认 1免打扰

        /*SessionCache session = SessionCache.getInstance(appContext);
        BlackHaveMyCache blackCache = BlackHaveMyCache.getMsgCache(appContext);
        if (black == 0) {
            List<OptionBean> userList = new ArrayList<OptionBean>();
            for (int i = 0; i < blackCache.getUserList().size(); i++) {
                OptionBean bean = blackCache.getUserList().get(i);
                if (bean.getMyUserId() != null
                        && bean.getMyUserId().equals(session.userId)
                        && !bean.getUserId().equals(targetUserId)) {
                    userList.add(bean);
                }
            }
            blackCache.setUserList(userList);
            blackCache.save();
            Log.i("", "对方 解除我的黑名单状态");
        } else if (black == 1) {
            OptionBean bean = new OptionBean();
            bean.setMyUserId(session.userId);
            bean.setUserId(targetUserId);
            blackCache.getUserList().add(bean);
            blackCache.save();
            Log.i("", "对方将我加入黑名单");
        }

        MianDaRaoHaveMyCache mianCache = MianDaRaoHaveMyCache
                .getMsgCache(appContext);
        if (interrupting == 0) {
            List<OptionBean> userList = new ArrayList<OptionBean>();
            for (int i = 0; i < mianCache.getUserList().size(); i++) {
                OptionBean bean = mianCache.getUserList().get(i);
                if (bean.getMyUserId() != null
                        && bean.getMyUserId().equals(session.userId)
                        && !bean.getUserId().equals(targetUserId)) {
                    userList.add(bean);
                }
            }
            mianCache.setUserList(userList);
            mianCache.save();
            Log.i("", "对方 解除我的消息免打扰状态");
        } else if (interrupting == 1) {
            OptionBean bean = new OptionBean();
            bean.setMyUserId(session.userId);
            bean.setUserId(targetUserId);
            mianCache.getUserList().add(bean);
            mianCache.save();
            Log.i("", "对方将我加入消息免打扰");
        }*/

    }

    /**
     * 将动态消息放入缓存
     */
    public static void addDongTaiCache(EMMessage message, TheNewMsgBean bean1, Context appContext) {
        String para = bean1.getPara();
        Log.i("", "para = " + para);
        JSONObject paraObject = JSONObject.parseObject(para);
        String pUserId = paraObject.getString("pUserId"); // 评论人
        String pName = paraObject.getString("pName"); // 名称
        String pImg = paraObject.getString("pImg"); // 头像
        int type = paraObject.getIntValue("type"); // 类型
        String content = paraObject.getString("content"); // 内容
        String id = paraObject.getString("id"); // 动态id
        Date pTime = paraObject.getDate("pTime"); // 时间
        String pic_img = paraObject.getString("pic_img"); // 动态图片
        int pic_type = paraObject.getIntValue("pic_type"); // 动态类型

        ReplyCacheBean bean = new ReplyCacheBean();//消息缓存
        bean.setMyUserId(bean1.getMyUserId());
        bean.setUserId(pUserId);
        bean.setName(pName);
        bean.setImg(pImg);
        bean.setType(type);
        bean.setContent(content);
        bean.setId(id);
        bean.setTime(pTime);
        bean.setPic_img(pic_img);
        bean.setPic_type(pic_type);
        bean.setMsgId(bean1.getMsgId());
        bean.setTheMsgTime(bean1.getMsgTime());

        if (bean.getType() == 1 || bean.getType() == 2) {
            ReplyCache cache = ReplyCache.getCommentCache(appContext);
            //判断是否已读
            boolean isHaveData = false;
            for (ReplyCacheBean rbean : cache.getReplyList()) {
                if (rbean.getMsgId() != null
                        && rbean.getMsgId().equals(bean.getMsgId())) {
                    isHaveData = true;
                }
            }
            if (!isHaveData) {
                cache.getReplyList().add(bean);
                cache.save();
            }

            SimpleMessageCache simpleCache = SimpleMessageCache
                    .getInstance(appContext);
            simpleCache.isNewMy = true;
            simpleCache.save();

            addNewMsg(message, bean1, appContext);
        }
    }

    /**
     * 将访客通知放入缓存
     */
    /*public static void addVisitorCache(TheNewMsgBean bean, Context appContext) {
        VisitorMessageCache cache = VisitorMessageCache.getMsgCache(appContext);
        boolean isHaveData = false;
        for (int i = 0; i < cache.getMsgList().size(); i++) {
            if (bean.getMsgId().equals(cache.getMsgList().get(i).getMsgId())) {
                isHaveData = true;
                break;
            }
        }
        if (!isHaveData) {
            Log.i("", "存储一条访客消息 MsgId：" + bean.getMsgId());

            VisitorMsgBean visitor = new VisitorMsgBean();
            visitor.setMyUserId(bean.getMyUserId());
            visitor.setMsgId(bean.getMsgId());
            visitor.setTheMsgTime(bean.getMsgTime());
            com.alibaba.fastjson.JSONObject paraObject = com.alibaba.fastjson.JSONObject
                    .parseObject(bean.getPara());
            String vUserId = paraObject.getString("vUserId");
            String vUserName = paraObject.getString("vUserName");
            String vHeadImg = paraObject.getString("vHeadImg");
            Date time = paraObject.getDate("time");
            int vSex = paraObject.getIntValue("vSex");

            visitor.setUserId(vUserId);
            visitor.setUserName(vUserName);
            visitor.setHeadImg(vHeadImg);
            visitor.setTime(time);
            visitor.setSex(vSex);
//			visitor.setTheDate(getParseDate(dealTimeMoon(time)));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            visitor.setTheDate(getParseDate(sdf.format(time)));

            cache.getMsgList().add(visitor);
            cache.save();

            addVisitorGroupCache(bean, visitor, appContext);

        }
    }*/

    /**
     * 将访客通知按日期分组后放入缓存
     */
    /*public static void addVisitorGroupCache(TheNewMsgBean bean,
                                            VisitorMsgBean visitor, Context appContext) {
        VisitorGroupCache groupCache = VisitorGroupCache
                .getMsgCache(appContext);
        boolean isHaveData = false;
        for (int i = 0; i < groupCache.getMsgList().size(); i++) {
            VisitorGroupBean groupBean = groupCache.getMsgList().get(i);
            if (groupBean.getMyUserId().equals(bean.getMyUserId())
                    && groupBean.getTime().equals(visitor.getTheDate())) {
                isHaveData = true;
                groupBean.getVisitorList().add(visitor);
            }
        }
        if (!isHaveData) {
            VisitorGroupBean groupBean = new VisitorGroupBean();
            groupBean.setMyUserId(bean.getMyUserId());
            groupBean.setTime(visitor.getTheDate());
            List<VisitorMsgBean> visitorList = new ArrayList<VisitorMsgBean>();
            visitorList.add(visitor);
            groupBean.setVisitorList(visitorList);
            groupCache.getMsgList().add(groupBean);
        }

        groupCache.save();
    }*/

    /**
     * 将系统消息放入缓存
     */
    public static void addSystemMessageCache(EMMessage message, TheNewMsgBean bean,
                                             Context appContext) {
        SystemMessageCache cache = SystemMessageCache.getMsgCache(appContext);
        boolean isHaveData = false;
        for (int i = 0; i < cache.getMsgList().size(); i++) {
            if (bean.getMsgId().equals(cache.getMsgList().get(i).getMsgId())) {
                isHaveData = true;
                break;
            }
        }
        if (!isHaveData) {
            Log.i("", "放入一条系统消息");


            NewSystemMsgBean systemBean = new NewSystemMsgBean();
            systemBean.setMyUserId(bean.getMyUserId());
            systemBean.setMsgId(bean.getMsgId());
            systemBean.setTheMsgTime(bean.getMsgTime());
            systemBean.setTransCode(bean.getTransCode());
            systemBean.setPara(bean.getPara());

            dealTheSystemMessage(systemBean);

            if (systemBean.getTransCode().equals("welcomebar")) {
                // 进入酒吧欢迎消息单独处理
                String para = systemBean.getPara();
                JSONObject paraObject = JSONObject.parseObject(para);
                String barId = paraObject.getString("barId");
                systemBean.setBarId(barId);
                List<Object> welList = paraObject.getJSONArray("welList");
                if (welList != null) {
                    for (int i = 0; i < welList.size(); i++) {
                        JSONObject obj = (JSONObject) welList.get(i);
                        NewSystemMsgBean itemBean = new NewSystemMsgBean();
                        itemBean.setMyUserId(systemBean.getMyUserId());
                        itemBean.setMsgId(systemBean.getMsgId());
                        itemBean.setTransCode(systemBean.getTransCode());
                        itemBean.setTheMsgTime(systemBean.getTheMsgTime());
                        itemBean.setPara(systemBean.getPara());
                        itemBean.setMsgTime(systemBean.getMsgTime());
                        itemBean.setTitle(obj.getString("title"));
                        itemBean.setContent(obj.getString("content"));
                        itemBean.setDescription(obj.getString("desc"));
                        cache.getMsgList().add(itemBean);
                        cache.save();
                    }
                    int num = welList.size() - 1;
                    if (num < 0) {
                        num = 0;
                    }

                    SimpleMessageCache simple = SimpleMessageCache.getInstance(appContext);
                    simple.newMsgNum = simple.newMsgNum + num;
                    simple.save();

                }

                addNewMsg(message, bean, appContext);


                return;
            }

            cache.getMsgList().add(systemBean);
            cache.save();

            addNewMsg(message, bean, appContext);

        }
    }

    /**
     * 将系统消息解析拼装数据
     */
    public static void dealTheSystemMessage(NewSystemMsgBean sysBean) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
        Date date = new Date(sysBean.getTheMsgTime());
        sysBean.setMsgTime(sdf.format(date));
        String transCode = sysBean.getTransCode();
        String para = sysBean.getPara();

        if (transCode.equals("send10321")) {
            // 礼物退回通知
            JSONObject paraObject = JSONObject.parseObject(para);
            String userId = paraObject.getString("fUid");
            String userName = paraObject.getString("fName");
            String headImg = paraObject.getString("fUhimg");
            String recordId = paraObject.getString("recordId");
            String giftName = paraObject.getString("giftName");
            int score = paraObject.getIntValue("score");
            String ratio = paraObject.getString("ratio");
            int type = paraObject.getIntValue("type");
            int replyType = paraObject.getIntValue("replyType");

            sysBean.setUserId(userId);
            sysBean.setUserName(userName);
            sysBean.setHeadImg(headImg);
            sysBean.setRecordId(recordId);
            sysBean.setScore(score);
            sysBean.setRatio(ratio);
            sysBean.setGiftName(giftName);
            sysBean.setContent("");
            sysBean.setType(type);
            sysBean.setReplyType(replyType);

        } else if (transCode.equals("send10318")) {
            // 酒水支付成功通知
            JSONObject paraObject = JSONObject.parseObject(para);
            String barId = paraObject.getString("barId");
            double money = paraObject.getDoubleValue("money");
            String barName = paraObject.getString("barName");
            String orderId = paraObject.getString("orderId");
            int status = paraObject.getIntValue("status");
            Date pTime = paraObject.getDate("pTime");

            sysBean.setBarId(barId);
            sysBean.setMoney(money);
            sysBean.setMoney(money);
            sysBean.setBarName(barName);
            sysBean.setOrderId(orderId);
            sysBean.setStatus(status);
            sysBean.setpTime(pTime);

        } else if (transCode.equals("sendSystem")) {
            JSONObject paraObject = JSONObject.parseObject(para);
            String title = paraObject.getString("title");
            String picUrl = paraObject.getString("picUrl");
            String url = paraObject.getString("url");
            String description = paraObject.getString("description");

            sysBean.setTitle(title);
            sysBean.setPicUrl(picUrl);
            sysBean.setUrl(url);
            sysBean.setDescription(description);
        } else if (transCode.equals("prize12502")) {
            JSONObject paraObject = JSONObject.parseObject(para);
            String barId = paraObject.getString("barId");
            String barName = paraObject.getString("barName");
            String orderId = paraObject.getString("orderId");
            String guessId = paraObject.getString("guessId");
            int score = paraObject.getIntValue("score");
            String shopName = paraObject.getString("shopName");
            int multi = paraObject.getIntValue("multi");
            String guessName = paraObject.getString("guessName");
            String prizeOption = paraObject.getString("prizeOption");
            String remark = paraObject.getString("remark");

            sysBean.setBarId(barId);
            sysBean.setBarName(barName);
            sysBean.setOrderId(orderId);
            sysBean.setGuessId(guessId);
            sysBean.setScore(score);
            sysBean.setShopName(shopName);
            sysBean.setMulti(multi);
            sysBean.setGuessName(guessName);
            sysBean.setPrizeOption(prizeOption);
            sysBean.setRemark(remark);

        } else if (transCode.equals("book")) {
            // 预定通知
            JSONObject paraObject = JSONObject.parseObject(para);
            String bookId = paraObject.getString("bookId");// 预定记录id
            int status = paraObject.getIntValue("status");// 当前状态
            String barId = paraObject.getString("barId");// 酒吧id
            String barName = paraObject.getString("barName");// 酒吧名称
            Date bookTime = paraObject.getDate("bookTime");// 预定时间
            double money = paraObject.getDouble("money");// 预付金
            String name = paraObject.getString("name");// 预定人名称
            String phone = paraObject.getString("phone");// 预定人电话
            String remark = paraObject.getString("remark");// 备注
            List<Object> objList = paraObject.getJSONArray("barPhones");// 酒吧电话
            List<String> barPhones = new ArrayList<String>();
            for (int i = 0; i < objList.size(); i++) {
                barPhones.add((String) objList.get(i));
            }
            String title = paraObject.getString("title"); // 标题
            String content = paraObject.getString("content");// 内容
            Date createTime = paraObject.getDate("createTime");// 变更时间

            sysBean.setBookId(bookId);
            sysBean.setStatus(status);
            sysBean.setBarName(barName);
            sysBean.setBookTime(bookTime);
            sysBean.setMoney(money);
            sysBean.setName(name);
            sysBean.setPhone(phone);
            sysBean.setRemark(remark);
            sysBean.setBarPhones(barPhones);
            sysBean.setTitle(title);
            sysBean.setContent(content);
            sysBean.setCreateTime(createTime);

        } else if (transCode.equals("order")) {
            // 订单状态改变通知
            JSONObject paraObject = JSONObject.parseObject(para);
            String barId = paraObject.getString("barId");// 酒吧id
            double money = paraObject.getDoubleValue("money");// 总金额
            String barName = paraObject.getString("barName");// 酒吧名称
            String orderId = paraObject.getString("orderId");// 订单id
            int status = paraObject.getIntValue("status");// 当前订单状态
            Date pTime = paraObject.getDate("pTime");// 时间
            String title = paraObject.getString("title");// 标题
            String content = paraObject.getString("content");// 内容
            String remark = paraObject.getString("remark");// 备注
            String phone = paraObject.getString("phone");// 手机号

            sysBean.setBarId(barId);
            sysBean.setMoney(money);
            sysBean.setBarName(barName);
            sysBean.setOrderId(orderId);
            sysBean.setStatus(status);
            sysBean.setpTime(pTime);
            sysBean.setTitle(title);
            sysBean.setContent(content);
            sysBean.setRemark(remark);
            sysBean.setPhone(phone);

        } else if (transCode.equals("send30002")) {
            // 中奖通知
            JSONObject paraObject = JSONObject.parseObject(para);
            String targetUserId = paraObject.getString("targetUserId");
            Date prize_time = paraObject.getDate("prize_time");
            String bar_name = paraObject.getString("bar_name");
            String prize_code = paraObject.getString("prize_code");
            String prize_name = paraObject.getString("prize_name");
            String activity_id = paraObject.getString("activity_id");
            boolean isWinner = paraObject.getBoolean("is_winner");

            sysBean.setWinner(isWinner);
            sysBean.setTargetUserId(targetUserId);
            sysBean.setPrizeTime(prize_time);
            sysBean.setBarName(bar_name);
            sysBean.setPrizeCode(prize_code);
            sysBean.setPrizeName(prize_name);
            sysBean.setActivityId(activity_id);
        } else if (transCode.equals("send30010")) {
            // 热榜结果通知
            JSONObject paraObject = JSONObject.parseObject(para);
            Date time = paraObject.getDate("time");
            String bar_name = paraObject.getString("bar_name");
            String content = paraObject.getString("content");
            String activity_id = paraObject.getString("activity_id");
            String periodId = paraObject.getString("periodId");

            sysBean.setPeriodId(periodId);
            sysBean.setCreateTime(time);
            sysBean.setBarName(bar_name);
            sysBean.setContent(content);
            sysBean.setActivityId(activity_id);

        } else if (transCode.equals("send30011")) {
            // 提现结果通知
            JSONObject paraObject = JSONObject.parseObject(para);
            Date time = paraObject.getDate("time");
            int result = paraObject.getIntValue("result");//0失败 1成功
            int money = paraObject.getIntValue("money");
            String content = paraObject.getString("content");

            sysBean.setCreateTime(time);
            sysBean.setStatus(result);
            sysBean.setMoney(money);
            sysBean.setContent(content);
        }
    }

    /**
     * 从系统消息缓存中取出数据(2.0.0版本)
     */
    /*public static List<NewSystemMsgBean> getSystemMessage(String userId,
                                                          Context appContext) {
        SystemMessageCache cache = SystemMessageCache.getMsgCache(appContext);
        List<NewSystemMsgBean> messageList = new ArrayList<NewSystemMsgBean>();
        for (int i = 0; i < cache.getMsgList().size(); i++) {
            NewSystemMsgBean bean = cache.getMsgList().get(i);
            bean.setRead(true);
            if (bean.getMyUserId().equals(userId)) {
                messageList.add(bean);
            }
        }
        cache.save();
        return messageList;

    }*/

    /**
     * 获取未读的系统消息数
     */
    /*public static int getSysteamMsgNum(String userId, Context appContext) {
        int num = 0;
        SystemMessageCache cache = SystemMessageCache.getMsgCache(appContext);
        for (int i = 0; i < cache.getMsgList().size(); i++) {
            NewSystemMsgBean bean = cache.getMsgList().get(i);
            if (bean.getMyUserId().equals(userId) && !bean.isRead()) {
                num++;
            }
        }
        return num;
    }*/

    /**
     * 判断是否有系统消息
     */
    /*public static boolean isHaveSystemMsg(String userId, Context appContext) {
        boolean answer = false;
        SystemMessageCache cache = SystemMessageCache.getMsgCache(appContext);
        for (int i = 0; i < cache.getMsgList().size(); i++) {
            NewSystemMsgBean bean = cache.getMsgList().get(i);
            if (bean.getMyUserId().equals(userId)) {
                answer = true;
                break;
            }
        }
        return answer;
    }*/

    /**
     * 删除系统消息缓存
     */
    /*public static void delSystemMsgChche(String userId, Context appContext) {
        SystemMessageCache cache = SystemMessageCache.getMsgCache(appContext);
        List<NewSystemMsgBean> messageList = new ArrayList<NewSystemMsgBean>();
        for (int i = 0; i < cache.getMsgList().size(); i++) {
            NewSystemMsgBean bean = cache.getMsgList().get(i);
            bean.setRead(true);
            if (!bean.getMyUserId().equals(userId)) {
                messageList.add(bean);
            }
        }
        cache.setMsgList(messageList);
        cache.save();
    }*/

    /**
     * 将招呼消息放入缓存
     */
    /*public static void addNearByMessageCache(TheNewMsgBean bean,
                                             Context appContext) {
        NearByMessageCache cache = NearByMessageCache.getMsgCache(appContext);
        boolean isHaveData = false;
        for (int i = 0; i < cache.getMsgList().size(); i++) {
            if (bean.getMsgId().equals(cache.getMsgList().get(i).getMsgId())) {
                isHaveData = true;
                break;
            }
        }
        if (!isHaveData) {
            Log.i("", "放入一条招呼消息");
            MsgHistroyBean msgBean = dealNearByMessage(bean);

            cache.getMsgList().add(msgBean);
            cache.save();
        }
    }*/

    /*public static MsgHistroyBean dealNearByMessage(TheNewMsgBean bean) {
        MsgHistroyBean sysBean = new MsgHistroyBean();
        sysBean.setMyUserId(bean.getMyUserId());
        sysBean.setFrom(bean.getFrom());
        sysBean.setMsgId(bean.getMsgId());
        sysBean.setMsgTime(dealTime(bean.getMsgTime()));
        sysBean.setTheMsgTime(bean.getMsgTime());
        sysBean.setSystemMsg(true);
        sysBean.setPara(bean.getPara());
        String para = bean.getPara();

        JSONObject paraObject = JSONObject.parseObject(para);
        String userId = paraObject.getString("fUid");
        String userName = paraObject.getString("fName");
        String headImg = paraObject.getString("fUhimg");
        String recordId = paraObject.getString("recordId");
        String chat = paraObject.getString("chat");
        int score = paraObject.getIntValue("score");
        String ratio = paraObject.getString("ratio");
        int type = paraObject.getIntValue("type");

        sysBean.setZhaohuType(type);
        sysBean.setUserId(userId);
        sysBean.setUserName(userName);
        sysBean.setHeadImg(headImg);
        sysBean.setRecordId(recordId);
        sysBean.setChat(chat);
        sysBean.setScore(score);
        sysBean.setRatio(ratio);

        if (type == 0) {
            // type为0是打招呼
            try {
                String giftStr = paraObject.getString("gift");
                if (!TextUtils.isEmpty(giftStr)) {
                    JSONObject giftObj = JSONObject.parseObject(giftStr);
                    MsgGiftBean gift = new MsgGiftBean();
                    gift.setId(giftObj.getString("id"));
                    gift.setName(giftObj.getString("name"));
                    gift.setUrl(giftObj.getString("url"));
                    gift.setType(giftObj.getIntValue("type"));
                    gift.setSsUrl(giftObj.getString("sUrl"));
                    sysBean.setGiftbean(gift);
                }
            } catch (Exception e) {
                Log.i("", "send10320 type为0是打招呼 解析礼物信息出错");
            }
        }

        return sysBean;
    }*/

    public static String dealTime(long timeStr) {
        Date msgDate = new Date(timeStr);
        Date date = new Date();
        long between = date.getTime() - timeStr;
        if (between > 1800000) {
            return "已过期";
        } else if (between > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("mm分ss秒");
            Date rDate = new Date(1800000 - between);
            return "剩余" + sdf.format(rDate);
        } else {
            return "已过期";
        }
    }

    /**
     * 获取招呼消息
     */
    /*public static List<MsgHistroyBean> getNearByMsg(String userId,
                                                    Context appContext) {
        List<MsgHistroyBean> nearByMsgList = new ArrayList<MsgHistroyBean>();
        NearByMessageCache cache = NearByMessageCache.getMsgCache(appContext);
        for (int i = 0; i < cache.getMsgList().size(); i++) {
            MsgHistroyBean bean = cache.getMsgList().get(i);
            bean.setRead(true);
            if (bean.getMyUserId() != null && bean.getMyUserId().equals(userId)) {
                bean.setMsgTime(dealTime(bean.getTheMsgTime()));
                nearByMsgList.add(bean);
            }
        }
        cache.save();
        return nearByMsgList;
    }*/

    /**
     * 判断是否有招呼
     */
    /*public static boolean isHaveNearByMsg(String userId, Context appContext) {
        boolean answer = false;
        NearByMessageCache cache = NearByMessageCache.getMsgCache(appContext);
        for (int i = 0; i < cache.getMsgList().size(); i++) {
            MsgHistroyBean bean = cache.getMsgList().get(i);
            if (bean.getMyUserId() != null && bean.getMyUserId().equals(userId)) {
                answer = true;
            }
        }
        return answer;
    }*/

    /**
     * 获取未读的招呼消息数
     */
    /*public static int getNearByNum(String userId, Context appContext) {
        int num = 0;
        NearByMessageCache cache = NearByMessageCache.getMsgCache(appContext);
        for (int i = 0; i < cache.getMsgList().size(); i++) {
            MsgHistroyBean bean = cache.getMsgList().get(i);
            if (bean.getMyUserId() != null && bean.getMyUserId().equals(userId)
                    && !bean.isRead()) {
                num++;
            }
        }
        return num;
    }*/

    /**
     * 删除系统消息缓存
     */
    /*public static void delNearByMsgChche(String userId, Context appContext) {
        SimpleMessageCache simpleCache = SimpleMessageCache
                .getInstance(appContext);
        simpleCache.isCancleNearMsg = true;
        simpleCache.save();

        NearByMessageCache cache = NearByMessageCache.getMsgCache(appContext);
        List<MsgHistroyBean> messageList = new ArrayList<MsgHistroyBean>();
        for (int i = 0; i < cache.getMsgList().size(); i++) {
            MsgHistroyBean bean = cache.getMsgList().get(i);
            bean.setRead(true);
            if (!bean.getMyUserId().equals(userId)) {
                messageList.add(bean);
            }
        }
        cache.setMsgList(messageList);
        cache.save();
    }*/

    /**
     * 删除单条招呼消息
     */
    /*public static void delSingleNearByMsg(String msgId, Context appContext) {
        NearByMessageCache cache = NearByMessageCache.getMsgCache(appContext);
        for (int i = 0; i < cache.getMsgList().size(); i++) {
            MsgHistroyBean bean = cache.getMsgList().get(i);
            bean.setRead(true);
            if (bean.getMsgId().equals(msgId)) {
                cache.getMsgList().remove(i);
                break;
            }
        }
        cache.save();
    }*/

    /**
     * 获取未读的访客消息数
     */
    /*public static int getVisitorNum(String userId, Context appContext) {
        int num = 0;
        VisitorMessageCache cache = VisitorMessageCache.getMsgCache(appContext);
        for (int i = 0; i < cache.getMsgList().size(); i++) {
            VisitorMsgBean bean = cache.getMsgList().get(i);
            if (bean.getMyUserId().equals(userId) && !bean.isRead()) {
                num++;
            }
        }
        return num;
    }*/

    /**
     * 判断是否有访客消息
     */
    /*public static boolean isHaveVisitorMsg(String userId, Context appContext) {
        // 删除超过7天的访客消息
        delOldVisitor(appContext);

        boolean answer = false;
        VisitorMessageCache cache = VisitorMessageCache.getMsgCache(appContext);
        for (int i = 0; i < cache.getMsgList().size(); i++) {
            VisitorMsgBean bean = cache.getMsgList().get(i);
            if (bean.getMyUserId().equals(userId)) {
                answer = true;
            }
        }
        return answer;
    }*/

    /**
     * 获得访客消息列表
     */
    /*public static List<VisitorMsgBean> getVisitorMsg(String userId,
                                                     Context appContext) {
        List<VisitorMsgBean> visitorMsgList = new ArrayList<VisitorMsgBean>();
        VisitorMessageCache cache = VisitorMessageCache.getMsgCache(appContext);
        for (int i = 0; i < cache.getMsgList().size(); i++) {
            VisitorMsgBean bean = cache.getMsgList().get(i);
            bean.setRead(true);
            if (bean.getMyUserId().equals(userId)) {
                visitorMsgList.add(bean);
            }
        }
        cache.save();
        return visitorMsgList;
    }*/

    /*public static void delOldVisitor(Context appContext) {
        VisitorMessageCache cache1 = VisitorMessageCache
                .getMsgCache(appContext);
        List<VisitorMsgBean> visitorList = new ArrayList<VisitorMsgBean>();
        for (int i = 0; i < cache1.getMsgList().size(); i++) {
            VisitorMsgBean bean1 = cache1.getMsgList().get(i);
            if (bean1.getTheDate() == null) {
                continue;
            }
            long time = bean1.getTheDate().getTime();
            long nowTime = new Date().getTime();
            if (nowTime - time <= (86400000 * 7)) {
                visitorList.add(bean1);
            }
        }
        cache1.setMsgList(visitorList);
        cache1.save();

        List<VisitorGroupBean> visitorGroupList = new ArrayList<VisitorGroupBean>();
        VisitorGroupCache groupCache = VisitorGroupCache
                .getMsgCache(appContext);
        for (int i = groupCache.getMsgList().size() - 1; i >= 0; i--) {
            VisitorGroupBean bean = groupCache.getMsgList().get(i);
            if (bean.getTime() == null) {
                continue;
            }
            long time = bean.getTime().getTime();
            long nowTime = new Date().getTime();
            if (nowTime - time <= (86400000 * 7)) {
                visitorGroupList.add(bean);
            }
        }
        groupCache.setMsgList(visitorGroupList);

    }*/

    /**
     * 倒序取出分组后的访客信息
     */
    /*public static List<VisitorGroupBean> getVisitorGroup(String userId,
                                                         Context appContext) {
        List<VisitorMsgBean> viList = getVisitorMsg(userId, appContext);

        List<VisitorGroupBean> visitorGroupList = new ArrayList<VisitorGroupBean>();
        VisitorGroupCache groupCache = VisitorGroupCache
                .getMsgCache(appContext);
        for (int i = groupCache.getMsgList().size() - 1; i >= 0; i--) {
            VisitorGroupBean bean = groupCache.getMsgList().get(i);
            if (bean.getMyUserId().equals(userId)) {
                visitorGroupList.add(bean);
            }
        }

        return visitorGroupList;
    }*/



    /**
     * 是否有重复数据
     */
    public static boolean isRepeat(List<String> list, String str) {
        boolean isHaveData = false;
        if (list == null || str == null) {
            return isHaveData;
        }

        for (int i = 0; i < list.size(); i++) {
            if (str.equals(list.get(i))) {
                isHaveData = true;
                break;
            }
        }

        return isHaveData;
    }

    /**
     * 加工时间以每天中午12点为界限
     */
    public static String dealTimeMoon(Date date) {
        String str = "";
        if (date == null) {
            return str;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfHour = new SimpleDateFormat("HH");
        String hour = sdfHour.format(date);
        Date theDate = null;
        if (Integer.parseInt(hour) > 12) {
            theDate = date;
        } else {
            theDate = new Date(date.getTime() - 86400000);
        }

        str = sdf.format(theDate);

        return str;
    }

    public static Date getParseDate(String theDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date overDate = null;
        try {
            overDate = sdf.parse(theDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return overDate;
    }

    /**
     * 删除访客消息
     */
    /*public static void delVisitorMessage(String userId, Context appContext) {
        List<VisitorMsgBean> visitorMsgList = new ArrayList<VisitorMsgBean>();
        VisitorMessageCache cache = VisitorMessageCache.getMsgCache(appContext);
        for (int i = 0; i < cache.getMsgList().size(); i++) {
            VisitorMsgBean bean = cache.getMsgList().get(i);
            bean.setRead(true);
            if (!bean.getMyUserId().equals(userId)) {
                visitorMsgList.add(bean);
            }
        }
        cache.setMsgList(visitorMsgList);
        cache.save();

        List<VisitorGroupBean> visitorGroupBeans = new ArrayList<VisitorGroupBean>();
        VisitorGroupCache groupCache = VisitorGroupCache
                .getMsgCache(appContext);
        for (int i = 0; i < groupCache.getMsgList().size(); i++) {
            VisitorGroupBean groupBean = groupCache.getMsgList().get(i);
            if (!groupBean.getMyUserId().equals(userId)) {
                visitorGroupBeans.add(groupBean);
            }
        }
        groupCache.setMsgList(visitorGroupBeans);
        groupCache.save();

    }*/

    /**
     * 移除访客消息横条
     */
    /*public static void removeVisitorView(String userId, Context appContext) {
        List<VisitorMsgBean> viList = SystemMsgUtil.getVisitorMsg(userId,
                appContext);
        SimpleMessageCache simpleCache = SimpleMessageCache
                .getInstance(appContext);
        simpleCache.isCancleVisitorMsg = true;
        simpleCache.save();
    }*/

    /**
     * 移除动态消息横条
     */
    /*public static void removeDongtaiView(String userId, Context appContext) {
        List<ReplyCacheBean> replyList = SystemMsgUtil.getReplyList(userId,
                appContext);
        List<ReplyCacheBean> otherList = SystemMsgUtil.getOtherReplyList(
                userId, appContext);
        // 清空我自己收到的新消息缓存
        ReplyCache replyCache = ReplyCache.getCommentCache(appContext);
        replyCache.setReplyList(otherList);
        replyCache.save();

        // 放入历史消息缓存
        ReplyHistoryCache hisCache = ReplyHistoryCache
                .getCommentCache(appContext);
        hisCache.getReplyHistoryList().addAll(replyList);
        hisCache.save();

        SimpleMessageCache simpleCache = SimpleMessageCache
                .getInstance(appContext);
        simpleCache.isCancleDongtaiMsg = true;
        simpleCache.save();

    }*/

    /**
     * 得到动态消息
     */
    public static List<ReplyCacheBean> getReplyList(String userId,
                                                    Context appContext) {
        ReplyCache cache = ReplyCache.getCommentCache(appContext);
        List<ReplyCacheBean> replyList = new ArrayList<ReplyCacheBean>();
        if (cache.getReplyList()!=null){//当缓存中回复的list不为空的时候
            for (int i = 0; i < cache.getReplyList().size(); i++) {//循环整个集合
                ReplyCacheBean bean = cache.getReplyList().get(i);
                if (bean.getMyUserId().equals(userId)) {//当myuserid相等时 加入到集合中
                    replyList.add(bean);
                }
            }
        }
        return replyList;
    }

    /**
     * 将动态消息变为已读
     */
    public static void makeRelayAsRead(String userId, Context appContext) {
        ReplyCache cache = ReplyCache.getCommentCache(appContext);
        List<ReplyCacheBean> replyList = new ArrayList<ReplyCacheBean>();
        for (int i = 0; i < cache.getReplyList().size(); i++) {
            ReplyCacheBean bean = cache.getReplyList().get(i);
            bean.setRead(true);
            if (bean.getMyUserId().equals(userId)) {
                replyList.add(bean);
            }
        }
        cache.save();
    }

    /**
     * 得到其他用户的动态消息
     */
    public static List<ReplyCacheBean> getOtherReplyList(String userId,
                                                         Context appContext) {
        ReplyCache cache = ReplyCache.getCommentCache(appContext);
        List<ReplyCacheBean> replyList = new ArrayList<ReplyCacheBean>();
        if (cache.getReplyList()!=null){
            for (int i = 0; i < cache.getReplyList().size(); i++) {
                ReplyCacheBean bean = cache.getReplyList().get(i);
                bean.setRead(true);
                if (!bean.getMyUserId().equals(userId)) {
                    replyList.add(bean);
                }
            }
        }

        cache.save();

        return replyList;
    }

    /**
     * 得到历史消息记录
     */
    /*public static List<ReplyCacheBean> getHistoryReplyList(String userId,
                                                           Context appContext) {
        ReplyHistoryCache replyHisCache = ReplyHistoryCache
                .getCommentCache(appContext);
        List<ReplyCacheBean> replyList = new ArrayList<ReplyCacheBean>();
        for (int i = 0; i < replyHisCache.getReplyHistoryList().size(); i++) {
            ReplyCacheBean bean = replyHisCache.getReplyHistoryList().get(i);
            if (bean.getMyUserId().equals(userId)) {
                replyList.add(bean);
            }
        }
        return replyList;
    }*/


    /**
     * 获取最后一条系统消息时间
     */
    /*public static Long getLastSystemMsgTime(String userId, Context appContext) {
        long time = 0L;
        SystemMessageCache cache = SystemMessageCache.getMsgCache(appContext);
        if (cache.getMsgList() != null) {
            int size = cache.getMsgList().size();
            for (int i = size - 1; i >= 0; i--) {
                NewSystemMsgBean bean = cache.getMsgList().get(i);
                if (bean.getMyUserId().equals(userId)) {
                    time = bean.getTheMsgTime();
                    break;
                }
            }
        }
        return time;
    }*/

    /**
     * 获取最后一条访客消息时间
     */
    /*public static Long getLastVisiterMsgTime(String userId, Context appContext) {
        long time = 0L;
        VisitorMessageCache cache = VisitorMessageCache.getMsgCache(appContext);
        if (cache.getMsgList() != null) {
            int size = cache.getMsgList().size();
            for (int i = size - 1; i >= 0; i--) {
                VisitorMsgBean bean = cache.getMsgList().get(i);
                if (bean.getMyUserId().equals(userId)) {
                    time = bean.getTheMsgTime();
                    break;
                }
            }
        }
        return time;
    }*/

    /**
     * 获取最后一条动态消息时间
     */
    public static Long getLastDongtaiMsgTime(String userId, Context appContext) {
        long time = 0L;
        ReplyCache cache = ReplyCache.getCommentCache(appContext);
        if (cache.getReplyList() != null) {
            int size = cache.getReplyList().size();
            for (int i = size - 1; i >= 0; i--) {
                ReplyCacheBean bean = cache.getReplyList().get(i);
                if (bean.getMyUserId().equals(userId)) {
                    time = bean.getTheMsgTime();
                    break;
                }
            }
        }
        return time;
    }

}
