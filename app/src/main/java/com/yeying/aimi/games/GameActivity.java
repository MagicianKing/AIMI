package com.yeying.aimi.games;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.yeying.aimi.R;
import com.yeying.aimi.aimibase.AIMIApplication;
import com.yeying.aimi.aimibase.BaseActivityWithoutSwipeBack;
import com.yeying.aimi.bean.GameTopBean;
import com.yeying.aimi.bean.JsonUserInfo;
import com.yeying.aimi.mode.HomeActivity;
import com.yeying.aimi.mode.bar_info.Rank_BP;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.utils.PromptUtils;
import com.yeying.aimi.utils.WeakHandler;

import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import static com.yeying.aimi.protocol.request.Request.content;

public class GameActivity extends BaseActivityWithoutSwipeBack implements SensorEventListener, HomeActivity.GameListener, AIMIApplication.GameRankListener, View.OnClickListener {

    private GifImageView mImgGameperson;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private long mInt = 0;
    private int shakeCount = 0;
    private int shakeSize = 0;
    private long mTimerinterval = 0;
    private SessionCache session;
    private EMConversation conversation;
    private String userRole = "gtq";
    private String gameId;
    private String barId;
    public static String USER_ROLE = "user_role";
    public static String GAME_ID = "game_id";
    public static String BAR_ID = "bar_id";
    private boolean isAllowShake = false;
    private GifDrawable mDrawable;
    private HandlerPlus mHandlerPlus;
    private Timer mTimer;
    private RelativeLayout mTitleLeft;
    private RelativeLayout layout_topbar;
    private TextView tv_dis;
    private boolean isAllowBackPress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        session = SessionCache.getInstance(this);
        if (HomeActivity.activityInstance != null) {
            HomeActivity.activityInstance.setOnGameListener(this);
        }
        mHandlerPlus = new HandlerPlus(this);
        initIntent();
        sendGameStartMsg();
        initSensor();
        initView();
    }

    private void initIntent() {
        barId = getIntent().getStringExtra(BAR_ID);
        gameId = getIntent().getStringExtra(GAME_ID);
        userRole = getIntent().getStringExtra(USER_ROLE);
    }

    /**
     * 给用户分配动态图
     */
    private int dealUserRole() {
        int userGif = R.drawable.qiang_run;
        if (userRole.equals("gtq")) {//光头强
            userGif = R.drawable.qiang_run;
        } else if (userRole.equals("hl")) {//葫芦娃
            userGif = R.drawable.hoist_run;
        } else if (userRole.equals("jqm")) {//机器猫
            userGif = R.drawable.doraemon_run;
        } else if (userRole.equals("kn")) {//柯南
            userGif = R.drawable.conan_run;
        } else if (userRole.equals("mr")) {//鸣人
            userGif = R.drawable.naruto_run;
        } else if (userRole.equals("mv")) {//美女
            userGif = R.drawable.beautiful_run;
        } else if (userRole.equals("ss")) {//死神
            userGif = R.drawable.death_run;
        } else if (userRole.equals("wk")) {//悟空
            userGif = R.drawable.wukong_run;
        } else if (userRole.equals("xm")) {//熊猫
            userGif = R.drawable.panda_run;
        } else if (userRole.equals("x")) {//熊
            userGif = R.drawable.bear_run;
        }
        return userGif;
    }

    /**
     * 初始化传感器
     */
    private void initSensor() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager == null) {
            return;
        }
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    private void initView() {
        tv_dis = (TextView) findViewById(R.id.tv_dis);
        tv_dis.setOnClickListener(this);
        layout_topbar = (RelativeLayout) findViewById(R.id.layout_topbar);
        mTitleLeft = (RelativeLayout) findViewById(R.id.title_left);
        mTitleLeft.setOnClickListener(this);
        mImgGameperson = (GifImageView) findViewById(R.id.img_gameperson);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
        mImgGameperson.setImageResource(dealUserRole());
        mDrawable = (GifDrawable) mImgGameperson.getDrawable();
        mDrawable.start();
        //设置游戏排行榜结果监听
        ((AIMIApplication) getApplicationContext()).setGameRankListener(this);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(this);
        //初始化计时
        mInt = 0;
        ((AIMIApplication) getApplicationContext()).unRegisteGameRankListener();
        super.onPause();
    }

    /**
     * Called when there is a new sensor event.  Note that "on changed"
     * is somewhat of a misnomer, as this will also be called if we have a
     * new reading from a sensor with the exact same sensor values (but a
     * newer timestamp).
     * <p>
     * <p>See {@link SensorManager SensorManager}
     * for details on possible sensor types.
     * <p>See also {@link SensorEvent SensorEvent}.
     * <p>
     * <p><b>NOTE:</b> The application doesn't own the
     * {@link SensorEvent event}
     * object passed as a parameter and therefore cannot hold on to it.
     * The object may be part of an internal pool and may be reused by
     * the framework.
     *
     * @param event the {@link SensorEvent SensorEvent}.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        float x = Math.abs(values[0]);
        float y = Math.abs(values[1]);
        float z = Math.abs(values[2]);
        int value = 15; //阀值
        if (x > value || y > value || z > value) {//每个轴变动次数
            shakeCount++;//记录变化次数
            if (allowShake()) {//大于间隔0.4秒才记录
                if (shakeCount > 4) {//四次变化归为一次摇动
                    Log.e("TAG", "onSensorChanged: " + shakeCount / 4);
                    send(String.valueOf(shakeCount / 4));
                    shakeCount = 0;
                }
            }
        }
    }

    private void send(String shakeNums) {
        if (isAllowShake) {
            sendTxtMessage(shakeNums);
        }
    }

    /**
     * Called when the accuracy of the registered sensor has changed.  Unlike
     * onSensorChanged(), this is only called when this accuracy value changes.
     * <p>
     * <p>See the SENSOR_STATUS_* constants in
     * {@link SensorManager SensorManager} for details.
     *
     * @param sensor
     * @param accuracy The new accuracy of this sensor, one of
     *                 {@code SensorManager.SENSOR_STATUS_*}
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //判断间隔是否是0.1秒,是否允许摇动
    private boolean allowShake() {
        if (mInt == 0) {//第一次摇动
            mInt = System.currentTimeMillis();
        } else {//第二次摇动
            if (System.currentTimeMillis() - mInt >= 1000) {//间隔0.4秒
                mInt = System.currentTimeMillis();//重新计时
                return true;
            } else {
                return false;
            }
        }
        return true;
    }


    private void sendTxtMessage(String shakeNums) {
        EMMessage sendMessage = EMMessage.createSendMessage(EMMessage.Type.TXT);
        // 设置为群聊
        sendMessage.setChatType(EMMessage.ChatType.Chat);
        TextMessageBody txtBody = new TextMessageBody("");
        // 设置消息body
        sendMessage.addBody(txtBody);
        // 设置要发给谁,用户username或者群聊groupid
        sendMessage.setReceipt(barId + "Runway");
        String chatInfo = dealChatInfo();
        //扩展消息
        sendMessage.setAttribute("userInfo", chatInfo);
        sendMessage.setAttribute("transCode", "cc00011");
        sendMessage.setAttribute("em_ignore_notification", false);
        sendMessage.setAttribute("isSystemMsg", false);
        sendMessage.setAttribute("userCharacter", userRole);//用户角色
        sendMessage.setAttribute("runFastId", gameId);//游戏ID
        sendMessage.setAttribute("identification", "1");//0 进入时发 1 摇的时候发
        sendMessage.setAttribute("stepNumber" , shakeNums);
        EMChatManager.getInstance().sendMessage(sendMessage, new EMCallBack() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    /**
     * 用户聊天信息
     *
     * @return
     */
    private String dealChatInfo() {
        JsonUserInfo jsonUserInfo = new JsonUserInfo();
        jsonUserInfo.setUserId(session.userId);
        jsonUserInfo.setUserName(session.nickname);
        jsonUserInfo.setHeadImg(session.headimgUrl);
        jsonUserInfo.setUserId2("");
        jsonUserInfo.setUserName2("");
        jsonUserInfo.setHeadImg2("");
        jsonUserInfo.setGender(session.sex);
        return JSON.toJSONString(jsonUserInfo);
    }

    public static void toGameActivity(Context context, String barId, String gameId, String userRole) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra(BAR_ID, barId);
        intent.putExtra(GAME_ID, gameId);
        intent.putExtra(USER_ROLE, userRole);
        context.startActivity(intent);
    }

    /**
     * 游戏开始
     */
    @Override
    public void gameStart() {
        mHandlerPlus.sendEmptyMessage(0);
    }

    /**
     * 你到达终点
     */
    @Override
    public void gameEnd() {
        mHandlerPlus.sendEmptyMessage(1);
    }

    /**
     * 游戏整体结束
     */
    @Override
    public void gameOver() {
        mHandlerPlus.sendEmptyMessage(2);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDrawable.stop();
    }

    /**
     * 游戏排行榜回调监听
     */
    @Override
    public void onGameRankListener(String tempInfo) {
        mTimer.cancel();
        Message msg = mHandlerPlus.obtainMessage();
        msg.what = 3;
        msg.obj = tempInfo;
        mHandlerPlus.sendMessage(msg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_left:
                finish();
                break;
            case R.id.tv_dis:
                startActivity(new Intent(this, ExplainActivity.class));
                break;
        }
    }

    private class HandlerPlus extends WeakHandler {

        public HandlerPlus(Object o) {
            super(o);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            GameActivity gameActivity = (GameActivity) getObjct();
            if (gameActivity != null) {
                switch (msg.what) {
                    case 0:
                        PromptUtils.showToast(GameActivity.this, "游戏开始");
                        startTimer();
                        isAllowShake = true;
                        break;
                    case 1:
                        PromptUtils.showToast(GameActivity.this, "您已到达终点");
                        isAllowShake = false;
                        break;
                    case 2:
                        PromptUtils.showToast(GameActivity.this, "游戏结束");
                        mSensorManager.unregisterListener(GameActivity.this);
                        isAllowShake = false;
                        break;
                    case 3:
                        String tempInfo = (String) msg.obj;
                        GameTopBean gameTopBean = JSON.parseObject(tempInfo, GameTopBean.class);
                        if (gameTopBean.getSeason().equals(gameId)){//是本场游戏
                            Rank_BP.toRank(GameActivity.this , barId , Rank_BP.RANK_GAME , gameTopBean);
                            finish();
                        }
                        break;
                    case 4://计时器到时间
                        isAllowBackPress = true;
                        layout_topbar.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isAllowBackPress){
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 向大屏发送游戏进入消息
     */
    private void sendGameStartMsg() {
        EMMessage sendMessage = EMMessage.createSendMessage(EMMessage.Type.TXT);
        // 设置为群聊
        sendMessage.setChatType(EMMessage.ChatType.Chat);
        TextMessageBody txtBody = new TextMessageBody(content);
        // 设置消息body
        sendMessage.addBody(txtBody);
        // 设置要发给谁,用户username或者群聊groupid
        sendMessage.setReceipt(barId + "Runway");
        String chatInfo = dealChatInfo();
        //扩展消息
        sendMessage.setAttribute("userInfo", chatInfo);
        sendMessage.setAttribute("transCode", "cc00011");
        sendMessage.setAttribute("em_ignore_notification", false);
        sendMessage.setAttribute("isSystemMsg", false);
        sendMessage.setAttribute("userCharacter", userRole);//用户角色
        sendMessage.setAttribute("runFastId", gameId);//游戏ID
        sendMessage.setAttribute("identification", "0");//0 进入时发 1 摇的时候发
        EMChatManager.getInstance().sendMessage(sendMessage, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.e(TAG, "onSuccess: " + "消息成功");
            }

            @Override
            public void onError(int i, String s) {
                Log.e(TAG, "onSuccess: " + "消息失败");
            }

            @Override
            public void onProgress(int i, String s) {
                Log.e(TAG, "onSuccess: " + "消息正在发送");
            }
        });
    }

    /**
     * 游戏开始后开启一个计时器 如果游戏结束后没有收到透传
     * 则65秒后自动finish掉此界面
     */
    private void startTimer(){
        mTimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                mHandlerPlus.sendEmptyMessage(4);
            }
        };
        mTimer.schedule(task , 65*1000);
    }

}
