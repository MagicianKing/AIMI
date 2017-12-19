package com.yeying.aimi.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.FileMessageBody;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.DateUtils;
import com.easemob.util.EMLog;
import com.yeying.aimi.API;
import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.AIMIApplication;
import com.yeying.aimi.huanxin.AlertDialog;
import com.yeying.aimi.huanxin.ImageCache;
import com.yeying.aimi.huanxin.ImageUtils;
import com.yeying.aimi.huanxin.LoadImageTask;
import com.yeying.aimi.huanxin.ShowBigImage;
import com.yeying.aimi.huanxin.SmileUtils;
import com.yeying.aimi.mode.bar_info.Activity_BaPing;
import com.yeying.aimi.mode.bar_info.ContextMenu;
import com.yeying.aimi.mode.otherdetails.MineHomepage;
import com.yeying.aimi.mode.otherdetails.OtherHomepage;
import com.yeying.aimi.mode.signlechat.SingleChat;
import com.yeying.aimi.protoco.DefaultTask;
import com.yeying.aimi.protoco.IProtocol;
import com.yeying.aimi.protocol.impl.P13306;
import com.yeying.aimi.protocol.impl.P13307;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.transformation.CircleTransform;
import com.yeying.aimi.utils.PromptUtils;
import com.yeying.aimi.utils.WeakHandler;

import java.io.File;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;




