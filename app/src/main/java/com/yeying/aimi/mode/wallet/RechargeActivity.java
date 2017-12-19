package com.yeying.aimi.mode.wallet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yeying.aimi.R;
import com.yeying.aimi.adapter.RechargeListAdapter;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.alipay.PayResult;
import com.yeying.aimi.bean.ChargeListBean;
import com.yeying.aimi.protoco.DefaultTask;
import com.yeying.aimi.protoco.IProtocol;
import com.yeying.aimi.protocol.impl.P10119;
import com.yeying.aimi.protocol.impl.P10323;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.utils.MD5;
import com.yeying.aimi.utils.PromptUtils;
import com.yeying.aimi.utils.WeakHandler;
import com.yeying.aimi.views.MeasuredGrideView;

import java.util.ArrayList;
import java.util.List;


/**
 * 用户充值
 */
public class RechargeActivity extends BaseActivity implements OnClickListener {
    private TextView titleTv; //标题
    private MeasuredGrideView rechargeValueGrid; //充值金额列表
    private Button rechargeBtn; // 充值
    private ImageView backBtn; //返回
    private RechargeListAdapter adapter; //充值列表适配器
    private String shopingId = "";
    private IWXAPI msgApi;
    private int mRechargeLim;
    private TextView mTvreChargeLim;
    private List<ChargeListBean> list = new ArrayList<>();
    private String mNumber;
    private int mBalance;
    private ImageButton title_menu;
    private TextView recharge_should;
    private TextView recharge_money;
    public static final int PAY_ALIPAY = 1;
    public static final int PAY_WXPAY = 2;
    public static final String PAY_TYEP = "pay_type";
    private HandlerPlus mHandlerPlus;
    private int mPay_type = PAY_WXPAY;
    private PayTask mAlipay;

    public static final int WXPAY_SUCCESS = 6;
    public static final int WXPAY_CANCLE = 7;
    public static final int WXPAY_ERROR = 8;
    public static final String WXPAY_STATUS = "wxpay_status";

