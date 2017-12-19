package com.yeying.aimi.mode.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.protoco.DefaultTask;
import com.yeying.aimi.protoco.IProtocol;
import com.yeying.aimi.protocol.impl.P620006;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.utils.PromptUtils;

/**
 * Created by tanchengkeji on 2017/9/21.
 */

public class FollowAndFans extends BaseActivity implements View.OnClickListener {

    private ImageView mBackFaf;
    private TextView mFansFaf;
    private TextView mLineLeftFaf;
    private TextView mFollowFaf;
    private TextView mLineRightFaf;
    private FrameLayout mContainerFaf;
    public static String flag = "flag";
    private boolean mBooleanExtra;
    private Fragment_Fans fragment_Fans;
    private Fragment_Follows fragment_Follows;
    private FragmentManager mFragmentManager;

    private P620006 mP620006;
    private SessionCache mSessionCache;
    PopupWindow mPopupWindow;
    private String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faf);
        mFragmentManager = getSupportFragmentManager();
        mSessionCache = SessionCache.getInstance(this);
        initView();
        initIntent();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initIntent() {
        userId = getIntent().getStringExtra("userId");
        mBooleanExtra = getIntent().getBooleanExtra(flag, false);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        hideLine();
        hideFragment(fragmentTransaction);
        if (mBooleanExtra){
            //显示粉丝
            mLineLeftFaf.setBackgroundResource(R.drawable.line_yellow);
            if (fragment_Fans == null){
                fragment_Fans = new Fragment_Fans();
                fragmentTransaction.add(R.id.faf_container,fragment_Fans);
            }else {
                fragmentTransaction.show(fragment_Fans);
            }
        }else {
            //显示关注
            mLineRightFaf.setBackgroundResource(R.drawable.line_yellow);
            if (fragment_Follows == null){
                fragment_Follows = new Fragment_Follows();
                fragmentTransaction.add(R.id.faf_container,fragment_Follows);
            }else {
                fragmentTransaction.show(fragment_Follows);
            }
        }
        fragmentTransaction.commit();
    }

    private void initView() {
        mBackFaf = (ImageView) findViewById(R.id.faf_back);
        mBackFaf.setOnClickListener(this);
        mFansFaf = (TextView) findViewById(R.id.faf_fans);
        mFansFaf.setOnClickListener(this);
        mLineLeftFaf = (TextView) findViewById(R.id.faf_line_left);
        mFollowFaf = (TextView) findViewById(R.id.faf_follow);
        mFollowFaf.setOnClickListener(this);
        mLineRightFaf = (TextView) findViewById(R.id.faf_line_right);
        mContainerFaf = (FrameLayout) findViewById(R.id.faf_container);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragment_Fans = new Fragment_Fans();
        fragment_Follows = new Fragment_Follows();
        fragmentTransaction.add(R.id.faf_container,fragment_Fans);
        fragmentTransaction.add(R.id.faf_container,fragment_Follows);
        fragmentTransaction.commit();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        requestFollowsFans();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.faf_back:
                finish();
                break;
            case R.id.faf_fans:
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                hideLine();
                hideFragment(fragmentTransaction);
                mLineLeftFaf.setBackgroundResource(R.drawable.line_yellow);
                if (fragment_Fans == null){
                    fragment_Fans = new Fragment_Fans();
                    fragmentTransaction.add(R.id.faf_container,fragment_Fans);
                }else {
                    fragmentTransaction.show(fragment_Fans);
                }
                fragmentTransaction.commit();
                break;
            case R.id.faf_follow:
                FragmentTransaction fragmentTransaction1 = mFragmentManager.beginTransaction();
                hideLine();
                hideFragment(fragmentTransaction1);
                mLineRightFaf.setBackgroundResource(R.drawable.line_yellow);
                if (fragment_Follows == null){
                    fragment_Follows = new Fragment_Follows();
                    fragmentTransaction1.add(R.id.faf_container,fragment_Follows);
                }else {
                    fragmentTransaction1.show(fragment_Follows);
                }
                fragmentTransaction1.commit();
                break;
            default:
                break;
        }
    }

    private void hideLine(){
        mLineLeftFaf.setBackgroundResource(R.drawable.line_black);
        mLineRightFaf.setBackgroundResource(R.drawable.line_black);
    }

    private void hideFragment(FragmentTransaction transaction){
        if (fragment_Fans != null){
            transaction.hide(fragment_Fans);
        }
        if (fragment_Follows != null){
            transaction.hide(fragment_Follows);
        }
    }

    public void requestFollowsFans(){
        Log.e(TAG, "requestFollowsFans: ------>"+userId);
        mP620006 = new P620006();
        mP620006.req.searchUserId = userId;
        mP620006.req.sessionId = mSessionCache.sessionId;
        new FollowsFansTask().execute(getApplicationContext(),mP620006);
    }

    public class FollowsFansTask extends DefaultTask {

        @Override
        public void preExecute() {
            super.preExecute();
            mPopupWindow = PromptUtils.getProgressDialogPop(FollowAndFans.this);
            mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER,0,0);
        }

        @Override
        public void onError(DefaultError obj) {
            super.onError(obj);
            if (mPopupWindow != null && mPopupWindow.isShowing()){
                mPopupWindow.dismiss();
            }
        }

        @Override
        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            if (mPopupWindow != null && mPopupWindow.isShowing()){
                mPopupWindow.dismiss();
            }
            P620006 p620006 = (P620006) protocol;
            if (p620006.resp.transcode.equals("9999")){
                PromptUtils.showToast(FollowAndFans.this,"系统异常");
            }else {
                String head = p620006.resp.imgUrl;
                if (fragment_Fans != null){
                    fragment_Fans.getList(p620006.resp.fansList,head);
                }
                if (fragment_Follows != null){
                    fragment_Follows.getList(p620006.resp.followList,head);
                }
            }
        }

        @Override
        public void postExecute() {
            super.postExecute();
        }
    }

    public static void toFollowFans(Context context,String userId,boolean arg){
        Intent intent = new Intent(context,FollowAndFans.class);
        intent.putExtra("userId",userId);
        intent.putExtra(flag,arg);
        context.startActivity(intent);
    }

}
