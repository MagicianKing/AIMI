package com.yeying.aimi.mode.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yeying.aimi.API;
import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.protoco.DefaultTask;
import com.yeying.aimi.protoco.IProtocol;
import com.yeying.aimi.protocol.impl.P10104;
import com.yeying.aimi.protocol.impl.P10108;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.utils.PromptUtils;
import com.yeying.aimi.utils.ToastUtils;
import com.yeying.aimi.utils.Tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tanchengkeji on 2017/11/2.
 */

public class BindPhone extends BaseActivity implements View.OnClickListener {

    private RelativeLayout mLeftTitle;
    private EditText mPhoneBindphone;
    private EditText mSmsBindphone;
    private Button mYmBindphone;
    private TextView mTimeBindphone;
    private Button mCxBindphone;
    private EditText mPwdBindphone;
    private TextView mNextBindphone;
    private RecordTime mRecordTime;
    private TextView title_center_view;
    private ImageView title_left_view;

    private String nickName;
    private String headUrl;
    private int gender;
    private String openId;
    private String smsCode;
    private int third_type;
    private String phone;
    private String pwd;
    private String filpath = "/sdcard/key.txt";
    private SessionCache session;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bindphone);
        session = SessionCache.getInstance(this);
        mSharedPreferences = getSharedPreferences(API.USER_INFO , MODE_PRIVATE);
        initIntent();
        initView();
        mRecordTime = new RecordTime(60000, 1000);
    }

    private void initIntent() {
        nickName = getIntent().getStringExtra(API.USER_NICK_NAME);
        headUrl = getIntent().getStringExtra(API.USER_HEAD_URL);
        gender = getIntent().getIntExtra(API.USER_GENDER , 0);
        openId = getIntent().getStringExtra(API.USER_OPEN_ID);
        third_type = getIntent().getIntExtra(API.USER_THIRD_TYPE , 0);
    }

    private void initView() {
        title_left_view = (ImageView) findViewById(R.id.title_left_view);
        title_left_view.setVisibility(View.VISIBLE);
        title_center_view = (TextView) findViewById(R.id.title_center_view);
        title_center_view.setText("手机绑定");
        title_center_view.setVisibility(View.VISIBLE);
        mLeftTitle = (RelativeLayout) findViewById(R.id.title_left);
        mLeftTitle.setOnClickListener(this);
        mLeftTitle.setVisibility(View.VISIBLE);
        mPhoneBindphone = (EditText) findViewById(R.id.bindphone_phone);
        mSmsBindphone = (EditText) findViewById(R.id.bindphone_sms);
        mYmBindphone = (Button) findViewById(R.id.bindphone_ym);
        mYmBindphone.setOnClickListener(this);
        mTimeBindphone = (TextView) findViewById(R.id.bindphone_time);
        mCxBindphone = (Button) findViewById(R.id.bindphone_cx);
        mCxBindphone.setOnClickListener(this);
        mPwdBindphone = (EditText) findViewById(R.id.bindphone_pwd);
        mNextBindphone = (TextView) findViewById(R.id.bindphone_next);
        mNextBindphone.setOnClickListener(this);
        mPhoneBindphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(mPhoneBindphone.getText().toString())){
                    matchedNum(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                dealNextStatus();
            }
        });
        mSmsBindphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                dealNextStatus();
            }
        });
        mPwdBindphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                dealNextStatus();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left:
                Intent intent = new Intent(this,LoginActivity.class);
                intent.putExtra("is_reback",true);
                intent.putExtra("isIn",false);
                setResult(RESULT_OK , intent);
                finish();
                break;
            case R.id.bindphone_ym://获取验证码
                requestSMS(mPhoneBindphone.getText().toString());
                break;
            case R.id.bindphone_cx://重新获取验证码
                reRequestSMS(mPhoneBindphone.getText().toString());
                break;
            case R.id.bindphone_next://绑定下一步
                phone = mPhoneBindphone.getText().toString();
                smsCode = mSmsBindphone.getText().toString();
                pwd = mPwdBindphone.getText().toString();
                bind2next();
                break;
            default:
                break;
        }
    }

    private void bind2next() {
        requestBindPhone();
    }

    /**
     * 匹配手机号
     * @param s
     */
    private void matchedNum(CharSequence s) {
        Pattern p = Pattern.compile("^((13[0-9])|(14[5,7])|(17[0,3,6,7,8])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(s);
        if (m.matches()) {
            mYmBindphone.setBackground(getResources().getDrawable(R.drawable.btn_border));
            mYmBindphone.setClickable(true);
            mYmBindphone.setEnabled(true);
        } else {
            mYmBindphone.setBackground(getResources().getDrawable(R.drawable.button_gray_disable));
            mYmBindphone.setClickable(false);
            mYmBindphone.setEnabled(false);
        }
    }

    /**
     * 绑定手机号的状态
     */
    private void dealNextStatus(){
        if (!TextUtils.isEmpty(mSmsBindphone.getText().toString())&&!TextUtils.isEmpty(mPhoneBindphone.getText().toString())&&!TextUtils.isEmpty(mPwdBindphone.getText().toString())){
            mNextBindphone.setBackgroundResource(R.drawable.round_yellow_bg);
            mNextBindphone.setFocusable(true);
            mNextBindphone.setClickable(true);
        }else{
            mNextBindphone.setBackgroundResource(R.drawable.grey_round_bg);
            mNextBindphone.setFocusable(false);
            mNextBindphone.setClickable(true);
        }
    }

    /**
     * 请求验证码
     * @param phone
     */
    private void requestSMS(String phone){
        if (phone.length() == 11) {
            //判断是否为手机号码
            Pattern p = Pattern.compile("^((13[0-9])|(14[5,7])|(17[0,3,6,7,8])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
            Matcher m = p.matcher(phone);
            if (m.matches()) {
                P10104 p10104 = new P10104();
                p10104.req.phone = phone;
                p10104.req.sessionId = "";
                new PhoneTask().execute(getApplicationContext(), p10104);
                mYmBindphone.setVisibility(View.GONE);
                mCxBindphone.setVisibility(View.GONE);
                mTimeBindphone.setVisibility(View.VISIBLE);
                mRecordTime.start();
                smsFocus();
            } else {
                PromptUtils.showToast(mActivity, "请您输入正确的手机号");
            }
        }
    }

    /**
     * 验证码请求task
     */
    public class PhoneTask extends DefaultTask {
        @Override
        public void onError(DefaultError obj) {
            // TODO Auto-generated method stub
            super.onError(obj);
            ToastUtils.getInstance().showToast(mActivity, obj.error, 0);

        }

        @Override
        public void onOk(IProtocol protocol) {
            // TODO Auto-generated method stub
            super.onOk(protocol);
            P10104 p10104 = (P10104) protocol;
            if (p10104.resp.transcode.equals("9999")) {
                PromptUtils.showToast(mActivity, p10104.resp.msg);
            }
        }
    }
    /**
     * 验证码输入框获得焦点
     */
    public void smsFocus() {
        mSmsBindphone.setText("");
        mSmsBindphone.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) mSmsBindphone
                .getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(mSmsBindphone, 0);
    }

    /**
     * 倒计时计时器
     */
    public class RecordTime extends CountDownTimer {

        public RecordTime(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            // TODO Auto-generated method stub
            mYmBindphone.setVisibility(View.GONE);
            mCxBindphone.setVisibility(View.VISIBLE);
            mTimeBindphone.setVisibility(View.GONE);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub
            int time = (int) millisUntilFinished / 1000;
            mTimeBindphone.setText(String.valueOf(time)+"s");
        }
    }
    /**
     * 重新获取验证码
     */
    private void reRequestSMS(String phone){
        mTimeBindphone.setVisibility(View.VISIBLE);
        mRecordTime.start();
        mYmBindphone.setVisibility(View.GONE);
        mCxBindphone.setVisibility(View.GONE);
        P10104 p10104 = new P10104();
        p10104.req.phone = phone;
        p10104.req.sessionId = "";
        smsFocus();
        new PhoneTask().execute(getApplicationContext(), p10104);
    }

    /**
     * 绑定手机号
     */
    private void requestBindPhone(){
        P10108 p10108 = new P10108();
        p10108.req.thirdType = third_type;
        p10108.req.phone = phone;
        p10108.req.smsCode = smsCode;
        p10108.req.pwd = pwd;
        p10108.req.openid = openId;
        p10108.req.nickName = nickName;
        p10108.req.sex = String.valueOf(gender);
        p10108.req.headImgUrl = headUrl;
        new BindTask().execute(getApplicationContext() , p10108);
    }

    private class BindTask extends DefaultTask{

        PopupWindow mPopupWindow;

        @Override
        public void preExecute() {
            super.preExecute();
            mPopupWindow = PromptUtils.getProgressDialogPop(BindPhone.this);
            mPopupWindow.showAtLocation(getWindow().getDecorView() , Gravity.CENTER , 0 , 0);
        }

        @Override
        public void onError(DefaultError obj) {
            super.onError(obj);
            dismissPop(mPopupWindow);
            PromptUtils.showToast(BindPhone.this , "网络错误");
        }

        @Override
        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            dismissPop(mPopupWindow);
            P10108 p10108 = (P10108) protocol;
            if (p10108.resp.transcode.equals("9999")){
                PromptUtils.showToast(BindPhone.this , p10108.resp.msg+"");
            }else {
                saveKey(filpath, p10108.resp.scretKey);
                session.phone = p10108.resp.phone;
                session.nickname = p10108.resp.nickname;
                session.userId = p10108.resp.userId;
                session.sessionId = p10108.resp.getSessionId();
                session.dataNeedComplete = p10108.resp.dataNeedComplete;
                session.headimgUrl = p10108.resp.headImg;
                session.imgUrl = p10108.resp.imgUrl;
                session.sex = String.valueOf(p10108.resp.gender);
                SharedPreferences.Editor edit = mSharedPreferences.edit();
                edit.putString(API.USER_ACCOUNT, p10108.resp.phone);
                edit.commit();
                //Tools图片加载头修改
                Tools.HEADURL = p10108.resp.imgUrl;
                session.save();
                Intent intent = new Intent(BindPhone.this,LoginActivity.class);
                intent.putExtra("isIn",true);
                intent.putExtra("is_reback",true);
                setResult(RESULT_OK , intent);
                finish();
            }
        }
    }

    private void dismissPop(PopupWindow popupWindow){
        if (popupWindow != null && popupWindow.isShowing()){
            popupWindow.dismiss();
        }
    }

    /**
     * 保存key
     *
     * @param filpath
     * @param scretKey
     */
    private void saveKey(String filpath, String scretKey) {
        File file = new File(filpath);
        if (file.exists()) {
            file.delete();
        }
        File file1 = new File(filpath);
        try {
            file1.createNewFile();
            FileOutputStream out = new FileOutputStream(file1);
            BufferedWriter wrte = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            wrte.write(scretKey);
            wrte.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
