package com.yeying.aimi.mode.otherdetails;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yeying.aimi.API;
import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.AIMIApplication;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.bean.FootMarkBean;
import com.yeying.aimi.bean.MineWordsBean;
import com.yeying.aimi.mode.mine.FollowAndFans;
import com.yeying.aimi.mode.mine.Fragment_Foot;
import com.yeying.aimi.mode.mine.Fragment_Photo;
import com.yeying.aimi.mode.person.PersonActivity;
import com.yeying.aimi.mode.person.SetActivity;
import com.yeying.aimi.mode.signlechat.SingleChat;
import com.yeying.aimi.mode.wallet.WalletActivity;
import com.yeying.aimi.protoco.DefaultTask;
import com.yeying.aimi.protoco.IProtocol;
import com.yeying.aimi.protocol.impl.P10150;
import com.yeying.aimi.protocol.impl.P10308;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.transformation.CompressTransform;
import com.yeying.aimi.utils.BlurTransformation;
import com.yeying.aimi.utils.PromptUtils;
import com.yeying.aimi.views.RoundImageView;
import com.yeying.aimi.views.ScrollViewPlus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanchengkeji on 2017/9/24.
 */

public class OtherHomepage extends BaseActivity implements View.OnClickListener {

    private ImageView mTopphotoOther;
    private FrameLayout mMengceng;
    private RoundImageView mHeadOther;
    private RoundImageView mEditOther;
    private RelativeLayout mHeadLOther;
    private TextView mNameOther;
    private TextView mFansOther;
    private TextView mAttentionOther;
    private TextView mPhotoOther;
    private TextView mFootOther;
    private TextView mLineLeftOther;
    private TextView mLineRightOther;
    private FrameLayout mContainerOther;
    private ScrollViewPlus mScrollOther;
    private FrameLayout mBgOther;
    private ImageView mMoneyOther;
    private ImageView mSetOther;
    private RelativeLayout mTopbarOther;
    private TextView mFollowOther;
    private TextView mChatOther;
    private RelativeLayout mRelMine;

    private SessionCache session;
    private List<MineWordsBean> mineWordsBeanList = new ArrayList<>();
    private List<FootMarkBean> footMarkBeanList = new ArrayList<>();
    private String headUrl;
    private Fragment_Photo mFragment_photo;
    private Fragment_Foot mFragment_foot;
    private ObjectAnimator mObjectAnimator;
    private ObjectAnimator mObjectAnimator1;
    private FragmentManager mFragmentManager;
    private int isSeefootmark;//int 0.未关注 1.关注 2.被关注 3.相互关注
    private int isFollw;//int 1.是 2.否
    private int isAttention;
    private PopupWindow popupWindow_guanzhu;
    private View view_guanzhu;
    private String friendId;
    private P10308.Resp mResp;
    private boolean isMine;
    private LinearLayout other_linear;
    private ImageView other_back;
    private PopupWindow pop;

