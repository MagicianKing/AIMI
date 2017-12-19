package com.yeying.aimi.mode.otherdetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.AIMIApplication;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.protoco.DefaultTask;
import com.yeying.aimi.protoco.IProtocol;
import com.yeying.aimi.protocol.impl.P620005;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.views.RoundImageView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tanchengkeji on 2017/9/28.
 */

public class OtherDetail extends BaseActivity implements View.OnClickListener {

    private ImageView title_back;
    private ImageButton title_menu;
    private TextView title_name;
    private RoundImageView mHeadOt;
    private TextView mNameOt;
    private TextView mSexOt;
    private TextView mBirthOt;
    private TextView mConstellationOt;
    private TextView mExplainOt;
    private P620005 mP620005;
    private DataTask mDataTask;

    public static final String SEARCH_ID = "search_id";
    private String searchId;
    private String headUrl;

    private SessionCache mSessionCache;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_detail);
        mSessionCache = SessionCache.getInstance(this);
        initIntent();
        initView();
        initRequest();
    }

    private void initRequest() {
        mP620005 = new P620005();
        mP620005.req.sessionId = mSessionCache.sessionId;
        mP620005.req.searchUserId = searchId;
        Log.e(TAG, "initRequest: "+mSessionCache.userId);
        Log.e(TAG, "initRequest: "+searchId);
        mDataTask = new DataTask();
        mDataTask.execute(getApplicationContext(), mP620005);
    }

    private void initIntent() {
        Intent intent = getIntent();
        searchId = intent.getStringExtra(SEARCH_ID);
    }

    private void initView() {
        title_back = (ImageView) findViewById(R.id.title_back);
        title_menu = (ImageButton) findViewById(R.id.title_menu);
        title_menu.setVisibility(View.GONE);
        title_name = (TextView) findViewById(R.id.title_name);
        title_name.setText("TA的资料");
        title_back.setOnClickListener(this);
        mHeadOt = (RoundImageView) findViewById(R.id.ot_head);
        mHeadOt.setOnClickListener(this);
        mNameOt = (TextView) findViewById(R.id.ot_name);
        mSexOt = (TextView) findViewById(R.id.ot_sex);
        mBirthOt = (TextView) findViewById(R.id.ot_birth);
        mConstellationOt = (TextView) findViewById(R.id.ot_constellation);
        mExplainOt = (TextView) findViewById(R.id.ot_explain);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.ot_head:
                if (!TextUtils.isEmpty(headUrl)) {
                    PreViewShowActivity.toPreView(OtherDetail.this, headUrl);
                }
                break;
            default:
                break;
        }
    }

    public static void toOtherDetail(Context context, String searchId) {
        Intent intent = new Intent(context, OtherDetail.class);
        intent.putExtra(SEARCH_ID, searchId);
        context.startActivity(intent);
    }

    //请求资料
    private class DataTask extends DefaultTask {

        @Override
        public void preExecute() {
            super.preExecute();
        }

        @Override
        public void onError(DefaultError obj) {
            super.onError(obj);
        }

        @Override
        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            P620005 p620005 = (P620005) protocol;
            initData(p620005);
        }
    }

    private void initData(P620005 p620005) {
        headUrl = AIMIApplication.dealHeadImg(p620005.resp.headImg);
        Glide.with(this).load(headUrl).into(mHeadOt);
        mNameOt.setText(p620005.resp.userName);
        if (p620005.resp.gender == 2) {
            mSexOt.setText("女");
        } else {
            mSexOt.setText("男");
        }
        mBirthOt.setText(dealDate(p620005.resp.birthday));
        mConstellationOt.setText(p620005.resp.constellation);
        if (!TextUtils.isEmpty(p620005.resp.autograph)) {
            mExplainOt.setText(p620005.resp.autograph);
        }
    }

    private String dealDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        return simpleDateFormat.format(date);
    }
}
