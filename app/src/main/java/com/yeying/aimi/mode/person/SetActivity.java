package com.yeying.aimi.mode.person;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.AIMIApplication;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.mode.login.LoginActivity;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.storage.SimpleMessageCache;
import com.yeying.aimi.utils.CleanMessageUtil;
import com.yeying.aimi.utils.FileUtil;

import java.io.File;

public class SetActivity extends BaseActivity implements View.OnClickListener {

    final String IMAGE_CAPTURE_NAME = "temp.jpg";
    private ImageView mImgLeftBack;
    private TextView mTvMiddleTitle;
    /**
     * self
     */
    private TextView mTvNumandsafe;
    /**
     * privacy
     */
    private TextView mTvPrivacy;
    private TextView mTvCachesize;
    private RelativeLayout mLayoutClear;
    /**
     * feedback
     */
    private TextView mTvFeedback;
    /**
     * aboutme
     */
    private TextView mTvAboutme;
    /**
     * exit
     */
    private Button mBtnExit;
    private View mClearDialogView;
    private Button mAgreeButton;
    private Button mRejectButton;
    private PopupWindow mDialog;
    private View mExitDialogView;
    private TextView mExitAgreeButton;
    private TextView mExitRejectButton;
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        initView();
        initData();
        initClearDialog();
        initExitPopWindow();
    }

    private void initExitPopWindow() {
        mExitDialogView = LayoutInflater.from(this).inflate(R.layout.del_msg_window, null);
        mExitAgreeButton = (TextView) mExitDialogView.findViewById(R.id.bt_sure);
        mExitRejectButton = (TextView) mExitDialogView.findViewById(R.id.bt_close);
        TextView msg = (TextView) mExitDialogView.findViewById(R.id.tv_msg);
        msg.setText("确定退出吗？");
        mExitAgreeButton.setOnClickListener(this);
        mExitRejectButton.setOnClickListener(this);
    }

    private void initClearDialog() {
        mClearDialogView = LayoutInflater.from(this).inflate(R.layout.layout_cleardialog, null);
        mPopupWindow = new PopupWindow(mClearDialogView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mAgreeButton = (Button) mClearDialogView.findViewById(R.id.btn_agree);
        mRejectButton = (Button) mClearDialogView.findViewById(R.id.btn_reject);
        mAgreeButton.setOnClickListener(this);
        mRejectButton.setOnClickListener(this);


    }


    private void initData() {
        mTvMiddleTitle.setText("设置");
        try {
            mTvCachesize.setText(CleanMessageUtil.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {

        mDialog = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        mImgLeftBack = (ImageView) findViewById(R.id.img_left_back);
        mImgLeftBack.setOnClickListener(this);
        mTvMiddleTitle = (TextView) findViewById(R.id.tv_middle_title);
        mTvNumandsafe = (TextView) findViewById(R.id.tv_numandsafe);
        mTvNumandsafe.setOnClickListener(this);
        mTvPrivacy = (TextView) findViewById(R.id.tv_privacy);
        mTvPrivacy.setOnClickListener(this);
        mTvCachesize = (TextView) findViewById(R.id.tv_cachesize);
        mLayoutClear = (RelativeLayout) findViewById(R.id.layout_clear);
        mLayoutClear.setOnClickListener(this);
        mTvFeedback = (TextView) findViewById(R.id.tv_feedback);
        mTvFeedback.setOnClickListener(this);
        mTvAboutme = (TextView) findViewById(R.id.tv_aboutme);
        mTvAboutme.setOnClickListener(this);
        mBtnExit = (Button) findViewById(R.id.btn_exit);
        mBtnExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_left_back:
                finish();
                break;
            case R.id.tv_numandsafe:
                AccountAndSafe.toAccountSafe(SetActivity.this);
                break;
            case R.id.tv_privacy:
                startActivity(new Intent(this, PrivacyActivity.class));
                break;
            case R.id.layout_clear:
                mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                break;
            case R.id.tv_feedback:
                startActivity(new Intent(SetActivity.this, FankuiActivity.class));
                break;
            case R.id.tv_aboutme:
                //关于我们
                startActivity(new Intent(SetActivity.this, AboutUs.class));
                break;
            case R.id.btn_exit:
                mDialog.setContentView(mExitDialogView);
                mDialog.showAtLocation(getWindow().getDecorView(),Gravity.CENTER,0,0);
                break;
            case R.id.btn_agree:
                boolean b = CleanMessageUtil.clearAllCache(this);
                try {
                    if (b) {
                        mTvCachesize.setText(CleanMessageUtil.getTotalCacheSize(this));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mPopupWindow.dismiss();
                break;
            case R.id.btn_reject:
                mPopupWindow.dismiss();
                break;
            case R.id.bt_sure:
                AIMIApplication.exitAll();
                startActivity(new Intent(this, LoginActivity.class).putExtra("is_reback" , true));
                //退出登录
                SessionCache session = SessionCache.getInstance(mActivity);
                session.del();
                //登出环信
                EMChatManager.getInstance().logout();
                Log.i("", "登出环信");
                //清除缓存的头像
                File file = new File(FileUtil.getHeadimageCache(mActivity), IMAGE_CAPTURE_NAME);
                if (file.exists()) {
                    file.delete();
                }
                //清除新消息红点提示
                SimpleMessageCache simpleCache = SimpleMessageCache.getInstance(mActivity);
                simpleCache.isNewXiu = false;
                simpleCache.isNewMsg = false;
                simpleCache.isNewMy = false;
                simpleCache.newMsgNum = 0;
                simpleCache.save();
                finish();
                mDialog.dismiss();
                break;
            case R.id.bt_close:
                mDialog.dismiss();
                break;

        }
    }

}
