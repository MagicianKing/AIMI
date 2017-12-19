package com.yeying.aimi.mode.person;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.mode.login.NewForgetActivity;
import com.yeying.aimi.utils.PromptUtils;

/**
 * Created by tanchengkeji on 2017/9/25.
 */

public class AccountAndSafe extends BaseActivity implements View.OnClickListener {

    private TextView mWeiboAs;
    private TextView mWeixinAs;
    private TextView mQqAs;
    private TextView mPhoneAs;
    private RelativeLayout mPswAs;

    private RelativeLayout title_left;
    private ImageView title_left_view;

    private TextView title_center_view;

    private RelativeLayout title_right;
    private ImageView title_right_img;
    private TextView title_right_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_safe);
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
        title_center_view.setText("账户与安全");
        title_left_view.setVisibility(View.VISIBLE);
        title_center_view.setVisibility(View.VISIBLE);

        mWeiboAs = (TextView) findViewById(R.id.as_weibo);
        mWeiboAs.setOnClickListener(this);
        mWeixinAs = (TextView) findViewById(R.id.as_weixin);
        mWeixinAs.setOnClickListener(this);
        mQqAs = (TextView) findViewById(R.id.as_qq);
        mQqAs.setOnClickListener(this);
        mPhoneAs = (TextView) findViewById(R.id.as_phone);
        mPhoneAs.setOnClickListener(this);
        mPswAs = (RelativeLayout) findViewById(R.id.as_psw);
        mPswAs.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left:
                finish();
                break;
            case R.id.as_weibo:
                PromptUtils.showToast(AccountAndSafe.this,"微博");
                break;
            case R.id.as_weixin:
                PromptUtils.showToast(AccountAndSafe.this,"微信");
                break;
            case R.id.as_qq:
                PromptUtils.showToast(AccountAndSafe.this,"QQ");
                break;
            case R.id.as_phone:
                PromptUtils.showToast(AccountAndSafe.this,"手机号");
                break;
            case R.id.as_psw:
                NewForgetActivity.toForget(AccountAndSafe.this,null,"修改密码");
                break;
            default:
                break;
        }
    }

    public static void toAccountSafe(Context context){
        Intent intent = new Intent(context,AccountAndSafe.class);
        context.startActivity(intent);
    }

}
