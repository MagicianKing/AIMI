package com.yeying.aimi.mode.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yeying.aimi.API;
import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.mode.HomeActivity;
import com.yeying.aimi.protoco.DefaultTask;
import com.yeying.aimi.protoco.IProtocol;
import com.yeying.aimi.protocol.impl.P11001;
import com.yeying.aimi.protocol.impl.P20101;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.storage.SimpleMessageCache;
import com.yeying.aimi.utils.FileUtil;
import com.yeying.aimi.utils.PromptUtils;
import com.yeying.aimi.utils.Tools;
import com.yeying.aimi.utils.WeakHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

public class LoginActivity extends BaseActivity implements View.OnClickListener, UMAuthListener {

    private EditText mLoginPhone;
    private EditText mLoginPwd;
    /**
     * login
     */
    private TextView mLoginBtn;
    /**
     * forgetpassword
     */
    private TextView mForget;
    /**
     * nowregister
     */
    private TextView mRegister;

    private String filpath = "/sdcard/key.txt";

    private SessionCache session;

    private PopupWindow mPopupWindow;
    private boolean isReback = false;
    private LogInTask mLogInTask;
    private ImageView mImgWx;
    private ImageView mImgQq;
    private ImageView mImgWeibo;
    private IWXAPI mWxapi;
    private PopupWindow mPopupWindow_ReAuth;
    private SHARE_MEDIA mSHARE_media;
    private TextView mTv_msg;
    private SharedPreferences mSharedPreferences;
    private String user_account = null;
    private String user_password = null;
    private HandlerPlus mHandlerPlus;

