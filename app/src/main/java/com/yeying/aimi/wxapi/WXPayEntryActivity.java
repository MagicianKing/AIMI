package com.yeying.aimi.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.mode.wallet.RechargeActivity;




public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private IWXAPI api;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            api = WXAPIFactory.createWXAPI(this, "wxa7030e690e05f443");
            api.handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        Log.e("WXPayEntryActivity", "onResp: "+resp.errCode );
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {//微信支付
            Bundle bundle = new Bundle();
            resp.toBundle(bundle);
            resp.fromBundle(bundle);
            if (resp.errCode == 0) {//成功
                /*Bundle bundle = new Bundle();
                resp.fromBundle(bundle);
                PayConst.setPay_status(PayConst.WEIXIN_SUCCESS);
                finish();*/
                Intent intent = new Intent(WXPayEntryActivity.this , RechargeActivity.class);
                intent.putExtra(RechargeActivity.WXPAY_STATUS , RechargeActivity.WXPAY_SUCCESS);
                startActivity(intent);
                finish();

            } else if (resp.errCode == -1) {//支付遇到问题
                /*PromptUtils.showToast(WXPayEntryActivity.this, "支付遇到问题");
                PayConst.setPay_fail(PayConst.WEIXIN_SUCCESS);
                finish();*/
                Intent intent = new Intent(WXPayEntryActivity.this , RechargeActivity.class);
                intent.putExtra(RechargeActivity.WXPAY_STATUS , RechargeActivity.WXPAY_ERROR);
                startActivity(intent);
                finish();
            } else if (resp.errCode == -2) {//取消支付
                /*Intent intent=new Intent(this,WalletActivity.class);
                startActivity(intent);
                finish();*/
                Intent intent = new Intent(WXPayEntryActivity.this , RechargeActivity.class);
                intent.putExtra(RechargeActivity.WXPAY_STATUS , RechargeActivity.WXPAY_CANCLE);
                startActivity(intent);
                finish();
            }

        }
    }
}
