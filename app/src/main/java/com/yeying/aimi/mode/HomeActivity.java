package com.yeying.aimi.mode;


import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.easemob.EMCallBack;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.AIMIApplication;
import com.yeying.aimi.aimibase.BaseActivityWithoutSwipeBack;
import com.yeying.aimi.bean.ReplyCacheBean;
import com.yeying.aimi.fragment.Fragment_Message;
import com.yeying.aimi.fragment.Fragment_Mine;
import com.yeying.aimi.fragment.Fragment_Search;
import com.yeying.aimi.fragment.Fragment_Total;
import com.yeying.aimi.huanxin.HXNotifier;
import com.yeying.aimi.mode.photopicker.AlbmActivity;
import com.yeying.aimi.mode.picture.UserFaBuActivity;
import com.yeying.aimi.protocol.impl.P630101;
import com.yeying.aimi.storage.ReplyCache;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.storage.SimpleMessageCache;
import com.yeying.aimi.utils.DownLoadListener;
import com.yeying.aimi.utils.DownLoadTask;
import com.yeying.aimi.utils.SystemMsgUtil;
import com.yeying.aimi.utils.WeakHandler;
import com.yeying.aimi.views.OnDoubleClickListener;

import java.util.Date;
import java.util.List;

import static com.easemob.EMNotifierEvent.Event.EventNewMessage;


public class HomeActivity extends BaseActivityWithoutSwipeBack implements View.OnClickListener, EMEventListener, DownLoadListener {

    public static HomeActivity activityInstance;
    public static boolean isMineRefresh = false;
    public HandlerPlus mHandlerPlus;
    private FrameLayout fragment_container;
    private LinearLayout ll_total, ll_search, ll_mine;
    private RelativeLayout rl_message;
    private ImageView img_total, img_search, img_message, img_mine;
    private TextView tv_total, tv_search, tv_message, tv_mine, tv_newmsg;
    private Fragment_Total mFragment_total;
    private Fragment_Search mFragment_search;
    private Fragment_Message mFragment_message;
    private Fragment_Mine mFragment_mine;
    private FragmentManager fragmentManager;
    private ImageButton menu_iv_btn;
    private SimpleMessageCache simpleCache;
    private boolean showFlag = false;
    private EMMessage.ChatType mChatType;

    private GameListener mGameListener;
    private String mMessageAttr;
    private Dialog mDialog;
    private View mView_dialog;
    /**
     * 最新版本:
     */
    private TextView mTvVersion;
    /**
     * 最新版本大小:
     */
    private TextView mTvSize;
    private TextView mTvTrait;
    /**
     * 立即更新
     */
    private Button mBtnUpdate;
   // private DealDoubleClick mDealDoubleClick;
    private String mUpdateurl;
    private Dialog mDialog_download;
    private DownLoadTask mDownLoadTask;
    private TextView mTvProgeress;
    private ProgressBar mProgressBar;
    private SessionCache mSessionCache;
    private ImageView mImgbtnCancle;
    private int updateindex = -1;
    private boolean isLocationCheck = false;

