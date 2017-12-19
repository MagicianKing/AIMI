package com.yeying.aimi.mode.wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.protoco.DefaultTask;
import com.yeying.aimi.protoco.IProtocol;
import com.yeying.aimi.protocol.impl.P10113;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.utils.PromptUtils;

/**
 * 钱包
 */
public class WalletActivity extends BaseActivity implements OnClickListener {

    private ImageView backBtn; //返回按钮
    private TextView titleTv; //标题
    private TextView moneyTv; //余额
    private Button rechargeBtn; //充值
    private int mRechargelim;
    private int mScore;
    private PopupWindow window;

    public static final int WALLET_CODE = 22;
    public static final String RECHARGE_MONEY = "recharge_money";
    private int pay_type = 2;

    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        mActivity = this;
        setContentView(R.layout.activity_wallet);
        initView();
    }

    private void initView() {
        backBtn = (ImageView) findViewById(R.id.wallet_back_image);
        titleTv = (TextView) findViewById(R.id.wallet_title_text);
        moneyTv = (TextView) findViewById(R.id.wallet_my_money_text);
        rechargeBtn = (Button) findViewById(R.id.wallet_recharge_btn);
        backBtn.setOnClickListener(this);
        rechargeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wallet_back_image:
                finish();
                break;

            case R.id.wallet_recharge_btn:
                Intent intent = new Intent(this, RechargeActivity.class);
                if (mRechargelim != 0) {
                    intent.putExtra("balance", mScore);//余额
                    intent.putExtra("lim", mRechargelim);//充值限制
                }
                Log.e("recharge", "onCreate: " + mScore);
                startActivityForResult(intent , WALLET_CODE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        initRecharge();//获取账户余额
    }

    //充值
    private void initRecharge() {
        P10113 p10113 = new P10113();
        p10113.req.userId = SessionCache.getInstance(this).userId;
        p10113.req.sessionId = SessionCache.getInstance(this).sessionId;
        new MysTask().execute(this, p10113);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        if (resultCode == RESULT_OK){
            if (requestCode == WALLET_CODE && result != null){
                int intExtra = result.getIntExtra(RECHARGE_MONEY, 0);
                moneyTv.setText((mScore + intExtra) + "猫币");
            }
        }
    }


    public class MysTask extends DefaultTask {

        @Override
        public void preExecute() {
            super.preExecute();
            window = PromptUtils.getProgressDialogPop(WalletActivity.this);
            window.showAtLocation(getWindow().getDecorView() , Gravity.CENTER , 0 , 0 );
        }

        public void onError(DefaultError obj) {
            super.onError(obj);
            Toast.makeText(mActivity, "网络错误", Toast.LENGTH_SHORT).show();
            if (window != null && window.isShowing()){
                window.dismiss();
            }
        }
        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            if (window != null && window.isShowing()){
                window.dismiss();
            }
            P10113 p10113 = (P10113) protocol;
            mRechargelim = p10113.resp.rechargeCeiling;
            mScore = p10113.resp.score;
            Log.e(TAG, "mScore------->"+mScore);
            Log.e(TAG, "p10113.resp.score--------->"+p10113.resp.score);
            if (p10113.resp.transcode.equals("9999")) {
                Toast.makeText(mActivity, p10113.resp.msg, Toast.LENGTH_SHORT)
                        .show();
                if (p10113.resp.code.equals("9062")) {

                }
            } else {
                moneyTv.setText(mScore + "猫币");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public static void toWallet(Context context){
        Intent intent = new Intent(context,WalletActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (window != null && window.isShowing()){
                window.dismiss();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
