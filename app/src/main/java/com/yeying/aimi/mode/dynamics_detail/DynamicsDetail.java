package com.yeying.aimi.mode.dynamics_detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.bumptech.glide.Glide;
import com.yeying.aimi.API;
import com.yeying.aimi.R;
import com.yeying.aimi.adapter.MyDetailsAdapter;
import com.yeying.aimi.aimibase.AIMIApplication;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.bean.DongTaiBean;
import com.yeying.aimi.bean.ReplyBean;
import com.yeying.aimi.mode.login.LoginActivity;
import com.yeying.aimi.mode.otherdetails.MineHomepage;
import com.yeying.aimi.mode.otherdetails.OtherHomepage;
import com.yeying.aimi.mode.otherdetails.PreViewShowActivity;
import com.yeying.aimi.protoco.DefaultTask;
import com.yeying.aimi.protoco.IProtocol;
import com.yeying.aimi.protocol.impl.P10128;
import com.yeying.aimi.protocol.impl.P10129;
import com.yeying.aimi.protocol.impl.P10141;
import com.yeying.aimi.protocol.impl.P10150;
import com.yeying.aimi.protocol.impl.P10325;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.utils.PromptUtils;
import com.yeying.aimi.utils.WeakHandler;
import com.yeying.aimi.views.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanchengkeji on 2017/9/14.
 */

public class DynamicsDetail extends BaseActivity implements OnRefreshListener, OnLoadMoreListener, View.OnClickListener {

    private static final int LIKE = 1;

    private SessionCache session;
    private SwipeToLoadLayout swipeToLoadLayout;

    private ImageView hanging;
    private TextView name;
    private TextView favour_num;
    private TextView date;
    private TextView bar_name;
    private Button followed;
    private Button attention;
    private RoundImageView head;
    private RelativeLayout like;
    private RelativeLayout dislike;
    private RoundImageView user1;
    private RoundImageView user2;
    private RoundImageView user3;
    private RoundImageView user4;
    private RoundImageView user5;
    private Button mutual;
    private ImageView sexImage;
    private TextView age;
    private TextView xingzuo;
    private RelativeLayout lickClick;
    private TextView sketch;
    private ListView listview;
    private LinearLayout arrow;
    private TextView title_name;
    private ImageView title_back;
    private ImageButton title_menu;

    private String messageCode;
    private String messageName;
    private String backUrl;
    private int wight;
    private int height;
    private String info;
    private int pageNum=1;
    private boolean isLoading=false;
    private String imgurl;
    private String wordUserId;
    private int isAttention;
    private int attentionType;
    private int likenum;
    private java.util.Date newdate;
    private List<ReplyBean> list = new ArrayList<>();
    private String messageString;
    private MyDetailsAdapter adapter;

    private String userIdString5;
    private String userIdString2;
    private String userIdString3;
    private String userIdString4;
    private String userIdString1;
    private boolean flag = false;
    private int kspType = 1;

    private HandlerPlus mHandlerPlus;

    private PopupWindow popupWindow_guanzhu;
    private View view_guanzhu;
    private int state;

    private PopupWindow pullDownList;

    private EditText details_content;
    private TextView details_send;

    private PopupWindow pop;

