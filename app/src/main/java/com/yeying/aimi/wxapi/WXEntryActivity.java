package com.yeying.aimi.wxapi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.PopupWindow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.yeying.aimi.API;
import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.mode.login.LoginActivity;
import com.yeying.aimi.utils.PromptUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * 微信登录回调界面
 */
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private PopupWindow pop;
    public static final String USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo?";
    //用户信息请求接口
    private static final String TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?";


    @SuppressLint("StaticFieldLeak")
    private AsyncTask<URL, String, String> mAsyncTaskUserInfo = new AsyncTask<URL, String, String>() {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e(TAG, "onPostExecute: " + s);
            if (s != null) {
                JSONObject jsonObject = JSON.parseObject(s);
                String openid = jsonObject.getString("openid");//唯一标识
                String nickname = jsonObject.getString("nickname");//昵称
                Integer sex = jsonObject.getInteger("sex");//1 男 2女
                String province = jsonObject.getString("province");//省份
                String country = jsonObject.getString("country");//国家
                String headimgurl = jsonObject.getString("headimgurl");//头像地址
                MobclickAgent.onProfileSignIn("WX",openid);
                dismissPop(pop);
                WXEntryActivity.this.finish();
                Intent intent = new Intent(WXEntryActivity.this , LoginActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("openid" , openid);
                bundle.putString("nickname" , nickname);
                bundle.putInt("sex" , sex);
                bundle.putString("province" , province);
                bundle.putString("country" , country);
                bundle.putString("headimgurl" , headimgurl);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }else {
                dismissPop(pop);
                showToast("网络错误，授权失败");
                finish();
            }
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            HttpURLConnection httpURLConnection = null;
            try {

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setReadTimeout(30000);
                httpURLConnection.setConnectTimeout(600000);
                httpURLConnection.connect();
                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {//链接成功
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuffer stringBuffer = new StringBuffer();
                    String len = "";
                    while ((len = bufferedReader.readLine()) != null) {
                        stringBuffer.append(len);
                    }
                    String s = stringBuffer.toString();
                    return s;
                } else {
                    //获取失败
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }

            }
            return null;
        }
    };
    @SuppressLint("StaticFieldLeak")
    private AsyncTask<URL, String, String> mAsyncTask = new AsyncTask<URL, String, String>() {

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param s The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e(TAG, "onPostExecute: " + s);
            if (!TextUtils.isEmpty(s)) {
                JSONObject jsonObject = JSON.parseObject(s);
                String access_token = jsonObject.getString("access_token");
                String openid = jsonObject.getString("openid");
                //access_token	接口调用凭证
                //expires_in	access_token接口调用凭证超时时间，单位（秒）
                //refresh_token	用户刷新access_token
                //openid	授权用户唯一标识
                //scope	用户授权的作用域，使用逗号（,）分隔
                //unionid 用户唯一标识
                jsonObject.getString("openid");
                jsonObject.getString("scope");
                jsonObject.getString("openid");
                jsonObject.getString("unionid");
                jsonObject.getString("refresh_token");
                jsonObject.getString("access_token");
                jsonObject.getString("expires_in");
                if (!TextUtils.isEmpty(access_token) && !TextUtils.isEmpty(openid)) {
                    String string = new StringBuilder(USERINFO_URL)
                            .append("access_token=")
                            .append(access_token)
                            .append("&openid=")
                            .append(openid)
                            .toString();
                    try {
                        mAsyncTaskUserInfo.execute(new URL(string));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }else {
                dismissPop(pop);
                showToast("网络错误，授权失败");
                finish();
            }
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            HttpURLConnection httpURLConnection = null;
            try {

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setReadTimeout(30000);
                httpURLConnection.setConnectTimeout(600000);
                httpURLConnection.connect();
                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {//链接成功
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuffer stringBuffer = new StringBuffer();
                    String len = "";
                    while ((len = bufferedReader.readLine()) != null) {
                        stringBuffer.append(len);
                    }
                    String s = stringBuffer.toString();
                    return s;
                } else {
                    //获取失败
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }

            }
            return null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wxentry_layout);
        IWXAPI wxapi = WXAPIFactory.createWXAPI(this, API.APP_ID, false);
        wxapi.handleIntent(getIntent(), this);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        pop = PromptUtils.getProgressDialogPop(this);
        pop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        SendAuth.Resp resp = (SendAuth.Resp) baseResp;
        Bundle bundle = new Bundle();
        resp.toBundle(bundle);
        resp.fromBundle(bundle);
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                String openId = resp.openId;
                String state = resp.state;//状态码
                String code = resp.code;//返回码,用来请求用户信息
                String lang = resp.lang;//语言
                String country = resp.country;//国家信息
                String s = new StringBuilder(TOKEN_URL).append("appid=")
                        .append(API.APP_ID)
                        .append("&secret=")
                        .append(API.APP_SECRET)
                        .append("&code=")
                        .append(code)
                        .append("&grant_type=authorization_code")
                        .toString();
                try {
                    URL url = new URL(s);
                    //获取 token
                    mAsyncTask.execute(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                showToast("授权失败");
                dismissPop(pop);
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                showToast("您已取消该授权");
                dismissPop(pop);
                finish();
                break;
        }
    }

    private void showToast(String s) {
        PromptUtils.showToast(this, s);
    }

    private void dismissPop(PopupWindow p) {
        if (p != null && p.isShowing()) {
            p.dismiss();
        }
    }
}
