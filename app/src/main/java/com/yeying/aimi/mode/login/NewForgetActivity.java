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
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.protoco.DefaultTask;
import com.yeying.aimi.protoco.IProtocol;
import com.yeying.aimi.protocol.impl.P10104;
import com.yeying.aimi.protocol.impl.P20102;
import com.yeying.aimi.service.bean.PositionBean;
import com.yeying.aimi.storage.LocationCache;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.utils.PromptUtils;
import com.yeying.aimi.utils.ToastUtils;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewForgetActivity extends BaseActivity implements OnClickListener {

    PositionBean positionbean;
    private TextView next;
    private NewForgetActivity mActivity;
    private EditText phone;
    private boolean Hid;
    private String strden;
    private EditText yzm;
    private TextView timett;
    private TextView textview;
    private TextView cxtext;
    private RecordTime timer;
    private EditText pwd;
    private RelativeLayout back;
    private TextView title;
    private String mPhone;
    private String title_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_forget);
        if (getIntent() != null) {
            mPhone = getIntent().getStringExtra("phone");
            title_tv=getIntent().getStringExtra("title");
        }
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
        back.setOnClickListener(this);

    }

    private void initView() {
        next = (TextView) findViewById(R.id.forget_next);
        phone = (EditText) findViewById(R.id.forget_phoen);
        yzm = (EditText) findViewById(R.id.forget_edit);
        timett = (TextView) findViewById(R.id.forget_time);
        textview = (TextView) findViewById(R.id.forget_ym);
        cxtext = (TextView) findViewById(R.id.foget_cx);
        pwd = (EditText) findViewById(R.id.forget_pwd);
        back = (RelativeLayout) findViewById(R.id.forget_back);
        title = ((TextView) findViewById(R.id.forget_title_tv));
        title.setText(title_tv);
        if(title_tv.equals("修改密码")){
            back.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(mPhone)){
            phone.setText(mPhone + "");
            matchedNum(mPhone);
        }
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)){
                    matchedNum(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(yzm.getText().toString())&&!TextUtils.isEmpty(phone.getText().toString())&&!TextUtils.isEmpty(pwd.getText().toString())){
                    next.setTextColor(getResources().getColor(R.color.black));
                    next.setBackgroundResource(R.drawable.round_yellow_bg);
                    next.setFocusable(true);
                    next.setClickable(true);
                }else{
                    next.setTextColor(getResources().getColor(R.color.black));
                    next.setBackgroundResource(R.drawable.grey_round_bg);
                    next.setFocusable(false);
                    next.setClickable(true);
                }
            }
        });
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
                    next.setTextColor(getResources().getColor(R.color.black));
                    next.setBackgroundResource(R.drawable.round_yellow_bg);
                    next.setFocusable(true);
                    next.setClickable(true);
                }else{
                    next.setTextColor(getResources().getColor(R.color.black));
                    next.setBackgroundResource(R.drawable.grey_round_bg);
                    next.setFocusable(false);
                    next.setClickable(true);
                }
            }
        });
        pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(yzm.getText().toString())&&!TextUtils.isEmpty(phone.getText().toString())&&!TextUtils.isEmpty(pwd.getText().toString())){
                    next.setTextColor(getResources().getColor(R.color.black));
                    next.setBackgroundResource(R.drawable.round_yellow_bg);
                    next.setFocusable(true);
                    next.setClickable(true);
                }else{
                    next.setTextColor(getResources().getColor(R.color.black));
                    next.setBackgroundResource(R.drawable.grey_round_bg);
                    next.setFocusable(false);
                    next.setClickable(true);
                }
            }
        });
    }

    private void matchedNum(CharSequence s) {
        Pattern p = Pattern.compile("^((13[0-9])|(14[5,7])|(17[0,3,6,7,8])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(s);
        if (m.matches()) {
            textview.setBackground(getResources().getDrawable(R.drawable.btn_border));
            textview.setEnabled(true);
        } else {
            textview.setBackground(getResources().getDrawable(R.drawable.button_gray_disable));
            textview.setEnabled(false);
        }
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.forget_next:
                //if (!Hid) {
                    putout();
                //    Hid = false;
                //}

                break;
            case R.id.forget_back:
                finish();
                break;
            case R.id.forget_ym:
                String str = phone.getText().toString();
                String mypwd = pwd.getText().toString();
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
                        PromptUtils.showToast(mActivity, "请输入正确的手机号!");
                    }
                } else {
                    PromptUtils.showToast(mActivity, "请输入正确的手机号!");
                }
                break;
            case R.id.foget_cx:
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
        String str = phone.getText().toString();
        String mypwd = pwd.getText().toString();
        strden = yzm.getText().toString();
        Log.i("yzm.getstring", yzm.getText().toString());
        Log.i("strden", strden);
        //三项都为空的时候 确定按钮点击无反应
        if(TextUtils.isEmpty(str)){
            //PromptUtils.showToast(mActivity,"手机号不能为空");
            return;
        }
        if(TextUtils.isEmpty(strden)){
            //PromptUtils.showToast(mActivity,"验证码不能为空");
            return;
        }
        if(TextUtils.isEmpty(mypwd)){
            //PromptUtils.showToast(mActivity,"密码不能为空");
            return;
        }

        if (!TextUtils.isEmpty(str)&&!TextUtils.isEmpty(strden)&&!TextUtils.isEmpty(mypwd)){
            //当三项都不为空的时候
            //判断手机号是否正确
            Pattern p = Pattern.compile("^((13[0-9])|(14[5,7])|(17[0,3,6,7,8])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
            Matcher m = p.matcher(str);
            if (!m.matches()){
                PromptUtils.showToast(mActivity,"请输入正确的手机号！");
                return;
            }
            if (mypwd.length()<6){
                PromptUtils.showToast(mActivity,"密码不少于6位！");
                return;
            }
            if (mypwd.length()>16){
                PromptUtils.showToast(mActivity,"密码不多于16位！");
                return;
            }
            if (strden.length()!=4){
                PromptUtils.showToast(mActivity,"验证码错误!");
                return;
            }
            DecimalFormat df = new DecimalFormat("0.000000");
//			if(strden.equals(yzmstr)){

            P20102 p20102 = new P20102();
            p20102.req.phone = str;
            p20102.req.smsCode = strden;
            p20102.req.pwd = mypwd;
            new LoginTask().execute(getApplicationContext(), p20102);
        }



//			}else{
//				//Toast.makeText(getApplicationContext(), "验证码错误",Toast.LENGTH_SHORT).show();
//				PromptUtils.showToast(mActivity, "验证码错误");
//			}

    }

    public class LoginTask extends DefaultTask {
        @Override
        public void onError(DefaultError obj) {
            // TODO Auto-generated method stub
            super.onError(obj);
            ToastUtils.getInstance().showToast(mActivity, "网络错误", 0);
        }

        @Override
        public void onOk(IProtocol protocol) {
            // TODO Auto-generated method stub
            super.onOk(protocol);
            P20102 p20102 = (P20102) protocol;
            if (p20102.resp.transcode.equals("9999")) {
                PromptUtils.showToast(mActivity, p20102.resp.msg);
            } else {
                if(title.getText().toString().equals("修改密码")){
                    /*if (NewMyActivity.activityInstance != null && !NewMyActivity.activityInstance.isFinishing()) {
                        NewMyActivity.activityInstance.finish();
                        NewMyActivity.activityInstance = null;
                    }
                    if (HallActivity.activityInstance != null && !HallActivity.activityInstance.isFinishing()) {
                        HallActivity.activityInstance.finish();
                        HallActivity.activityInstance = null;
                    }
                    if (ButtomMainActivity.activityInstance != null && !ButtomMainActivity.activityInstance.isFinishing()) {
                        ButtomMainActivity.activityInstance.finish();
                        ButtomMainActivity.activityInstance = null;
                    }
                    if (SetActivity.activityInstance != null && !SetActivity.activityInstance.isFinishing()) {
                        SetActivity.activityInstance.finish();
                        SetActivity.activityInstance = null;
                    }*/

                    //退出登录
                    SessionCache session = SessionCache.getInstance(mActivity);
                    session.del();
                    //登出环信
                    //    EMChatManager.getInstance().logout();
//                Log.i("", "登出环信");

                    //清除新消息红点提示
                   /* SimpleMessageCache simpleCache = SimpleMessageCache.getInstance(mActivity);
                    simpleCache.isNewXiu = false;
                    simpleCache.isNewMsg = false;
                    simpleCache.isNewMy = false;
                    simpleCache.newMsgNum = 0;
                    simpleCache.save();*/

                }
                PromptUtils.showToast(mActivity,"密码修改成功");
                startActivity(new Intent(mActivity, LoginActivity.class));
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

    public static void toForget(Context context,String phone ,String title){
        Intent intent = new Intent(context,NewForgetActivity.class);
        intent.putExtra("phone",phone);
        intent.putExtra("title",title);
        context.startActivity(intent);
    }


}