    //用户信息
    private String nickName;
    private String headUrl;
    private int gender;
    private String openId;
    private int third_type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mWxapi = WXAPIFactory.createWXAPI(this, API.APP_ID, true);
        mWxapi.registerApp(API.APP_ID);
        session = SessionCache.getInstance(getApplicationContext());
        checkLocationPermission(false);
        mHandlerPlus = new HandlerPlus(this);
        mSharedPreferences = getSharedPreferences(API.USER_INFO, MODE_PRIVATE);
        user_account = mSharedPreferences.getString(API.USER_ACCOUNT, null);
        user_password = mSharedPreferences.getString(API.USER_PASSWORD, null);
        initIntent();
        initView();
        initPop();
    }

    /**
     * 询问是否重新授权的窗口
     */
    private void initPop() {
        View view = LayoutInflater.from(this).inflate(R.layout.del_msg_window, null, false);
        mPopupWindow_ReAuth = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        mTv_msg = (TextView) view.findViewById(R.id.tv_msg);
        mTv_msg.setTextSize(14f);
        TextView bt_close = (TextView) view.findViewById(R.id.bt_close);
        TextView bt_sure = (TextView) view.findViewById(R.id.bt_sure);
        mTv_msg.setText("当前账号已授权改应用，是否直接登录？");
        bt_close.setOnClickListener(this);
        bt_sure.setOnClickListener(this);
    }

    private void initIntent() {
        isReback = getIntent().getBooleanExtra("is_reback", true);
    }

    private void initView() {
        mLoginPhone = (EditText) findViewById(R.id.login_phone);
        mLoginPwd = (EditText) findViewById(R.id.login_pwd);
        mLoginBtn = (TextView) findViewById(R.id.login_btn);
        mForget = (TextView) findViewById(R.id.forget);
        mForget.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mForget.setOnClickListener(this);
        mRegister = (TextView) findViewById(R.id.register);
        mRegister.setOnClickListener(this);
        if (!TextUtils.isEmpty(user_account)) {
            mLoginPhone.setText(user_account);
        }
        mLoginPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11) {

                    if (mLoginPwd.getText().toString().trim() != null && mLoginPwd.getText().toString().length() >= 6) {
                        mLoginBtn.setEnabled(true);
                        mLoginBtn.setBackground(getResources().getDrawable(R.drawable.new_btnstyle));
                    } else {
                        mLoginBtn.setEnabled(false);
                        mLoginBtn.setBackground(getResources().getDrawable(R.drawable.button_gray_disable));
                    }
                } else {
                    mLoginBtn.setEnabled(false);
                    mLoginBtn.setBackground(getResources().getDrawable(R.drawable.button_gray_disable));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mLoginPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 6) {

                    if (mLoginPhone.getText().toString().trim() != null && mLoginPhone.getText().toString().length() >= 11) {
                        mLoginBtn.setEnabled(true);
                        mLoginBtn.setBackground(getResources().getDrawable(R.drawable.new_btnstyle));
                    } else {
                        mLoginBtn.setEnabled(false);
                        mLoginBtn.setBackground(getResources().getDrawable(R.drawable.button_gray_disable));
                    }
                } else {
                    mLoginBtn.setEnabled(false);
                    mLoginBtn.setBackground(getResources().getDrawable(R.drawable.button_gray_disable));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mLoginBtn = (TextView) findViewById(R.id.login_btn);
        mLoginBtn.setOnClickListener(this);
        mImgWx = (ImageView) findViewById(R.id.img_wx);
        mImgWx.setOnClickListener(this);
        mImgQq = (ImageView) findViewById(R.id.img_qq);
        mImgQq.setOnClickListener(this);
        mImgWeibo = (ImageView) findViewById(R.id.img_weibo);
        mImgWeibo.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isReback) {
            if (!TextUtils.isEmpty(session.phone) && !TextUtils.isEmpty(session.nickname) && !TextUtils.isEmpty(session.sex)) {
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }
        if (session.sessionId != null) {
            //登出环信
            EMChatManager.getInstance().logout();
            final String IMAGE_CAPTURE_NAME = "temp.jpg";
            //清除缓存的头像
            File file = new File(FileUtil.getHeadimageCache(getApplicationContext()),
                    IMAGE_CAPTURE_NAME);
            if (file.exists()) {
                file.delete();
            }
            //清除新消息红点提示
            SimpleMessageCache simpleCache = SimpleMessageCache.getInstance(getApplicationContext());
            simpleCache.isNewXiu = false;
            simpleCache.isNewMsg = false;
            simpleCache.newMsgNum = 0;
            simpleCache.isNewMy = false;
            simpleCache.save();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.forget:
                intent = new Intent(this, NewForgetActivity.class);
                intent.putExtra("title", "忘记密码");
                intent.putExtra("phone", mLoginPhone.getText().toString().trim());
                startActivity(intent);
                break;
            case R.id.register:
                intent = new Intent(LoginActivity.this, NewRegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.login_btn:
                mPopupWindow = PromptUtils.getProgressDialogPop(LoginActivity.this);
                mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                initLogIn(mLoginPhone.getText().toString(), mLoginPwd.getText().toString());
                break;
            case R.id.img_wx:
                SendAuth.Req req = new SendAuth.Req();
                req.scope = API.APP_SCOPE;
                req.state = System.currentTimeMillis() + "";
                mWxapi.sendReq(req);
                PromptUtils.showToast(LoginActivity.this, "正在拉取授权信息");
                break;
            case R.id.img_qq:
                authQQ();
                break;
            case R.id.img_weibo:
                authSINA();
                break;
            case R.id.bt_close:
                dismissPop(mPopupWindow_ReAuth);
                break;
            case R.id.bt_sure:
                deleteAuto(mSHARE_media);
                dismissPop(mPopupWindow_ReAuth);
                break;
        }
    }


    /**
     * 登录逻辑
     *
     * @param account
     * @param password
     */
    private void initLogIn(String account, String password) {
        if (TextUtils.isEmpty(account)) {
            PromptUtils.showToast(LoginActivity.this, "请输入账号");
        } else if (TextUtils.isEmpty(password)) {
            PromptUtils.showToast(LoginActivity.this, "请输入密码");
        } else {
            P20101 p20101 = new P20101();
            p20101.req.phone = account;
            p20101.req.pwd = password;
            mLogInTask = new LogInTask();
            mLogInTask.execute(getApplicationContext(), p20101);
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

    /**
     * popwindow消失
     *
     * @param popupWindow
     */
    private void dismissPop(PopupWindow popupWindow) {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    /**
     * 开始授权
     *
     * @param share_media
     */
    @Override
    public void onStart(SHARE_MEDIA share_media) {
        PromptUtils.showToast(LoginActivity.this, "正在拉取授权信息");
    }

    /**
     * QQ 微博授权完成后的处理 微信单独授权 不使用友盟授权
     *
     * @param share_media
     * @param i
     * @param map
     */
    @Override
    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
        switch (share_media) {
            case QQ:
                if (map != null && map.size() > 0) {
                    String name = map.get("name");//姓名
                    String uid = map.get("uid");//uid
                    String expiration = map.get("expiration");//过期时间
                    String access_token = map.get("access_token");
                    String iconurl = map.get("iconurl");//头像 url
                    String sex = map.get("gender");
                    MobclickAgent.onProfileSignIn("QQ", uid);
                    third_type = 1;
                    nickName = name;
                    openId = uid;
                    gender = dealGender(sex);
                    headUrl = iconurl;
                    generateP11001(uid , 1);
                }

                break;
            case SINA:
                if (map != null && map.size() > 0) {
                    String name = map.get("name");
                    String uid = map.get("uid");
                    String accesstoken = map.get("accesstoken");
                    String expiration = map.get("expiration");
                    String refreshtoken = map.get("refreshtoken");
                    String location = map.get("location");
                    String iconurl = map.get("iconurl");
                    String sex = map.get("gender");
                    String followers_count = map.get("followers_count");//关注数量
                    String friends_count = map.get("friends_count");//好友数量
                    MobclickAgent.onProfileSignIn("SINA", uid);
                    third_type = 2;
                    nickName = name;
                    openId = uid;
                    gender = dealGender(sex);
                    headUrl = iconurl;
                    generateP11001(uid , 2);
                }
                break;
        }
    }

    @Override
    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
        PromptUtils.showToast(LoginActivity.this, "授权失败");
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media, int i) {
        PromptUtils.showToast(LoginActivity.this, "您已取消该授权");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == API.LOGIN_REQUEST) {
                if (data != null) {
                    boolean isIn = data.getBooleanExtra("isIn", false);
                    isReback = data.getBooleanExtra("is_reback", true);
                    if (isIn) {
                        startActivity(new Intent(this, HomeActivity.class));
                        finish();
                        //loginHX();
                    }
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        isReback = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLogInTask = null;
    }

    private class LogInTask extends DefaultTask {
        @Override
        public void onError(DefaultError obj) {
            super.onError(obj);
            dismissPop(mPopupWindow);
            PromptUtils.showToast(LoginActivity.this, "网络错误");
        }

        @Override
        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            P20101 p20101 = (P20101) protocol;
            if (p20101.resp.transcode.equals("9999")) {
                PromptUtils.showToast(LoginActivity.this, p20101.resp.msg);
                if (mPopupWindow != null && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
            } else {
                saveKey(filpath, p20101.resp.scretKey);
                session.phone = p20101.resp.phone;
                session.nickname = p20101.resp.nickname;
                session.userId = p20101.resp.userId;
                session.sessionId = p20101.resp.getSessionId();
                session.dataNeedComplete = p20101.resp.dataNeedComplete;
                session.real_name = p20101.resp.real_name;
                session.card_no = p20101.resp.card_no;
                session.headimgUrl = p20101.resp.headUrl;
                session.imgUrl = p20101.resp.imgUrl;
                session.sex = String.valueOf(p20101.resp.gender);
                SharedPreferences.Editor edit = mSharedPreferences.edit();
                edit.putString(API.USER_ACCOUNT, p20101.resp.phone);
                edit.commit();
                //Tools图片加载头修改
                Tools.HEADURL = p20101.resp.imgUrl;
                Log.e(TAG, "onOk========>: " + Tools.HEADURL);
                Log.e(TAG, "onOk========>: " + p20101.resp.imgUrl);
                session.save();
                MobclickAgent.onProfileSignIn(session.userId);
                int complete = p20101.resp.dataNeedComplete;
                if (complete == 1) {
                    if (mPopupWindow != null && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                    Intent personIntent = new Intent(LoginActivity.this, PerfectDataActivity.class);
                    startActivityForResult(personIntent, API.LOGIN_REQUEST);
                } else if (complete == 0) {
                    loginHX();
                }
            }
        }
    }

    /**
     * 授权QQ登录
     */
    private void authQQ() {
        mSHARE_media = SHARE_MEDIA.QQ;
        boolean authorize = UMShareAPI.get(this).isAuthorize(this, SHARE_MEDIA.QQ);
        if (authorize) {
            mTv_msg.setText("当前QQ账号已授权该应用，是否重新授权?");
            mPopupWindow_ReAuth.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        } else {
            PromptUtils.showToast(this, "正在拉取授权信息");
            UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.QQ, this);
        }
    }

    /**
     * 授权微博回调
     */
    private void authSINA() {
        mSHARE_media = SHARE_MEDIA.SINA;
        boolean authorize1 = UMShareAPI.get(this).isAuthorize(this, SHARE_MEDIA.SINA);
        if (authorize1) {
            mTv_msg.setText("当前weibo账号已授权该应用，是否重新授权?");
            mPopupWindow_ReAuth.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        } else {
            PromptUtils.showToast(this, "正在拉取授权信息");
            UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.SINA, this);
        }
    }

    /**
     * 如果 QQ或者微博 账号已经授权 则询问是否再次授权
     *
     * @param share_media
     */
    private void deleteAuto(SHARE_MEDIA share_media) {
        UMShareAPI.get(this).deleteOauth(this, share_media, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this, share_media, LoginActivity.this);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {

            }
        });
    }

    /**
     * 接收微信登录回调的信息
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        String openId_one = bundle.getString("openid");
        third_type = 0;
        nickName = bundle.getString("nickname");
        headUrl = bundle.getString("headimgurl");
        gender = bundle.getInt("sex");
        openId = openId_one;
        generateP11001(openId_one , 0);
    }

    /**
     * 登录环信
     */
    private void loginHX() {
        if (EMChat.getInstance().isLoggedIn()) {
            Log.e("success", "已经登陆成功--------");
            loginHXSuccess();
        } else {
            EMChatManager.getInstance().login(session.userId, session.userId, new EMCallBack() {//回调
                @Override
                public void onSuccess() {
                    Log.e("success", "onSuccess: 环信登录成功");
                    loginHXSuccess();
                }

                @Override
                public void onProgress(int progress, String status) {

                }

                @Override
                public void onError(int code, String message) {
                    Log.e("success", "onError: 环信登录失败");
                }
            });
        }
    }

    /**
     * 环信登录成功后逻辑
     */
    private void loginHXSuccess() {
        mHandlerPlus.sendEmptyMessage(0);
    }

    private class HandlerPlus extends WeakHandler {

        public HandlerPlus(Object o) {
            super(o);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LoginActivity objct = (LoginActivity) getObjct();
            if (objct != null) {
                switch (msg.what) {
                    case 0:
                        if (mPopupWindow != null && mPopupWindow.isShowing()) {
                            mPopupWindow.dismiss();
                        }
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
            }
        }
    }

    private void generateP11001(String openId , int type){
        P11001 p11001 = new P11001();
        p11001.req.thirdId = openId;
        p11001.req.thirdType = type;
        new ThridAuthTask().execute(getApplicationContext() , p11001);
    }

    /**
     * 三方登录绑定
     */
    private class ThridAuthTask extends DefaultTask{

        PopupWindow mPopupWindow ;

        @Override
        public void preExecute() {
            super.preExecute();
            mPopupWindow= PromptUtils.getProgressDialogPop(LoginActivity.this);
            mPopupWindow.showAtLocation(getWindow().getDecorView() , Gravity.CENTER , 0 , 0);
        }

        @Override
        public void onError(DefaultError obj) {
            super.onError(obj);
            PromptUtils.showToast(LoginActivity.this , "网络错误");
            dismissPop(mPopupWindow);
        }

        @Override
        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            P11001 p11001 = (P11001) protocol;
            dismissPop(mPopupWindow);
            if (p11001.resp.transcode.equals("9999")){
                PromptUtils.showToast(LoginActivity.this , p11001.resp.msg+"");
            }else {
                if (p11001.resp.register == 1){//用户绑定过-->直接进入
                    saveKey(filpath, p11001.resp.scretKey);
                    session.phone = p11001.resp.phone;
                    session.nickname = p11001.resp.nickname;
                    session.userId = p11001.resp.userId;
                    session.sessionId = p11001.resp.getSessionId();
                    session.dataNeedComplete = p11001.resp.dataNeedComplete;
                    session.headimgUrl = p11001.resp.headImg;
                    session.imgUrl = p11001.resp.imgUrl;
                    session.sex = String.valueOf(p11001.resp.gender);
                    //Tools图片加载头修改
                    Tools.HEADURL = p11001.resp.imgUrl;
                    session.save();
                    startActivity(new Intent(LoginActivity.this , HomeActivity.class));
                    finish();
                }else {//用户未绑定过-->进入绑定页面
                    Intent personIntent = new Intent(LoginActivity.this, BindPhone.class);
                    personIntent.putExtra(API.USER_NICK_NAME , nickName);
                    personIntent.putExtra(API.USER_HEAD_URL , headUrl);
                    personIntent.putExtra(API.USER_GENDER , gender);
                    personIntent.putExtra(API.USER_OPEN_ID , openId);
                    personIntent.putExtra(API.USER_THIRD_TYPE , third_type);
                    startActivityForResult(personIntent, API.LOGIN_REQUEST);
                }
            }
        }
    }

    private int dealGender(String value){
        if (value.equals("男")){
            return 1;
        }else {
            return 2;
        }
    }

}
