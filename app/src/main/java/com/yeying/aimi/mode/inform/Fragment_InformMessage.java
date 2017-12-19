package com.yeying.aimi.mode.inform;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.bumptech.glide.Glide;
import com.yeying.aimi.R;
import com.yeying.aimi.adapter.Adapter_InfromMessage;
import com.yeying.aimi.aimibase.AIMIApplication;
import com.yeying.aimi.bean.FateAgreeBean;
import com.yeying.aimi.bean.FateUser;
import com.yeying.aimi.bean.ReplyCacheBean;
import com.yeying.aimi.fragment.BaseFragment;
import com.yeying.aimi.mode.dynamics_detail.DynamicsDetail;
import com.yeying.aimi.mode.otherdetails.OtherHomepage;
import com.yeying.aimi.protoco.DefaultTask;
import com.yeying.aimi.protoco.IProtocol;
import com.yeying.aimi.protocol.impl.P13304;
import com.yeying.aimi.storage.ReplyCache;
import com.yeying.aimi.storage.ReplyHistoryCache;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.utils.PromptUtils;
import com.yeying.aimi.utils.WeakHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by tanchengkeji on 2017/8/14.
 */

public class Fragment_InformMessage extends BaseFragment implements OnRefreshListener, OnLoadMoreListener, Adapter_InfromMessage.ItemClickListener, View.OnClickListener {

    private static final String TAG = "Fragment_InformMessage";

    private View fragmentView;

    private List<ReplyCacheBean> list = new ArrayList<>();

    private SwipeToLoadLayout swipetoloadlayout;
    private RecyclerView swipe_target;
    private Adapter_InfromMessage adapter_infromMessage;

    private View fateSuccessView;
    private ImageView head_img_left;
    private ImageView head_img_right;
    private Button mBtnSendmessage;
    private FrameLayout mBtnBarshow;
    private TextView mBtnBack;

    private View fateView;
    private TextView mTv_card;
    private Button mBtn_card;
    private RelativeLayout mRl_card;

    private View deleteView;
    private TextView bt_close;
    private TextView bt_sure;

    private SessionCache sessionCache;

    private FateUser fateUser;

    private String fateCardId;
    private String barId;
    private String fateUserInfo;
    private String userId;
    private int type;
    private String deleteMsgId;
    private int deletePsn;

