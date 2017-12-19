package com.yeying.aimi.mode.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.protoco.DefaultTask;
import com.yeying.aimi.protoco.IProtocol;
import com.yeying.aimi.protocol.impl.P10101;
import com.yeying.aimi.protocol.impl.P10104;
import com.yeying.aimi.service.bean.PositionBean;
import com.yeying.aimi.storage.LocationCache;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.utils.PromptUtils;
import com.yeying.aimi.utils.ToastUtils;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewRegisterActivity extends BaseActivity implements OnClickListener {

    private boolean Hid;
    private TextView next;
    private String strden;
    private EditText phone;
    private EditText yzm;
    private TextView timett;
    private Button textview;
    private Button cxtext;
    private PositionBean positionbean;
    private RecordTime timer;
    private NewRegisterActivity mActivity;
    private EditText pwd;
    private RelativeLayout back;
    private TextView userAgreement;
    private boolean isPhone=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_register);
        mActivity = this;
        initView();
        initEvent();
        initLogic();
    }

    private void initLogic() {
        // TODO Auto-generated method stub
        LocationCache cache = LocationCache
                .getLocationCache(this);
        this.positionbean = cache.positionbean;
        timer = new RecordTime(60000, 1000);

    }

    private void initEvent() {
        // TODO Auto-generated method stub
        next.setOnClickListener(this);
        yzm.setOnClickListener(this);
        textview.setOnClickListener(this);
        cxtext.setOnClickListener(this);
        userAgreement.setOnClickListener(this);
    }

    private void initView() {
        back = (RelativeLayout) findViewById(R.id.register_back);
        back.setOnClickListener(this);
        next = (TextView) findViewById(R.id.register_next);
        phone = (EditText) findViewById(R.id.register_phone);
        yzm = (EditText) findViewById(R.id.register_edit);
        timett = (TextView) findViewById(R.id.register_time);
        textview = (Button) findViewById(R.id.register_ym);
        cxtext = (Button) findViewById(R.id.register_cx);
        pwd = (EditText) findViewById(R.id.register_pwd);
        userAgreement = (TextView) findViewById(R.id.tv_register_agreement);
        //手机号
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(phone.getText().toString())){
                    matchedNum(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(yzm.getText().toString())&&!TextUtils.isEmpty(phone.getText().toString())&&!TextUtils.isEmpty(pwd.getText().toString())){
                    next.setBackgroundResource(R.drawable.round_yellow_bg);
                    next.setFocusable(true);
                    next.setClickable(true);
                }else{
                    next.setBackgroundResource(R.drawable.grey_round_bg);
                    next.setFocusable(false);
                    next.setClickable(true);
                }
            }
        });
        //验证码
        yzm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(yzm.getText().toString())&&!TextUtils.isEmpty(phone.getText().toString())&&!TextUtils.isEmpty(pwd.getText().toString())){
                    next.setBackgroundResource(R.drawable.round_yellow_bg);
                    next.setFocusable(true);
                    next.setClickable(true);
                }else{
                    next.setBackgroundResource(R.drawable.grey_round_bg);
                    next.setFocusable(false);
                    next.setClickable(true);
                }
            }
        });
        //密码
        pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(pwd.getText().toString())&&!TextUtils.isEmpty(phone.getText().toString())&&!TextUtils.isEmpty(pwd.getText().toString())){
                    next.setBackgroundResource(R.drawable.round_yellow_bg);
                    next.setFocusable(true);
                    next.setClickable(true);
                }else{
                    next.setBackgroundResource(R.drawable.grey_round_bg);
                    next.setFocusable(false);
                    next.setClickable(true);
                }
            }
        });
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.register_next:
                //点击下一步
                if (!Hid) {
                    putout();
                    Hid = false;
                }

                break;
            case R.id.register_ym:
                //获取验证码
                String str = phone.getText().toString();
                if (str.length() == 11) {
                    //判断是否为手机号码
                    Pattern p = Pattern.compile("^((13[0-9])|(14[5,7])|(17[0,3,6,7,8])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
                    Matcher m = p.matcher(str);
                    if (m.matches()) {
                        P10104 p10104 = new P10104();
                        p10104.req.phone = str;
                        p10104.req.sessionId = "";
                        new PhoneTask().execute(getApplicationContext(), p10104);
                        textview.setVisibility(View.GONE);
                        cxtext.setVisibility(View.GONE);
                        timett.setVisibility(View.VISIBLE);
                        timer.start();

                        smsFocus();

                    } else {
                        PromptUtils.showToast(mActivity, "请您输入正确的手机号");
                    }
                }
                break;
            case R.id.register_cx:
                //重新获取验证码
                Log.i("重新发送", "chongxinfasong");
                timett.setVisibility(View.VISIBLE);
                timer.start();
                textview.setVisibility(View.GONE);
                cxtext.setVisibility(View.GONE);
                String st = phone.getText().toString();
                P10104 p10104 = new P10104();
                p10104.req.phone = st;
                p10104.req.sessionId = "";
                new PhoneTask().execute(getApplicationContext(), p10104);
                smsFocus();
                break;
            case R.id.register_back:
                //返回
                finish();
                break;
            case R.id.tv_register_agreement://注册协议
                startActivity(new Intent(getBaseContext(),UserAgreementActivty.class));
            default:
                break;
        }
    }



    /**
     * 验证码输入框获得焦点
     */
    public void smsFocus() {
        yzm.setText("");
        yzm.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) yzm
                .getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(yzm, 0);
    }

    public void putout() {
        String php = phone.getText().toString();
        strden = yzm.getText().toString();
        String register_pwd = pwd.getText().toString();
        Log.i("yzm.getstring", yzm.getText().toString());
        Log.i("strden", strden);
        DecimalFormat df = new DecimalFormat("0.000000");

        //三项为空的时候，点击下一步无反应
        if (php.isEmpty()){
            return;
        }
        if (strden.isEmpty()){
            return;
        }
        if (register_pwd.isEmpty()){
            return;
        }
        //三项不为空的时候
        if (!php.isEmpty()&&!strden.isEmpty()&&!register_pwd.isEmpty()){

            Pattern p = Pattern.compile("^((13[0-9])|(14[5,7])|(17[0,3,6,7,8])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
            Matcher m = p.matcher(php);
            if (!m.matches()) {
                PromptUtils.showToast(mActivity, "请您输入正确的手机号");
                return;
            }
            if (register_pwd.length()<6){
                PromptUtils.showToast(mActivity, "密码不能少于6位！");
                return;
            }
            if (register_pwd.length()>16){
                PromptUtils.showToast(mActivity, "密码不能多于16位！");
                return;
            }
            P10101 p10101 = new P10101();
            p10101.req.phone = phone.getText().toString();
            p10101.req.smsCode = strden;
            p10101.req.pwd = register_pwd;
                /*	p10101.req.locationX=Double.parseDouble(df.format(this.positionbean.getLond()));
					p10101.req.locationY=Double.parseDouble(df.format(this.positionbean.getLatd()));*/
            new LoginTask().execute(getApplicationContext(), p10101);
        }

    }

    public class LoginTask extends DefaultTask {

        @Override
        public void onError(DefaultError obj) {
            // TODO Auto-generated method stub
            super.onError(obj);
            PromptUtils.showToast(mActivity, obj.error);
        }

        @Override
        public void onOk(IProtocol protocol) {
            // TODO Auto-generated method stub
            super.onOk(protocol);
            P10101 p10101 = (P10101) protocol;
            if (p10101.resp.transcode.equals("9999")) {
                PromptUtils.showToast(mActivity, p10101.resp.msg);
            } else {
                SessionCache session = SessionCache.getInstance(getApplicationContext());
                session.userId = p10101.resp.userId;
                session.sessionId = p10101.resp.getSessionId();
                session.dataNeedComplete = p10101.resp.dataNeedComplete;
                session.real_name = p10101.resp.real_name;
                session.card_no = p10101.resp.card_no;
                session.save();
                startActivity(new Intent(mActivity, PerfectDataActivity.class).putExtra("fromRegister",true));
                finish();
            }

        }

    }

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

    public class RecordTime extends CountDownTimer {

        public RecordTime(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            // TODO Auto-generated method stub
            textview.setVisibility(View.GONE);
            cxtext.setVisibility(View.VISIBLE);
            timett.setVisibility(View.GONE);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub
            int time = (int) millisUntilFinished / 1000;
            timett.setText(String.valueOf(time)+"s");
        }

    }

    private void matchedNum(CharSequence s) {
        Pattern p = Pattern.compile("^((13[0-9])|(14[5,7])|(17[0,3,6,7,8])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(s);
        if (m.matches()) {
            textview.setBackground(getResources().getDrawable(R.drawable.btn_border));
            textview.setClickable(true);
            textview.setEnabled(true);
        } else {
            textview.setBackground(getResources().getDrawable(R.drawable.button_gray_disable));
            textview.setClickable(false);
            textview.setEnabled(false);
        }
    }


}