    private String otherUserId;
    private DetailsTask mDetailsTask;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamics);
        session = SessionCache.getInstance(this);
        mHandlerPlus = new HandlerPlus(this);
        initIntent();
        initViews();
        initEvents();
    }

    private void initEvents() {
        if (backUrl != null) {
            Glide.with(this).load(backUrl).into(hanging);
        }
        requestData();
    }

    private void initIntent() {
        Intent intent = getIntent();
        messageCode = intent.getStringExtra("messageCode");
        messageName = intent.getStringExtra("messageName");
        backUrl = intent.getStringExtra("backUrl");
        wight = intent.getIntExtra("wight",0);
        height = intent.getIntExtra("height",0);
        info = intent.getStringExtra("info");
    }

    public static void toDynamics(Context context , String messageCode , String messageName , String backUrl , int wight , int height , String info){
        Intent intent = new Intent(context , DynamicsDetail.class);
        intent.putExtra("messageCode" , messageCode);
        intent.putExtra("messageName" , messageName);//酒吧或者是距离
        intent.putExtra("backUrl" , backUrl);
        intent.putExtra("wight" , wight);
        intent.putExtra("height" , height);
        intent.putExtra("info" , info);//可不传
        context.startActivity(intent);
    }

    private void initViews() {
        details_content = (EditText) findViewById(R.id.details_content);
        details_send = (TextView) findViewById(R.id.details_send);
        details_send.setOnClickListener(this);
        title_back = (ImageView) findViewById(R.id.title_back);
        title_back.setOnClickListener(this);
        title_menu = (ImageButton) findViewById(R.id.title_menu);
        title_menu.setOnClickListener(this);
        swipeToLoadLayout= (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout_details);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setRefreshEnabled(false);
        title_name = (TextView) findViewById(R.id.title_name);
        title_name.setText("照片");
        sketch = (TextView) findViewById(R.id.details_sketch);
        hanging = (ImageView) findViewById(R.id.details_hanging);//酒吧背景图
        hanging.setOnClickListener(this);
        listview = (ListView) findViewById(R.id.details_listview);
        name = (TextView) findViewById(R.id.details_name);//用户名
        favour_num = (TextView) findViewById(R.id.favour_num);//点赞数量
        date = (TextView) findViewById(R.id.details_date);//点赞日期
        bar_name = (TextView) findViewById(R.id.bar_name);//酒吧名称
        followed = (Button) findViewById(R.id.details_followed);//已关注
        followed.setOnClickListener(this);
        attention = (Button) findViewById(R.id.details_attention);//关注
        sexImage = ((ImageView) findViewById(R.id.details_gril)); //性别
        age = ((TextView) findViewById(R.id.details_age)); //年龄
        xingzuo = ((TextView) findViewById(R.id.deails_xingzuo));//星座
        attention.setOnClickListener(this);
        mutual = (Button) findViewById(R.id.details_mutual);//相互关注
        mutual.setOnClickListener(this);
        head = (RoundImageView) findViewById(R.id.details_head);//获取头像
        head.setOnClickListener(this);
        like = (RelativeLayout) findViewById(R.id.details_like);
        lickClick = ((RelativeLayout) findViewById(R.id.oo));
        dislike = (RelativeLayout) findViewById(R.id.details_dislike);
        user1 = (RoundImageView) findViewById(R.id.im_user1);
        arrow = (LinearLayout) findViewById(R.id.arrow);
        user1.setOnClickListener(this);
        user2 = (RoundImageView) findViewById(R.id.im_user2);
        user2.setOnClickListener(this);
        user3 = (RoundImageView) findViewById(R.id.im_user3);
        user3.setType(RoundImageView.TYPE_ROUND);
        user4 = (RoundImageView) findViewById(R.id.im_user4);
        user4.setType(RoundImageView.TYPE_ROUND);
        user5 = (RoundImageView) findViewById(R.id.im_user5);
        user5.setType(RoundImageView.TYPE_ROUND);
        lickClick.setOnClickListener(this);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        if (messageCode != null) {
            P10325 p10325 = new P10325();
            p10325.req.sessionId = session.sessionId;
            p10325.req.messageId = messageCode;
            p10325.req.locationX = TextUtils.isEmpty(session.locationX) ? 0.0 : Double.parseDouble(session.locationX);
            p10325.req.locationY = TextUtils.isEmpty(session.locationY) ? 0.0 : Double.parseDouble(session.locationY);
            p10325.req.pageNum = pageNum;
            p10325.req.pageSize = 20;
            isLoading=true;
            mDetailsTask = new DetailsTask();
            mDetailsTask.execute(getApplicationContext(), p10325);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.oo:
                if (messageCode != null) {
                    P10141 p10141 = new P10141();
                    p10141.req.userId = session.userId;
                    p10141.req.messageId = messageString;
                    p10141.req.type = LIKE;
                    p10141.req.sessionId = session.sessionId;
                    p10141.req.likeType = LIKE;
                    new LikeTask().execute(getApplicationContext(), p10141);
                }
                break;
            case R.id.details_followed:
                //已关注
                kspType = 2;
                createPopWindow_guanzhu();
                break;
            case R.id.details_attention:

                kspType = 1;
                addNotice(kspType);
                break;
            case R.id.details_mutual:
                //相互关注
                kspType = 2;
                createPopWindow_guanzhu();
                break;
            case R.id.bt_close:
                if (popupWindow_guanzhu!=null&&popupWindow_guanzhu.isShowing()){
                    popupWindow_guanzhu.dismiss();
                }
                break;
            case R.id.bt_sure:
                addNotice(kspType);
                break;
            case R.id.title_back:
                //返回
                finish();
                break;
            case R.id.title_menu:
                //菜单键
                pullDown();
                break;
            case R.id.ram_delete:
                initDelete();//删除动态
                break;
            case R.id.ram_report://举报
                PromptUtils.showToast(this, "举报成功");
                pullDownList.dismiss();
                break;
            case R.id.ram_lahei://拉黑
                PromptUtils.showToast(this, "已将该用户拉入黑名单");
                pullDownList.dismiss();
                break;
            case R.id.details_send:
                //发送评论
                if (!TextUtils.isEmpty(details_content.getText().toString())){
                    if (messageCode != null){
                        hidenInputMethod(details_send,DynamicsDetail.this);
                        P10128 p10128 = new P10128();
                        p10128.req.sessionId = session.sessionId;
                        p10128.req.messageId = messageCode;
                        p10128.req.content = details_content.getText().toString();
                        new CommentTask().execute(getApplicationContext(), p10128);
                        details_content.setText("");
                    }
                }
                break;
            case R.id.details_head:
                //进入他人主页
                Log.e("iii", "onClick: "+otherUserId);
                toOtherHomePage(otherUserId);
                break;
            case R.id.details_hanging:
                //点击展示大图
                PreViewShowActivity.toPreView(DynamicsDetail.this,backUrl);
                break;
            case R.id.im_user1:
                toOtherHomePage(userIdString1);
                break;
            case R.id.im_user2:
                toOtherHomePage(userIdString2);
                break;
            case R.id.im_user3:
                toOtherHomePage(userIdString3);
                break;
            case R.id.im_user4:
                toOtherHomePage(userIdString4);
                break;
            case R.id.im_user5:
                toOtherHomePage(userIdString5);
                break;
        }
    }

    private void toOtherHomePage(String userId){
        if (userId != null){
            if (userId.equals(session.userId)){
                MineHomepage.toOtherHomePage(DynamicsDetail.this,userId,
                        TextUtils.isEmpty(session.locationX) ? 0.0 : Double.parseDouble(session.locationX),
                        TextUtils.isEmpty(session.locationY) ? 0.0 : Double.parseDouble(session.locationY),true);
            }else {
                OtherHomepage.toOtherHomePage(DynamicsDetail.this,userId,
                        TextUtils.isEmpty(session.locationX) ? 0.0 : Double.parseDouble(session.locationX),
                        TextUtils.isEmpty(session.locationY) ? 0.0 : Double.parseDouble(session.locationY),false);
            }
        }
    }

    private void requestData(){
        if (messageCode != null) {
            pageNum=1;
            isLoading=false;
            P10325 p10325 = new P10325();
            p10325.req.sessionId = session.sessionId;
            p10325.req.messageId = messageCode;
            if (session.locationX == null && session.locationY == null) {
                session.locationX = "0";
                session.locationY = "0";
            }
            p10325.req.locationX = TextUtils.isEmpty(session.locationX) ? 0.0 : Double.parseDouble(session.locationX);
            p10325.req.locationY = TextUtils.isEmpty(session.locationY) ? 0.0 : Double.parseDouble(session.locationY);
            p10325.req.pageNum = 0;
            p10325.req.pageSize = 0;
            mDetailsTask = new DetailsTask();
            mDetailsTask.execute(getApplicationContext(), p10325);
        }
    }

    //隐藏输入法
    public void hidenInputMethod(View view, Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public class DetailsTask extends DefaultTask {

        @Override
        public void onError(DefaultError obj) {
            // TODO Auto-generated method stub
            super.onError(obj);
            PromptUtils.showToast(DynamicsDetail.this, "网络错误");
            if (swipeToLoadLayout.isLoadingMore()){
                swipeToLoadLayout.setLoadingMore(false);
            }
            if (swipeToLoadLayout.isRefreshing()){
                swipeToLoadLayout.setRefreshing(false);
            }
        }

        @Override
        public void onOk(IProtocol protocol) {
            // TODO Auto-generated method stub
            super.onOk(protocol);
            P10325 p10325 = (P10325) protocol;
            if (p10325.resp.transcode.equals("9999")) {
                if (p10325.resp.msg.contains("用户未登录")) {
                    session.phone = "";
                    session.save();
                    Intent intent = new Intent(DynamicsDetail.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    PromptUtils.showToast(DynamicsDetail.this,"系统异常");
                }
            } else {
                if (swipeToLoadLayout.isLoadingMore()){
                    swipeToLoadLayout.setLoadingMore(false);
                }
                if (swipeToLoadLayout.isRefreshing()){
                    swipeToLoadLayout.setRefreshing(false);
                }
                pageNum++;
                DongTaiBean wordBean = p10325.resp.word;
                imgurl = p10325.resp.imgUrl;
                wordUserId = p10325.resp.word.getWordUserId();
                isAttention = p10325.resp.word.getIsAttention();
                //TODO  设置酒吧名字和距离
                String barname=p10325.resp.word.getBarName();
                String bardistance=p10325.resp.word.getDistance();
                if (barname!=null&&!barname.equals("")){
                    bar_name.setText(barname);
                }else{
                    bar_name.setText(bardistance+"km");
                }
                initValuation(wordBean);
                initLikePrint(wordBean);
                otherUserId = p10325.resp.word.getWordUserId();
            }
        }
    }

    private void initLikePrint(DongTaiBean wordBean) {
        // TODO Auto-generated method stub
        if (wordBean.getLikeUsers().size() > 0) {
            for (int i = 0; i < wordBean.getLikeUsers().size(); i++) {
                if (i == 0) {
                    user1.setVisibility(View.VISIBLE);
                    userIdString1 = wordBean.getLikeUsers().get(i).getUserId();
                    Glide.with(this).load(AIMIApplication.dealHeadImg(wordBean.getLikeUsers().get(i).getHeadImg())).into(user1);
                } else if (i == 1) {
                    user2.setVisibility(View.VISIBLE);
                    Glide.with(this).load(AIMIApplication.dealHeadImg(wordBean.getLikeUsers().get(i).getHeadImg())).into(user2);
                    userIdString2 = wordBean.getLikeUsers().get(i).getUserId();
                } else if (i == 2) {
                    user3.setVisibility(View.VISIBLE);
                    Glide.with(this).load(AIMIApplication.dealHeadImg(wordBean.getLikeUsers().get(i).getHeadImg())).into(user3);
                    userIdString3 = wordBean.getLikeUsers().get(i).getUserId();
                } else if (i == 3) {
                    user4.setVisibility(View.VISIBLE);
                    Glide.with(this).load(AIMIApplication.dealHeadImg(wordBean.getLikeUsers().get(i).getHeadImg())).into(user4);
                    userIdString4 = wordBean.getLikeUsers().get(i).getUserId();
                } else if (i == 4) {
                    user5.setVisibility(View.VISIBLE);
                    Glide.with(this).load(AIMIApplication.dealHeadImg(wordBean.getLikeUsers().get(i).getHeadImg())).into(user5);
                    userIdString5 = wordBean.getLikeUsers().get(i).getUserId();
                }
            }
        }


    }

    /**
     * 获取数据并赋值
     *
     * @param
     */
    private void initValuation(DongTaiBean wordBean) {
        // TODO Auto-generated method stub

        if (!isLoading){
            messageString = wordBean.getMessageId();
            Glide.with(DynamicsDetail.this)
                    .load(AIMIApplication.dealHeadImg(wordBean.getHeadImg())).placeholder(R.drawable.default_icon)//没有加载图片时显示的默认图像
                    .error(R.drawable.default_icon)// 图像加载错误时显示的图像

                    .into(head);// 被加载的控件
            Log.e(TAG, "initValuation: "+imgurl+wordBean.getHeadImg());
            if (!TextUtils.isEmpty(wordBean.getMessage())){
                sketch.setVisibility(View.VISIBLE);
                sketch.setText(wordBean.getMessage());
            }
            name.setText(wordBean.getWordUserName());
            if (wordBean.getWordUserGender() == 1){
                sexImage.setImageResource(R.drawable.gender_man);
            }else {
                sexImage.setImageResource(R.drawable.gender_woman);
            }
            xingzuo.setText(wordBean.getWordUserConstellation());
            age.setText(wordBean.getWordUserAge() + "");
            /**
             * 改变关注状态，
             */
            Log.i("isAttention", "关注状态: " + isAttention);
            if (wordBean.getWordUserId().equals(session.userId)){
                attention.setVisibility(View.GONE);
                followed.setVisibility(View.GONE);
                mutual.setVisibility(View.GONE);
            }else {
                if (isAttention == 0){
                    attention.setVisibility(View.VISIBLE);
                    followed.setVisibility(View.GONE);
                    mutual.setVisibility(View.GONE);
                }else if (isAttention == 1){
                    attention.setVisibility(View.GONE);
                    followed.setVisibility(View.VISIBLE);
                    mutual.setVisibility(View.GONE);
                }else if (isAttention == 3){
                    attention.setVisibility(View.GONE);
                    followed.setVisibility(View.GONE);
                    mutual.setVisibility(View.VISIBLE);
                }
            }

            attentionType = wordBean.getLikeStatus();
            newdate = wordBean.getCreateTime();
            String dateString = API.getDateStrSimple(newdate);
            date.setText(dateString);
            likenum = wordBean.getLikeNum();
            favour_num.setText("赞 " + likenum);
            list.clear();
            list.addAll(wordBean.getReplys());
            if (attentionType == 0) {
                like.setVisibility(View.VISIBLE);
                dislike.setVisibility(View.GONE);
            } else if (attentionType == 1) {
                like.setVisibility(View.GONE);
                dislike.setVisibility(View.VISIBLE);
            }
        }else{
            list.addAll(wordBean.getReplys());
        }
        initAdapterData();
    }

    private void initAdapterData() {
        adapter = new MyDetailsAdapter(list, DynamicsDetail.this, imgurl);
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        setListViewHeight(listview);
    }

    /**
     * 重新计算ListView的高度，解决ScrollView和ListView两个View都有滚动的效果，在嵌套使用时起冲突的问题
     *
     * @param listView
     */
    public void setListViewHeight(ListView listView) {

        // 获取ListView对应的Adapter

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public class LikeTask extends DefaultTask {

        @Override
        public void preExecute() {
            super.preExecute();
            pop = PromptUtils.getProgressDialogPop(DynamicsDetail.this);
            pop.showAtLocation(getWindow().getDecorView(),Gravity.CENTER,0,0);
        }

        @Override
        public void onOk(IProtocol protocol) {
            // TODO Auto-generated method stub
            super.onOk(protocol);
            Log.e("iiii", "onError: 点赞请求成功");
            if (pop != null && pop.isShowing()){
                pop.dismiss();
            }
            P10141 p10141 = (P10141) protocol;
            if (p10141.resp.transcode.equals("9999")) {
                PromptUtils.showToast(DynamicsDetail.this, p10141.resp.msg);
            } else {
                Message message = mHandlerPlus.obtainMessage();
                message.obj = messageCode;
                message.what = LIKE;
                mHandlerPlus.sendMessage(message);
            }

        }

        @Override
        public void onError(DefaultError obj) {
            // TODO Auto-generated method stub
            super.onError(obj);
            if (pop != null && pop.isShowing()){
                pop.dismiss();
            }
            PromptUtils.showToast(DynamicsDetail.this, "网络错误");
            Log.e("iiii", "onError: 点赞请求错误");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.KEYCODE_BACK){
            if (pop != null && pop.isShowing()){
                pop.dismiss();
            }
            if (pullDownList != null && pullDownList.isShowing()){
                pullDownList.dismiss();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private class HandlerPlus extends WeakHandler{

        public HandlerPlus(Object o) {
            super(o);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DynamicsDetail dynamicsDetail = (DynamicsDetail) mHandlerPlus.getObjct();
            if (dynamicsDetail == null) {
                return;
            }
            dynamicsDetail.messageCode = (String) msg.obj;
            if (msg.what == 0) {
                dynamicsDetail.flag = true;
            } else if (msg.what == LIKE) {
                dynamicsDetail.flag = false;
            }
            dynamicsDetail.requestData();
        }
    }

    private void createPopWindow_guanzhu(){
        if (view_guanzhu==null){
            view_guanzhu= LayoutInflater.from(this).inflate(R.layout.del_msg_window,null,false);
            TextView tv_msg= (TextView) view_guanzhu.findViewById(R.id.tv_msg);
            TextView bt_close= (TextView) view_guanzhu.findViewById(R.id.bt_close);
            TextView bt_sure= (TextView) view_guanzhu.findViewById(R.id.bt_sure);
            tv_msg.setText("确认不再关注TA吗？");
            bt_close.setText("取消");
            bt_sure.setText("确定");
            tv_msg.setOnClickListener(this);
            bt_sure.setOnClickListener(this);
            bt_close.setOnClickListener(this);
        }
        if (popupWindow_guanzhu==null){
            popupWindow_guanzhu=new PopupWindow(view_guanzhu, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        }
        popupWindow_guanzhu.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER,0,0);
    }

    public void addNotice(int type) {
        if (wordUserId != null) {
            P10150 p10150 = new P10150();
            p10150.req.sessionId = session.sessionId;
            p10150.req.transcode = "10150";
            p10150.req.type = type;
            p10150.req.friendId = wordUserId;
            new addNoteTask().execute(getApplicationContext(), p10150);
        }

    }

    public class addNoteTask extends DefaultTask {
        @Override
        public void onError(DefaultError obj) {
            // TODO Auto-generated method stub
            super.onError(obj);
        }

        @Override
        public void onOk(IProtocol protocol) {
            // TODO Auto-generated method stub
            super.onOk(protocol);
            P10150 p10150 = (P10150) protocol;
            if (p10150.resp.transcode.equals("9999")) {
                PromptUtils.showToast(DynamicsDetail.this, p10150.resp.msg);
            } else {
                state = p10150.resp.state;
                isAttention = p10150.resp.isAttention;
                if (state == 1) {
                    if (popupWindow_guanzhu!=null&&popupWindow_guanzhu.isShowing()){
                        popupWindow_guanzhu.dismiss();
                    }
                    //关注成功
                    changeSuccess();
                } else {
                    //关注失败
                }
            }
        }
    }
    public void changeSuccess() {

        if (isAttention == 0 || isAttention == 2) {
            attention.setVisibility(View.VISIBLE);
            followed.setVisibility(View.GONE);
            mutual.setVisibility(View.GONE);
        } else if (isAttention == 1) {
            attention.setVisibility(View.GONE);
            followed.setVisibility(View.VISIBLE);
            mutual.setVisibility(View.GONE);
        } else if (isAttention == 3) {
            attention.setVisibility(View.GONE);
            followed.setVisibility(View.GONE);
            mutual.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 下拉列表
     */

    private void pullDown() {
        View view = View.inflate(this, R.layout.details_downlist, null);
        pullDownList = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, false);
        LinearLayout ram_dele = (LinearLayout) view.findViewById(R.id.ram_delete);
        LinearLayout ram_report = (LinearLayout) view.findViewById(R.id.ram_report);//举报视图
        LinearLayout ram_lahei = (LinearLayout) view.findViewById(R.id.ram_lahei);//拉黑
        if (wordUserId != null){
            if (wordUserId.equals(session.userId)) {//我的
                ram_report.setVisibility(View.GONE);
                ram_dele.setVisibility(View.VISIBLE);
                ram_lahei.setVisibility(View.GONE);
            } else {
                ram_dele.setVisibility(View.GONE);
                ram_report.setVisibility(View.VISIBLE);
                ram_lahei.setVisibility(View.VISIBLE);
            }
        }
        pullDownList.setOutsideTouchable(true);
        pullDownList.setFocusable(true);
        pullDownList.setBackgroundDrawable(new ColorDrawable(0));
        pullDownList.showAsDropDown(findViewById(R.id.title_menu), 0, 0);
        ram_dele.setOnClickListener(this);
        ram_report.setOnClickListener(this);
        ram_lahei.setOnClickListener(this);
    }

    /**
     * 删除动态
     */
    private void initDelete() {
        if (messageCode.length() != 0) {
            P10129 p10129 = new P10129();
            p10129.req.message_id = messageCode;
            p10129.req.sessionId = session.sessionId;
            new DeleteTask().execute(getApplicationContext(), p10129);
        }
    }

    public class DeleteTask extends DefaultTask {
        @Override
        public void onOk(IProtocol protocol) {
            // TODO Auto-generated method stub
            super.onOk(protocol);
            P10129 p10129 = (P10129) protocol;
            if (p10129.resp.transcode.equals("9999")) {
                PromptUtils.showToast(DynamicsDetail.this, p10129.resp.msg);
            } else {
                String status = p10129.resp.status;

                if (status.equals("1")) {
                    if (pullDownList.isShowing()){
                        pullDownList.dismiss();
                    }
                    PromptUtils.showToast(DynamicsDetail.this, "操作成功");
                    Intent intent = new Intent();
                    intent.putExtra("flag",true);
                    setResult(RESULT_OK,intent);
                    finish();
                } else {
                    PromptUtils.showToast(DynamicsDetail.this, p10129.resp.status);
                }
            }
        }

        @Override
        public void onError(DefaultError obj) {
            super.onError(obj);
        }
    }

    public class CommentTask extends DefaultTask {

        @Override
        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            P10128 p10128 = (P10128) protocol;
            if (p10128.resp.transcode.equals("9999")) {
                PromptUtils.showToast(DynamicsDetail.this, p10128.resp.msg);
            } else {
                requestData();
            }
        }

        @Override
        public void onError(DefaultError obj) {
            super.onError(obj);
            PromptUtils.showToast(DynamicsDetail.this, "网络错误");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDetailsTask = null;
    }
}
