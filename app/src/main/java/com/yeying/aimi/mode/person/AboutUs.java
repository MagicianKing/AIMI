package com.yeying.aimi.mode.person;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.protoco.DefaultTask;
import com.yeying.aimi.protoco.IProtocol;
import com.yeying.aimi.protocol.impl.P10311;
import com.yeying.aimi.storage.SessionCache;

import static com.yeying.aimi.R.id.tv_phone;

/**
 * Created by tanchengkeji on 2017/9/28.
 */

public class AboutUs extends BaseActivity implements View.OnClickListener {

    private ImageView mLogoIm;
    private TextView mNameTv;
    private TextView mVersionTv;
    private TextView mDescTv;
    private RelativeLayout mBusinessRl;
    private TextView mPhoneTv;
    private RelativeLayout mPhoneRl;
    private TextView mEmailTv;
    private RelativeLayout mEmailRl;
    private TextView mLineqqTv;
    private Button mShengmingBt;
    private Button mTiaokuanBt;
    private SessionCache mSessionCache;

    private RelativeLayout title_left;
    private ImageView title_left_view;

    private TextView title_center_view;

    private RelativeLayout title_right;
    private ImageView title_right_img;
    private TextView title_right_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        mSessionCache = SessionCache.getInstance(this);
        initView();
    }

    private void initView() {
        title_left = (RelativeLayout) findViewById(R.id.title_left);
        title_left_view = (ImageView) findViewById(R.id.title_left_view);
        title_center_view = (TextView) findViewById(R.id.title_center_view);
        title_right = (RelativeLayout) findViewById(R.id.title_right);
        title_right_img = (ImageView) findViewById(R.id.title_right_img);
        title_right_tv = (TextView) findViewById(R.id.title_right_tv);
        title_left.setOnClickListener(this);
        title_center_view.setText("关于我们");
        title_left_view.setVisibility(View.VISIBLE);
        title_center_view.setVisibility(View.VISIBLE);

        mLogoIm = (ImageView) findViewById(R.id.im_logo);
        mNameTv = (TextView) findViewById(R.id.tv_name);
        mVersionTv = (TextView) findViewById(R.id.tv_version);
        mDescTv = (TextView) findViewById(R.id.tv_desc);
        mBusinessRl = (RelativeLayout) findViewById(R.id.rl_business);
        mPhoneTv = (TextView) findViewById(tv_phone);
        mPhoneRl = (RelativeLayout) findViewById(R.id.rl_phone);
        mEmailTv = (TextView) findViewById(R.id.tv_email);
        mEmailRl = (RelativeLayout) findViewById(R.id.rl_email);
        mLineqqTv = (TextView) findViewById(R.id.tv_lineqq);
        mShengmingBt = (Button) findViewById(R.id.bt_shengming);
        mShengmingBt.setOnClickListener(this);
        mTiaokuanBt = (Button) findViewById(R.id.bt_tiaokuan);
        mTiaokuanBt.setOnClickListener(this);
        //版本
        String versonName = "";
        try {
            versonName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mVersionTv.setText("V"+versonName);
        P10311 p = new P10311();
        p.req.userId = mSessionCache.userId;
        p.req.sessionId = mSessionCache.sessionId;
        new AboutTask().execute(mActivity, p);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left:
                finish();
                break;
            case R.id.bt_shengming:
                MianZeShengMingActivity.toMianZe(AboutUs.this,true);
                break;
            case R.id.bt_tiaokuan:
                MianZeShengMingActivity.toMianZe(AboutUs.this,false);
                break;
            default:
                break;
        }
    }

    /**
     * 关于我们
     */
    public class AboutTask extends DefaultTask {

        public void onError(DefaultError obj) {
            super.onError(obj);
            Toast.makeText(mActivity, "网络错误", Toast.LENGTH_SHORT).show();
        }

        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            P10311 p = (P10311) protocol;

            if (p.resp.transcode.equals("9999")) {
                Toast.makeText(mActivity, " " + p.resp.msg, Toast.LENGTH_SHORT).show();
            } else {
                if (p.resp.content != null) {
                    mDescTv.setText(p.resp.content);
                }
                if (p.resp.phone != null) {
                    mPhoneTv.setText(p.resp.phone);
                }
                if (p.resp.email != null) {
                    mEmailTv.setText(p.resp.email);
                }
                if (p.resp.qq != null) {
                    mLineqqTv.setText(p.resp.qq);
                }

            }
        }
    }
}