    private RelativeLayout wallet_wechat,wallet_ali;
    private ImageView wallet_wechat_select,wallet_ali_select;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_rechage);
        mHandlerPlus = new HandlerPlus(this);
        mActivity = this;
        if (getIntent() != null) {
            mRechargeLim = getIntent().getIntExtra("lim", -1);//充值限制
            mBalance = getIntent().getIntExtra("balance", 0);//余额
        }
        initView();
    }

    public void initView() {
        wallet_ali = (RelativeLayout) findViewById(R.id.wallet_ali);
        wallet_wechat = (RelativeLayout) findViewById(R.id.wallet_wechat);
        wallet_ali_select = (ImageView) findViewById(R.id.wallet_ali_select);
        wallet_wechat_select = (ImageView) findViewById(R.id.wallet_wechat_select);
        wallet_ali.setOnClickListener(this);
        wallet_wechat.setOnClickListener(this);
        mTvreChargeLim = (TextView) findViewById(R.id.tv_rechargelim);
        recharge_money = (TextView) findViewById(R.id.recharge_money);
        recharge_money.setText("我的余额："+mBalance+"猫币");
        recharge_should = (TextView) findViewById(R.id.recharge_should);
        title_menu = (ImageButton) findViewById(R.id.title_menu);
        title_menu.setVisibility(View.GONE);
        titleTv = (TextView) findViewById(R.id.title_name);
        rechargeValueGrid = (MeasuredGrideView) findViewById(R.id.recharge_money_grid);
        rechargeBtn = (Button) findViewById(R.id.recharge_recharge_btn);
        backBtn = (ImageView) findViewById(R.id.title_back);
        titleTv.setText("充值 ");
        backBtn.setOnClickListener(this);
        rechargeBtn.setOnClickListener(this);

        adapter = new RechargeListAdapter(this, list);
        rechargeValueGrid.setAdapter(adapter);
        rechargeValueGrid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> partent, View view, int position,
                                    long arg3) {
                // TODO Auto-generated method stub
                adapter.setChecked(position);
                adapter.notifyDataSetChanged();
                shopingId = adapter.getItem(position).getsId();
                mNumber = list.get(position).getsNumber();
                StringBuilder stringBuilder = new StringBuilder(list.get(position).getsMoney());
                int i = stringBuilder.lastIndexOf(".");
                String front = stringBuilder.substring(0,i);
                String behind = stringBuilder.substring(i,stringBuilder.length());
                if (behind.length()>2){
                    recharge_should.setText("应付："+stringBuilder+"元");
                }else {
                    recharge_should.setText("应付："+front + "元");
                }
            }
        });

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        initRechargeList();
    }

    private void initRechargeList() {
        P10323 p10323 = new P10323();
        p10323.req.userId = SessionCache.getInstance(this).userId;
        p10323.req.sessionId = SessionCache.getInstance(this).sessionId;
        new MyTask().execute(getApplicationContext(), p10323);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recharge_recharge_btn:
                if (mNumber == null) {
                    return;
                }
                if (mBalance + Integer.parseInt(mNumber) >= mRechargeLim) {
                    PromptUtils.showToast(this, "猫币最高额度为" + mRechargeLim);
                    return;
                }
                //如果可以充值的话
                SharedPreferences sharedPreferences = getSharedPreferences("recharge", MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putInt("balance", Integer.parseInt(mNumber));
                edit.apply();
                edit.commit();
                initRecharge();
                break;
            case R.id.title_back:
                finish();
                break;
            case R.id.wallet_ali://支付方式--》支付宝
                resetColorAndImg();
                mPay_type = PAY_ALIPAY;
                wallet_ali.setBackgroundColor(getResources().getColor(R.color.pay_color));
                wallet_ali_select.setImageResource(R.drawable.paytype_select);
                break;
            case R.id.wallet_wechat://支付方式--》微信
                resetColorAndImg();
                mPay_type = PAY_WXPAY;
                wallet_wechat.setBackgroundColor(getResources().getColor(R.color.pay_color));
                wallet_wechat_select.setImageResource(R.drawable.paytype_select);
                break;
            default:
                break;
        }
    }

    public void initRecharge() {
        P10119 p10119 = new P10119();
        //支付方式
        p10119.req.payType = mPay_type;
        p10119.req.sessionId = SessionCache.getInstance(this).sessionId;
        p10119.req.transcode = "10119";
        //商品id
        p10119.req.shoppingId = shopingId;
        new RechargeTask().execute(this, p10119);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);

    }

    /**
     * 充值接口
     */
    public class RechargeTask extends DefaultTask {

        PopupWindow mPopupWindow;

        @Override
        public void preExecute() {
            // TODO Auto-generated method stub
            super.preExecute();
            mPopupWindow = PromptUtils.getProgressDialogPop(RechargeActivity.this);
            mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER,0,0);
        }

        public void onError(DefaultError obj) {
            super.onError(obj);
            Toast.makeText(mActivity, "网络错误", Toast.LENGTH_SHORT).show();
            if (mPopupWindow != null && mPopupWindow.isShowing()){
                mPopupWindow.dismiss();
            }
        }

        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            P10119 p = (P10119) protocol;
            if (mPopupWindow != null && mPopupWindow.isShowing()){
                mPopupWindow.dismiss();
            }
            if (p.resp.transcode.equals("9999")) {
                Toast.makeText(mActivity, " " + p.resp.msg, Toast.LENGTH_SHORT)
                        .show();
            } else {
                PromptUtils.showToast(RechargeActivity.this, "正在获取订单信息...");
                if (mPay_type == PAY_ALIPAY){//支付宝支付
                    Log.e("ooooo", "onOk: "+p.resp.payUrl);
                    pay_Ali(p.resp.payUrl);
                }else {//微信支付
                    Log.e("ooooo", "onOk: "+p.resp.payUrl);
                    pay_WeChat(p.resp.payUrl);
                }

            }
        }
    }

    /**
     * 微信支付
     * @param payUrl
     */
    private void pay_WeChat(String payUrl){
        JSONObject json = JSON.parseObject(payUrl);
        msgApi = WXAPIFactory.createWXAPI(RechargeActivity.this, json.getString("appid"),false);
        msgApi.registerApp(json.getString("appid"));
        PayReq request = new PayReq();
        request.appId = json.getString("appid");
        request.partnerId = json.getString("partnerid");
        request.prepayId = json.getString("prepayid");
        request.packageValue=json.getString("package");
        request.nonceStr = json.getString("noncestr");
        request.timeStamp = json.getString("timestamp");
        request.sign = json.getString("sign");
        msgApi.sendReq(request);
        //finish();
    }

    /**
     * 生成微信支付签名
     * @param key
     * @param appid
     * @param nonceStr
     * @param packageValue
     * @param partnerId
     * @param prepayId
     * @param timeStamp
     * @return
     */
    private String generateSign(String key , String appid , String nonceStr , String packageValue , String partnerId , String prepayId , String timeStamp){
        String stringA= "appid="+appid
                        +"&noncestr="+nonceStr
                        +"&package="+packageValue
                        +"&partnerid="+partnerId
                        +"&prepayid="+prepayId
                        +"&timestamp="+timeStamp;
        String stringSignTemp = stringA+"&key="+key;
        String sign = MD5.getMessageDigest(stringSignTemp.getBytes()).toUpperCase();
        return  sign;
    }

    /**
     * 支付宝支付
     * @param payOrderUrl
     */
    private void pay_Ali(final String payOrderUrl){
        final String payUrl = payOrderUrl;
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                mAlipay = new PayTask(mActivity);
                // 调用支付接口，获取支付结果
                String result = mAlipay.pay(payUrl, true);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = result;
                mHandlerPlus.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private class HandlerPlus extends WeakHandler{

        public HandlerPlus(Object o) {
            super(o);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            RechargeActivity activity = (RechargeActivity) getObjct();
            if (activity != null){
                switch (msg.what){
                    case 0:
                        PayResult payResult = new PayResult((String) msg.obj);
                        if (payResult.getResultStatus().equals("9000")){
                            PromptUtils.showToast(RechargeActivity.this , "支付成功");
                            Intent intent = new Intent();
                            intent.putExtra(WalletActivity.RECHARGE_MONEY , Integer.valueOf(mNumber));
                            setResult(RESULT_OK , intent);
                            finish();
                        }else {
                            if (payResult.getResultStatus().equals("8000")){
                                PromptUtils.showToast(RechargeActivity.this , "支付结果确认中...");
                            }else {
                                PromptUtils.showToast(RechargeActivity.this , "支付失败");
                            }
                        }
                        break;
                }
            }
        }
    }

    /**
     * 接收微信支付结果回调
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int intExtra = intent.getIntExtra(WXPAY_STATUS, 0);
        switch (intExtra){//支付宝支付
            case WXPAY_SUCCESS://支付成功
                PromptUtils.showToast(RechargeActivity.this , "支付成功");
                Intent intent1 = new Intent();
                intent1.putExtra(WalletActivity.RECHARGE_MONEY , Integer.valueOf(mNumber));
                setResult(RESULT_OK , intent1);
                finish();
                break;
            case WXPAY_CANCLE://取消支付
                PromptUtils.showToast(RechargeActivity.this , "支付失败");
                break;
            case WXPAY_ERROR://支付失败
                PromptUtils.showToast(RechargeActivity.this , "支付失败");
                break;
        }
    }

    /**
     * 充值信息列表
     */
    public class MyTask extends DefaultTask {

        PopupWindow mPopupWindow;

        @Override
        public void preExecute() {
            super.preExecute();
            mPopupWindow = PromptUtils.getProgressDialogPop(RechargeActivity.this);
            mPopupWindow.showAtLocation(getWindow().getDecorView(),Gravity.CENTER,0,0);
        }

        public void onError(DefaultError obj) {
            super.onError(obj);
            if (mPopupWindow != null && mPopupWindow.isShowing()){
                mPopupWindow.dismiss();
            }
        }

        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            P10323 p = (P10323) protocol;
            if (mPopupWindow != null && mPopupWindow.isShowing()){
                mPopupWindow.dismiss();
            }
            if (p.resp.transcode.equals("9999")) {
                Toast.makeText(mActivity, " " + p.resp.msg, Toast.LENGTH_SHORT)
                        .show();
            } else {
                List<ChargeListBean> chargeList = p.resp.chargeList;
                //默认首选
                chargeList.get(0).setChecked(true);
                shopingId = chargeList.get(0).getsId();
                mNumber = chargeList.get(0).getsNumber();
                StringBuilder stringBuilder = new StringBuilder(chargeList.get(0).getsMoney());
                int i = stringBuilder.lastIndexOf(".");
                String front = stringBuilder.substring(0,i);
                String behind = stringBuilder.substring(i,stringBuilder.length());
                if (behind.length()>2){
                    recharge_should.setText("应付："+stringBuilder+"元");
                }else {
                    recharge_should.setText("应付："+front + "元");
                }
                adapter.addData(chargeList);
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAlipay = null;
    }

    private void resetColorAndImg(){
        wallet_ali.setBackgroundColor(getResources().getColor(R.color.top_bar));
        wallet_wechat.setBackgroundColor(getResources().getColor(R.color.top_bar));
        wallet_ali_select.setImageResource(R.drawable.paytype_unselect);
        wallet_wechat_select.setImageResource(R.drawable.paytype_unselect);
    }
}
