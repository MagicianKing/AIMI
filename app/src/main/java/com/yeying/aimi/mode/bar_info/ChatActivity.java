package com.yeying.aimi.mode.bar_info;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.yeying.aimi.API;
import com.yeying.aimi.R;
import com.yeying.aimi.adapter.ChatAdapter;
import com.yeying.aimi.aimibase.AIMIApplication;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.bean.ChatBean;
import com.yeying.aimi.bean.ChatMsgComparator;
import com.yeying.aimi.bean.FollBean;
import com.yeying.aimi.bean.JsonGiftMsg;
import com.yeying.aimi.bean.JsonUserInfo;
import com.yeying.aimi.bean.MsgHistroyBean;
import com.yeying.aimi.bean.ReplyCacheBean;
import com.yeying.aimi.bean.SimpleMsgBean;
import com.yeying.aimi.huanxin.ImageUtils;
import com.yeying.aimi.mode.inform.InformActivity;
import com.yeying.aimi.mode.signlechat.SingleChat;
import com.yeying.aimi.storage.AttendCache;
import com.yeying.aimi.storage.CancleMsgCache;
import com.yeying.aimi.storage.MianDaRaoMsgCache;
import com.yeying.aimi.storage.NearestMsgCache;
import com.yeying.aimi.storage.NormalMsgCache;
import com.yeying.aimi.storage.ReplyCache;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.storage.SimpleMessageCache;
import com.yeying.aimi.utils.PromptUtils;
import com.yeying.aimi.utils.SystemMsgUtil;
import com.yeying.aimi.utils.WeakHandler;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;


/**
 * Created by tanchengkeji on 2017/9/29.
 */

public class ChatActivity extends BaseActivity implements View.OnClickListener, ChatAdapter.ItemClick {

    //点击去通知页面
    private RelativeLayout chat_msg;
    //消息列表
    private SwipeToLoadLayout swipeToLoadLayout_msg;
    private RecyclerView mRecyclerView;
    //消息小红点
    private RelativeLayout chat_num_news;
    private TextView chat_news;

    private SessionCache session;

    private String systemId;//系统账号
    private List<SimpleMsgBean> cancleMsgList = new ArrayList<>();
    private List<MsgHistroyBean> msList;//历史消息
    private List<ChatBean> nearMsgList;//最近消息
    private CancleMsgCache cancleCache;//已删除消息
    private boolean isMyFriend = true;
    private long lastNearByMsgTime;//最后一条招呼消息时间
    private long lastSystemMsgTime;//最后一条系统消息时间
    private long lastVisiterMsgTime;//最后一条访客消息时间
    private long lastDongtaiMsgTime;//最后一条动态消息时间
    private int zhaohuNum;//新的招呼消息数
    private boolean isHaveSystemMsg;
    private boolean isHaveNearMsg;
    private List<ChatBean> ctlist = new ArrayList<>();//聊天消息

    private ChatAdapter mChatAdapter;
    public HandlerPlus mHandlerPlus = new HandlerPlus(this);

