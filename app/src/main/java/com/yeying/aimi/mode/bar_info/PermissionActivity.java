package com.yeying.aimi.mode.bar_info;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yeying.aimi.R;
import com.yeying.aimi.adapter.PermissionAdapter;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.bean.BarScreenBean;
import com.yeying.aimi.protoco.DefaultTask;
import com.yeying.aimi.protoco.IProtocol;
import com.yeying.aimi.protocol.impl.P630001;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.utils.PromptUtils;

import java.util.List;

public class PermissionActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mImgLeftBack;
    private TextView mTvMiddleTitle;
    private ListView mLvImglist;
    private BarScreenTask mTask;
    private P630001 mP630001;
    private SessionCache mSessionCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        mTask = new BarScreenTask();
        mP630001 = new P630001();
        mSessionCache = SessionCache.getInstance(this);
        mP630001.req.userId = mSessionCache.userId;
        mP630001.req.sessionId = mSessionCache.sessionId;
        mP630001.req.barId = mSessionCache.barId;
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTask.execute(this,mP630001);
    }

    private void initData() {
        //mTvMiddleTitle.setText(mSessionCache.barName+"权限");
        mTvMiddleTitle.setText("权限");
    }

    private void initView() {
        mImgLeftBack = (ImageView) findViewById(R.id.img_left_back);
        mImgLeftBack.setOnClickListener(this);
        mTvMiddleTitle = (TextView) findViewById(R.id.tv_middle_title);
        mLvImglist = (ListView) findViewById(R.id.lv_imglist);
        FrameLayout emptyView = (FrameLayout) findViewById(R.id.fram_empty);
        mLvImglist.setEmptyView(emptyView);
        mImgLeftBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_left_back:
                finish();
                break;
        }
    }

    class BarScreenTask extends DefaultTask{

        private PopupWindow mDialog;

        @Override
        public void preExecute() {
            super.preExecute();
            mDialog = PromptUtils.getProgressDialogPop(PermissionActivity.this);
           // mDialog.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0,0);
        }

        @Override
        public void onError(DefaultError obj) {
            super.onError(obj);
           // mDialog.dismiss();
        }

        @Override
        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
           // mDialog.dismiss();
            P630001  p630001 = (P630001) protocol;
            String imgUrl = p630001.resp.imgUrl;
            List<BarScreenBean> soList = p630001.resp.soList;
            PermissionAdapter adapter = new PermissionAdapter(soList, PermissionActivity.this,imgUrl);
            mLvImglist.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            if (soList.size() >1){
                mLvImglist.smoothScrollToPosition(soList.size()-1);
            }

        }
    }

}