    private String head_url_left;
    private String head_url_right;
    private PopupWindow popupWindow;
    private static final int TYPE = 1;
    private View view;
    private RelativeLayout mLayoutNone;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (fragmentView != null) {
            ViewGroup viewGroup = (ViewGroup) fragmentView.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(fragmentView);
            }
            return fragmentView;
        }


        fragmentView = inflater.inflate(R.layout.fragment_informmessage, container, false);
        //popwindow 三个
        fateView = inflater.inflate(R.layout.popwindow_yuan, null, false);
        deleteView = inflater.inflate(R.layout.del_msg_window, null, false);
        initFateView(fateView);
        initDeleteView(deleteView);
        //Session
        sessionCache = SessionCache.getInstance(getActivity());
        //Gson

        initView();

        ReplyCache replyCache = ReplyCache.getCommentCache(getActivity());
        //加到历史缓存中
        ReplyHistoryCache caChe = ReplyHistoryCache.getCommentCache(getActivity());
        caChe.getReplyHistoryList().addAll(replyCache.getReplyList());
        caChe.save();
        //清空
        replyCache.getReplyList().clear();
        replyCache.save();
        list.clear();
        /**
         * 初始化消息列表
         * getTypeInt()  4 抢到缘分牌后有异性抢到相同的缘分牌
         * getTypeInt()  5 缘分牌是否同意消息
         * getTypeInt()  6 缘分牌每轮结束匹配失败消息
         * getType()     2 评论消息
         * getTypeInt()  1 关注消息
         * setTypeInt()  7 霸屏排行榜
         */
        for (ReplyCacheBean bean : caChe.getReplyHistoryList()) {
            if (bean.getType() != 1) {
                list.add(bean);
            }
        }
        if(list.size()<=0){
            mLayoutNone.setVisibility(View.VISIBLE);
            swipetoloadlayout.setVisibility(View.GONE);
        }else{
            mLayoutNone.setVisibility(View.GONE);
            swipetoloadlayout.setVisibility(View.VISIBLE);
        }
        Collections.reverse(list);

        return fragmentView;
    }

    private void initView() {
        swipetoloadlayout = (SwipeToLoadLayout) fragmentView.findViewById(R.id.swipetoloadlayout);
        swipetoloadlayout.setOnRefreshListener(this);
        swipetoloadlayout.setOnLoadMoreListener(this);
        swipetoloadlayout.setRefreshEnabled(true);
        swipetoloadlayout.setLoadMoreEnabled(true);
        swipe_target = (RecyclerView) fragmentView.findViewById(R.id.swipe_target);
        swipe_target.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLayoutNone = (RelativeLayout) fragmentView.findViewById(R.id.layout_none);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter_infromMessage = new Adapter_InfromMessage(list, getActivity(), getActivity());
        swipe_target.setAdapter(adapter_infromMessage);
        adapter_infromMessage.notifyDataSetChanged();
        adapter_infromMessage.setItemClickListener(this);
    }

    /**
     * 配对成功PopupWindow
     * @param fateSuccessView
     */
    /*private void initFateSuccessView(View fateSuccessView) {
        head_img_left= (ImageView) fateSuccessView.findViewById(R.id.head_img_left);
        head_img_right= (ImageView) fateSuccessView.findViewById(R.id.head_img_right);
        mBtnBarshow = (FrameLayout) fateSuccessView.findViewById(R.id.btn_barshow);
        mBtnBarshow.setOnClickListener(this);
        mBtnSendmessage = (Button) fateSuccessView.findViewById(R.id.btn_sendmessage);
        mBtnSendmessage.setOnClickListener(this);
        mBtnBack = (TextView) fateSuccessView.findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(this);
    }*/

    /**
     * 缘分牌通知PopupWindow
     *
     * @param fateView
     */
    private void initFateView(View fateView) {
        mTv_card = (TextView) fateView.findViewById(R.id.mTv_card);
        mBtn_card = (Button) fateView.findViewById(R.id.mBtn_card);
        mRl_card = (RelativeLayout) fateView.findViewById(R.id.mRl_card);
        mRl_card.getBackground().setAlpha(150);
        mBtn_card.setOnClickListener(this);
        //点击 popupWindow 外部 popupWindow消失
        fateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
    }

    /**
     * 删除条目PopupWindow
     *
     * @param deleteView
     */
    private void initDeleteView(View deleteView) {
        //取消
        bt_close = (TextView) deleteView.findViewById(R.id.bt_close);
        //确定
        bt_sure = (TextView) deleteView.findViewById(R.id.bt_sure);
        bt_close.setOnClickListener(this);
        bt_sure.setOnClickListener(this);
        //点击 popupWindow 外部 popupWindow消失
        deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_close:
                if (popupWindow != null && popupWindow.isShowing())
                    popupWindow.dismiss();
                break;
            case R.id.bt_sure:
                ReplyHistoryCache commentCache = ReplyHistoryCache.getCommentCache(getActivity());
                for (int i = 0; i < commentCache.getReplyHistoryList().size(); i++) {
                    if (commentCache.getReplyHistoryList().get(i).getMsgId() != null) {
                        if (commentCache.getReplyHistoryList().get(i).getMsgId().equals(deleteMsgId)) {
                            commentCache.getReplyHistoryList().remove(commentCache.getReplyHistoryList().get(i));
                            commentCache.save();
                        }
                    }
                }
                list.remove(deletePsn);
                adapter_infromMessage.notifyDataSetChanged();
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                if(list.size()<=0){
                    mLayoutNone.setVisibility(View.VISIBLE);
                    swipetoloadlayout.setVisibility(View.GONE);
                }else{
                    mLayoutNone.setVisibility(View.GONE);
                    swipetoloadlayout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.mBtn_card:
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                break;
        }
    }

    private static class MyHandler extends WeakHandler {

        public MyHandler(Object o) {
            super(o);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Fragment_InformMessage informMessage = (Fragment_InformMessage) getObjct();
            if (informMessage == null) {
                return;
            }
            switch (msg.what) {
                case 0:
                    informMessage.swipetoloadlayout.setRefreshing(false);
                    break;
                case 1:
                    informMessage.swipetoloadlayout.setLoadingMore(false);
                    break;
            }
        }
    }

    private MyHandler handler = new MyHandler(this);

    /**
     * 伪刷新加载
     */
    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(0, 1500);
    }

    @Override
    public void onLoadMore() {
        handler.sendEmptyMessageDelayed(1, 1500);
    }

    /**
     * itemview点击事件  接口回调
     *
     * @param psn
     */
    @Override
    public void onItemViewClick(int psn) {
        int type = list.get(psn).getTypeInt();
        if (type == 4) {//send20004
            //匹配到相同的缘分牌
            createPop(fateView);
            mTv_card.setText("有缘遇见TA!快去缘分牌入口查看吧！");
        } else if (type == 5) {//send20008
            //缘分牌配对成功或者失败  点击进入配对页面
            /*FateAgreeBean fateAgreeBean = gson.fromJson(list.get(psn).getFateUserInfo(), FateAgreeBean.class);
            barId=fateAgreeBean.getBarId();
            fateCardId=fateAgreeBean.getFateCardId();
            fateUser=fateAgreeBean.getFateUser();
            head_url_left=fateAgreeBean.getFateUser().getHeadImg();
            head_url_right=sessionCache.headimgUrl;
            Intent intent=new Intent(getActivity(),FatePop_Activity.class);
            intent.putExtra(FatePop_Activity.FROM_FLAG,false);
            intent.putExtra(FatePop_Activity.BAR_ID,barId);
            intent.putExtra(FatePop_Activity.FATE_CARD_ID,fateCardId);
            intent.putExtra(FatePop_Activity.USER_ID, fateUser.getUserId());
            intent.putExtra(FatePop_Activity.USER_NAME, fateUser.getNickName());
            intent.putExtra(FatePop_Activity.URL_LEFT,head_url_left);
            intent.putExtra(FatePop_Activity.URL_RIGHT,head_url_right);
            startActivity(intent);*/
            /*createPop(fateSuccessView);
            showPopWindow()*/
        } else if (type == 6) {//send20007
            createPop(fateView);
            mTv_card.setText("缘分未到，祝下轮好运");
        } else if (type == 1) {//关注消息
            OtherHomepage.toOtherHomePage(getActivity(),
                    list.get(psn).getUserId(),
                    TextUtils.isEmpty(sessionCache.locationX) ? 0.0 : Double.parseDouble(sessionCache.locationX),
                    TextUtils.isEmpty(sessionCache.locationY) ? 0.0 : Double.parseDouble(sessionCache.locationY), false);
        }
        if (list.get(psn).getType() == 2) {
            Intent messageintent2 = new Intent(getActivity(), DynamicsDetail.class);
            messageintent2.putExtra("arg1", 1);
            messageintent2.putExtra("backUrl", list.get(psn).getPic_img());
            messageintent2.putExtra("messageCode", list.get(psn).getId());
            startActivity(messageintent2);
        }
    }

    /**
     * 头像点击事件 接口回调
     *
     * @param psn
     */
    @Override
    public void onHeadImgClick(int psn) {
        int type = list.get(psn).getTypeInt();
        if (type == 5) {
            //缘分牌配对成功或者失败  点击进入配对页面
            FateAgreeBean fateAgreeBean = JSON.parseObject(list.get(psn).getTempParaInfo(), FateAgreeBean.class);
            fateUser = fateAgreeBean.getFateUser();
            barId = fateAgreeBean.getBarId();
            fateCardId = fateAgreeBean.getFateCardId();
            head_url_left = fateAgreeBean.getFateUser().getHeadImg();
            head_url_right = sessionCache.headimgUrl;
            createPop(fateSuccessView);
            showPopWindow();
        } else if (type == 4) {
            //匹配到相同的缘分牌
            createPop(fateView);
            mTv_card.setText("有缘遇见TA!快去缘分牌入口查看吧！");
        } else if (type == 6) {//send20007
            createPop(fateView);
            mTv_card.setText("缘分未到，祝下轮好运");
        }else if (type == 7){//霸屏排行

        }else {
            OtherHomepage.toOtherHomePage(getActivity(),
                    list.get(psn).getUserId(),
                    TextUtils.isEmpty(sessionCache.locationX) ? 0.0 : Double.parseDouble(sessionCache.locationX),
                    TextUtils.isEmpty(sessionCache.locationY) ? 0.0 : Double.parseDouble(sessionCache.locationY), false);
        }
    }

    /**
     * 长按删除事件 接口回调
     *
     * @param psn
     */
    @Override
    public void onItemLongClick(int psn) {
        deleteMsgId = list.get(psn).getMsgId();
        deletePsn = psn;
        popupWindow = new PopupWindow(deleteView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
        popupWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    /**
     * 创建pop
     *
     * @param view
     */
    private void createPop(View view) {
        popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    /**
     * 配对成功pop 按钮处理
     */
    private void showPopWindow() {
        String head_left = AIMIApplication.dealHeadImg(head_url_left);
        String head_right = AIMIApplication.dealHeadImg(head_url_right);
        Glide.with(getActivity()).load(head_left).into(head_img_left);
        Glide.with(getActivity()).load(head_right).into(head_img_right);
        //查询吧台见状态
        P13304 p13304 = new P13304();
        p13304.req.sessionId = sessionCache.sessionId;
        p13304.req.barId = barId;
        p13304.req.fateCardId = fateCardId;
        p13304.req.userId = sessionCache.userId;
        new BarStatusTask().execute(getActivity().getApplicationContext(), p13304);
    }

    /**
     * 从 通知 部分进来展示窗口 调用接口控制 吧台见 三个字的显示状态
     */
    private class BarStatusTask extends DefaultTask {
        @Override
        public void onProgress(RWProgress obj) {
            super.onProgress(obj);
        }

        @Override
        public void onError(DefaultError obj) {
            super.onError(obj);
            PromptUtils.showToast((Activity) getActivity(), "系统异常");
        }

        @Override
        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            P13304 p13304 = (P13304) protocol;
            if (p13304.resp.transcode.equals("9999")) {
                PromptUtils.showToast((Activity) getActivity(), "网络错误");
            } else if (p13304.resp.status == 1) {
                //不在活动内
                mBtnBarshow.setVisibility(View.INVISIBLE);
            } else if (p13304.resp.status == 2) {
                //已点击
                mBtnBarshow.setVisibility(View.VISIBLE);
                mBtnBarshow.setBackgroundResource(R.drawable.grey_round_bg);
                mBtnBarshow.setClickable(false);
            }
        }
    }
    /**
     * 根据 吧台见 的状态判断点击 发送消息的时候用不用发送 吧台见 三个字
     * status	吧台见按钮状态	int 0未点过 1不在活动内（按钮不展示） 2已点击(按钮置灰)
     *//*
    private class BarStatusTaskA extends DefaultTask {
        @Override
        public void onProgress(RWProgress obj) {
            super.onProgress(obj);
        }

        @Override
        public void onError(DefaultError obj) {
            super.onError(obj);
            PromptUtils.showToast((Activity) getActivity(),"网络异常");
        }
        @Override
        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            P13304 p13304 = (P13304) protocol;
            Intent chatIntent = new Intent(getActivity(), ChatActivity.class);
            //聊天类型
            chatIntent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
            //对方名字
            chatIntent.putExtra("username", fateUser.getNickName());
            //对方ID
            chatIntent.putExtra("userId", fateUser.getUserId());
            //对方头像
            chatIntent.putExtra("headimg", Palmapplication.dealHeadImg(head_url_left));
            //缘分牌ID
            chatIntent.putExtra("fateCardId",fateCardId);
            //barId
            chatIntent.putExtra("barId",barId);
            //头像前的拼接网址
            chatIntent.putExtra("headUrl", Tools.HEAD_HEAD);
            if (p13304.resp.transcode.equals("9999")){
                PromptUtil.showToast((Activity) getActivity(),"网络错误");
            }else if (p13304.resp.status==2){
                //已点击 不用发送
                chatIntent.putExtra("sendmessage", false);
                getActivity().startActivity(chatIntent);
            }else if (p13304.resp.status==1){
                //不在活动内 可以进行聊天，但是不用发送
                chatIntent.putExtra("sendmessage", false);
                getActivity().startActivity(chatIntent);
            } else{
                //status为0  未点击过
                //判断 SharedPreferences 中有没有存入相同的值
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("fateCardId", MODE_PRIVATE);
                String cardId = sharedPreferences.getString("fateCardId", "");
                if (fateCardId.equals(cardId)){
                    //有相同的值 不需要再发送吧台见三个字了
                    chatIntent.putExtra("sendmessage", false);
                    getActivity().startActivity(chatIntent);
                }else{//没有相同的值 可以发送吧台见三个字 同时存入值
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putString("fateCardId", fateCardId);
                    edit.putLong("time", System.currentTimeMillis());
                    edit.apply();
                    chatIntent.putExtra("sendmessage", true);
                    getActivity().startActivity(chatIntent);
                }
            }
        }
    }
    */

    /**
     * 点击 吧台见 请求接口 判断是否需要重复发送邀请
     * status	对应信息	int 1超出范围 2已失效 3已经发送过邀请
     *//*
    public class BaTaiShowClickTask extends DefaultTask {
        @Override
        public void onProgress(RWProgress obj) {
            super.onProgress(obj);
        }
        @Override
        public void onError(DefaultError obj) {
            super.onError(obj);
            PromptUtil.showToast((Activity) getActivity(),"网络错误");
        }
        @Override
        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            P13306 p13306 = (P13306) protocol;
            Intent chatIntent = new Intent(getActivity(), ChatActivity.class);
            //聊天类型
            chatIntent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
            //对方名字
            chatIntent.putExtra("username", fateUser.getNickName());
            //对方ID
            chatIntent.putExtra("userId", fateUser.getUserId());
            //对方头像
            chatIntent.putExtra("headimg", Palmapplication.dealHeadImg(head_url_left));
            //缘分牌ID
            chatIntent.putExtra("fateCardId",fateCardId);
            //barId
            chatIntent.putExtra("barId",barId);
            //头像前的拼接网址
            chatIntent.putExtra("headUrl", Tools.HEAD_HEAD);
            if (p13306.resp.transcode.equals("9999")){
                PromptUtil.showToast((Activity) getActivity(),p13306.resp.msg);
                Log.e("接口调试", "点击吧台见-------》"+p13306.resp.msg);
            }else if (p13306.resp.status==0){
                //可以发送邀请
                chatIntent.putExtra("barshow", true);
                getActivity().startActivity(chatIntent);
            } else if (p13306.resp.status == 1) {
                //超出范围
                PromptUtil.showToast((Activity) getActivity(), "您已超出酒吧范围范围");
            } else if (p13306.resp.status==2){
                PromptUtil.showToast((Activity) getActivity(),"该按钮已失效");
            }else if (p13306.resp.status==3){
                //已经发送过邀请 再次进入的时候不用发送
                chatIntent.putExtra("barshow", false);
                getActivity().startActivity(chatIntent);
            }
        }
    }*/

}