    private BlurTransformation mBlurTransformation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        session =SessionCache.getInstance(this);
        mFragmentManager = getSupportFragmentManager();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        initIntent();
    }

    private void initIntent() {
        isMine = getIntent().getBooleanExtra("isMine",false);
        if (isMine){
            mHeadOther.setClickable(true);
            mFansOther.setClickable(true);
            mAttentionOther.setClickable(true);
            other_linear.setVisibility(View.GONE);
            mMoneyOther.setVisibility(View.GONE);
            mSetOther.setVisibility(View.GONE);
            other_back.setVisibility(View.VISIBLE);
            mEditOther.setVisibility(View.VISIBLE);
        }else {
            mHeadLOther.setClickable(true);
            mFansOther.setClickable(false);
            mAttentionOther.setClickable(false);
            other_linear.setVisibility(View.VISIBLE);
            mMoneyOther.setVisibility(View.GONE);
            mSetOther.setVisibility(View.GONE);
            other_back.setVisibility(View.VISIBLE);
            mEditOther.setVisibility(View.GONE);
        }
        friendId = getIntent().getStringExtra("userId");
        double locationX = getIntent().getDoubleExtra("locationX", 0);
        double locationY = getIntent().getDoubleExtra("locationY", 0);
        requestData(friendId,locationX,locationY);
    }

    private void initView() {
        other_back = (ImageView) findViewById(R.id.other_back);
        other_linear = (LinearLayout) findViewById(R.id.other_linear);
        mTopphotoOther = (ImageView) findViewById(R.id.other_topphoto);
        //mMengceng = (FrameLayout) findViewById(R.id.mengceng);
        mHeadOther = (RoundImageView) findViewById(R.id.other_head);
        mEditOther = (RoundImageView) findViewById(R.id.other_edit);
        mHeadLOther = (RelativeLayout) findViewById(R.id.other_head_l);
        mNameOther = (TextView) findViewById(R.id.other_name);
        mFansOther = (TextView) findViewById(R.id.other_fans);
        mAttentionOther = (TextView) findViewById(R.id.other_attention);
        mPhotoOther = (TextView) findViewById(R.id.other_photo);
        mFootOther = (TextView) findViewById(R.id.other_foot);
        mLineLeftOther = (TextView) findViewById(R.id.other_line_left);
        mLineRightOther = (TextView) findViewById(R.id.other_line_right);
        mContainerOther = (FrameLayout) findViewById(R.id.other_container);
        mScrollOther = (ScrollViewPlus) findViewById(R.id.other_scroll);
        mBgOther = (FrameLayout) findViewById(R.id.other_bg);
        mMoneyOther = (ImageView) findViewById(R.id.other_money);
        mSetOther = (ImageView) findViewById(R.id.other_set);
        mTopbarOther = (RelativeLayout) findViewById(R.id.other_topbar);
        mFollowOther = (TextView) findViewById(R.id.other_follow);
        mChatOther = (TextView) findViewById(R.id.other_chat);
        mScrollOther.setLimitHeight(getViewHeight(mTopphotoOther));
        mScrollOther.setContext(this);
        mScrollOther.setChangeTitleListener(new ScrollViewPlus.ChangeTitle() {
            @Override
            public void changeTitle() {
                mBgOther.setVisibility(View.VISIBLE);
                mObjectAnimator = ObjectAnimator.ofFloat(mBgOther,"alpha",0,1);
                mObjectAnimator.setDuration(500);
                mObjectAnimator.start();
            }

            @Override
            public void resetTitle() {
                mObjectAnimator1 = ObjectAnimator.ofFloat(mBgOther,"alpha",1,0);
                mObjectAnimator1.setDuration(500);
                mObjectAnimator1.start();
            }
        });
        initFragments();
        mAttentionOther.setOnClickListener(this);
        mFansOther.setOnClickListener(this);
        mPhotoOther.setOnClickListener(this);
        mFootOther.setOnClickListener(this);
        mFollowOther.setOnClickListener(this);
        mChatOther.setOnClickListener(this);
        other_back.setOnClickListener(this);
        mHeadOther.setOnClickListener(this);
        mEditOther.setOnClickListener(this);
        mSetOther.setOnClickListener(this);
        mMoneyOther.setOnClickListener(this);
    }

    private void initFragments() {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        mFragment_foot = new Fragment_Foot();
        mFragment_photo = new Fragment_Photo();
        fragmentTransaction.add(R.id.other_container,mFragment_foot);
        fragmentTransaction.add(R.id.other_container,mFragment_photo);
        hideFragment(fragmentTransaction);
        fragmentTransaction.show(mFragment_photo);
        fragmentTransaction.commit();
    }

    private void requestData(String userId,double locationX,double locationY){
        P10308 p10308 = new P10308();
        p10308.req.sessionId = session.sessionId;
        p10308.req.searchUserId = userId;
        p10308.req.locationX = locationX;
        p10308.req.locationY = locationY;
        new MineDataTask().execute(getApplicationContext(),p10308);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.other_attention:
                FollowAndFans.toFollowFans(OtherHomepage.this,friendId,false);
                break;
            case R.id.other_fans:
                FollowAndFans.toFollowFans(OtherHomepage.this,friendId,true);
                break;
            case R.id.other_photo:
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                hideColor();
                hideFragment(fragmentTransaction);
                mLineLeftOther.setBackgroundResource(R.drawable.line_yellow);
                if (mFragment_photo == null){
                    mFragment_photo = new Fragment_Photo();
                    fragmentTransaction.add(R.id.mine_container,mFragment_photo);
                }else {
                    fragmentTransaction.show(mFragment_photo);
                }
                fragmentTransaction.commit();
                break;
            case R.id.other_foot:
                if (isSeefootmark == 1){
                    //可以查看足迹
                    FragmentTransaction fragmentTransaction1 = mFragmentManager.beginTransaction();
                    hideColor();
                    hideFragment(fragmentTransaction1);
                    mLineRightOther.setBackgroundResource(R.drawable.line_yellow);
                    if (mFragment_foot == null){
                        mFragment_foot = new Fragment_Foot();
                        fragmentTransaction1.add(R.id.mine_container,mFragment_foot);
                        mFragment_foot.setMarkBeanList(footMarkBeanList,headUrl);
                    }else {
                        fragmentTransaction1.show(mFragment_foot);
                        mFragment_foot.setMarkBeanList(footMarkBeanList,headUrl);
                    }
                    fragmentTransaction1.commit();
                }else {
                    //不可以查看
                    if (isMine){//是自己的依然可以查看
                        FragmentTransaction fragmentTransaction1 = mFragmentManager.beginTransaction();
                        hideColor();
                        hideFragment(fragmentTransaction1);
                        mLineRightOther.setBackgroundResource(R.drawable.line_yellow);
                        if (mFragment_foot == null){
                            mFragment_foot = new Fragment_Foot();
                            fragmentTransaction1.add(R.id.mine_container,mFragment_foot);
                            mFragment_foot.setMarkBeanList(footMarkBeanList,headUrl);
                        }else {
                            fragmentTransaction1.show(mFragment_foot);
                            mFragment_foot.setMarkBeanList(footMarkBeanList,headUrl);
                        }
                        fragmentTransaction1.commit();
                    }else {
                        PromptUtils.showToast(OtherHomepage.this,"对方不允许查看足迹");
                    }
                }

                break;
            case R.id.other_follow:
                //添加 取消 关注
                if (isFollw == 0 || isFollw ==2){
                    //0.未关注 2.被关注
                    addNotice(friendId,1);
                }else {
                    createPopWindow_guanzhu();
                }
                break;
            case R.id.bt_close:
                if (popupWindow_guanzhu != null && popupWindow_guanzhu.isShowing()){
                    popupWindow_guanzhu.dismiss();
                }
                break;
            case R.id.bt_sure:
                if (popupWindow_guanzhu != null && popupWindow_guanzhu.isShowing()){
                    popupWindow_guanzhu.dismiss();
                }
                addNotice(friendId,2);
                break;
            case R.id.other_chat:
                SingleChat.toSingleChat(OtherHomepage.this,mResp.userName,mResp.userId, API.CHATTYPE_SINGLE,AIMIApplication.dealHeadImg(mResp.headUrl));
                Log.e(TAG, "onClick:-------> "+mResp.headUrl);
                Log.e(TAG, "onClick:-------> "+AIMIApplication.dealHeadImg(mResp.headUrl));
                break;
            case R.id.other_back:
                finish();
                break;
            case R.id.other_money:
                WalletActivity.toWallet(OtherHomepage.this);
                break;
            case R.id.other_head:
                //头像点击
                if (isMine){
                    startActivity(new Intent(OtherHomepage.this, PersonActivity.class));
                }else {
                    OtherDetail.toOtherDetail(OtherHomepage.this,mResp.userId);
                }
                break;
            case R.id.other_edit:
                //编辑资料
                startActivity(new Intent(OtherHomepage.this, PersonActivity.class));
                break;
            case R.id.other_set:
                //用户设置
                startActivity(new Intent(OtherHomepage.this, SetActivity.class));
                break;
        }
    }

    /**
     * 数据请求
     */
    class MineDataTask extends DefaultTask {

        @Override
        public void preExecute() {
            super.preExecute();
            pop = PromptUtils.getProgressDialogPop(OtherHomepage.this);
            pop.showAtLocation(getWindow().getDecorView(),Gravity.CENTER,0,0);
        }

        @Override
        public void onError(DefaultError obj) {
            super.onError(obj);
            PromptUtils.showToast(OtherHomepage.this,"网络错误");
            if (pop != null && pop.isShowing()){
                pop.dismiss();
            }
        }

        @Override
        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            final P10308 p10308 = (P10308) protocol;
            if (pop != null && pop.isShowing()){
                pop.dismiss();
            }
            if (p10308.resp.transcode.equals("9999")){
                PromptUtils.showToast(OtherHomepage.this,"系统异常");
            }else {
                mResp = p10308.resp;
                Glide.with(OtherHomepage.this)
                        .load(AIMIApplication.dealHeadImg(p10308.resp.headUrl))
                        .transform(new CompressTransform(OtherHomepage.this , p10308.resp.imgUrl+p10308.resp.headUrl))
                        .centerCrop()
                        .placeholder(R.drawable.default_icon)
                        .into(mHeadOther);
                mBlurTransformation = new BlurTransformation(OtherHomepage.this , p10308.resp.imgUrl+p10308.resp.headUrl);
                Glide.with(OtherHomepage.this)
                        .load(AIMIApplication.dealHeadImg(p10308.resp.headUrl))
                        .placeholder(R.drawable.default_icon)
                        .override(mTopphotoOther.getMeasuredWidth(),mTopphotoOther.getMeasuredHeight())
                        .transform(mBlurTransformation)
                        .override(200,200)
                        .into(mTopphotoOther);
                mNameOther.setText(p10308.resp.userName);
                mFansOther.setText("粉丝 "+p10308.resp.fansNum);
                mAttentionOther.setText("关注 "+p10308.resp.followNum);
                mineWordsBeanList = p10308.resp.words;
                footMarkBeanList = p10308.resp.footmark;
                headUrl = p10308.resp.imgUrl;
                mFragment_photo.setPhotoList(mineWordsBeanList,p10308.resp.imgUrl);
                isFollw = p10308.resp.isFollow;
                isSeefootmark = p10308.resp.isSeefootmark;
                if (isFollw == 0){
                    //未关注
                    mFollowOther.setText(" + 关注");
                }else if (isFollw ==1){
                    //关注
                    mFollowOther.setText("已关注");
                }else if (isFollw ==2){
                    //被关注
                    mFollowOther.setText(" + 关注");
                }else if (isFollw ==3){
                    //互相关注
                    mFollowOther.setText("相互关注");
                }
            }
        }

        @Override
        public void postExecute() {
            super.postExecute();
        }
    }

    private void hideFragment(FragmentTransaction fragmentTransaction){
        if (mFragment_photo != null){
            fragmentTransaction.hide(mFragment_photo);
        }
        if (mFragment_foot != null){
            fragmentTransaction.hide(mFragment_foot);
        }
    }

    private void hideColor(){
        mLineLeftOther.setBackgroundResource(R.drawable.line_black);
        mLineRightOther.setBackgroundResource(R.drawable.line_black);
    }

    private int getViewHeight(View view){
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(width,height);
        return view.getMeasuredHeight();
    }

    /**
     * 需要携带参数
     * @param context
     * @param userId
     * @param locationX
     * @param locationY
     * @param isMine
     */
    public static void toOtherHomePage(Context context,String userId,double locationX,double locationY,boolean isMine){
        Intent intent = new Intent(context,OtherHomepage.class);
        intent.putExtra("userId",userId);
        intent.putExtra("locationX",locationX);
        intent.putExtra("locationY",locationY);
        intent.putExtra("isMine",isMine);
        context.startActivity(intent);
    }

    /**
     * 添加关注
     * @param friendId
     * @param type
     */
    public void addNotice(String friendId,int type) {
        if (friendId != null) {
            P10150 p10150 = new P10150();
            p10150.req.sessionId = session.sessionId;
            p10150.req.type = type;
            p10150.req.friendId = friendId;
            new addNoteTask().execute(getApplicationContext(), p10150);
        }

    }

    /**
     * 关注
     */
    public class addNoteTask extends DefaultTask {

        PopupWindow mPopupWindow;

        @Override
        public void preExecute() {
            super.preExecute();
            mPopupWindow = PromptUtils.getProgressDialogPop(OtherHomepage.this);
            mPopupWindow.showAtLocation(getWindow().getDecorView(),Gravity.CENTER,0,0);
        }

        @Override
        public void onError(DefaultError obj) {
            // TODO Auto-generated method stub
            super.onError(obj);
            if (mPopupWindow!=null && mPopupWindow.isShowing()){
                mPopupWindow.dismiss();
            }
        }

        @Override
        public void onOk(IProtocol protocol) {
            // TODO Auto-generated method stub
            super.onOk(protocol);
            P10150 p10150 = (P10150) protocol;
            if (mPopupWindow!=null && mPopupWindow.isShowing()){
                mPopupWindow.dismiss();
            }
            if (p10150.resp.transcode.equals("9999")) {
                PromptUtils.showToast(OtherHomepage.this, p10150.resp.msg);
            } else {
                isAttention = p10150.resp.isAttention;
                if (p10150.resp.state == 1) {
                    if (popupWindow_guanzhu!=null&&popupWindow_guanzhu.isShowing()){
                        popupWindow_guanzhu.dismiss();
                    }
                    requestData(friendId,
                            TextUtils.isEmpty(session.locationX) ? 0.0 : Double.parseDouble(session.locationX),
                            TextUtils.isEmpty(session.locationY) ? 0.0 : Double.parseDouble(session.locationY));
                } else {
                    //关注失败
                }
            }
        }
    }

    /**
     * 关注弹窗
     */
    private void createPopWindow_guanzhu(){
        if (view_guanzhu==null){
            view_guanzhu= LayoutInflater.from(OtherHomepage.this).inflate(R.layout.del_msg_window,null,false);
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
        popupWindow_guanzhu.showAtLocation(getWindow().getDecorView(), Gravity.CENTER,0,0);
    }

    /**
     * 返回键
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (popupWindow_guanzhu != null && popupWindow_guanzhu.isShowing()){
                popupWindow_guanzhu.dismiss();
            }
            if (pop != null && pop.isShowing()){
                pop.dismiss();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBlurTransformation = null;
    }
}