    private TextView chat_title;
    private ImageView chat_back,chat_img;
    private RelativeLayout chat_inform;
    private boolean is_forward;
    private String forward_msg_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_message);
        session = SessionCache.getInstance(this);
        initViews();
        initIntent();
    }

    private void initIntent() {
        is_forward = getIntent().getBooleanExtra("is_forward" , false);
        forward_msg_id = getIntent().getStringExtra("forward_msg_id");
        if (is_forward){
            chat_title.setVisibility(View.GONE);
            chat_inform.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AIMIApplication)getApplicationContext()).unReachNums = 0;
        loadData();
    }

    private void initViews() {
        chat_inform = (RelativeLayout) findViewById(R.id.chat_inform);
        chat_inform.setVisibility(View.VISIBLE);
        chat_img = (ImageView) findViewById(R.id.chat_img);
        chat_img.setVisibility(View.GONE);
        chat_title = (TextView) findViewById(R.id.chat_title);
        chat_back = (ImageView) findViewById(R.id.chat_back);
        chat_back.setOnClickListener(this);
        chat_msg = (RelativeLayout) findViewById(R.id.chat_msg);
        chat_msg.setOnClickListener(this);
        swipeToLoadLayout_msg = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout_msg);
        swipeToLoadLayout_msg.setRefreshEnabled(false);
        swipeToLoadLayout_msg.setLoadMoreEnabled(false);
        mRecyclerView = (RecyclerView) findViewById(R.id.swipe_target);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chat_num_news = (RelativeLayout) findViewById(R.id.chat_num_news);
        chat_news = (TextView) findViewById(R.id.chat_news);
        mRecyclerView.setAdapter(mChatAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chat_back:
                finish();
                break;
            case R.id.chat_msg:
                //清除未读消息数量
                SimpleMessageCache simpleCache = SimpleMessageCache.getInstance(ChatActivity.this);
                simpleCache.isCancleSystemMsg = true;
                simpleCache.save();
                //未读消息标志消失
                chat_num_news.setVisibility(View.GONE);
                chat_news.setText("0");
                startActivity(new Intent(ChatActivity.this, InformActivity.class));
                break;
        }
    }

    /**
     * item点击事件
     * @param psn
     */
    @Override
    public void onItemClick(int psn) {
        if (is_forward){
            //转发消息
            forwardMessage(forward_msg_id,psn);
            PromptUtils.showToast(ChatActivity.this,"转发成功");
            finish();
        }else {
            //进入聊天
            SingleChat.toSingleChat(ChatActivity.this, ctlist.get(psn).getName(), ctlist.get(psn).getUserId(), API.CHATTYPE_SINGLE, AIMIApplication.dealHeadImg(ctlist.get(psn).getImage()));
        }
    }

    public class HandlerPlus extends WeakHandler {

        public HandlerPlus(Object o) {
            super(o);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ChatActivity activity = (ChatActivity) getObjct();
            if (activity != null) {
                switch (msg.what) {
                    case 0:
                        activity.loadData();
                        break;
                    case 1:

                        break;
                }
            }
        }
    }

    public void loadData() {
        SimpleMessageCache simpleCache = SimpleMessageCache.getInstance(this);
        simpleCache.isCancleDongtaiMsg = false;
        simpleCache.save();
        //将最后一条招呼消息时间赋值为零
        lastNearByMsgTime = 0;
        lastDongtaiMsgTime = SystemMsgUtil.getLastDongtaiMsgTime(session.userId, this);
        //得到动态消息
        List<ReplyCacheBean> replyList = SystemMsgUtil.getReplyList(session.userId, this);
        //获取评论消息
        List<ReplyCacheBean> otherList = SystemMsgUtil.getOtherReplyList(session.userId, this);
        //评论 关注 点赞的消息不为0的时候 显示通知上的小红点
        if (replyList.size() > 0 || otherList.size() > 0) {
            chat_num_news.setVisibility(View.VISIBLE);
            chat_news.setText("" + replyList.size());
        }
        zhaohuNum = 0;
        ctlist = getList();
        //更新最近聊天缓存
        NearestMsgCache cache = NearestMsgCache.getCache(this);
        cache.setMsgList(ctlist);
        cache.save();
        //按照最后一条消息时间倒序排列
        Collections.sort(ctlist, new ChatMsgComparator());

        List<ChatBean> theTempList = new ArrayList<ChatBean>();//系统消息、招呼消息、访客消息、动态消息
        for (ChatBean bean : ctlist) {
            if (bean.getSuperType() == 0) {
                theTempList.add(bean);
            }
        }
        ctlist.clear();
        ctlist.addAll(theTempList);
        mChatAdapter = new ChatAdapter(ctlist,this);
        mChatAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mChatAdapter);
    }

    public List<ChatBean> getList() {
        msList = getMsgList();
        List<ChatBean> list = new ArrayList<ChatBean>();
        for (int i = 0; i < msList.size(); i++) {
            ChatBean bean = new ChatBean();
            MsgHistroyBean bean1 = msList.get(i);
            bean.setChatType(bean1.getChatType());
            bean.setImage(bean1.getHeadImg());

            bean.setName(bean1.getUserName());
            bean.setUserId(bean1.getUserId());
            bean.setText(bean1.getContent());
            bean.setType(bean1.getType());
            bean.setTime(bean1.getMsgTime());
            bean.setTheMsgTime(bean1.getTheMsgTime());
            bean.setNum(bean1.getUnreadCount());
            bean.setSystemMsg(bean1.isSystemMsg());
            bean.setMsgId(bean1.getMsgId());
            if (bean1.isSystemMsg()) {
                if (bean1.getZhaohuType() == 0) {
                    //普通系统消息

                } else {
                    //礼物系统消息判断好友关系
                    bean.setGiftbean(bean1.getGiftbean());
                    bean.setRecordId(bean1.getRecordId());

                }
                //系统消息全部跳过
                continue;
            }
            list.add(bean);
        }

        List<ChatBean> list2 = new ArrayList<ChatBean>();
        nearMsgList = new ArrayList<ChatBean>();
        AttendCache cache = AttendCache.getAttendCache(this);
        NormalMsgCache msgCache = NormalMsgCache.getCache(this);
        List<FollBean> frlist = cache.getFoll();
        List<SimpleMsgBean> msglist = msgCache.getNormalmsg();
        for (int i = 0; i < list.size(); i++) {
            ChatBean cbean = list.get(i);
            if (!isMyFriend) {
                if (!cbean.isSystemMsg()) {
                    nearMsgList.add(cbean);
                }
            } else {
                if (!cbean.isSystemMsg()) {
                    list2.add(cbean);
                }
            }
        }

        SessionCache session = SessionCache.getInstance(this);
        Log.i(TAG, "session.isOnlyFriend = " + session.isOnlyFriend);
        for (int k = 0; k < nearMsgList.size(); k++) {
            ChatBean bean9 = nearMsgList.get(k);
            if (k == 0) {
                lastNearByMsgTime = bean9.getTheMsgTime();
            }
            isHaveNearMsg = true;
            EMConversation nearCon = new EMConversation(bean9.getUserId());
            if (nearCon.getUnreadMsgCount() > 0) {//&&!session.isOnlyFriend
                zhaohuNum = zhaohuNum + 1;
            }
        }


        //处理消息免打扰
        MianDaRaoMsgCache mianCache = MianDaRaoMsgCache.getMsgCache(this);
        List<SimpleMsgBean> mianList = new ArrayList<SimpleMsgBean>();
        if (mianCache.getMsgList()!=null){
            for (int i = 0; i < mianCache.getMsgList().size(); i++) {
                if (mianCache.getMsgList().get(i).getMyUserId().equals(session.userId)) {
                    mianList.add(mianCache.getMsgList().get(i));
                }
            }
        }

        if (mianList.size() > 0) {
            for (ChatBean cBean : list2) {
                boolean isMianDaRao = false;
                for (int a = 0; a < mianList.size(); a++) {
                    if (cBean.getUserId().equals(mianList.get(a).getUserId())) {
                        isMianDaRao = true;
                        cBean.setMianDaRao(true);
                    }
                }
            }
        }

        return list2;
    }

    public List<MsgHistroyBean> getMsgList() {
        SessionCache session = SessionCache.getInstance(ChatActivity.this);
        List<EMConversation> conversationList = new ArrayList<EMConversation>();
        MsgHistroyBean historyBean;
        List<MsgHistroyBean> histroyList = new ArrayList<MsgHistroyBean>();
        conversationList.addAll(loadConversationsWithRecentChat());
        //循环每一个会话
        for (int i = 0; i < conversationList.size(); i++) {
            //获取每个会话的最后一条消息
            EMMessage lastMessage = null;
            if (conversationList.get(i).getMsgCount() != 0) {
                lastMessage = conversationList.get(i).getLastMessage();
            } else {
                break;
            }
            historyBean = new MsgHistroyBean();
            try {
                historyBean.setMsgId(lastMessage.getMsgId());
                int unreadCount = conversationList.get(i).getUnreadMsgCount();
                historyBean.setUnreadCount(unreadCount);
                historyBean.setMsgTime(AIMIApplication.getDateStr(new Date(lastMessage.getMsgTime())));
                //设置聊天类型
                if (lastMessage.getChatType().equals(EMMessage.ChatType.Chat)) {
                    historyBean.setChatType(0);
                } else {
                    historyBean.setChatType(1);
                }
                //设置消息内容类型
                if (lastMessage.getType().equals(EMMessage.Type.TXT)) {
                    historyBean.setType(0);
                    TextMessageBody txtBody = (TextMessageBody) lastMessage.getBody();
                    String type = lastMessage.getStringAttribute("type", "");
                    //对 吧台见 消息的处理
                    //当消息是 吧台见三个字时
                    if (type.equals("2") || type.equals("3")) {
                        //当扩展消息是吧台见得其他消息时  显示 [吧台见]
                        historyBean.setContent("吧台见");
                    } else {
                        //当扩展消息不是吧台见相关消息时  显示最后一条聊天消息
                        historyBean.setContent(txtBody.getMessage());
                    }
                } else if (lastMessage.getType().equals(EMMessage.Type.IMAGE)) {
                    historyBean.setType(1);
                    historyBean.setContent("[图片信息]");
                } else if (lastMessage.getType().equals(EMMessage.Type.VOICE)) {
                    historyBean.setType(2);
                    historyBean.setContent("[语音信息]");
                } else if (lastMessage.getType().equals(EMMessage.Type.VIDEO)) {
                    historyBean.setType(3);
                    historyBean.setContent("[视频信息]");
                }
                String theTransCode = lastMessage.getStringAttribute("transCode", "");
                if (theTransCode.equals("cc0007")) {
                    historyBean.setContent("[酒水礼物]");
                }
                historyBean.setTheMsgTime(lastMessage.getMsgTime());
                try {
                    boolean isSystemMsg = lastMessage.getBooleanAttribute("isSystemMsg", false);
                    if (isSystemMsg) {
                        //获取系统账号
                        systemId = lastMessage.getFrom();
                        //收到服务器系统消息
                        for (int s = 0; s < conversationList.get(i).getAllMessages().size(); s++) {
                            EMMessage eMsg = conversationList.get(i).getAllMessages().get(s);
                            Log.i(TAG, "EMMessage :" + eMsg.toString() + " , transCode = " + eMsg.getStringAttribute("transCode", "") + " , para = " + eMsg.getStringAttribute("para", ""));
                            //循环内加到列表中
                            MsgHistroyBean sysBean = new MsgHistroyBean();
                            sysBean.setFrom(eMsg.getFrom());
                            sysBean.setMsgId(eMsg.getMsgId());
                            SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
                            Date date = new Date(eMsg.getMsgTime());
                            sysBean.setMsgTime(sdf.format(date));
                            sysBean.setSystemMsg(true);
                            String transCode = eMsg.getStringAttribute("transCode");
//							Log.i("", "systemMsg transCode = "+transCode);
                            String para = eMsg.getStringAttribute("para");
                            Log.i(TAG, "systemMsg para = " + para);
                            if (transCode.equals("send10150")) {
                                //酒友关系变更通知
                                JSONObject paraObject = JSONObject.parseObject(para);
                                String userId = paraObject.getString("userId");
                                String userName = paraObject.getString("userName");
                                int type = paraObject.getIntValue("type");//状态	0 取消关注  1 关注
                                String userHeadImg = paraObject.getString("userHeadImg");
                                Date time = paraObject.getDate("time");
                                if (type == 1) {
                                    mHandlerPlus.sendEmptyMessage(0);
                                    ReplyCacheBean messageBean = new ReplyCacheBean();
                                    SessionCache messageSession = SessionCache.getInstance(this);
                                    messageBean.setMyUserId(messageSession.userId);
                                    messageBean.setUserHeadImg(userHeadImg);
                                    messageBean.setUserIdString(userId);
                                    messageBean.setUserName(userName);
                                    messageBean.setTypeInt(type);
                                    messageBean.setUserTime(time);
                                    messageBean.setUserId(userId);
                                    ReplyCache cache = ReplyCache.getCommentCache(this);
                                    boolean isHaveData = false;
                                    for (ReplyCacheBean rbean : cache.getReplyList()) {
                                        if (rbean.getMsgId() != null && rbean.getMsgId().equals(messageBean.getMsgId())) {
                                            isHaveData = true;
                                            break;
                                        }
                                    }
                                    if (!isHaveData) {
                                        cache.getReplyList().add(messageBean);
                                        cache.save();
                                    }
                                }
                                EMConversation commentCon = EMChatManager.getInstance().getConversationByType(systemId, EMConversation.EMConversationType.Chat);
                                //设为已读
                                commentCon.markMessageAsRead(eMsg.getMsgId());
                                //清除消息
                                commentCon.removeMessage(eMsg.getMsgId());
                                continue;
                            } else if (transCode.equals("send10231")) {
                                //我的动态通知(send10231)
                                mHandlerPlus.sendEmptyMessage(0);
                                JSONObject paraObject = JSONObject.parseObject(para);
                                String pUserId = paraObject.getString("pUserId");    //评论人
                                String pName = paraObject.getString("pName");    //名称
                                String pImg = paraObject.getString("pImg");        //头像
                                int type = paraObject.getIntValue("type");        //类型
                                String content = paraObject.getString("content");    //内容
                                String id = paraObject.getString("id");            //动态id
                                Date pTime = paraObject.getDate("pTime");        //时间
                                String pic_img = paraObject.getString("pic_img");    //动态图片
                                int pic_type = paraObject.getIntValue("pic_type");    //动态类型

                                ReplyCacheBean bean = new ReplyCacheBean();
                                bean.setMyUserId(session.userId);
                                bean.setUserId(pUserId);
                                bean.setName(pName);
                                bean.setImg(pImg);
                                bean.setType(type);
                                bean.setContent(content);
                                bean.setId(id);
                                bean.setTime(pTime);
                                bean.setPic_img(pic_img);
                                bean.setPic_type(pic_type);
                                bean.setMsgId(eMsg.getMsgId());
                                bean.setTheMsgTime(eMsg.getMsgTime());

                                ReplyCache cache = ReplyCache.getCommentCache(this);
                                boolean isHaveData = false;
                                for (ReplyCacheBean rbean : cache.getReplyList()) {
                                    if (rbean.getMsgId() != null && rbean.getMsgId().equals(bean.getMsgId())) {
                                        isHaveData = true;
                                        break;
                                    }
                                }
                                if (!isHaveData) {
                                    cache.getReplyList().add(bean);
                                    cache.save();
                                }

                                Log.i(TAG, "动态有新消息，放入缓存");

                                EMConversation commentCon = EMChatManager.getInstance().getConversationByType(systemId, EMConversation.EMConversationType.Chat);
                                //设为已读
                                commentCon.markMessageAsRead(eMsg.getMsgId());
                                //清除消息
                                commentCon.removeMessage(eMsg.getMsgId());

                                continue;
                            } else if (transCode.equals("send20001")) {
                                continue;
                            } else if (transCode.equals("send20002")) {
                                continue;
                            } else if (transCode.equals("cc0001")) {
                                continue;
                            } else if (transCode.equals("cc0006")) {
                                continue;
                            } else {
                                continue;
                            }
                        }

                        continue;
                    } else {
                        historyBean.setSystemMsg(false);
                    }

                } catch (Exception e) {
                    Log.i(TAG, "系统消息问题");
                    continue;
                }


                Log.i(TAG, "lastMessage.getBody = " + lastMessage.getBody().toString());
                //设置昵称和头像地址
                String userInfo = lastMessage.getStringAttribute("userInfo");
                JSONObject userObject = JSONObject.parseObject(userInfo);
                String userId = userObject.getString("userId");
                String userId2 = userObject.getString("userId2");
                String userName = userObject.getString("userName");
                String userName2 = userObject.getString("userName2");
                String headImg = userObject.getString("headImg");
                String headImg2 = userObject.getString("headImg2");
                Log.e("这个是单聊对方头像部分", headImg);
                try {
                    String transCode = lastMessage.getStringAttribute("transCode");
                    if (transCode.equals("cc0003")) {
                        String content = lastMessage.getStringAttribute("para");
                        JsonGiftMsg ext = JSON.parseObject(content, JsonGiftMsg.class);
                        historyBean.setContent("[礼物消息]");

                        if (ext.getMsg_type() == 1) {
                            if (lastMessage.direct == EMMessage.Direct.SEND) {
                                historyBean.setContent("[心意招呼]");
                            } else {
                                conversationList.get(i).removeMessage(lastMessage.getMsgId());
                                continue;
                            }
                        }
                    } else if (transCode.equals("cc0004")) {
                        String content = lastMessage.getStringAttribute("para");
                        JsonGiftMsg ext = JSON.parseObject(content, JsonGiftMsg.class);
                        if (lastMessage.direct == EMMessage.Direct.SEND) {
                            historyBean.setContent("您已接受对方的" + ext.getGift().getGiftName());
                        } else {
                            historyBean.setContent("对方已接受您的" + ext.getGift().getGiftName());
                        }
                    }
                } catch (Exception e) {
                    Log.i(TAG, "消息列表解析transCode失败");
                    e.printStackTrace();
                }

                if (historyBean.getChatType() == 0) {
                    if (session.userId.equals(userId)) {
                        historyBean.setUserId(userId2);
                        historyBean.setUserName(userName2);
                        historyBean.setHeadImg(headImg2);
                    } else {
                        historyBean.setUserId(userId);
                        historyBean.setUserName(userName);
                        historyBean.setHeadImg(headImg);
                        Log.e(TAG, headImg);
                    }
                } else {
                    //暂时去掉群聊消息
                    continue;
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //判断是否删除过会话并且未有新消息
            boolean ishaveData = false;
            for (int j = 0; j < cancleMsgList.size(); j++) {
                if (cancleMsgList.get(j).getUserId().equals(historyBean.getUserId()) && cancleMsgList.get(j).getNum() >= historyBean.getUnreadCount()) {
                    ishaveData = true;
                }
            }
            if (!ishaveData) {
                histroyList.add(historyBean);
            }
        }

        return histroyList;
    }

    /**
     * 从环信数据库加载所有的聊天信息
     *
     * @return
     */
    private List<EMConversation> loadConversationsWithRecentChat() {
        Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
        /**
         * 如果在排序过程中有新消息收到，lastMsgTime会发生变化
         * 影响排序过程，Collection.sort会产生异常
         * 保证Conversation在Sort过程中最后一条消息的时间不变
         * 避免并发问题
         */
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    EMMessage lastMessage = conversation.getLastMessage();
                    String arrive = "";
                    try {
                        arrive = lastMessage.getStringAttribute("arrive");
                    } catch (EaseMobException e) {
                        e.printStackTrace();
                    }
                    if(conversation.getType() == EMConversation.EMConversationType.Chat && !arrive.equals("end")){
                        sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                    }
                }
            }
        }
        try {
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first == con2.first) {
                    return 0;
                } else if (con2.first > con1.first) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    protected void forwardMessage(String forward_msg_id, int psn) {
        final EMMessage forward_msg = EMChatManager.getInstance().getMessage(forward_msg_id);
        EMMessage.Type type = forward_msg.getType();
        switch (type) {
            case TXT:
                // 获取消息内容，发送消息
                String content = ((TextMessageBody) forward_msg.getBody()).getMessage();
                sendTxtMessage(content, psn);
                break;
            case IMAGE:
                // 发送图片
                String filePath = ((ImageMessageBody) forward_msg.getBody()).getLocalUrl();
                if (filePath != null) {
                    File file = new File(filePath);
                    if (!file.exists()) {
                        // 不存在大图发送缩略图
                        filePath = ImageUtils.getThumbnailImagePath(filePath);
                    }
                    sendPicture(filePath, psn);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 发送文字
     *
     * @param content
     */
    private void sendTxtMessage(String content , int position) {
        if (TextUtils.isEmpty(content)) {
            PromptUtils.showToast(ChatActivity.this, "请输入内容");
        } else {
            EMMessage sendMessage = EMMessage.createSendMessage(EMMessage.Type.TXT);
            // 设置为群聊
            sendMessage.setChatType(EMMessage.ChatType.Chat);
            TextMessageBody txtBody = new TextMessageBody(content);
            // 设置消息body
            sendMessage.addBody(txtBody);
            // 设置要发给谁,用户username或者群聊groupid
            sendMessage.setReceipt(ctlist.get(position).getUserId());
            String chatInfo = dealChatInfo(ctlist.get(position));
            //扩展消息
            sendMessage.setAttribute("userInfo", chatInfo);
            sendMessage.setAttribute("transCode", "cc0001");
            sendMessage.setAttribute("em_ignore_notification",false);
            sendMessage.setAttribute("isSystemMsg",false);
            EMConversation conversation = EMChatManager.getInstance().getConversationByType(ctlist.get(position).getUserId(), EMConversation.EMConversationType.Chat);
            conversation.addMessage(sendMessage);
            addNormalMsg(ctlist.get(position).getUserId());
        }
    }

    /**
     * 发送图片
     *
     * @param filePath
     */
    private void sendPicture(final String filePath , int position) {
        if (filePath != null){
            final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.IMAGE);
            message.setChatType(EMMessage.ChatType.Chat);
            message.setReceipt(ctlist.get(position).getUserId());
            ImageMessageBody body = new ImageMessageBody(new File(filePath));
            // 默认超过100k的图片会压缩后发给对方，可以设置成发送原图
            body.setSendOriginalImage(true);
            message.addBody(body);
            String chatInfo = dealChatInfo(ctlist.get(position));
            //消息扩展
            message.setAttribute("userInfo", chatInfo);
            message.setAttribute("transCode", "cc0001");
            EMConversation conversation = EMChatManager.getInstance().getConversationByType(ctlist.get(position).getUserId(), EMConversation.EMConversationType.Chat);
            conversation.addMessage(message);
            addNormalMsg(ctlist.get(position).getUserId());
        }
    }

    /**
     * 用户聊天信息
     *
     * @return
     */
    private String dealChatInfo(ChatBean chatBean) {
        JsonUserInfo jsonUserInfo = new JsonUserInfo();
        jsonUserInfo.setUserId(session.userId);
        jsonUserInfo.setUserName(session.nickname);
        jsonUserInfo.setHeadImg(session.headimgUrl);
        jsonUserInfo.setUserId2(chatBean.getUserId());
        jsonUserInfo.setUserName2(chatBean.getName());
        jsonUserInfo.setHeadImg2(chatBean.getImage());
        return JSON.toJSONString(jsonUserInfo);
    }

    /***
     *放入临时消息缓存
     */
    public void addNormalMsg(String userid) {
        //临时消息缓存
        Log.i("", "放入临时消息缓存");
        NormalMsgCache cache = NormalMsgCache.getCache(mActivity);
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

}