    public HomeActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fragmentManager = getSupportFragmentManager();
        activityInstance = this;
        mHandlerPlus = new HandlerPlus(this);
        if (!isLocationCheck){
            checkLocationPermission(isLocationCheck);
            isLocationCheck = true;
        }
        mSessionCache = SessionCache.getInstance(this);
        initView();
        loginHX();
        initDialog();
        initDownLoadDialog();
        initDownloadTask();
        checkNewVersion();
    }

    private void initDownloadTask() {
        mDownLoadTask = new DownLoadTask(this);
        mDownLoadTask.setDownLoadListener(this);
    }

    private void initDownLoadDialog() {
        View view_download = LayoutInflater.from(this).inflate(R.layout.dialog_download,null);
        mProgressBar = (ProgressBar) view_download.findViewById(R.id.progressBar);
        mTvProgeress = (TextView) view_download.findViewById(R.id.tv_progress);
        mDialog_download = new Dialog(this);
        mDialog_download.setContentView(view_download);
        mDialog_download.setCancelable(false);
        mDialog_download.setCanceledOnTouchOutside(false);
    }

    private void initDialog() {
        mView_dialog = LayoutInflater.from(this).inflate(R.layout.dialog_update, null);
        mTvVersion = (TextView) mView_dialog.findViewById(R.id.tv_version);
        mTvSize = (TextView) mView_dialog.findViewById(R.id.tv_size);
        mTvTrait = (TextView) mView_dialog.findViewById(R.id.tv_trait);
        mBtnUpdate = (Button) mView_dialog.findViewById(R.id.btn_update);
        mImgbtnCancle = (ImageView) mView_dialog.findViewById(R.id.imgbtn_cancel);
        mImgbtnCancle.setOnClickListener(this);
        mBtnUpdate.setOnClickListener(this);
        mDialog = new Dialog(this);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setContentView(mView_dialog);
       // mDialog.show();
    }



    private void checkNewVersion() {
        P630101 p630101 = new P630101();
        p630101.req.appType = "android";
        p630101.req.version = getVersionName();
        p630101.req.sessionId = mSessionCache.sessionId;
        //new CheckNewVersionTask().execute(this, p630101);
    }

    public String getVersionName() {
        String versionName = null;
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mDialog.isShowing()){
            mDialog.dismiss();
        }
    }



    /**
     * 环信登录
     */
    private void loginHX() {
        if (EMChat.getInstance().isLoggedIn()) {
            Log.e("success", "已经登陆成功");

            EMGroupManager.getInstance().loadAllGroups();
            EMChatManager.getInstance().loadAllConversations();
        } else {
            Log.e("success", "重新登录");
            SessionCache session = SessionCache.getInstance(mActivity);
            EMChatManager.getInstance().login(session.userId, session.userId, new EMCallBack() {//回调
                @Override
                public void onSuccess() {
                    mActivity.runOnUiThread(new Runnable() {
                        public void run() {
                            EMGroupManager.getInstance().loadAllGroups();
                            EMChatManager.getInstance().loadAllConversations();
                            Log.e("success", "登陆聊天服务器成功！");
                        }
                    });
                }

                @Override
                public void onProgress(int progress, String status) {

                }

                @Override
                public void onError(int code, String message) {
                    Log.e("success", "登陆聊天服务器失败！");
                }
            });

        }
    }

    private void initView() {
        fragment_container = (FrameLayout) findViewById(R.id.fragment_container);
        tv_newmsg = (TextView) findViewById(R.id.tv_newmsg);
        menu_iv_btn = (ImageButton) findViewById(R.id.menu_iv_btn);
        ll_total = (LinearLayout) findViewById(R.id.ll_total);
        ll_search = (LinearLayout) findViewById(R.id.ll_search);
        ll_mine = (LinearLayout) findViewById(R.id.ll_mine);
        rl_message = (RelativeLayout) findViewById(R.id.rl_message);
        img_total = (ImageView) findViewById(R.id.img_total);
        img_search = (ImageView) findViewById(R.id.img_search);
        img_message = (ImageView) findViewById(R.id.img_message);
        img_mine = (ImageView) findViewById(R.id.img_mine);
        tv_total = (TextView) findViewById(R.id.tv_total);
        tv_search = (TextView) findViewById(R.id.tv_search);
        tv_message = (TextView) findViewById(R.id.tv_message);
        tv_mine = (TextView) findViewById(R.id.tv_mine);
        ll_total.setOnClickListener(this);
        ll_search.setOnClickListener(this);
        ll_mine.setOnClickListener(this);
        rl_message.setOnClickListener(this);
        //先显示第三个fragment
        mFragment_message = new Fragment_Message();
        FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
        fragmentTransaction1.add(R.id.fragment_container,mFragment_message);
        fragmentTransaction1.commit();
        //再显示第一个fragment
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(mFragment_message);
        if (mFragment_search == null){
            mFragment_search = new Fragment_Search();
            fragmentTransaction.add(R.id.fragment_container,mFragment_search);
        }else {
            fragmentTransaction.show(mFragment_search);
        }
        fragmentTransaction.commit();
        ll_total.setOnTouchListener(new OnDoubleClickListener(new OnDoubleClickListener.DoubleClickCallback() {
            @Override
            public void onDoubleClick() {
                mDealDoubleClick.dealDoubleClick();
            }
        }));
        menu_iv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, "", new PermissionCheckedLister() {
                        @Override
                        public void onAllGranted() {
                            AlbmActivity.toAlbm(HomeActivity.this,true,false,true);
                        }

                        @Override
                        public void onGranted(List<String> grantPermissions) {

                        }

                        @Override
                        public void onDenied(List<String> deniedPermissions) {

                        }
                    });
                } else {
                    AlbmActivity.toAlbm(HomeActivity.this,true,false,true);
                }

            }
        });
    }

    private void hideAll(){
        img_total.setImageResource(R.drawable.miw);
        img_search.setImageResource(R.drawable.guangguang);
        img_message.setImageResource(R.drawable.xiaoxi_b);
        img_mine.setImageResource(R.drawable.my);
        tv_total.setTextColor(getResources().getColor(R.color.whit));
        tv_search.setTextColor(getResources().getColor(R.color.whit));
        tv_message.setTextColor(getResources().getColor(R.color.whit));
        tv_mine.setTextColor(getResources().getColor(R.color.whit));
    }

    private void hideFragment(FragmentTransaction transaction){
        if (mFragment_total!=null){
            transaction.hide(mFragment_total);
        }
        if (mFragment_search!=null){
            transaction.hide(mFragment_search);
        }
        if (mFragment_message!=null){
            transaction.hide(mFragment_message);
        }
        if (mFragment_mine!=null){
            transaction.hide(mFragment_mine);
        }
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (v.getId()){
            case R.id.ll_total:
                showFlag = false;
                AIMIApplication.isMineFragmentShow = false;
                hideAll();
                img_total.setImageResource(R.drawable.miw1);
                tv_total.setTextColor(getResources().getColor(R.color.my_bk));
                hideFragment(transaction);
                if (mFragment_total==null){
                    mFragment_total = new Fragment_Total();
                    transaction.add(R.id.fragment_container,mFragment_total);
                }else{
                    transaction.show(mFragment_total);
                }

                break;
            case R.id.ll_search:
                showFlag = false;
                AIMIApplication.isMineFragmentShow = false;
                hideAll();
                img_search.setImageResource(R.drawable.guangguang1);
                tv_search.setTextColor(getResources().getColor(R.color.my_bk));
                hideFragment(transaction);
                if (mFragment_search == null){
                    mFragment_search = new Fragment_Search();
                    transaction.add(R.id.fragment_container,mFragment_search);
                }else{
                    transaction.show(mFragment_search);
                }

                break;
            case R.id.rl_message:
                showFlag = true;
                AIMIApplication.isMineFragmentShow = false;
                hideAll();
                ((AIMIApplication)getApplicationContext()).unReachNums = 0;
                tv_newmsg.setVisibility(View.GONE);
                img_message.setImageResource(R.drawable.xiaoxi1_b);
                tv_message.setTextColor(getResources().getColor(R.color.my_bk));
                hideFragment(transaction);
                if (mFragment_message == null){
                    mFragment_message = new Fragment_Message();
                    transaction.add(R.id.fragment_container,mFragment_message);
                }else {
                    transaction.show(mFragment_message);
                }

                break;
            case R.id.ll_mine:
                //用户发布完图片后马上刷新
                if (isMineRefresh){
                    if (mFragment_mine != null){
                        mFragment_mine.requestData();
                    }
                    isMineRefresh = false;
                }
                showFlag = false;
                AIMIApplication.isMineFragmentShow = true;
                hideAll();
                img_mine.setImageResource(R.drawable.my1);
                tv_mine.setTextColor(getResources().getColor(R.color.my_bk));
                hideFragment(transaction);
                if (mFragment_mine == null){
                    mFragment_mine = new Fragment_Mine();
                    transaction.add(R.id.fragment_container,mFragment_mine);
                }else {
                    transaction.show(mFragment_mine);
                }
                break;
            case R.id.btn_update:
                if(!TextUtils.isEmpty(mUpdateurl)){

                    mDownLoadTask.execute(mUpdateurl);
                    mDialog.dismiss();
                    mDialog_download.show();

                }


                break;
            case R.id.imgbtn_cancel:
                if(mDialog.isShowing()){
                    mDialog.dismiss();
                }
                break;
        }
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private DealDoubleClick mDealDoubleClick;

    @Override
    public void onDownLoadListener(int i) {

    }

    @Override
    public void onDownLoadListenerFinish(String s) {

    }

    @Override
    public void onDownLoadListenerCancel(String s) {

    }

    public interface DealDoubleClick{
        void dealDoubleClick();
    }

    public void setDealDoubleClick(DealDoubleClick dealDoubleClick){
        mDealDoubleClick = dealDoubleClick;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    @Override
    protected void onResume() {
        super.onResume();
       // updateindex=0;
        if(updateindex>-1&&!mDialog.isShowing()){
         mDialog.show();
        }
        HXNotifier.getInstance().reset();
        EMChatManager.getInstance().registerEventListener(this, new EMNotifierEvent.Event[]{EventNewMessage});//注册全部消息监听
        if (((AIMIApplication)getApplicationContext()).unReachNums == 0){
            tv_newmsg.setVisibility(View.GONE);
        }else {
            redPointVisbile();
        }
    }

    @Override
    public void onEvent(EMNotifierEvent emNotifierEvent) {
        final EMMessage message = (EMMessage) emNotifierEvent.getData();
        String tempPara = message.getStringAttribute("para", "");
        String transCode = message.getStringAttribute("transCode", "");
        Log.i("ButtomMainActivity", "主页activity中有新消息来！message.toString : " + message.toString() + " , tempPara = " + tempPara + "transcode" + message.getStringAttribute("transCode", ""));
        //游戏中带的扩展
        mMessageAttr = message.getStringAttribute("arrive" , "");
        if (mMessageAttr.equals("end")){//代表游戏中发的消息
            TextMessageBody textMessageBody = (TextMessageBody) message.getBody();
            Log.e("game", "onEvent: "+textMessageBody.getMessage());
            String messageContent = textMessageBody.getMessage();
            if (messageContent.equals("开始")){//游戏开始
                mGameListener.gameStart();
            }else if (messageContent.equals("到达")){//你到达终点
                mGameListener.gameEnd();
            }else if (messageContent.equals("结束")){//游戏结束
                mGameListener.gameOver();
            }
            return;
        }
        mChatType = message.getChatType();
        switch (emNotifierEvent.getEvent()) {
            case EventNewMessage: {//接受新消息
                simpleCache = SimpleMessageCache.getInstance(mActivity);
                //获取到message
                String username = null;
                //群组消息
                if (message.getChatType() == EMMessage.ChatType.GroupChat || message.getChatType() == EMMessage.ChatType.ChatRoom) {
                    username = message.getTo();
                    getGroupChatInstance(username);
                } else {
                    //单聊消息
                    username = message.getFrom();
                }
                //当消息的扩展为1的时候 为 吧台见 三个字 将这条消息删除
                EMConversation conversation = EMChatManager.getInstance().getConversation(username);
                if (message.getStringAttribute("type", "").equals("1")) {
                    conversation.removeMessage(message.getMsgId());
                }
                //单聊
                EMConversation commentCon = EMChatManager.getInstance().getConversationByType(username, EMConversation.EMConversationType.Chat);
                //如果是系统消息
                boolean isSystemMsg = message.getBooleanAttribute("isSystemMsg", false);
                if (isSystemMsg) {
                    try {
                        if (transCode.equals("send10150")) {
                            //处理关注状态修改的通知
                            dealAttentionRelationship(commentCon,message);
                            toRefresh();
                        } else if (transCode.equals("send10231")) {
                            //赞 取消赞 评论 通知
                            dealOtherMessage(commentCon,message);
                            toRefresh();
                        } else if (transCode.equals("send20001")) {
                        } else if (transCode.equals("cc0001")) {
                        } else if (transCode.equals("cc0006")) {
                        } else {
                            //处理系统发来的消息(2.0.0版本处理)
                            SystemMsgUtil.dealSystemMessage(message, mActivity);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //接收到聊天消息的时候 会通知HibarChatframent进行刷新操作，重新从环信加载聊天消息，来刷新会话数量
                //消息提示的自加和会话数量的刷新
                if (message.getChatType() == EMMessage.ChatType.Chat){
                    if (transCode.equals("cc0001")){
                        if (!message.getStringAttribute("type", "").equals("1")&&
                                !message.getStringAttribute("arrive" , "").equals("end")) {//缘分牌的 游戏的
                            dealUnReachNums();
                            toRefresh();
                        }
                    }
                }
                //消息红点的显示
                if (!message.getStringAttribute("arrive" , "").equals("end")){//游戏的不显示
                    redPointVisbile();
                }
                break;
            }
            case EventDeliveryAck: {//接收已发送回执
                break;
            }
            case EventReadAck: {
                break;
            }
            case EventOfflineMessage: {
                break;
            }
            default:
                break;
        }
    }

    /**
     * 获得群组的实例
     * @param username
     */
    private void getGroupChatInstance(String username){
        try {
            EMGroup returnGroup = EMGroupManager.getInstance().getGroup(username);
            if (returnGroup == null) {
                //获取群组实例
                new MyThread1(username).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            //获取群组实例
            new MyThread1(username).start();
        }
    }

    /**
     * 获取单个群组实例
     */
    public class MyThread1 extends Thread {
        private String thirsGroupId;

        public MyThread1(String thirsGroupId) {
            this.thirsGroupId = thirsGroupId;
        }

        public void run() {
            // 在这里进行 http request.网络请求相关操作
            try {
                final EMGroup returnGroup = EMGroupManager.getInstance().getGroupFromServer(thirsGroupId);
                // 更新本地数据
                EMGroupManager.getInstance().createOrUpdateLocalGroup(returnGroup);
                EMGroupManager.getInstance().loadAllGroups();
                Log.i(TAG, "获取群组实例成功");
            } catch (EaseMobException e) {
                // TODO Auto-generated catch block
                Log.i(TAG, "获取群组实例失败！");
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理关注状态修改的通知
     * @param commentCon
     * @param message
     */
    private void dealAttentionRelationship(EMConversation commentCon,EMMessage message){
        //酒友关系变更通知
        String para = message.getStringAttribute("para", "");
        Log.e(TAG, "onEvent: " + para);
        JSONObject paraObject = JSONObject.parseObject(para);
        String userId = paraObject.getString("userId");
        String userName = paraObject.getString("userName");
        String userHeadImg = paraObject.getString("userHeadImg");
        Date time = paraObject.getDate("time");
        int type = paraObject.getIntValue("type");//状态	0 取消关注  1 关注2 互相关注
        if (type == 0) {
            //取消关注
        } else if (type == 1) {//关注
            dealUnReachNums();
            ReplyCacheBean messageBean = new ReplyCacheBean();
            SessionCache session = SessionCache.getInstance(mActivity);
            messageBean.setMyUserId(session.userId);
            messageBean.setUserHeadImg(userHeadImg);
            messageBean.setUserId(userId);
            messageBean.setUserName(userName);
            messageBean.setTypeInt(type);
            messageBean.setUserTime(time);
            messageBean.setMsgId(message.getMsgId());
            ReplyCache cache = ReplyCache.getCommentCache(mActivity);
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
        } else if (type == 2) {
            //相互关注
        }
        //设为已读
        commentCon.markMessageAsRead(message.getMsgId());
        //清除消息
        commentCon.removeMessage(message.getMsgId());
    }

    /**
     * 其他消息处理
     * @param commentCon
     * @param message
     */
    private void dealOtherMessage(EMConversation commentCon , EMMessage message){
        //0 取消赞
        //1 攒了我
        //2 评论了我
        dealUnReachNums();
        String para = message.getStringAttribute("para","");
        Log.i(TAG, "para = " + para);
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
        SessionCache session = SessionCache.getInstance(mActivity);
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
        bean.setMsgId(message.getMsgId());

        if (bean.getType() == 1 || bean.getType() == 2) {
            ReplyCache cache = ReplyCache.getCommentCache(mActivity);
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
            simpleCache.isNewMy = true;
            simpleCache.save();
        }
        //设为已读
        commentCon.markMessageAsRead(message.getMsgId());
        //清除消息
        commentCon.removeMessage(message.getMsgId());
    }

    /**
     * 未读消息数量自加
     */
    public void dealUnReachNums(){
        if (mFragment_message == null){
            mFragment_message = new Fragment_Message();
            if (!showFlag){
                ((AIMIApplication)getApplicationContext()).unReachNums++;
            }
        }else {
            if (!showFlag){
                ((AIMIApplication)getApplicationContext()).unReachNums++;
            }
        }
    }

    /**
     * 通知消息界面刷新
     */
    public void toRefresh() {
        Message msg = new Message();
        msg.what = 0;
        if (mFragment_message == null){
            mFragment_message = new Fragment_Message();
            mFragment_message.mHandlerPlus.sendMessage(msg);
        }else {
            mFragment_message.mHandlerPlus.sendMessage(msg);
        }

    }

    /**
     * 红点显示
     */
    public void redPointVisbile(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!showFlag){
                    if (mChatType != EMMessage.ChatType.GroupChat){
                        if (((AIMIApplication)getApplicationContext()).unReachNums<=99){
                            tv_newmsg.setVisibility(View.VISIBLE);
                            tv_newmsg.setText(((AIMIApplication)getApplicationContext()).unReachNums+"");
                        }else{
                            tv_newmsg.setVisibility(View.VISIBLE);
                            tv_newmsg.setText("99+");
                        }
                    }
                }
            }
        });
    }

    public class HandlerPlus extends WeakHandler{

        public HandlerPlus(Object o) {
            super(o);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            HomeActivity activity = (HomeActivity) getObjct();
            if (activity != null){
                switch (msg.what){
                    case 1:
                        activity.toRefresh();
                        break;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMChatManager.getInstance().unregisterEventListener(this);
        activityInstance = null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //用户从通知栏点击进入后显示消息页
        if (intent.getBooleanExtra("notify_flag",false)){
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            hideAll();
            img_message.setImageResource(R.drawable.xiaoxi1_b);
            tv_message.setTextColor(getResources().getColor(R.color.my_bk));
            hideFragment(transaction);
            if (mFragment_message == null){
                mFragment_message = new Fragment_Message();
                transaction.add(R.id.fragment_container,mFragment_message);
            }else {
                transaction.show(mFragment_message);
            }
            transaction.commit();
            ((AIMIApplication)getApplicationContext()).unReachNums = 0;
            tv_newmsg.setVisibility(View.GONE);
        }
        //用户发布新图片以后默认显示首页
        if (intent.getBooleanExtra(UserFaBuActivity.FABU_FLAG,false)){
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            hideAll();
            hideFragment(transaction);
            img_total.setImageResource(R.drawable.miw1);
            tv_total.setTextColor(getResources().getColor(R.color.my_bk));
            if (mFragment_total == null){
                mFragment_total = new Fragment_Total();
                transaction.add(R.id.fragment_container,mFragment_total);
            }else {
                transaction.show(mFragment_total);
            }
            transaction.commit();
        }
    }

    public interface GameListener{
        void gameStart();//游戏开始
        void gameEnd();//游戏中你到达终点
        void gameOver();//游戏全部结束
    }

    public void setOnGameListener(GameListener gameListener){
        mGameListener = gameListener;
    }

}
