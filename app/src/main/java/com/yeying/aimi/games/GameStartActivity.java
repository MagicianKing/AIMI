package com.yeying.aimi.games;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.mode.wallet.WalletActivity;
import com.yeying.aimi.protoco.DefaultTask;
import com.yeying.aimi.protoco.IProtocol;
import com.yeying.aimi.protocol.impl.P650000;
import com.yeying.aimi.protocol.impl.P650001;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.utils.PromptUtils;

public class GameStartActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 游戏介绍
     */
    private TextView mTvDis;
    /**
     * 入场计时:85s
     */
    private TextView mTvTime;
    /**
     * 跑得快
     */
    private TextView mTvTitle;
    private ImageView mImgbtnJoin;
    private PopupWindow mPayPopupWindow;
    /**
     * 入场
     */
    private TextView mTvJoin;
    private ImageView mImgbtnCancle;
    private ImageView mImgTicket;
    /**
     * 账户余额:10猫币
     */
    private TextView mTvBalance;
    /**
     * 充值
     */
    private TextView mTvRecharge;
    /**
     * 支付
     */
    private TextView mTvPay;
    /**
     * 游戏已经开始,下轮手速要快哦~
     */
    private TextView mTitle;
    /**
     * 确定
     */
    private TextView mSure;
    private Dialog mDialog;
    private RelativeLayout mImgBack;

    private int gameStatus;//游戏状态
    private int userScore;//用户余额
    private int gameScore;//游戏价格

    private int ingameStatus;//报名状态 0 失败 1 成功
    private String ingameUserRole;//游戏角色名
    private String ingameUUID;//游戏ID

    private SessionCache mSessionCache;

    public static final String BAR_ID = "game_start_bar_id";
    private String barId;
    private PopupWindow mPopupWindow;
    private TextView tv_gameScore;

    private PopupWindow popWindow_inGame;
    private ImageView game_bg;
    private FrameLayout gamestart_mengceng;
    private TextView gamestart_reday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamestart);
        mSessionCache = SessionCache.getInstance(this);
        initIntent();
        initView();
        initPayPopWindow();
        initDialog();

    }

    private void initIntent() {
        barId = getIntent().getStringExtra(BAR_ID);
        //barId = "20170807175615503";
    }

    /**
     * 游戏已经开始 弹窗提醒
     */
    private void initDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_startalert, null);
        mSure = (TextView) view.findViewById(R.id.sure);
        mSure.setOnClickListener(this);
        mDialog = new Dialog(this);
        mDialog.setContentView(view);
    }

    /**
     * 支付窗口
     */
    private void initPayPopWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.pop_joingames, null);

        initPopView(view);
        mPayPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPayPopupWindow.setFocusable(true);
        mPayPopupWindow.setOutsideTouchable(true);
        mPayPopupWindow.setAnimationStyle(R.style.GamePopAnim);
    }

    private void initPopView(View view) {
        mTvJoin = (TextView) view.findViewById(R.id.tv_join);
        mTvJoin.setOnClickListener(this);
        mImgbtnCancle = (ImageView) view.findViewById(R.id.imgbtn_cancle);
        mImgbtnCancle.setOnClickListener(this);
        mImgTicket = (ImageView) view.findViewById(R.id.img_ticket);
        tv_gameScore = (TextView) view.findViewById(R.id.tv_gameScore);
        mTvBalance = (TextView) view.findViewById(R.id.tv_balance);

        mTvRecharge = (TextView) view.findViewById(R.id.tv_recharge);
        mTvRecharge.setOnClickListener(this);
        mTvPay = (TextView) view.findViewById(R.id.tv_pay);
        mTvPay.setOnClickListener(this);

        SpannableString spannableString = new SpannableString(mTvRecharge.getText());
        spannableString.setSpan(new UnderlineSpan(), 0, mTvRecharge.getText().toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvRecharge.setText(spannableString);
    }

    private void initView() {
        gamestart_reday = (TextView) findViewById(R.id.gamestart_reday);
        gamestart_mengceng= (FrameLayout) findViewById(R.id.gamestart_mengceng);
        game_bg = (ImageView) findViewById(R.id.game_bg);
        mTvDis = (TextView) findViewById(R.id.tv_dis);
        mTvDis.setOnClickListener(this);
        mTvTime = (TextView) findViewById(R.id.tv_time);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mImgbtnJoin = (ImageView) findViewById(R.id.imgbtn_join);
        mImgbtnJoin.setOnClickListener(this);


        mTitle = (TextView) findViewById(R.id.title);

        mImgBack = (RelativeLayout) findViewById(R.id.title_left);
        mImgBack.setOnClickListener(this);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        requestData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        if (mPayPopupWindow != null && mPayPopupWindow.isShowing()) {
            mPayPopupWindow.dismiss();
            gamestart_mengceng.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_dis) {
            startActivity(new Intent(this, ExplainActivity.class));
        } else if (v.getId() == R.id.title_left) {
            finish();
        } else if (v.getId() == R.id.imgbtn_join) {//点击加入游戏
            if (gameStatus == 0){// 未开始
                PromptUtils.showToast(GameStartActivity.this , "游戏暂未开始");
            }else if (gameStatus == 1){//报名中
                if (mPayPopupWindow != null) {
                    if (!mPayPopupWindow.isShowing()) {
                        gamestart_mengceng.setVisibility(View.VISIBLE);
                        mPayPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                    } else {
                        gamestart_mengceng.setVisibility(View.GONE);
                        mPayPopupWindow.dismiss();
                    }
                }
            }else if (gameStatus == 2){//游戏中
                PromptUtils.showToast(GameStartActivity.this , "游戏已经开始");
            }
        } else if (v.getId() == R.id.tv_join) {

        } else if (v.getId() == R.id.imgbtn_cancle) {
            if (mPayPopupWindow != null && mPayPopupWindow.isShowing()) {
                mPayPopupWindow.dismiss();
                gamestart_mengceng.setVisibility(View.GONE);
            }
        } else if (v.getId() == R.id.tv_recharge) {
            WalletActivity.toWallet(GameStartActivity.this);
        } else if (v.getId() == R.id.tv_pay) {//支付完成后进入摇一摇界面
            requestInGame();
            //sendGameStartMsg();
        } else if (v.getId() == R.id.sure) {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }
    }

    /**
     * 请求当前游戏状态
     */
    private void requestData(){
        P650000 p650000 = new P650000();
        p650000.req.sessionId = mSessionCache.sessionId;
        p650000.req.userId = mSessionCache.userId;
        p650000.req.barId = barId;
        new GameStartTask().execute(getApplicationContext() , p650000);
    }

    /**
     * 当前游戏状态
     */
    private class GameStartTask extends DefaultTask{
        @Override
        public void preExecute() {
            super.preExecute();
            mPopupWindow = PromptUtils.getProgressDialogPop(GameStartActivity.this);
            mPopupWindow.showAtLocation(getWindow().getDecorView() , Gravity.CENTER , 0 , 0);
        }

        @Override
        public void onError(DefaultError obj) {
            super.onError(obj);
            PromptUtils.showToast(GameStartActivity.this , "网络错误");
            if (mPopupWindow != null && mPopupWindow.isShowing()){
                mPopupWindow.dismiss();
            }
        }

        @Override
        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            P650000 p650000 = (P650000) protocol;
            if (mPopupWindow != null && mPopupWindow.isShowing()){
                mPopupWindow.dismiss();
            }
            if (p650000.resp.transcode.equals("9999")){
                PromptUtils.showToast(GameStartActivity.this , p650000.resp.msg);
            }else {
                gameStatus = p650000.resp.status;// 0 未开始 1 报名中 2 游戏中
                if (gameStatus == 0){
                    //mImgbtnJoin.setImageResource(R.drawable.img_gamestop);
                    game_bg.setImageResource(R.drawable.game_unstart_bg);
                    gamestart_reday.setVisibility(View.GONE);
                    mImgbtnJoin.setVisibility(View.GONE);
                }else if (gameStatus == 1){
                    mImgbtnJoin.setImageResource(R.drawable.imgbtn_start);
                }else if (gameStatus == 2){
                    mImgbtnJoin.setImageResource(R.drawable.img_gamestop);
                }
                gameScore = p650000.resp.gameScore;
                userScore = p650000.resp.userScore;
                mTvBalance.setText("账户余额:"+userScore+"猫币");
                tv_gameScore.setText(gameScore+"猫币");
            }
        }
    }

    /**
     * 请求进入游戏
     */
    private void requestInGame(){
        P650001 p650001 = new P650001();
        p650001.req.sessionId = mSessionCache.sessionId;
        p650001.req.userId = mSessionCache.userId;
        p650001.req.barId = barId;
        new InGameTask().execute(getApplicationContext() , p650001);
    }

    /**
     * 游戏报名
     */
    private class InGameTask extends DefaultTask{
        @Override
        public void preExecute() {
            super.preExecute();
            popWindow_inGame = PromptUtils.getProgressDialogPop(GameStartActivity.this);
            popWindow_inGame.showAtLocation(getWindow().getDecorView() , Gravity.CENTER , 0 , 0);
        }

        @Override
        public void onError(DefaultError obj) {
            super.onError(obj);
            PromptUtils.showToast(GameStartActivity.this , "网络错误");
            if (popWindow_inGame != null && popWindow_inGame.isShowing()){
                popWindow_inGame.dismiss();
            }
        }

        @Override
        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            P650001 p650001 = (P650001) protocol;
            if (p650001.resp.transcode.equals("9999")){
                PromptUtils.showToast(GameStartActivity.this , p650001.resp.msg);
                if (popWindow_inGame != null && popWindow_inGame.isShowing()){
                    popWindow_inGame.dismiss();
                }
            }else {
                ingameStatus = p650001.resp.status;//0 失败 1 成功
                ingameUserRole = p650001.resp.userRole;
                ingameUUID = p650001.resp.uuid;
                if (ingameStatus == 0){
                    if (popWindow_inGame != null && popWindow_inGame.isShowing()){
                        popWindow_inGame.dismiss();
                    }
                    PromptUtils.showToast(GameStartActivity.this , "游戏加入失败");
                }else {
                    //进入游戏界面
                    if (popWindow_inGame != null && popWindow_inGame.isShowing()){
                        popWindow_inGame.dismiss();
                    }
                    GameActivity.toGameActivity(GameStartActivity.this , barId , ingameUUID , ingameUserRole);
                    finish();
                }
            }
        }
    }

    public static void toGameStart(Context context , String barId){
        Intent intent = new Intent(context , GameStartActivity.class);
        intent.putExtra(BAR_ID , barId);
        context.startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (mPopupWindow != null && mPopupWindow.isShowing()){
                mPopupWindow.dismiss();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