/**
 * Created by tanchengkeji on 2017/9/11.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdapter_VH> {

    private static final String TAG = "MessageAdapter";

    public static final String IMAGE_DIR = "chat/image/";

    private Map<String, Timer> timers = new Hashtable<String, Timer>();

    private Context mContext;
    private Activity mActivity;
    private String toChatName;
    private LayoutInflater inflater;
    private String myheadurl;
    private String chatheadurl;

    private String mBarScreenId;

    private static final int HANDLER_MESSAGE_REFRESH_LIST = 0;
    private static final int HANDLER_MESSAGE_SELECT_LAST = 1;
    private static final int HANDLER_MESSAGE_SEEK_TO = 2;

    private EMConversation mEMConversation;
    private EMMessage[] messages = null;
    private RecyclerView mRecyclerView;

    private static final int MESSAGE_TYPE_RECV_TXT = 0;
    private static final int MESSAGE_TYPE_SENT_TXT = 1;
    private static final int MESSAGE_TYPE_SENT_IMAGE = 2;
    private static final int MESSAGE_TYPE_RECV_IMAGE = 5;

    private int view_id;
    private int type_id;

    private SessionCache session;

    private MyHandler handler;

    public MessageAdapter(Context context, String toChatName, RecyclerView recyclerView, String myheadurl, String chatheadurl) {
        mContext = context;
        mActivity = (Activity) context;
        this.toChatName = toChatName;
        inflater = LayoutInflater.from(context);
        this.myheadurl = myheadurl;
        this.chatheadurl = chatheadurl;
        mRecyclerView = recyclerView;
        session = SessionCache.getInstance(context);
        handler = new MyHandler(this);
        this.mEMConversation = EMChatManager.getInstance().getConversation(toChatName);
        messages = mEMConversation.getAllMessages().toArray(new EMMessage[mEMConversation.getAllMessages().size()]);
        Log.e(TAG, "MessageAdapter: " + messages.length);
    }

    @Override
    public MessageAdapter_VH onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case MESSAGE_TYPE_SENT_TXT:
                //文字消息 发送方
                view_id = R.layout.row_sent_message;
                type_id = MESSAGE_TYPE_SENT_TXT;
                break;
            case MESSAGE_TYPE_RECV_TXT:
                //文字消息 接收方
                view_id = R.layout.row_received_message;
                type_id = MESSAGE_TYPE_RECV_TXT;
                break;
            case MESSAGE_TYPE_SENT_IMAGE:
                //图片消息 发送方
                view_id = R.layout.row_sent_picture;
                type_id = MESSAGE_TYPE_SENT_IMAGE;
                break;
            case MESSAGE_TYPE_RECV_IMAGE:
                //图片消息 接收方
                view_id = R.layout.row_received_picture;
                type_id = MESSAGE_TYPE_RECV_IMAGE;
                break;
        }
        View itemView = inflater.inflate(view_id, parent, false);
        return new MessageAdapter_VH(itemView);
    }

    @Override
    public void onBindViewHolder(MessageAdapter_VH holder, final int position) {
        final EMMessage message = messages[position];
        EMMessage.ChatType chatType = message.getChatType();

        // 群聊时，显示接收的消息的发送人的名称
        if ((chatType == EMMessage.ChatType.GroupChat || chatType == EMMessage.ChatType.ChatRoom) && message.direct == EMMessage.Direct.RECEIVE) {
            //demo里使用username代码nick
            //UserUtils.setUserNick(message.getFrom(), holder.tv_usernick);
        }
        //单聊是聊天背景有改变
        if (message.getChatType() == EMMessage.ChatType.Chat){
            if (message.direct == EMMessage.Direct.SEND){
                holder.tv.setBackgroundResource(R.drawable.send);
                holder.tv.setTextColor(mContext.getResources().getColor(R.color.black));
            }else {
                holder.tv.setBackgroundResource(R.drawable.accept);
                holder.tv.setTextColor(mContext.getResources().getColor(R.color.black));
            }
        }else {
            holder.tv.setBackgroundResource(R.drawable.baping_edit);
            holder.tv.setTextColor(Color.WHITE);
        }
        /*if (message.getChatType() != EMMessage.ChatType.Chat){
            if (message.direct == EMMessage.Direct.SEND){
                message.direct = EMMessage.Direct.RECEIVE;
                EMChatManager.getInstance().updateMessageBody(message);
            }
        }*/
        //设置用户头像 群聊还是单聊都需要头像 还有名字
        setUserAvatar(message, holder.iv_avatar, holder.txt_name);
        //根据不同的消息类型显示不同的控件 及 做相应的操作
        switch (message.getType()) {
            // 根据消息type显示item
            case IMAGE: // 图片
                handleImageMessage(message, holder, position);
                break;
            case TXT: // 文本
                handleTextMessage(message, holder, position);
                break;
        }
        //关于 重发消息 的处理
        if (message.direct == EMMessage.Direct.SEND) {//发送方
            // 重发按钮点击事件
            holder.staus_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 显示重发消息的自定义alertdialog
                    Intent intent = new Intent(mContext, AlertDialog.class);
                    intent.putExtra("msg", ((Activity)mContext).getString(R.string.confirm_resend));
                    intent.putExtra("title", ((Activity)mContext).getString(R.string.re_message));
                    intent.putExtra("method", AlertDialog.reSendMsg);
                    intent.putExtra("cancel", true);
                    intent.putExtra("position", position);
                    if (message.getType() == EMMessage.Type.TXT)
                        ((Activity)mContext).startActivityForResult(intent, API.REQUEST_CODE_TEXT);
                    else if (message.getType() == EMMessage.Type.VOICE)
                        ((Activity)mContext).startActivityForResult(intent, API.REQUEST_CODE_VOICE);
                    else if (message.getType() == EMMessage.Type.IMAGE)
                        ((Activity)mContext).startActivityForResult(intent, API.REQUEST_CODE_PICTURE);
                    else if (message.getType() == EMMessage.Type.LOCATION)
                        ((Activity)mContext).startActivityForResult(intent, API.REQUEST_CODE_LOCATION);
                    else if (message.getType() == EMMessage.Type.FILE)
                        ((Activity)mContext).startActivityForResult(intent, API.REQUEST_CODE_FILE);
                    else if (message.getType() == EMMessage.Type.VIDEO)
                        ((Activity)mContext).startActivityForResult(intent, API.REQUEST_CODE_VIDEO);

                }
            });
        } else {

        }
        if (position == 0) {
            holder.timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
            holder.timestamp.setVisibility(View.VISIBLE);
        } else {
            String type = message.getStringAttribute("type", "");
            if (type.equals("1") || type.equals("3") || type.equals("2")) {
                //当发送吧台见的时候不用显示时间戳
                holder.timestamp.setVisibility(View.GONE);
            } else {
                // 两条消息时间离得如果稍长，显示时间
                EMMessage prevMessage = messages[position - 1];
                if (prevMessage != null && DateUtils.isCloseEnough(message.getMsgTime(), prevMessage.getMsgTime())) {
                    holder.timestamp.setVisibility(View.GONE);
                } else {
                    holder.timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
                    holder.timestamp.setVisibility(View.VISIBLE);
                }
            }

        }
    }

    @Override
    public int getItemViewType(int position) {
        EMMessage message = messages[position];
        switch (message.getType()) {
            case IMAGE:
                if (message.getChatType() != EMMessage.ChatType.Chat){
                    return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_IMAGE : MESSAGE_TYPE_RECV_IMAGE;
                }else {
                    return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_IMAGE : MESSAGE_TYPE_SENT_IMAGE;
                }
            default:
                if(message.getChatType() != EMMessage.ChatType.Chat){
                    return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_TXT : MESSAGE_TYPE_RECV_TXT;
                }else {
                    return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_TXT : MESSAGE_TYPE_SENT_TXT;
                }


        }
    }

    public EMMessage getItem(int position) {
        if (messages != null && position < messages.length) {
            return messages[position];
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return messages == null ? 0 : messages.length;
    }

    class MessageAdapter_VH extends RecyclerView.ViewHolder {

        Button mBtnDelete;
        ImageView iv;
        TextView tv;
        TextView content;
        ProgressBar pb;
        ImageView staus_iv;
        ImageView iv_avatar;
        TextView tv_usernick;
        TextView iv_sendMessage;
        // 显示已读回执状态
        TextView tv_ack;
        // 显示送达回执状态
        TextView tv_delivered;
        RelativeLayout rl_tongzhi;
        TextView tv_tongzhi;
        //吧台见相关
        TextView invation_send;
        RelativeLayout bataijian;
        TextView invation_receive_yes;
        LinearLayout invation_receive;
        LinearLayout ll_jietouanyu;
        TextView jietouanyu;
        LinearLayout batai_tongzhi;
        //时间戳
        TextView timestamp;
        TextView txt_name;
        //霸屏flag
        ImageView baping_flag;
        RelativeLayout row_pic;


        public MessageAdapter_VH(View itemView) {
            super(itemView);
            switch (type_id) {
                case MESSAGE_TYPE_SENT_TXT:
                case MESSAGE_TYPE_RECV_TXT:
                    findTextId(itemView);
                    break;
                case MESSAGE_TYPE_SENT_IMAGE:
                case MESSAGE_TYPE_RECV_IMAGE:
                    findImgId(itemView);
                    break;
            }
        }

        private void findImgId(View convertView) {
            try {
                row_pic = (RelativeLayout) convertView.findViewById(R.id.row_pic);
                baping_flag = (ImageView) convertView.findViewById(R.id.iv_sendBP_flag);
                txt_name = (TextView) convertView.findViewById(R.id.img_name);
                timestamp = (TextView) convertView.findViewById(R.id.timestamp);
                iv = ((ImageView) convertView.findViewById(R.id.iv_sendPicture));
                iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
                mBtnDelete = (Button) convertView.findViewById(R.id.btn_delete);
                tv = (TextView) convertView.findViewById(R.id.percentage);
                pb = (ProgressBar) convertView.findViewById(R.id.progressBar);
                staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
                tv_usernick = (TextView) convertView.findViewById(R.id.tv_userid);
                iv_sendMessage = (TextView) convertView.findViewById(R.id.iv_sendMessage);
            } catch (Exception e) {
            }
        }

        private void findTextId(View convertView) {
            try {
                txt_name = (TextView) convertView.findViewById(R.id.txt_name);
                timestamp = (TextView) convertView.findViewById(R.id.timestamp);
                tv_tongzhi = (TextView) convertView.findViewById(R.id.tv_tongzhi);
                rl_tongzhi = (RelativeLayout) convertView.findViewById(R.id.rl_tongzhi);
                pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
                staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
                iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
                // 这里是文字内容
                tv = (TextView) convertView.findViewById(R.id.tv_chatcontent);
                tv_usernick = (TextView) convertView.findViewById(R.id.tv_userid);

                //吧台见相关控件
                bataijian = (RelativeLayout) convertView.findViewById(R.id.bataijian);
                invation_send = (TextView) convertView.findViewById(R.id.invation_send);
                invation_receive = (LinearLayout) convertView.findViewById(R.id.invation_receive);
                invation_receive_yes = (TextView) convertView.findViewById(R.id.invation_receive_yes);
                batai_tongzhi = (LinearLayout) convertView.findViewById(R.id.batai_tongzhi);
                //吧台见邀请暗语
                ll_jietouanyu = (LinearLayout) convertView.findViewById(R.id.ll_jietouanyu);
                jietouanyu = (TextView) convertView.findViewById(R.id.jietouanyu);
            } catch (Exception e) {
            }
        }
    }

    /**
     * 显示用户头像
     *
     * @param message
     * @param imageView
     */
    private void setUserAvatar(final EMMessage message, ImageView imageView, TextView nameView) {
        nameView.setVisibility(View.VISIBLE);
        nameView.setText("whathhhh");
        if (message.direct == EMMessage.Direct.SEND) {//发送方
            //名字
            nameView.setText(session.nickname);
            //名字颜色
            setNameColor(nameView,session.sex);
            //显示自己头像
            Log.e(TAG, "setUserAvatar: "+AIMIApplication.dealHeadImg(session.headimgUrl));
            Log.e(TAG, "setUserAvatar: "+session.headimgUrl);
            Glide.with(mContext)
                    .load(AIMIApplication.dealHeadImg(session.headimgUrl))
                    .placeholder(R.drawable.default_icon)
                    .error(R.drawable.default_icon)
                    .transform(new CircleTransform(mContext,session.headimgUrl))
                    .into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MineHomepage.toOtherHomePage(mContext,session.userId,TextUtils.isEmpty(session.locationX) ? 0.0 : Double.parseDouble(session.locationX),TextUtils.isEmpty(session.locationY) ? 0.0 : Double.parseDouble(session.locationY),true);
                }
            });
        } else {//接收方
            String userInfo = message.getStringAttribute("userInfo", "");
            JSONObject userObject = JSONObject.parseObject(userInfo);
            String headUrl = userObject.getString("headImg");
            String userName = userObject.getString("userName");
            String gender = userObject.getString("gender");
            final String userId = userObject.getString("userId");
            Log.e("hhhhhh", "setUserAvatar: 头像----->" + headUrl);
            imageView.setVisibility(View.VISIBLE);
            if (message.getChatType() == EMMessage.ChatType.Chat){
                if (!TextUtils.isEmpty(chatheadurl)) {
                    Glide.with(mContext).load(AIMIApplication.dealHeadImg(chatheadurl)).placeholder(R.drawable.default_icon).error(R.drawable.default_icon).transform(new CircleTransform(mContext,chatheadurl)).into(imageView);
                    Log.e(TAG, "setUserAvatar: 拼接后头像----> " + AIMIApplication.dealHeadImg(headUrl));
                }
            }else {
                if (!TextUtils.isEmpty(headUrl)) {
                    Glide.with(mContext).load(AIMIApplication.dealHeadImg(headUrl)).placeholder(R.drawable.default_icon).error(R.drawable.default_icon).transform(new CircleTransform(mContext , headUrl)).into(imageView);
                    Log.e(TAG, "setUserAvatar: 拼接后头像----> " + AIMIApplication.dealHeadImg(headUrl));
                }
            }
            nameView.setText(userName);
            setNameColor(nameView,gender);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OtherHomepage.toOtherHomePage(mContext,userId,TextUtils.isEmpty(session.locationX) ? 0.0 : Double.parseDouble(session.locationX),TextUtils.isEmpty(session.locationY) ? 0.0 : Double.parseDouble(session.locationY),false);
                }
            });
        }
        if (message.getChatType() == EMMessage.ChatType.Chat){
            nameView.setVisibility(View.GONE);
        }
    }

    /**
     * 文本消息
     * 处理接收或者是发送的文本消息
     *
     * @param message
     * @param holder
     * @param position
     */
    @SuppressWarnings("deprecation")
    private void handleTextMessage(final EMMessage message, MessageAdapter_VH holder, final int position) {
        TextMessageBody txtBody = (TextMessageBody) message.getBody();
        Spannable span = SmileUtils.getSmiledText(mContext, txtBody.getMessage());
        final SessionCache session = SessionCache.getInstance(mContext);
        // 设置内容 设置文本内容和头像
        holder.tv.setVisibility(View.VISIBLE);
        holder.tv.setText(span, TextView.BufferType.SPANNABLE);
        holder.iv_avatar.setVisibility(View.VISIBLE);
        // 设置长按事件监听
        int tempType = 0;
        if (message.direct == EMMessage.Direct.SEND) {
            tempType = 0;
        } else {
            tempType = 1;
        }
        //聊天类型
        int chatType = 0;
        if (message.getChatType() == EMMessage.ChatType.Chat){
            //单聊
            chatType = 0;
        }else {
            chatType = 1;
        }

        final int msgType = tempType;
        final long msgTime = message.getMsgTime();
        final int finalChatType = chatType;
        holder.tv.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                int[] localData = new int[2];
                v.getLocationOnScreen(localData);
                Log.i("location_item", "position[0]=" + localData[0] + ",	position[1]=" + localData[1]);

                ((Activity) mContext).startActivityForResult(
                        (new Intent(mContext, ContextMenu.class))
                                .putExtra("chatType", finalChatType)
                                .putExtra("msgTime", msgTime).putExtra("msgType", msgType)
                                .putExtra("localData", localData).putExtra("position", position)
                                .putExtra("type", EMMessage.Type.TXT.ordinal()), API.REQUEST_CODE_CONTEXT_MENU);
                return true;
            }
        });
        try {

            //获得扩展消息transCode
            String transCode = message.getStringAttribute("transCode", "");
            holder.bataijian.setVisibility(View.GONE);
            holder.invation_receive.setVisibility(View.GONE);
            holder.invation_send.setVisibility(View.GONE);
            holder.invation_receive_yes.setVisibility(View.GONE);
            holder.rl_tongzhi.setVisibility(View.GONE);
            holder.tv_tongzhi.setVisibility(View.GONE);
            //根据扩展消息进行对布局的处理
            if (transCode.equals("cc0002")) {
                //cc0002代表红包
            } else {

                //holder.rl_msg_hb.setVisibility(View.GONE);
                holder.bataijian.setVisibility(View.GONE);
                boolean isRevoke = message.getBooleanAttribute(API.EASE_ATTR_REVOKE, false);
                boolean isFriendFail = message.getBooleanAttribute(API.EASE_ATTR_FRIEND_FAIL, false);
                boolean isBlackFail = message.getBooleanAttribute(API.EASE_ATTR_BLACK_FAIL, false);
                final String type = message.getStringAttribute("type", "");
                boolean isclick = message.getBooleanAttribute("isClick", false);
                boolean isClick_invation = message.getBooleanAttribute("isClick_invation", false);

                //对方同意吧台见
                holder.batai_tongzhi.setVisibility(View.GONE);
                //头像
                holder.iv_avatar.setVisibility(View.GONE);
                //通知
                holder.rl_tongzhi.setVisibility(View.GONE);
                holder.tv_tongzhi.setVisibility(View.GONE);
                //文字内容
                holder.tv.setVisibility(View.GONE);
                //type=3
                holder.ll_jietouanyu.setVisibility(View.GONE);
                holder.jietouanyu.setVisibility(View.GONE);
                //type=2
                holder.bataijian.setVisibility(View.GONE);
                holder.invation_send.setVisibility(View.GONE);
                holder.invation_receive_yes.setVisibility(View.GONE);
                holder.invation_receive.setVisibility(View.GONE);

                //对方同意吧台见
                if (type.equals("3")) {
                    if (message.getChatType() == EMMessage.ChatType.Chat){
                        if (message.direct == EMMessage.Direct.SEND) {
                            //发送方
                            Log.e(TAG, "同意吧台见，发送方显示接头暗语");
                            holder.ll_jietouanyu.setVisibility(View.VISIBLE);
                            holder.jietouanyu.setVisibility(View.VISIBLE);
                            holder.rl_tongzhi.setVisibility(View.GONE);
                            holder.tv_tongzhi.setVisibility(View.GONE);
                        } else {
                            //接收方

                            holder.ll_jietouanyu.setVisibility(View.VISIBLE);
                            holder.jietouanyu.setVisibility(View.VISIBLE);
                            holder.rl_tongzhi.setVisibility(View.VISIBLE);
                            holder.tv_tongzhi.setVisibility(View.VISIBLE);
                            holder.tv_tongzhi.setText("对方同意您的邀请");
                        }

                        holder.jietouanyu.setClickable(true);
                        holder.jietouanyu.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(mContext, ContextMenu.class);
                                intent.putExtra("jietouanyu", true);
                                intent.putExtra("type", EMMessage.Type.TXT.ordinal());
                                intent.putExtra("fateCardId", message.getStringAttribute("fateCardId", ""));
                                intent.putExtra("barId", message.getStringAttribute("barId", ""));
                                ((Activity) mContext).startActivityForResult(intent, API.REQUEST_CODE_JIETOUANYU);
                            }
                        });
                    }
                } else if (type.equals("2")) {
                    if (message.getChatType() == EMMessage.ChatType.Chat){
                        if (message.direct == EMMessage.Direct.SEND) {
                            //发送方显示文字描述
                            holder.iv_avatar.setVisibility(View.VISIBLE);
                            holder.bataijian.setVisibility(View.VISIBLE);
                            holder.invation_send.setVisibility(View.VISIBLE);
                            holder.staus_iv.setVisibility(View.GONE);
                        } else {
                            //接收方显示同意按钮
                            holder.iv_avatar.setVisibility(View.VISIBLE);
                            holder.bataijian.setVisibility(View.VISIBLE);
                            holder.invation_send.setVisibility(View.GONE);
                            holder.invation_receive.setVisibility(View.VISIBLE);
                            holder.invation_receive_yes.setVisibility(View.VISIBLE);
                            holder.invation_receive_yes.setClickable(true);
                            holder.invation_receive_yes.setBackgroundResource(R.drawable.bottom_round);
                        }
                        if (isClick_invation) {
                            holder.iv_avatar.setVisibility(View.VISIBLE);
                            holder.bataijian.setVisibility(View.VISIBLE);
                            holder.invation_send.setVisibility(View.GONE);
                            holder.invation_receive.setVisibility(View.VISIBLE);
                            holder.invation_receive_yes.setVisibility(View.VISIBLE);
                            holder.invation_receive_yes.setBackgroundResource(R.drawable.bottom_round_grey);
                            holder.invation_receive_yes.setClickable(false);
                        } else {
                            holder.invation_receive_yes.setClickable(true);
                            holder.invation_receive_yes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    P13307 p13307 = new P13307();
                                    p13307.req.sessionId = session.sessionId;
                                    p13307.req.barId = message.getStringAttribute("barId", "");
                                    p13307.req.userId = session.userId;
                                    p13307.req.fateCardId = message.getStringAttribute("fateCardId", "");
                                    Log.e(TAG, "type====" + type + "--------fateCardId==" + message.getStringAttribute("fateCardId", "") + "------barId" + message.getStringAttribute("barId", ""));
                                    p13307.req.locationX = TextUtils.isEmpty(session.locationX) ? 0.0 : Double.parseDouble(session.locationX);
                                    p13307.req.locationY = TextUtils.isEmpty(session.locationY) ? 0.0 : Double.parseDouble(session.locationY);
                                    new Invation_Agree(message).execute(mContext.getApplicationContext(), p13307);
                                }
                            });
                        }
                    }
                } else if (type.equals("1")) {
                    if (message.getChatType() == EMMessage.ChatType.Chat){
                        holder.batai_tongzhi.setBackgroundResource(R.drawable.round_yellow_bg);
                        holder.batai_tongzhi.setClickable(true);
                        if (message.direct == EMMessage.Direct.SEND) {
                            holder.staus_iv.setVisibility(View.GONE);
                            holder.batai_tongzhi.setVisibility(View.VISIBLE);
                        } else {
                            //接收方不做任何处理
                        }
                        if (isclick) {
                            holder.batai_tongzhi.setBackgroundResource(R.drawable.grey_round_bg);
                            holder.batai_tongzhi.setClickable(false);
                        } else {
                            holder.batai_tongzhi.setClickable(true);
                            holder.batai_tongzhi.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    P13306 p13306 = new P13306();
                                    p13306.req.sessionId = session.sessionId;
                                    p13306.req.barId = message.getStringAttribute("barId", "");
                                    p13306.req.userId = session.userId;
                                    p13306.req.fateCardId = message.getStringAttribute("fateCardId", "");
                                    Log.e(TAG, "type====" + type + "--------fateCardId==" + message.getStringAttribute("fateCardId", ""));
                                    p13306.req.locationX = TextUtils.isEmpty(session.locationX) ? 0.0 : Double.parseDouble(session.locationX);
                                    p13306.req.locationY = TextUtils.isEmpty(session.locationY) ? 0.0 : Double.parseDouble(session.locationY);
                                    new BaTaiShowClickTask(message).execute(mContext.getApplicationContext(), p13306);
                                }
                            });
                        }
                    }
                } else if (isRevoke) {//撤回

                    holder.batai_tongzhi.setVisibility(View.GONE);
                    //头像
                    holder.iv_avatar.setVisibility(View.GONE);
                    //通知
                    holder.rl_tongzhi.setVisibility(View.VISIBLE);
                    holder.tv_tongzhi.setVisibility(View.VISIBLE);
                    //文字内容
                    holder.tv.setVisibility(View.GONE);
                    //type=3
                    holder.ll_jietouanyu.setVisibility(View.GONE);
                    holder.jietouanyu.setVisibility(View.GONE);
                    //type=2
                    holder.bataijian.setVisibility(View.GONE);
                    holder.invation_send.setVisibility(View.GONE);
                    holder.invation_receive_yes.setVisibility(View.GONE);
                    holder.invation_receive.setVisibility(View.GONE);

                    if (message.direct == EMMessage.Direct.SEND) {
                        holder.txt_name.setVisibility(View.GONE);
                        holder.tv_tongzhi.setText("您撤回了一条消息");
                    } else {
                        holder.txt_name.setVisibility(View.GONE);
                        holder.ll_jietouanyu.setVisibility(View.GONE);
                        String userInfo = message.getStringAttribute("userInfo", "");
                        JSONObject userObject = JSONObject.parseObject(userInfo);
                        String userName = userObject.getString("userName");
                        if (message.getChatType() == EMMessage.ChatType.Chat){
                            holder.tv_tongzhi.setText("对方撤回了一条消息");
                        }else {
                            String str = TextUtils.isEmpty(userName) ? "对方" : userName;
                            holder.tv_tongzhi.setText(str + "  撤回了一条消息");
                        }
                    }
                } else if (isFriendFail) {
                    holder.iv_avatar.setVisibility(View.GONE);
                    holder.rl_tongzhi.setVisibility(View.VISIBLE);
                    holder.tv.setVisibility(View.GONE);
                    holder.tv_tongzhi.setText("对方设置不接受陌生人消息");
                } else if (isBlackFail) {
                    holder.iv_avatar.setVisibility(View.GONE);
                    holder.rl_tongzhi.setVisibility(View.VISIBLE);
                    holder.tv.setVisibility(View.GONE);
                    holder.tv_tongzhi.setText("对方已将您加入黑名单");
                } else {
                    holder.batai_tongzhi.setVisibility(View.GONE);
                    //头像
                    holder.iv_avatar.setVisibility(View.VISIBLE);
                    //通知
                    holder.rl_tongzhi.setVisibility(View.GONE);
                    holder.tv_tongzhi.setVisibility(View.GONE);
                    //文字内容
                    holder.tv.setVisibility(View.VISIBLE);
                    //type=3
                    holder.ll_jietouanyu.setVisibility(View.GONE);
                    holder.jietouanyu.setVisibility(View.GONE);
                    //type=2
                    holder.bataijian.setVisibility(View.GONE);
                    holder.invation_send.setVisibility(View.GONE);
                    holder.invation_receive_yes.setVisibility(View.GONE);
                    holder.invation_receive.setVisibility(View.GONE);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("", "解析消息中的扩展   出现异常");
        }

        if (message.direct == EMMessage.Direct.SEND) {
            switch (message.status) {
                case SUCCESS: // 发送成功
                    if (holder.pb != null) {
                        holder.pb.setVisibility(View.GONE);
                    }
                    if (holder.staus_iv != null) {
                        holder.staus_iv.setVisibility(View.GONE);
                    }
                    break;
                case FAIL: // 发送失败
                    holder.pb.setVisibility(View.GONE);
                    holder.staus_iv.setVisibility(View.VISIBLE);
                    break;
                case INPROGRESS: // 发送中
                    holder.pb.setVisibility(View.VISIBLE);
                    holder.staus_iv.setVisibility(View.GONE);
                    break;
                default:
                    // 发送消息
                    sendMsgInBackground(message, holder);
            }
        } else {
            //是受到的消息
            if (holder.pb != null) {
                holder.pb.setVisibility(View.GONE);
            }
            if (holder.staus_iv != null) {
                holder.staus_iv.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 图片消息
     *
     * @param message
     * @param holder
     * @param position
     */
    private void handleImageMessage(final EMMessage message, final MessageAdapter_VH holder, final int position) {
        if (message.getChatType() == EMMessage.ChatType.Chat){
            holder.tv.setBackgroundResource(R.color.touming);
            holder.row_pic.setBackgroundResource(R.drawable.round_white);
        }else {
            holder.tv.setBackgroundResource(R.color.set_item);
            holder.row_pic.setBackgroundResource(R.drawable.baping_edit);
        }
        holder.pb.setTag(position);
        int tempType = 0;
        if (message.direct == EMMessage.Direct.SEND) {
            tempType = 0;
        } else {
            tempType = 1;
        }
        boolean tempBoolean = false;
        /*if (message.getChatType() == EMMessage.ChatType.Chat) {
            tempBoolean = true;
        }
        final boolean needLongclick = tempBoolean;*/
        final int msgType = tempType;
        final long msgTime = message.getMsgTime();
        holder.iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                    int[] localData = new int[2];
                    v.getLocationOnScreen(localData);

                    ((Activity) mContext).startActivityForResult(
                            (new Intent(mContext, ContextMenu.class))
                                    .putExtra("msgTime", msgTime)
                                    .putExtra("msgType", msgType)
                                    .putExtra("localData", localData)
                                    .putExtra("position", position)
                                    .putExtra("type", EMMessage.Type.IMAGE.ordinal()), API.REQUEST_CODE_CONTEXT_MENU);
                    /*if (((Activity)mContext).isMyGroup() && ((Activity)mContext).getIsType() == 1) {
                        ImageMessageBody imgBody = (ImageMessageBody) message.getBody();
                        if (imgBody.getRemoteUrl() == null || "".equals(imgBody.getRemoteUrl())) {
                            Toast.makeText(activity, "获取图片资源失败", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        //如果是我们维护的群组显示上传到群相册alertdialog
                        Intent intent = new Intent(activity, AlertDialog.class);
                        intent.putExtra("msg", ((Activity)mContext).getString(R.string.confirm_resend));
                        intent.putExtra("myGroupId", ((Activity)mContext).getMyGroupId());
                        intent.putExtra("title", "是否上传到群相册?");
                        intent.putExtra("method", AlertDialog.uploadPic);
                        intent.putExtra("localUrl", imgBody.getLocalUrl());
                        intent.putExtra("imagePath", imgBody.getRemoteUrl());
                        intent.putExtra("cancel", true);

                        intent.putExtra("secret", imgBody.getSecret());
                        ((Activity)mContext).startActivityForResult(intent, 0);
                    }*/
                return true;
            }
        });
        holder.iv.setClickable(true);
        try {
            mBarScreenId = message.getStringAttribute("screenorderid");//霸屏id
        } catch (EaseMobException e) {
            e.printStackTrace();
        }
        // 接收方向的消息
        if (message.direct == EMMessage.Direct.RECEIVE) {
            if (message.status == EMMessage.Status.INPROGRESS) {
                holder.iv.setImageResource(R.drawable.default_icon);
                showDownloadImageProgress(message, holder);
            } else {
                String content = null;
                try {
                    content = message.getStringAttribute("content");
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
                holder.pb.setVisibility(View.GONE);
                holder.tv.setVisibility(View.GONE);
                holder.iv_sendMessage.setVisibility(View.GONE);
                holder.baping_flag.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(content)) {//是否霸屏
                    holder.iv_sendMessage.setVisibility(View.VISIBLE);
                    holder.iv_sendMessage.setText(content);
                    holder.baping_flag.setVisibility(View.VISIBLE);
                }
                holder.iv.setImageResource(R.drawable.default_icon);
                ImageMessageBody imgBody = (ImageMessageBody) message.getBody();
                if (imgBody.getRemoteUrl() != null) {
                    //final String remotePath = imgBody.getLocalUrl();
                    final String remotePath = imgBody.getRemoteUrl();
                    final String filePath = ImageUtils.getImagePath(remotePath);
                    String thumbRemoteUrl = imgBody.getThumbnailUrl();
                    Log.e("chat", "handleImageMessage: " + thumbRemoteUrl + "..." + remotePath);
                    if (TextUtils.isEmpty(thumbRemoteUrl) && !TextUtils.isEmpty(remotePath)) {
                        thumbRemoteUrl = remotePath;
                    }
                    String thumbnailPath = ImageUtils.getThumbnailImagePath(thumbRemoteUrl);
                    showImageView(thumbnailPath, holder.iv, filePath, imgBody.getRemoteUrl(), message, position);
                    //图片点击事件
                    holder.iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EMLog.d(TAG, "image view on click");
                            Intent intent = new Intent(mContext, ShowBigImage.class);
                            File file = new File(filePath);
                            if (file.exists()) {
                                Uri uri = Uri.fromFile(file);
                                intent.putExtra("uri", uri);
                                intent.putExtra("position", position);
                                EMLog.d(TAG, "herehere need to check why download everytime");
                            } else {
                                ImageMessageBody body = (ImageMessageBody) message.getBody();
                                intent.putExtra("secret", body.getSecret());
                                if (filePath != null && new File(filePath).exists()) {
                                    intent.putExtra("remotepath", (Parcelable[]) null);
                                } else {
                                    intent.putExtra("remotepath", remotePath);

                                }
                                intent.putExtra("position", position);
                            }
                            if (message != null && message.direct == EMMessage.Direct.RECEIVE && !message.isAcked
                                    && message.getChatType() != EMMessage.ChatType.GroupChat && message.getChatType() != EMMessage.ChatType.ChatRoom) {
                                try {
                                    EMChatManager.getInstance().ackMessageRead(message.getFrom(), message.getMsgId());
                                    message.isAcked = true;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            ((Activity) mContext).startActivityForResult(intent, API.REQUEST_CODE_CONTEXT_MENU);
                        }
                    });
                }
            }
            return;
        }
        // 发送的消息
        ImageMessageBody imgBody = (ImageMessageBody) message.getBody();
        final String filePath = imgBody.getLocalUrl();
        if (filePath != null && new File(filePath).exists()) {
            showImageView(ImageUtils.getThumbnailImagePath(filePath), holder.iv, filePath, null, message, position);
        } else {
            showImageView(ImageUtils.getThumbnailImagePath(filePath), holder.iv, filePath, IMAGE_DIR, message, position);
        }
        String content = null;
        try {
            content = message.getStringAttribute("content");
            holder.iv_sendMessage.setVisibility(View.GONE);
            holder.pb.setVisibility(View.GONE);
            holder.tv.setVisibility(View.GONE);
            holder.baping_flag.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(content)&&content!=null) {
                holder.iv_sendMessage.setVisibility(View.VISIBLE);
                holder.iv_sendMessage.setText(content + "");
                holder.baping_flag.setVisibility(View.VISIBLE);
                //是否管理员
                if (session.isAdmin) {
                    holder.mBtnDelete.setVisibility(View.VISIBLE);
                    holder.mBtnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onDeleteBarScreenListener.deleteBarScreen(mBarScreenId, message);
                        }
                    });
                }
            }
        } catch (EaseMobException e) {
            e.printStackTrace();
        }
        //图片点击事件
        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EMLog.d(TAG, "image view on click");
                Intent intent = new Intent(mContext, ShowBigImage.class);
                File file = new File(filePath);
                if (file.exists()) {
                    Uri uri = Uri.fromFile(file);
                    intent.putExtra("uri", uri);
                    intent.putExtra("position", position);
                    EMLog.d(TAG, "herehere need to check why download everytime");
                } else {
                    ImageMessageBody body = (ImageMessageBody) message.getBody();
                    intent.putExtra("secret", body.getSecret());
                    if (filePath != null && new File(filePath).exists()) {
                        intent.putExtra("remotepath", "");
                    } else {
                        intent.putExtra("remotepath", IMAGE_DIR);

                    }
                    intent.putExtra("position", position);
                }
                if (message != null && message.direct == EMMessage.Direct.RECEIVE && !message.isAcked
                        && message.getChatType() != EMMessage.ChatType.GroupChat && message.getChatType() != EMMessage.ChatType.ChatRoom) {
                    try {
                        EMChatManager.getInstance().ackMessageRead(message.getFrom(), message.getMsgId());
                        message.isAcked = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                ((Activity) mContext).startActivityForResult(intent, API.REQUEST_CODE_CONTEXT_MENU);
            }
        });
        switch (message.status) {
            case SUCCESS:
                holder.pb.setVisibility(View.GONE);
                holder.tv.setVisibility(View.GONE);
                holder.staus_iv.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(content)) {
                    holder.iv_sendMessage.setVisibility(View.VISIBLE);
                    holder.iv_sendMessage.setText(content + "");
                }
                break;
            case FAIL:
                holder.pb.setVisibility(View.GONE);
                holder.tv.setVisibility(View.GONE);
                holder.staus_iv.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(content)) {
                    holder.iv_sendMessage.setVisibility(View.VISIBLE);
                    holder.iv_sendMessage.setText(content + "");
                }
                break;
            case INPROGRESS:
                holder.staus_iv.setVisibility(View.GONE);
                holder.pb.setVisibility(View.VISIBLE);
                holder.tv.setVisibility(View.VISIBLE);
                if (timers.containsKey(message.getMsgId()))
                    return;
                // set a timer
                final Timer timer = new Timer();
                timers.put(message.getMsgId(), timer);
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            public void run() {
                                String content = null;
                                try {
                                    content = message.getStringAttribute("content");
                                } catch (EaseMobException e) {
                                    e.printStackTrace();
                                }
                                holder.pb.setVisibility(View.VISIBLE);
                                holder.tv.setVisibility(View.VISIBLE);
                                holder.tv.setText(message.progress + "%");
                                if (!TextUtils.isEmpty(content)) {
                                    holder.iv_sendMessage.setVisibility(View.VISIBLE);
                                    holder.iv_sendMessage.setText(content + "");
                                    MessageAdapter.this.notifyDataSetChanged();
                                }
                                if (message.status == EMMessage.Status.SUCCESS) {
                                    holder.pb.setVisibility(View.GONE);
                                    holder.tv.setVisibility(View.GONE);
                                    //message.setSendingStatus(Message.SENDING_STATUS_SUCCESS);
                                    timer.cancel();
                                } else if (message.status == EMMessage.Status.FAIL) {
                                    holder.pb.setVisibility(View.GONE);
                                    holder.tv.setVisibility(View.GONE);
                                    holder.staus_iv.setVisibility(View.VISIBLE);
                                    timer.cancel();
                                }

                            }
                        });

                    }
                }, 0, 500);
                break;
            default:
                sendPictureMessage(message, holder);
                break;
        }
    }

    /**
     * 图片下载状态显示
     * @param message
     * @param holder
     */
    private void showDownloadImageProgress(final EMMessage message, final MessageAdapter_VH holder) {
        EMLog.d(TAG, "!!! show download image progress");
        final FileMessageBody msgbody = (FileMessageBody) message.getBody();
        if (holder.pb != null)
            holder.pb.setVisibility(View.VISIBLE);
        if (holder.tv != null)
            holder.tv.setVisibility(View.VISIBLE);

        msgbody.setDownloadCallback(new EMCallBack() {

            @Override
            public void onSuccess() {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // message.setBackReceive(false);
                        if (message.getType() == EMMessage.Type.IMAGE) {
                            holder.pb.setVisibility(View.GONE);
                            holder.tv.setVisibility(View.GONE);
                        }
                        refreshSelectLast();
                    }
                });
            }

            @Override
            public void onError(int code, String message) {

            }

            @Override
            public void onProgress(final int progress, String status) {
                if (message.getType() == EMMessage.Type.IMAGE) {
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            holder.tv.setText(progress + "%");

                        }
                    });
                }

            }

        });
    }

    /**
     * 图片消息重发
     * @param message
     * @param holder
     */
    private void sendPictureMessage(final EMMessage message, final MessageAdapter_VH holder) {
        try {
            String to = message.getTo();
            holder.staus_iv.setVisibility(View.GONE);
            holder.pb.setVisibility(View.VISIBLE);
            holder.tv.setVisibility(View.VISIBLE);
            holder.tv.setText("0%");
            String content = null;
            try {
                content = message.getStringAttribute("content");
            } catch (EaseMobException e) {
                e.printStackTrace();
            }
            holder.iv_sendMessage.setText(content + "");
            final long start = System.currentTimeMillis();
            EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

                @Override
                public void onSuccess() {
                    Log.d(TAG, "send image message successfully");
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        public void run() {
                            // send success
                            holder.pb.setVisibility(View.GONE);
                            holder.tv.setVisibility(View.GONE);
                        }
                    });
                }

                @Override
                public void onError(int code, String error) {

                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        public void run() {
                            holder.pb.setVisibility(View.GONE);
                            holder.tv.setVisibility(View.GONE);
                            holder.staus_iv.setVisibility(View.VISIBLE);
                        }
                    });
                }

                @Override
                public void onProgress(final int progress, String status) {
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        public void run() {
                            holder.tv.setText(progress + "%");
                        }
                    });
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *文字消息重发
     * @param message
     * @param holder
     */
    public void sendMsgInBackground(final EMMessage message, final MessageAdapter_VH holder) {
        holder.staus_iv.setVisibility(View.GONE);
        holder.pb.setVisibility(View.VISIBLE);

        final long start = System.currentTimeMillis();
        // 调用sdk发送异步发送方法
        EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

            @Override
            public void onSuccess() {

                updateSendedView(message, holder);
            }

            @Override
            public void onError(int code, String error) {
                //重新发送
                updateSendedView(message, holder);
            }

            @Override
            public void onProgress(int progress, String status) {
            }

        });
    }

    /**
     * 更新ui上消息发送状态
     *
     * @param message
     * @param holder
     */
    private void updateSendedView(final EMMessage message, final MessageAdapter_VH holder) {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // send success
                if (message.getType() == EMMessage.Type.VIDEO) {
                    holder.tv.setVisibility(View.GONE);
                }
                EMLog.d(TAG, "message status : " + message.status);
                if (message.status == EMMessage.Status.SUCCESS) {

                } else if (message.status == EMMessage.Status.FAIL) {

                }

                notifyDataSetChanged();
            }
        });
    }

    /**
     * load image into image view
     *
     * @param thumbernailPath
     * @param iv
     * @param position
     * @return the image exists or not
     */
    private boolean showImageView(final String thumbernailPath, final ImageView iv, final String localFullSizePath, String remoteDir,
                                  final EMMessage message, final int position) {
        final String remote = remoteDir;
        EMLog.d("###", "local = " + localFullSizePath + " remote: " + remote);
        // first check if the thumbnail image already loaded into cache
        Bitmap bitmap = ImageCache.getInstance().get(thumbernailPath);
        if (bitmap != null) {
            iv.setImageBitmap(bitmap);
            return true;
        } else {
            new LoadImageTask().execute(thumbernailPath, localFullSizePath, remote, message.getChatType(), iv, mContext, message);
            return true;
        }

    }

    /**
     * 点击 吧台见 请求接口
     */
    public class BaTaiShowClickTask extends DefaultTask {

        private EMMessage message;

        public BaTaiShowClickTask(EMMessage message) {
            this.message = message;
        }

        @Override
        public void onProgress(RWProgress obj) {
            super.onProgress(obj);
        }

        @Override
        public void onError(DefaultError obj) {
            super.onError(obj);
            PromptUtils.showToast((Activity) mContext, "网络错误");
        }

        @Override
        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            P13306 p13306 = (P13306) protocol;
            if (p13306.resp.transcode.equals("9999")) {
                PromptUtils.showToast((Activity) mContext, p13306.resp.msg);
            } else {
                if (p13306.resp.status == 0) {//可以正常发送邀请
                    //设置新的扩展消息
                    message.setAttribute("isClick", true);
                    //将当前的消息移除 如果不移除会出现两条
                    EMConversation conversation = EMChatManager.getInstance().getConversation(message.getFrom());
                    conversation.removeMessage(message.getMsgId());
                    //将新增扩展消息后的消息添加到数据库
                    EMChatManager.getInstance().importMessage(message, false);
                    Intent intent = new Intent(mContext, ContextMenu.class);
                    intent.putExtra("batai_invation", true);
                    intent.putExtra("type", EMMessage.Type.TXT.ordinal());
                    intent.putExtra("fateCardId", message.getStringAttribute("fateCardId", ""));
                    intent.putExtra("barId", message.getStringAttribute("barId", ""));
                    ((Activity) mContext).startActivityForResult(intent, API.REQUEST_CODE_BATAI_INVATION);
                } else if (p13306.resp.status == 1) {
                    PromptUtils.showToast((Activity) mContext, "超出范围");
                } else if (p13306.resp.status == 2) {//失效了 置灰
                    PromptUtils.showToast((Activity) mContext, "已失效");
                    //设置新的扩展消息
                    message.setAttribute("isClick", true);
                    //将当前的消息移除 如果不移除会出现两条
                    EMConversation conversation = EMChatManager.getInstance().getConversation(message.getFrom());
                    conversation.removeMessage(message.getMsgId());
                    //将新增扩展消息后的消息添加到数据库
                    EMChatManager.getInstance().importMessage(message, false);
                    refresh();
                } else if (p13306.resp.status == 3) {//已经发送过邀请 置灰
                    PromptUtils.showToast((Activity) mContext, "您已发送过邀请");
                    //设置新的扩展消息
                    message.setAttribute("isClick", true);
                    //将当前的消息移除 如果不移除会出现两条
                    EMConversation conversation = EMChatManager.getInstance().getConversation(message.getFrom());
                    conversation.removeMessage(message.getMsgId());
                    //将新增扩展消息后的消息添加到数据库
                    EMChatManager.getInstance().importMessage(message, false);
                    refresh();
                }
            }
        }

        @Override
        public void postExecute() {
            super.postExecute();
        }
    }

    /**
     * 同意 吧台见 接口请求
     */
    public class Invation_Agree extends DefaultTask {

        private EMMessage message;

        public Invation_Agree(EMMessage message) {
            this.message = message;
        }

        @Override
        public void onProgress(RWProgress obj) {
            super.onProgress(obj);
        }

        @Override
        public void onError(DefaultError obj) {
            super.onError(obj);
            PromptUtils.showToast((Activity) mContext, "网络错误");
        }

        @Override
        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            P13307 p13307 = (P13307) protocol;
            if (p13307.resp.transcode.equals("9999")) {
                PromptUtils.showToast((Activity) mContext, p13307.resp.msg);
                Log.i("接口调试", "吧台见同意按钮点击------》" + p13307.resp.msg);
            } else {
                Log.i("接口调试", "吧台见同意按钮点击------》" + p13307.resp.status);
                if (p13307.resp.status == 1) {//超出范围
                    PromptUtils.showToast((Activity) mContext, "超出范围");
                } else if (p13307.resp.status == 2) {//已失效
                    PromptUtils.showToast((Activity) mContext, "已失效");
                } else if (p13307.resp.status == 3) {//同意
                    //设置新的扩展消息
                    message.setAttribute("isClick_invation", true);
                    //将当前的消息移除 如果不移除会出现两条
                    EMConversation conversation = EMChatManager.getInstance().getConversation(message.getFrom());
                    conversation.removeMessage(message.getMsgId());
                    //将新增扩展消息后的消息添加到数据库
                    EMChatManager.getInstance().importMessage(message, true);
                    Intent intent = new Intent(mContext, ContextMenu.class);
                    intent.putExtra("invation_agree", true);
                    intent.putExtra("type", EMMessage.Type.TXT.ordinal());
                    intent.putExtra("fateCardId", message.getStringAttribute("fateCardId", ""));
                    intent.putExtra("barId", message.getStringAttribute("barId", ""));
                    ((Activity) mContext).startActivityForResult(intent, API.REQUEST_CODE_INVATION_AGREE);
                } else if (p13307.resp.status == 4) {//已经同意过 置灰
                    //设置新的扩展消息
                    message.setAttribute("isClick_invation", true);
                    //将当前的消息移除 如果不移除会出现两条
                    EMConversation conversation = EMChatManager.getInstance().getConversation(message.getFrom());
                    conversation.removeMessage(message.getMsgId());
                    //将新增扩展消息后的消息添加到数据库
                    EMChatManager.getInstance().importMessage(message, true);
                }
            }
        }

        @Override
        public void postExecute() {
            super.postExecute();
        }
    }

    /**
     * 霸屏删除接口回调
     */
    public interface onDeleteBarScreenListener {
        void deleteBarScreen(String id, EMMessage message);
    }
    private onDeleteBarScreenListener onDeleteBarScreenListener;
    public void setOnDeleteBarScreenListener(onDeleteBarScreenListener onDeleteBarScreenListener) {
        this.onDeleteBarScreenListener = onDeleteBarScreenListener;
    }

    /**
     * 刷新页面
     */
    public void refresh() {
        if (handler.hasMessages(HANDLER_MESSAGE_REFRESH_LIST)) {
            return;
        }
        android.os.Message msg = handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST);
        handler.sendMessage(msg);
    }

    /**
     * 刷新页面, 选择最后一个
     */
    public void refreshSelectLast() {
        handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST));
        handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_SELECT_LAST));
    }

    /**
     * 刷新页面, 选择Position
     */
    public void refreshSeekTo(int position) {
        handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST));
        android.os.Message msg = handler.obtainMessage(HANDLER_MESSAGE_SEEK_TO);
        msg.arg1 = position;
        handler.sendMessage(msg);
    }


    /**
     * 对消息的刷新进行处理
     */
    static class MyHandler extends WeakHandler {

        private MessageAdapter mAdapter;

        public MyHandler(Object o) {
            super(o);

        }

        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            mAdapter = (MessageAdapter) getObjct();
            if (mAdapter == null) {
                return;
            }
            switch (message.what) {
                case HANDLER_MESSAGE_REFRESH_LIST:
                    refreshList();
                    break;
                case HANDLER_MESSAGE_SELECT_LAST:
                    if (mAdapter.mContext instanceof Activity_BaPing) {
                        RecyclerView recyclerView = ((Activity_BaPing) mAdapter.mContext).getListView();
                        if (mAdapter.messages.length > 0) {
                            recyclerView.smoothScrollToPosition(mAdapter.messages.length - 1);
                        }
                    }
                    if (mAdapter.mContext instanceof SingleChat){
                        RecyclerView recyclerView = ((SingleChat) mAdapter.mContext).getListView();
                        if (mAdapter.messages.length > 0) {
                            recyclerView.smoothScrollToPosition(mAdapter.messages.length - 1);
                        }
                    }
                    break;
                case HANDLER_MESSAGE_SEEK_TO:
                    int position = message.arg1;
                    if (mAdapter.mContext instanceof Activity_BaPing) {
                        RecyclerView recyclerView = ((Activity_BaPing) mAdapter.mContext).getListView();
                        recyclerView.smoothScrollToPosition(position);
                    }
                    break;
                default:
                    break;
            }
        }

        private void refreshList() {
            // UI线程不能直接使用conversation.getAllMessages()
            // 否则在UI刷新过程中，如果收到新的消息，会导致并发问题
            mAdapter = (MessageAdapter) getObjct();
            if (mAdapter == null) {
                return;
            }
            mAdapter.messages = (EMMessage[]) mAdapter.mEMConversation.getAllMessages().toArray(new EMMessage[mAdapter.mEMConversation.getAllMessages().size()]);
            for (int i = 0; i < mAdapter.messages.length; i++) {
                mAdapter.mEMConversation.getMessage(i);
            }
            mAdapter.notifyDataSetChanged();
        }


    }

    /**
     * 性别不同 名字显示的颜色不同
     * @param nameView
     * @param flag
     */
    private void setNameColor(TextView nameView , String flag){
        if (!TextUtils.isEmpty(flag)){
            if (flag.equals("2")){
                //女
                nameView.setTextColor(mContext.getResources().getColor(R.color.gender_woman));
            }else {
                //男
                nameView.setTextColor(mContext.getResources().getColor(R.color.gender_man));
            }
        }

    }

}
