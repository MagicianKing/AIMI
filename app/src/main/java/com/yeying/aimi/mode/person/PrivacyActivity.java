package com.yeying.aimi.mode.person;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.protoco.DefaultTask;
import com.yeying.aimi.protoco.IProtocol;
import com.yeying.aimi.protocol.impl.P620007;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.utils.PromptUtils;

public class PrivacyActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTvMiddleTitle;
    private ToggleButton mCkShow;
    private PrivacyTask mPrivacyTask;
    private P620007 mP620007;
    private SessionCache mSessionCache;
    private ImageView mImgLeftBack;
    private TextView mTvRightTxt;
    private ImageView mImgRightImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        mSessionCache = SessionCache.getInstance(this);
        initView();
    }

    private void initView() {
        mTvMiddleTitle = (TextView) findViewById(R.id.tv_middle_title);
        mTvMiddleTitle.setText("隐私");
        mCkShow = (ToggleButton) findViewById(R.id.ck_show);
        mCkShow.setOnClickListener(this);
        if (mSessionCache.isSeeFootMark){
            mCkShow.setChecked(true);
        }else {
            mCkShow.setChecked(false);
        }
        mPrivacyTask = new PrivacyTask();
        mP620007 = new P620007();
        mP620007.req.sessionId = mSessionCache.sessionId;
        mP620007.req.userId = mSessionCache.userId;
        mImgLeftBack = (ImageView) findViewById(R.id.img_left_back);
        mImgLeftBack.setOnClickListener(this);
        mImgRightImg = (ImageView) findViewById(R.id.img_right_img);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ck_show:
                if (mSessionCache.isSeeFootMark) {//当前可查看
                    mP620007.req.isSeefootmark = 2;//false 不可查看
                } else {//当前不可查看
                    mP620007.req.isSeefootmark = 1;//true  可查看
                }
                mPrivacyTask.execute(getApplicationContext(), mP620007);
                break;
            case R.id.img_left_back:
                finish();
                break;

        }
    }

    class PrivacyTask extends DefaultTask {

        private PopupWindow mProgressDialogPop;

        @Override
        public void preExecute() {
            super.preExecute();
            mProgressDialogPop = PromptUtils.getProgressDialogPop(PrivacyActivity.this);
            mProgressDialogPop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        }

        @Override
        public void onError(DefaultError obj) {
            super.onError(obj);
            if (mProgressDialogPop != null && mProgressDialogPop.isShowing()) {
                mProgressDialogPop.dismiss();
            }

        }

        @Override
        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            if (mProgressDialogPop != null && mProgressDialogPop.isShowing()) {
                mProgressDialogPop.dismiss();
            }
            P620007 p620007 = (P620007) protocol;
            int state = p620007.resp.status;
            if (state == 1) {
                if (mSessionCache.isSeeFootMark){
                    mSessionCache.isSeeFootMark = false;
                }else {
                    mSessionCache.isSeeFootMark = true;
                }
                mSessionCache.save();
                PromptUtils.showToast(PrivacyActivity.this, "保存成功");
            } else {
                PromptUtils.showToast(PrivacyActivity.this, "保存失败");
                /*if (mCkShow.isChecked()) {
                    mCkShow.setChecked(false);
                } else {
                    mCkShow.setChecked(true);
                }*/
            }

        }
    }
}
