package com.yeying.aimi.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.easemob.chat.EMChatManager;
import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.AIMIApplication;
import com.yeying.aimi.bean.ChatBean;
import com.yeying.aimi.huanxin.SmileUtils;
import com.yeying.aimi.storage.NormalMsgCache;
import com.yeying.aimi.views.RoundImageView;

import java.util.List;

/**
 * Created by tanchengkeji on 2017/9/19.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatAdapter_VH> implements View.OnClickListener {

    private List<ChatBean> mChatBeanList;
    private Context mContext;
    private LayoutInflater inflater;
    private PopupWindow mPopupWindow;
    private String deleteId;
    private ItemClick mItemClick;

    public ChatAdapter(List<ChatBean> chatBeanList, Context context) {
        mChatBeanList = chatBeanList;
        mContext = context;
        inflater = LayoutInflater.from(context);
        initPop();
    }

    private void initPop() {
        View popupView = inflater.inflate(R.layout.del_msg_window, null);
        popupView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        TextView tv_msg = (TextView) popupView.findViewById(R.id.tv_msg);
        TextView bt_sure = (TextView) popupView.findViewById(R.id.bt_sure);
        TextView bt_close = (TextView) popupView.findViewById(R.id.bt_close);
        tv_msg.setText("确定删除该对话？");
        bt_sure.setOnClickListener(this);
        bt_close.setOnClickListener(this);
    }

    @Override
    public ChatAdapter_VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.main_chatlist, parent, false);
        return new ChatAdapter_VH(itemView);
    }

    @Override
    public void onBindViewHolder(final ChatAdapter_VH holder, final int position) {
        final ChatBean chatBean = mChatBeanList.get(position);
        Glide.with(mContext).load(AIMIApplication.dealHeadImg(chatBean.getImage()))
                .placeholder(R.drawable.default_icon).error(R.drawable.default_icon)
                .into(holder.chat_headimage);
        Log.e("ii", "onBindViewHolder: "+chatBean.getImage());
        Log.e("ii", "onBindViewHolder: "+AIMIApplication.dealHeadImg(chatBean.getImage()));
        holder.chat_name.setText(chatBean.getName());
        holder.chat_time.setText(chatBean.getTime());
        holder.chat_newsnum.setText(chatBean.getNum() + "");
        if (chatBean.getType() == 0) {
            if (chatBean.getChatType() == 1) {
                holder.chat_say.setText("");
            } else {
                if (!chatBean.isSystemMsg()) {
                    CharSequence ce = chatBean.getText();
                    Spannable spn = SmileUtils.getSmiledText(mContext, ce);
                    holder.chat_say.setText(spn, TextView.BufferType.SPANNABLE);
                }
            }
        } else if (chatBean.getType() == 1) {
            if (chatBean.getChatType() == 1) {
                holder.chat_say.setText(chatBean.getFrom() + "：[图片消息]");
            } else {
                holder.chat_say.setText("[图片消息]");
            }
        } else if (chatBean.getType() == 2) {
            if (chatBean.getChatType() == 1) {
                holder.chat_say.setText(chatBean.getFrom() + "：[语音消息]");
            } else {
                holder.chat_say.setText("[语音消息]");
            }
        } else if (chatBean.getType() == 3) {
            if (chatBean.getChatType() == 1) {
                holder.chat_say.setText(chatBean.getFrom() + "：[视频消息]");
            } else {
                holder.chat_say.setText("[视频消息]");
            }
        }
        if (chatBean.getNum() > 0) {
            if (chatBean.isMianDaRao()) {
                holder.chat_news.setVisibility(View.VISIBLE);
                holder.chat_newsnum.setVisibility(View.GONE);
            } else {
                holder.chat_news.setVisibility(View.VISIBLE);
            }
        } else {
            holder.chat_news.setVisibility(View.GONE);
            holder.chat_newsnum.setVisibility(View.GONE);
        }
        holder.chat_list_layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClick.onItemClick(position);
            }
        });
        holder.chat_list_layout1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mPopupWindow != null) {
                    mPopupWindow.showAtLocation(holder.chat_list_layout1, Gravity.CENTER, 0, 0);
                }
                deleteId = chatBean.getUserId();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mChatBeanList != null ? mChatBeanList.size() : 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_close:
                if (mPopupWindow != null && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                break;
            case R.id.bt_sure:
                if (mPopupWindow != null && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                delNormalMsg(deleteId);
                break;
        }
    }

    public void delNormalMsg(String userId) {
        if (userId == null) {
            userId = "";
        }
        //从临时消息中移除
        NormalMsgCache msgCache = NormalMsgCache.getCache(mContext);
        for (int a = 0; a < msgCache.getNormalmsg().size(); a++) {
            if (userId.equals(msgCache.getNormalmsg().get(a).getUserId())) {
                msgCache.getNormalmsg().remove(a);
                break;
            }
        }
        msgCache.save();

        for (int i = 0; i < mChatBeanList.size(); i++) {
            ChatBean bean1 = mChatBeanList.get(i);
            String userId2 = bean1.getUserId();
            if (userId2 == null) {
                userId2 = "";
            }
            if (userId2.equals(userId)) {
                mChatBeanList.remove(i);
                EMChatManager.getInstance().deleteConversation(bean1.getUserId());
                notifyDataSetChanged();
                break;
            }
        }
    }

    class ChatAdapter_VH extends RecyclerView.ViewHolder {

        RoundImageView chat_headimage;
        TextView chat_name, chat_say, chat_time, chat_newsnum;
        RelativeLayout chat_news, chat_list_layout1;


        public ChatAdapter_VH(View itemView) {
            super(itemView);
            chat_headimage = (RoundImageView) itemView.findViewById(R.id.chat_headimage);
            chat_name = (TextView) itemView.findViewById(R.id.chat_name);
            chat_say = (TextView) itemView.findViewById(R.id.chat_say);
            chat_time = (TextView) itemView.findViewById(R.id.chat_time);
            chat_newsnum = (TextView) itemView.findViewById(R.id.chat_newsnum);
            chat_news = (RelativeLayout) itemView.findViewById(R.id.chat_news);
            chat_list_layout1 = (RelativeLayout) itemView.findViewById(R.id.chat_list_layout1);
        }
    }

    public interface ItemClick{
        void onItemClick(int psn);
    }

    public void setOnItemClickListener(ItemClick itemClickListener){
        mItemClick = itemClickListener;
    }

}
