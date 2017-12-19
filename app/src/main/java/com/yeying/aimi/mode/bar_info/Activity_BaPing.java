package com.yeying.aimi.mode.bar_info;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.easemob.EMCallBack;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.yeying.aimi.API;
import com.yeying.aimi.R;
import com.yeying.aimi.adapter.BPHeadAdapter;
import com.yeying.aimi.adapter.BPMoneyAdapter;
import com.yeying.aimi.adapter.ExpressionAdapter;
import com.yeying.aimi.adapter.MessageAdapter;
import com.yeying.aimi.aimibase.AIMIApplication;
import com.yeying.aimi.aimibase.BaseActivity;
import com.yeying.aimi.bean.FollBean;
import com.yeying.aimi.bean.JsonGiftMsg;
import com.yeying.aimi.bean.JsonUserInfo;
import com.yeying.aimi.bean.RedPacketBean;
import com.yeying.aimi.bean.ReplyCacheBean;
import com.yeying.aimi.bean.ScreenOptionBean;
import com.yeying.aimi.bean.UserPermissionBean;
import com.yeying.aimi.database.HBData;
import com.yeying.aimi.games.GameStartActivity;
import com.yeying.aimi.huanxin.EaseConstant;
import com.yeying.aimi.huanxin.ExpressionPagerAdapter;
import com.yeying.aimi.huanxin.PasteEditText;
import com.yeying.aimi.huanxin.SmileUtils;
import com.yeying.aimi.mode.otherdetails.MineHomepage;
import com.yeying.aimi.mode.otherdetails.OtherHomepage;
import com.yeying.aimi.mode.photopicker.AlbmActivity;
import com.yeying.aimi.mode.wallet.WalletActivity;
import com.yeying.aimi.protoco.DefaultTask;
import com.yeying.aimi.protoco.FormFile;
import com.yeying.aimi.protoco.IProtocol;
import com.yeying.aimi.protoco.SocketBean;
import com.yeying.aimi.protoco.SocketHttpRequester;
import com.yeying.aimi.protocol.impl.P10112;
import com.yeying.aimi.protocol.impl.P10113;
import com.yeying.aimi.protocol.impl.P10220;
import com.yeying.aimi.protocol.impl.P10221;
import com.yeying.aimi.protocol.impl.P13101;
import com.yeying.aimi.protocol.impl.P13102;
import com.yeying.aimi.protocol.impl.P23104;
import com.yeying.aimi.protocol.impl.P610001;
import com.yeying.aimi.storage.AttendCache;
import com.yeying.aimi.storage.ReplyCache;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.storage.SimpleMessageCache;
import com.yeying.aimi.utils.BackgroundCacheStuffer;
import com.yeying.aimi.utils.BiliDanmukuParser;
import com.yeying.aimi.utils.CacheStufferAdapter;
import com.yeying.aimi.utils.CornerBitmapTransformation;
import com.yeying.aimi.utils.EaseCommonUtils;
import com.yeying.aimi.utils.FileUtils;
import com.yeying.aimi.utils.PromptUtils;
import com.yeying.aimi.utils.SystemMsgUtil;
import com.yeying.aimi.utils.Utils;
import com.yeying.aimi.utils.WeakHandler;
import com.yeying.aimi.views.ExpandGridView;
import com.yeying.aimi.views.RoundImageView;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.ui.widget.DanmakuView;

import static com.bumptech.glide.Glide.with;
import static com.easemob.EMNotifierEvent.Event.EventDeliveryAck;
import static com.easemob.EMNotifierEvent.Event.EventNewCMDMessage;
import static com.easemob.EMNotifierEvent.Event.EventOfflineMessage;
import static com.easemob.EMNotifierEvent.Event.EventReadAck;
import static com.yeying.aimi.API.BAPING_PHOTO;
import static com.yeying.aimi.API.CHATTYPE_GROUP;
import static com.yeying.aimi.API.CHATTYPE_SINGLE;
import static com.yeying.aimi.API.CHAT_GROUP_PHOTO;
import static com.yeying.aimi.R.id.pop_bp_needmonty;

/**
 * Created by tanchengkeji on 2017/9/11.
 */

public class Activity_BaPing extends BaseActivity implements View.OnClickListener, DrawHandler.Callback, AIMIApplication.TyrantscreenlIstener, EMEventListener, AIMIApplication.RedPacketListener {

    public static final String COPY_IMAGE = "EASEMOBIMG";

    public static final String PERMISSION = "permission";

    private RelativeLayout title_left;
    private ImageView title_left_view;

    private TextView title_center_view;

    private RelativeLayout title_right;
    private ImageView title_right_img;
    private TextView title_right_tv;

    public static Activity_BaPing activityInstance;

    public static final String DATA = "DATA";
    public static int resendPos;
    private final int pagesize = 20;
    //跳转携带
    private int chatType;//API.CHATTYPE_GROUP
    private String barId;
    private String barName;
    public String toChatUserId;
    //用户缓存
    private SessionCache mSessionCache;
    private EMGroup group;
    private EMConversation conversation;
    private String myHeadimg;
    private String chatHeadimg;
    private RecyclerView mHeadimgBaping;
    private TextView mSysTvBaping;
    private PasteEditText mContentBaping;
    private ImageButton mIconBaping;
    private ImageButton mMoreBaping;
    private LinearLayout mEditBaping;
    private RecyclerView mRecyclerBaping;
    private ImageView mHongbaoBaping;
    private ImageView mGreetcardBaping;
    private ImageView mBapingBaping;
    private ImageView mMessageBaping;
    private TextView baping_send;
    private LinearLayout baping_more_container;
    private LinearLayout baping_face_container;
    private ViewPager baping_face_vPager;
    private LinearLayout baping_photo;
    private LinearLayout baping_bp;
    private ImageView baping_rank;

    private List<String> emoji = new ArrayList<>();
    private InputMethodManager mInputMethodManager;
    private LinearLayout mChatBaping;
    private int windowWidth;
    private ObjectAnimator objectAnimator;
    private LinearLayout mSysBaping;
    private MessageAdapter mMessageAdapter;
    private boolean show_emoji = true;
    private boolean show_more = true;
    private HandlerPlus handlerplus;
    private String redpacket_id = null;

    /**
     * 触摸监听
     */
    View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            hideAll();
            if (isSlideToBottom(mRecyclerBaping)){
                baping_chat_linear.setVisibility(View.GONE);
                unreach_nums = 0;
            }
            return false;
        }
    };
    View.OnTouchListener mOnTouchListener1 = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mRecyclerBaping.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mRecyclerBaping.getAdapter().getItemCount() > 1) {
                        mRecyclerBaping.smoothScrollToPosition(mRecyclerBaping.getAdapter().getItemCount() - 1);
                    }
                }
            }, 100);
            return false;
        }
    };
    private View pop_baping;
    private PopupWindow mPopupWindow;
    private PopupWindow mPopupWindow_toWallet;
    private ImageView pop_bp_close;
    private RecyclerView pop_bp_recycler;
    private EditText pop_bp_content;
    private TextView pop_bp_tvnum, pop_bp_needmoney, pop_bp_charge, pop_bp_money;
    private Button pop_bp_send;
    private FrameLayout pop_bp_photo;
    private RoundImageView pop_bp_img;
    private List<ScreenOptionBean> mScreeOptionBeenArr = new ArrayList<>();
    private BPMoneyAdapter mBPMoneyAdapter;
    private int bp_need_money;
    private int tv_nums = 30;
    private LayoutInflater inflater;
    //霸屏组件
    private String baping_img_path;
    private String optionId;
    private String money;
    private String time;
    private String baping_content;
    private File mTmpFile;
    private Bitmap bitmap;
    private HandlerPlus mHandlerPlus;
    private DanmakuView mVDanmaku;
    private DanmakuContext mContext;
    private BaseDanmakuParser mParser;
    /**
     * 500
     */
    private TextView mTvCatnum;
    private View mPop_redpacket;
    /**
     * 每人抽到的金额随机
     */
    private EditText mHongbaoMoney;
    /**
     * 红包个数需小于红包总金额
     */
    private EditText mHongbaoNums;
    /**
     * 恭喜发财
     */
    private EditText mHongbaoContent;
    /**
     * 支付：0猫币
     */
    private TextView mHongbaoNeedmonty;
    /**
     * 账户余额：0猫币
     */
    private TextView mHongbaoMymoney;
    /**
     * 充值
     */
    private TextView mHongbaoCharge;
    /**
     * 发送
     */
    private Button mHongbaoSend;
    private ImageButton hongbao_close;
    private SendTask mSendTask;
    private P10220 mP10220;

    public String mScreenOrderId;

    private String delete_flag;
    //删除霸屏
    private View delete_baping;
    private TextView delete_msg, delete_cancle, delete_yes;
    private P23104 mP23104;
    private DeleteTask mDeleteTask;
    private String delMsgId;
    private int delIndex;

    private ClipboardManager clipboard;

    private EMChatOptions mEMChatOptions;
    //店内数据
    private P10112.Resp mResp;
    //上部的头像 适配器
    private BPHeadAdapter mBPHeadAdapter;

    private ImageView pop_bp_img_bg;

    public static boolean flag = false;
    //店内夜猫
    private P610001 p610001;
    private IntoCatTask mIntoCatTask;
    private PopupWindow catPop;

    //刷新加载
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean haveMoreData = true;

    //红包
    private ImageView baping_redpacket;
    //红包定时器
    private Timer mTimer;
    //消息提示小红点
    private TextView baping_chat_flag;
    //未读消息数
    private TextView baping_chat_nums;
    private LinearLayout baping_chat_linear;
    private ImageView baping_chat_close;
    private int unreach_nums;
    private int mMyMoney;
    private List<HBData> mDatas;
    private RelativeLayout mLayoutDanmaku;
    private List<UserPermissionBean> mUserPermissionList;
    private PopupWindow mPopupWindow_Game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baping);
        mDatas = new ArrayList<>();
        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        windowWidth = getWindowWidth();
        activityInstance = this;
        mSessionCache = SessionCache.getInstance(this);
        inflater = LayoutInflater.from(this);
        mHandlerPlus = new HandlerPlus(this);
        mTmpFile = FileUtils.createTmpFile(this, ".jpg");
        //红包监听
        ((AIMIApplication) getApplicationContext()).setRedPacketListener(this);
        ((AIMIApplication) getApplicationContext()).setmTyrantscreenlIstener(this);
        initIntent();
        initView();
        initPop();
        //initP23014();
        //弹幕格式
        initDanmakuOptions();
        dealHuanXin();
        checkLocationPermission(false);
        mMessageAdapter.refreshSelectLast();
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        p610001 = new P610001();
    }


    private void initP23014() {
        mP23104 = new P23104();
        mP23104.req.sessionId = mSessionCache.sessionId;
        mP23104.req.barId = mSessionCache.barId;
        mP23104.req.userId = mSessionCache.userId;
        mDeleteTask = new DeleteTask();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVDanmaku != null && mVDanmaku.isPrepared()) {
            mVDanmaku.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mTimer!=null){
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }

        EMChatManager.getInstance().unregisterEventListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //注册聊天监听
        EMChatManager.getInstance().registerEventListener(this, new EMNotifierEvent.Event[]{EMNotifierEvent.Event.EventNewMessage, EventOfflineMessage,
                EventDeliveryAck, EventReadAck, EventNewCMDMessage});
        EMChat.getInstance().setAppInited();
        if (mVDanmaku != null && mVDanmaku.isPrepared() && mVDanmaku.isPaused()) {
            mVDanmaku.resume();
        }
        //查询红包
        mHandlerPlus.sendEmptyMessage(46);

    }
    /*
    * 添加弹幕
    * */

    private void initDanmakuOptions() {
        CacheStufferAdapter cacheStufferAdapter = new CacheStufferAdapter(mVDanmaku,this);
        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 5); // 滚动弹幕最大显示3行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_BOTTOM,true);

        mContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_NONE, 0)
                .setDuplicateMergingEnabled(false)
                .setScrollSpeedFactor(1.2f)
                .setScaleTextSize(1.2f)
              //  .setCacheStuffer(new SpannedCacheStuffer(), cacheStufferAdapter) // 图文混排使用SpannedCacheStuffer
                 .setCacheStuffer(new BackgroundCacheStuffer(this),cacheStufferAdapter)  // 绘制背景使用BackgroundCacheStuffer
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair)
                .setDanmakuMargin(100);//间距

    }

    /*
    * 添加弹幕
    * */
    private Bitmap mBitmap;
    public void addDanmaku(HBData packet) {
        //构建弹幕
        BaseDanmaku danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if (danmaku == null || mVDanmaku == null) {
            return;
        }
        try {
            mBitmap = Glide.with(Activity_BaPing.this).load(packet.getImgUrl())
                    .asBitmap() //必须
                    .centerCrop()
                    .transform(new CornerBitmapTransformation(Activity_BaPing.this, packet.getImgUrl()))
                    .into(50, 50)
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (mBitmap == null) {
            mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.img_default);
        }
        danmaku.tag = packet;
        danmaku.padding = 0;
        danmaku.priority = 1;  // 一定会显示, 一般用于本机发送的弹幕
        danmaku.isLive = true;
        danmaku.textSize = 35;
        danmaku.setTime(mVDanmaku.getCurrentTime() + 1000);
        danmaku.text = createSpannable(packet, mBitmap);
        mVDanmaku.addDanmaku(danmaku);
    }
    private SpannableStringBuilder createSpannable(HBData packetBean, Bitmap bitmap) {
        String text = "bitmap";//图像位置
        String name = "\t" + packetBean.getUser_name() + "\t";//名字位置
        String value = packetBean.getRemark();//值
        String tx = text + name + value;
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(tx);
        ImageSpan imageSpan = new ImageSpan(this, bitmap);

        int color;
        if(packetBean.getLimit_sex().equals("1")){//男
            color = Color.parseColor("#9fc5e9");
        }else{
            color = Color.parseColor("#937d8c");
        }
        ForegroundColorSpan nameSpan = new ForegroundColorSpan(color);
        ForegroundColorSpan valueSpan = new ForegroundColorSpan(Color.WHITE);
        spannableStringBuilder.setSpan(imageSpan, 0, text.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableStringBuilder.setSpan(nameSpan, text.length(), text.length() + name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(valueSpan, text.length() + name.length(), tx.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }

    /**
     * 所有的popwindow
     */
    private void initPop() {
        mPopupWindow_toWallet = new PopupWindow(LinearLayout.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        mPopupWindow = new PopupWindow(LinearLayout.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
        mPopupWindow.setAnimationStyle(R.style.GamePopAnim);
        pop_baping = inflater.inflate(R.layout.pop_baping, null, false);
        mPop_redpacket = inflater.inflate(R.layout.pop_hongbao, null, false);
        delete_baping = inflater.inflate(R.layout.del_msg_window, null, false);
        mPopupWindow.setFocusable(true);
        mPopupWindow_toWallet.setFocusable(true);

        initBP(pop_baping);
        initDeleteView(delete_baping);
        initRedPakcet(mPop_redpacket);
    }

    /**
     * 删除
     * @param delete_baping
     */
    private void initDeleteView(View delete_baping) {
        delete_msg = (TextView) delete_baping.findViewById(R.id.tv_msg);
        delete_cancle = (TextView) delete_baping.findViewById(R.id.bt_close);
        delete_yes = (TextView) delete_baping.findViewById(R.id.bt_sure);
        delete_msg.setText("确定删除该条霸屏？");
        delete_cancle.setOnClickListener(this);
        delete_yes.setOnClickListener(this);

    }

    /**
     * 红包
     * @param pop_redpacket
     */
    private void initRedPakcet(View pop_redpacket) {
        RelativeLayout hongbao_home = (RelativeLayout) pop_redpacket.findViewById(R.id.hongbao_home);
        hongbao_home.getBackground().setAlpha(150);
        mHongbaoMoney = (EditText) pop_redpacket.findViewById(R.id.hongbao_money);
        mHongbaoNums = (EditText) pop_redpacket.findViewById(R.id.hongbao_nums);
        mHongbaoContent = (EditText) pop_redpacket.findViewById(R.id.hongbao_content);
        mHongbaoNeedmonty = (TextView) pop_redpacket.findViewById(R.id.hongbao_needmonty);
        mHongbaoMymoney = (TextView) pop_redpacket.findViewById(R.id.hongbao_mymoney);
        mHongbaoCharge = (TextView) pop_redpacket.findViewById(R.id.hongbao_charge);
        mHongbaoCharge.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mHongbaoCharge.setOnClickListener(this);
        mHongbaoSend = (Button) pop_redpacket.findViewById(R.id.hongbao_send);
        hongbao_close = (ImageButton) pop_redpacket.findViewById(R.id.hongbao_close);
        hongbao_close.setOnClickListener(this);
        mHongbaoSend.setOnClickListener(this);
        mHongbaoMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    mHongbaoNeedmonty.setText("支付 :0 猫币");
                    return;
                }
                if (Integer.parseInt(s.toString()) > 2000) {
                    PromptUtils.showToast(Activity_BaPing.this, "最大金额为2000");
                    return;
                }

                mHongbaoNeedmonty.setText("支付 :" + s.toString() + " 猫币");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((!TextUtils.isEmpty(mHongbaoMoney.getText().toString()) && !mHongbaoMoney.getText().toString().equals("0")) && (!TextUtils.isEmpty(mHongbaoNums.getText().toString()))){
                    mHongbaoSend.setBackgroundResource(R.drawable.round_yellow);
                }else {
                    mHongbaoSend.setBackgroundResource(R.drawable.grey_round_bg);
                }
            }
        });
        mHongbaoNums.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    // s=0+"";
                    return;
                }
                if (Integer.parseInt(s.toString()) > 100) {
                    PromptUtils.showToast(Activity_BaPing.this, "最大数量为100");
                    return;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((!TextUtils.isEmpty(mHongbaoMoney.getText().toString()) && !mHongbaoMoney.getText().toString().equals("0")) && (!TextUtils.isEmpty(mHongbaoNums.getText().toString()))){
                    mHongbaoSend.setBackgroundResource(R.drawable.round_yellow);
                }else {
                    mHongbaoSend.setBackgroundResource(R.drawable.grey_round_bg);
                }
            }
        });

    }

    /**
     * 霸屏
     * @param pop_baping
     */
    private void initBP(View pop_baping) {
        RelativeLayout baping_home = (RelativeLayout) pop_baping.findViewById(R.id.baping_home);
        baping_home.getBackground().setAlpha(150);
        pop_bp_close = (ImageView) pop_baping.findViewById(R.id.pop_bp_close);
        pop_bp_recycler = (RecyclerView) pop_baping.findViewById(R.id.pop_bp_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        pop_bp_recycler.setLayoutManager(linearLayoutManager);
        mBPMoneyAdapter = new BPMoneyAdapter(mScreeOptionBeenArr, this);
        pop_bp_recycler.setAdapter(mBPMoneyAdapter);
        pop_bp_content = (EditText) pop_baping.findViewById(R.id.pop_bp_content);
        pop_bp_photo = (FrameLayout) pop_baping.findViewById(R.id.pop_bp_photo);
        pop_bp_tvnum = (TextView) pop_baping.findViewById(R.id.pop_bp_tvnum);
        pop_bp_needmoney = (TextView) pop_baping.findViewById(pop_bp_needmonty);
        pop_bp_money = (TextView) pop_baping.findViewById(R.id.pop_bp_money);
        pop_bp_charge = (TextView) pop_baping.findViewById(R.id.pop_bp_charge);
        pop_bp_charge.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        pop_bp_send = (Button) pop_baping.findViewById(R.id.pop_bp_send);
        pop_bp_img = (RoundImageView) pop_baping.findViewById(R.id.pop_bp_img);
        pop_bp_img_bg = (ImageView) pop_baping.findViewById(R.id.pop_bp_img_bg);
        mBPMoneyAdapter.setOnItemClickListener(new BPMoneyAdapter.onItemClick() {
            @Override
            public void onItemClickListener(int position) {
                bp_need_money = (int) mScreeOptionBeenArr.get(position).getMoney();
                pop_bp_needmoney.setText("支付：" + bp_need_money + "猫币");
                optionId = mScreeOptionBeenArr.get(position).getScreenOptionId();
                money = mScreeOptionBeenArr.get(position).getMoney() + "";
                time = mScreeOptionBeenArr.get(position).getContinueTime() + "";
                setChecked(position);
                mBPMoneyAdapter.notifyDataSetChanged();
            }
        });
        pop_bp_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pop_bp_tvnum.setText(tv_nums - s.length() + "");
            }
        });
        pop_bp_close.setOnClickListener(this);
        pop_bp_photo.setOnClickListener(this);
        pop_bp_send.setOnClickListener(this);
        pop_bp_charge.setOnClickListener(this);
    }

    /**
     * intent取值
     */
    private void initIntent() {
        chatType = getIntent().getIntExtra(API.CHAT_TYPE, CHATTYPE_GROUP);
        barId = getIntent().getStringExtra(API.BAR_ID);
        toChatUserId = getIntent().getStringExtra(API.TO_CHAT_USERID);
        mResp = (P10112.Resp) getIntent().getSerializableExtra(DATA);
        barName = mResp.barName;
        mUserPermissionList = mResp.userPermissionList;
        mSessionCache.barId = barId;
        mSessionCache.barName = barName;
        mSessionCache.save();
    }

    private void initView() {
        title_left = (RelativeLayout) findViewById(R.id.title_left);
        title_left_view = (ImageView) findViewById(R.id.title_left_view);
        title_center_view = (TextView) findViewById(R.id.title_center_view);
        title_right = (RelativeLayout) findViewById(R.id.title_right);
        title_right_img = (ImageView) findViewById(R.id.title_right_img);
        title_right_tv = (TextView) findViewById(R.id.title_right_tv);
        title_left.setOnClickListener(this);
        title_right_img.setVisibility(View.GONE);
        title_right_img.setOnClickListener(this);
        title_center_view.setVisibility(View.VISIBLE);
        title_center_view.setText(barName);

        baping_rank = (ImageView) findViewById(R.id.baping_rank);
        baping_rank.setOnClickListener(this);
        mLayoutDanmaku = (RelativeLayout) findViewById(R.id.layout_danmaku);
        baping_chat_nums = (TextView) findViewById(R.id.baping_chat_nums);
        baping_chat_linear = (LinearLayout) findViewById(R.id.baping_chat_linear);
        baping_chat_close = (ImageView) findViewById(R.id.baping_chat_close);
        baping_chat_nums.setOnClickListener(this);
        baping_chat_close.setOnClickListener(this);
        baping_chat_flag = (TextView) findViewById(R.id.baping_chat_flag);
        baping_redpacket = (ImageView) findViewById(R.id.baping_redpacket);
        baping_redpacket.setOnClickListener(this);
        baping_more_container = (LinearLayout) findViewById(R.id.baping_more_container);
        baping_face_container = (LinearLayout) findViewById(R.id.baping_face_container);
        baping_face_vPager = (ViewPager) findViewById(R.id.baping_face_vPager);
        baping_photo = (LinearLayout) findViewById(R.id.baping_photo);
        baping_photo.setOnClickListener(this);
        baping_bp = (LinearLayout) findViewById(R.id.baping_bp);
        baping_bp.setOnClickListener(this);
        initEmoji();
        dealMenuVisiable();
        mHeadimgBaping = (RecyclerView) findViewById(R.id.baping_headimg);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mHeadimgBaping.setLayoutManager(linearLayoutManager);
        mSysTvBaping = (TextView) findViewById(R.id.baping_sys_tv);
        mContentBaping = (PasteEditText) findViewById(R.id.baping_content);
        mIconBaping = (ImageButton) findViewById(R.id.baping_icon);
        mIconBaping.setOnClickListener(this);
        mIconBaping.setOnTouchListener(mOnTouchListener1);
        mMoreBaping = (ImageButton) findViewById(R.id.baping_more);
        mMoreBaping.setOnClickListener(this);
        mMoreBaping.setOnTouchListener(mOnTouchListener1);
        mEditBaping = (LinearLayout) findViewById(R.id.baping_edit);
        mRecyclerBaping = (RecyclerView) findViewById(R.id.baping_recycler);
        mRecyclerBaping.setLayoutManager(new LinearLayoutManager(this));
        mHongbaoBaping = (ImageView) findViewById(R.id.baping_hongbao);
        mHongbaoBaping.setOnClickListener(this);
        mGreetcardBaping = (ImageView) findViewById(R.id.baping_greetcard);
        mGreetcardBaping.setOnClickListener(this);
        mBapingBaping = (ImageView) findViewById(R.id.baping_baping);
        mBapingBaping.setOnClickListener(this);
        mMessageBaping = (ImageView) findViewById(R.id.baping_message);
        mMessageBaping.setOnClickListener(this);
        mChatBaping = (LinearLayout) findViewById(R.id.baping_chat);
        mRecyclerBaping.setOnTouchListener(mOnTouchListener);
        //mSysTvBaping.setText("这是一条会滚的文字！！！！！！！！！！！！！！！");
        //initTextAnmi(mSysTvBaping);
        mSysBaping = (LinearLayout) findViewById(R.id.baping_sys);
        mSysBaping.setVisibility(View.GONE);
        baping_send = (TextView) findViewById(R.id.baping_send);
        baping_send.setOnClickListener(this);
        dealEditText(mContentBaping);
        mVDanmaku = (DanmakuView) findViewById(R.id.v_danmaku);
        mVDanmaku.setCallback(this);
        mContext = DanmakuContext.create();
        mParser = createParser(this.getResources().openRawResource(R.raw.comments));
        mVDanmaku.prepare(mParser, mContext);
        mVDanmaku.enableDanmakuDrawingCache(true);
        mTvCatnum = (TextView) findViewById(R.id.tv_catnum);
        mTvCatnum.setOnClickListener(this);

        mSendTask = new SendTask();
        mP10220 = new P10220();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.baping_swipe);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (haveMoreData) {
                            List<EMMessage> messages;
                            try {
                                if (chatType == API.CHATTYPE_SINGLE) {
                                    messages = conversation.loadMoreMsgFromDB(mMessageAdapter.getItem(0).getMsgId(), pagesize);
                                } else {
                                    messages = conversation.loadMoreGroupMsgFromDB(mMessageAdapter.getItem(0).getMsgId(), pagesize);
                                }
                            } catch (Exception e1) {
                                swipeRefreshLayout.setRefreshing(false);
                                return;
                            }

                            if (messages.size() > 0) {
                                mMessageAdapter.notifyDataSetChanged();
                                mMessageAdapter.refreshSeekTo(messages.size() - 1);
                                if (messages.size() != pagesize) {
                                    haveMoreData = false;
                                }
                            } else {
                                haveMoreData = false;
                            }
                        } else {
                            PromptUtils.showToast(Activity_BaPing.this, getResources().getString(R.string.no_more_messages));
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        initHeadimgR(mHeadimgBaping);
    }

    /**
     * 权限按钮是否显示
     */
    private void dealMenuVisiable() {
        if (mUserPermissionList != null && mUserPermissionList.size() != 0){
            for (UserPermissionBean bean : mUserPermissionList) {
                if (bean.getBarId().equals(barId)){
                    if (bean.getPermissionId().equals("0")){//霸屏
                        title_right_img.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    /**
     * 顶部夜猫显示
     * @param recyclerView
     */
    private void initHeadimgR(RecyclerView recyclerView) {
        mTvCatnum.setText(mResp.yeMaoList.size() + "");
        mBPHeadAdapter = new BPHeadAdapter(this, mResp.yeMaoList, mResp.imgUrl);
        recyclerView.setAdapter(mBPHeadAdapter);
        mBPHeadAdapter.setOnHeadClickListener(new BPHeadAdapter.HeadClick() {
            @Override
            public void onHeadClick(int position) {
                if (mResp.yeMaoList.get(position).getUserId().equals(mSessionCache.userId)) {
                    MineHomepage.toOtherHomePage(Activity_BaPing.this,
                            mResp.yeMaoList.get(position).getUserId(),
                            TextUtils.isEmpty(mSessionCache.locationX) ? 0.0 : Double.parseDouble(mSessionCache.locationX),
                            TextUtils.isEmpty(mSessionCache.locationY) ? 0.0 : Double.parseDouble(mSessionCache.locationY), true);
                } else {
                    OtherHomepage.toOtherHomePage(Activity_BaPing.this,
                            mResp.yeMaoList.get(position).getUserId(),
                            TextUtils.isEmpty(mSessionCache.locationX) ? 0.0 : Double.parseDouble(mSessionCache.locationX),
                            TextUtils.isEmpty(mSessionCache.locationY) ? 0.0 : Double.parseDouble(mSessionCache.locationY), false);
                }

            }
        });
    }

    /**
     * 表情包
     */
    private void initEmoji() {
        emoji = getExpressionRes(35);
        List<View> views = new ArrayList<View>();
        View gv1 = getGridChildView(1);
        views.add(gv1);
        baping_face_vPager.setAdapter(new ExpressionPagerAdapter(views));
    }

    public List<String> getExpressionRes(int getSum) {
        List<String> reslist = new ArrayList<String>();
        for (int x = 1; x <= getSum; x++) {
            String filename = "face_" + x;
            reslist.add(filename);
        }
        return reslist;
    }

    /**
     * 获取表情的gridview的子view
     *
     * @param i
     * @return
     */
    private View getGridChildView(int i) {
        View view = View.inflate(this, R.layout.expression_gridview, null);
        ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
        List<String> list = new ArrayList<String>();
        if (i == 1) {
            List<String> list1 = emoji.subList(0, emoji.size());
            list.addAll(list1);
        }
        final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this, 1, list);
        gv.setAdapter(expressionAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filename = expressionAdapter.getItem(position);
                try {
                    // 文字输入框可见时，才可输入表情
                    if (filename != "delete_expression") { // 不是删除键，显示表情
                        // 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
                        Class clz = Class.forName("com.yeying.aimi.huanxin.SmileUtils");
                        Field field = clz.getField(filename);
                        mContentBaping.append(SmileUtils.getSmiledText(Activity_BaPing.this, field.get(null).toString()));
                    } else { // 删除文字或者表情
                        if (!TextUtils.isEmpty(mContentBaping.getText())) {

                            int selectionStart = mContentBaping.getSelectionStart();// 获取光标的位置
                            if (selectionStart > 0) {
                                String body = mContentBaping.getText().toString();
                                String tempStr = body.substring(0, selectionStart);
                                int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                                if (i != -1) {
                                    CharSequence cs = tempStr.substring(i, selectionStart);
                                    if (SmileUtils.containsKey(cs.toString()))
                                        mContentBaping.getEditableText().delete(i, selectionStart);
                                    else
                                        mContentBaping.getEditableText().delete(selectionStart - 1,
                                                selectionStart);
                                } else {
                                    mContentBaping.getEditableText().delete(selectionStart - 1, selectionStart);
                                }
                            }
                        }

                    }

                } catch (Exception e) {
                    Log.e(TAG, "onItemClick: " + e.getMessage());
                }

            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left:
                hideSoftKeyBoard();
                finish();
                break;
            case R.id.baping_rank:
                Rank_BP.toRank(Activity_BaPing.this , barId , Rank_BP.RANK_BAPING);
                break;
            case R.id.title_right_img:
                hideSoftKeyBoard();
                startActivity(new Intent(this, PermissionActivity.class));
                break;
            case R.id.baping_icon:
                hideSoftKeyBoard();
                show_more = true;
                if (show_emoji) {
                    baping_face_container.setVisibility(View.VISIBLE);
                    baping_more_container.setVisibility(View.GONE);
                    show_emoji = false;
                } else {
                    baping_face_container.setVisibility(View.GONE);
                    show_emoji = true;
                }
                break;
            case R.id.baping_more:
                hideSoftKeyBoard();
                show_emoji = true;
                if (show_more) {
                    baping_more_container.setVisibility(View.VISIBLE);
                    baping_face_container.setVisibility(View.GONE);
                    show_more = false;
                } else {
                    baping_more_container.setVisibility(View.GONE);
                    show_more = true;
                }
                break;
            case R.id.baping_hongbao:
                hideSoftKeyBoard();
                mPopupWindow.setContentView(mPop_redpacket);
                mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                initMyMoney();
                break;
            case R.id.baping_greetcard:
                hideSoftKeyBoard();
                loginHXRepte();
                break;
            case R.id.baping_baping:
                //霸屏
                hideSoftKeyBoard();
                baping_img_path = "";
                pop_bp_img.setVisibility(View.GONE);
                pop_bp_img_bg.setVisibility(View.VISIBLE);
                baping_content = "";
                pop_bp_content.setText("");
                pop_bp_needmoney.setText("支付：0猫币");
                mPopupWindow.setContentView(pop_baping);
                mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                initBPChargeTask();
                break;
            case R.id.baping_message:
                //进入单聊界面
                hideSoftKeyBoard();
                baping_chat_flag.setVisibility(View.GONE);
                startActivity(new Intent(Activity_BaPing.this, ChatActivity.class));
                break;
            case R.id.baping_send:
                //群聊发送消息
                hideAll();
                sendTxtMessage(mContentBaping.getText().toString());
                mContentBaping.setText("");
                //滑动到最底部
                if (mMessageAdapter.getItemCount() > 1) {
                    mRecyclerBaping.smoothScrollToPosition(mMessageAdapter.getItemCount() - 1);
                }
                mMessageAdapter.refreshSelectLast();
                break;
            case R.id.baping_photo:
                //进入相册
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, "", new PermissionCheckedLister() {
                        @Override
                        public void onAllGranted() {
                            AlbmActivity.toAlbmWithResult(Activity_BaPing.this, CHAT_GROUP_PHOTO, false, false, false);
                        }

                        @Override
                        public void onGranted(List<String> grantPermissions) {

                        }

                        @Override
                        public void onDenied(List<String> deniedPermissions) {

                        }
                    });
                } else {
                    AlbmActivity.toAlbmWithResult(Activity_BaPing.this, CHAT_GROUP_PHOTO, false, false, false);
                }

                break;
            case R.id.baping_bp:
                //发送霸屏
                hideSoftKeyBoard();
                baping_img_path = "";
                pop_bp_img.setVisibility(View.GONE);
                pop_bp_img_bg.setVisibility(View.VISIBLE);
                baping_content = "";
                pop_bp_content.setText("");
                mPopupWindow.setContentView(pop_baping);
                mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                initBPChargeTask();
                break;
            case R.id.pop_bp_close:
                if (mPopupWindow != null && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                break;
            case R.id.pop_bp_photo:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, "", new PermissionCheckedLister() {
                        @Override
                        public void onAllGranted() {
                            AlbmActivity.toAlbmWithResult(Activity_BaPing.this, BAPING_PHOTO, true, true, false);
                        }

                        @Override
                        public void onGranted(List<String> grantPermissions) {

                        }

                        @Override
                        public void onDenied(List<String> deniedPermissions) {

                        }
                    });
                } else {
                    AlbmActivity.toAlbmWithResult(Activity_BaPing.this, BAPING_PHOTO, true, true, false);
                }
                break;
            case R.id.pop_bp_send:
                //发送霸屏
                baping_content = pop_bp_content.getText().toString();
                //内容 图片 不为空时可发送
                if (TextUtils.isEmpty(baping_content)){
                    PromptUtils.showToast(Activity_BaPing.this,"请输入霸屏留言");
                }else if (TextUtils.isEmpty(baping_img_path)){
                    PromptUtils.showToast(Activity_BaPing.this,"请上传图片");
                }else if (bp_need_money>mMyMoney){
                    mPopupWindow_toWallet.setContentView(delete_baping);
                    delete_msg.setText("当前余额不足，请充值");
                    delete_yes.setText("去充值");
                    delete_cancle.setText("取消");
                    delete_flag = "delete_to_charge";
                    mPopupWindow_toWallet.showAtLocation(getWindow().getDecorView(),Gravity.CENTER,0,0);
                }else {
                    if (mPopupWindow != null && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                    sendBaPing();
                }
                break;
            case R.id.bt_close:
                //取消删除霸屏
                if (delete_flag.equals("delete_to_charge")){
                    if (mPopupWindow_toWallet != null && mPopupWindow_toWallet.isShowing()) {
                        mPopupWindow_toWallet.dismiss();
                    }
                }else {
                    if (mPopupWindow != null && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                }
                break;
            case R.id.bt_sure:
                //删除框确定按钮
                if (delete_flag.equals("delete_chat_msg")) {
                    //删除聊天信息
                    conversation.removeMessage(delMsgId);
                    mMessageAdapter.refreshSeekTo(delIndex);
                } else if (delete_flag.equals("delete_baping")) {
                    //确定删除霸屏
                    if (mScreenOrderId != null) {
                        //  mP23104.req.messageId = mScreenOrderId;
                        Log.e("wtf", "删除霸屏mP23104.req.messageId--------->" + mScreenOrderId);
                        mDeleteTask.execute(getApplicationContext(), mP23104);
                    }
                }else if (delete_flag.equals("delete_to_charge")){
                    //前往充值
                    WalletActivity.toWallet(Activity_BaPing.this);
                    if (mPopupWindow_toWallet != null && mPopupWindow_toWallet.isShowing()) {
                        mPopupWindow_toWallet.dismiss();
                    }
                    return;
                }
                if (mPopupWindow != null && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                break;
            case R.id.tv_catnum:
                //进入查看夜猫页面
                flag = true;
                catPop = PromptUtils.getProgressDialogPop(Activity_BaPing.this);
                catPop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                startLocation();
                break;
            case R.id.hongbao_send:
                sendRedPacket();
                break;
            case R.id.hongbao_close:
                //重置
                mHongbaoMoney.setText("");
                mHongbaoNums.setText("");
                mHongbaoContent.setText("");
                if (mPopupWindow != null && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                break;
            case R.id.pop_bp_charge:
                //霸屏充值
                WalletActivity.toWallet(Activity_BaPing.this);
                break;
            case R.id.hongbao_charge:
                //红包充值
                WalletActivity.toWallet(Activity_BaPing.this);
                break;
            case R.id.baping_redpacket:
                //点击抢红包
                P10221 p10221 = new P10221();
                p10221.req.sessionId = mSessionCache.sessionId;
                p10221.req.userId = mSessionCache.userId;
                p10221.req.red_pack_id = redpacket_id;
                new RedPackTask().execute(getApplicationContext(), p10221);
                break;
            case R.id.baping_chat_nums:
                mMessageAdapter.refreshSelectLast();
                baping_chat_linear.setVisibility(View.GONE);
                unreach_nums = 0;
                break;
            case R.id.baping_chat_close:
                baping_chat_linear.setVisibility(View.GONE);
                unreach_nums = 0;
                break;
            default:
                break;
        }
    }

    /**
     * 环信登录操作 知道登录成功后再进行业务逻辑
     */
    public void loginHXRepte() {
        mPopupWindow_Game = PromptUtils.getProgressDialogPop(Activity_BaPing.this);
        mPopupWindow_Game.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        if (EMChat.getInstance().isLoggedIn()) {
            loginHXSuccessed();
        } else {
            SessionCache session = SessionCache.getInstance(Activity_BaPing.this);
            EMChatManager.getInstance().login(session.userId, session.userId, new EMCallBack() {//回调
                @Override
                public void onSuccess() {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            loginHXSuccessed();
                        }
                    });
                }

                @Override
                public void onProgress(int progress, String status) {

                }

                @Override
                public void onError(int code, String message) {
                    loginHXRepte();
                }
            });

        }
    }

    /**
     * 登录环信成功后的业务逻辑
     */
    public void loginHXSuccessed(){
        if (mPopupWindow_Game != null && mPopupWindow_Game.isShowing()){
            mPopupWindow_Game.dismiss();
        }
        GameStartActivity.toGameStart(Activity_BaPing.this , barId);
    }

    /**
     * 查询账户余额
     */
    private void initMyMoney() {
        P10113 p10113 = new P10113();
        p10113.req.userId = SessionCache.getInstance(this).userId;
        p10113.req.sessionId = SessionCache.getInstance(this).sessionId;
        new MysTask().execute(this, p10113);
    }

    public class MysTask extends DefaultTask {

        public void onError(DefaultError obj) {
            super.onError(obj);
            Toast.makeText(mActivity, "网络错误", Toast.LENGTH_SHORT).show();
        }

        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            P10113 p10113 = (P10113) protocol;
            if (p10113.resp.transcode.equals("9999")) {
                Toast.makeText(mActivity, p10113.resp.msg, Toast.LENGTH_SHORT)
                        .show();
                if (p10113.resp.code.equals("9062")) {

                }
            } else {
                mMyMoney = p10113.resp.score;
                mHongbaoMymoney.setText("账户余额： " + p10113.resp.score + " 猫币");
            }
        }
    }

    /**
     * 进入店内夜猫
     */
    @Override
    public void intoCatList() {
        super.intoCatList();
        p610001.req.sessionId = mSessionCache.sessionId;
        p610001.req.userId = mSessionCache.userId;
        p610001.req.locationX = mSessionCache.locationX;
        p610001.req.locationY = mSessionCache.locationY;
        p610001.req.barId = barId;
        mIntoCatTask = new IntoCatTask();
        mIntoCatTask.execute(getApplicationContext(), p610001);
    }

    /**
     * 发送红包
     */
    private void sendRedPacket() {
        SessionCache session = SessionCache.getInstance(this);
        String money = mHongbaoMoney.getText().toString();
        String nums = mHongbaoNums.getText().toString();
        String content = mHongbaoContent.getText().toString();

        if (TextUtils.isEmpty(money)) {
            return;
        }
        if (nums.equals("0") && money.equals("0")){
            return;
        }
        if (TextUtils.isEmpty(nums) || nums.equals("0")) {
            PromptUtils.showToast(Activity_BaPing.this, "请选择红包个数");
            return;
        }
        if (Integer.parseInt(money) < Integer.parseInt(nums)) {
            PromptUtils.showToast(Activity_BaPing.this, "红包个数需小于红包总金额");
            return;
        }
        if (Integer.parseInt(money)>2000){
            PromptUtils.showToast(Activity_BaPing.this, "最大金额为2000");
            return;
        }
        if (Integer.parseInt(nums)>100){
            PromptUtils.showToast(Activity_BaPing.this, "最大红包个数为100");
            return;
        }
        if (Integer.parseInt(money) > mMyMoney){
            mPopupWindow_toWallet.setContentView(delete_baping);
            delete_msg.setText("当前余额不足，请充值");
            delete_yes.setText("去充值");
            delete_cancle.setText("取消");
            delete_flag = "delete_to_charge";
            mPopupWindow_toWallet.showAtLocation(getWindow().getDecorView(),Gravity.CENTER,0,0);
            return;
        }

        mP10220.req.userId = session.userId;
        mP10220.req.sessionId = session.sessionId;
        mP10220.req.red_pack_type = "2";// 0群 1个人2酒吧红包
        mP10220.req.total_count = nums;
        mP10220.req.score_count = money;
        mP10220.req.type = "0";// 0拼手气 1普通红包
        mP10220.req.limit = 0;// 0 全部 1男 2女
        mP10220.req.redpack_choose = 0;// 0是普通 1是密令2 图片3 视频 ',20160506
        mP10220.req.remark = TextUtils.isEmpty(content) ? "恭喜发财" : content;
        mP10220.req.groupId = session.barId;// 可以为空 red_pack_type 为2时填写酒吧id
        mSendTask.execute(this, mP10220);
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //聊天发送图片
        if (resultCode == RESULT_OK && requestCode == CHAT_GROUP_PHOTO) {
            if (data != null) {
                String photo_front = data.getStringExtra(AlbmActivity.filePath);
                Log.e(TAG, "onActivityResult: " + photo_front);
                hideAll();
                if (!TextUtils.isEmpty(photo_front)) {
                    sendPicture(photo_front);
                } else {
                    PromptUtils.showToast(Activity_BaPing.this, "请选择图片");
                }
            }
        }
        //霸屏图片
        if (resultCode == RESULT_OK && (requestCode == BAPING_PHOTO)) {
            if (data != null) {
                String photo_front = data.getStringExtra(AlbmActivity.filePath);
                Log.e(TAG, "onActivityResult: " + photo_front);
                baping_img_path = photo_front;
                pop_bp_img.setVisibility(View.VISIBLE);
                pop_bp_img_bg.setVisibility(View.GONE);
                with(this).load(new File(baping_img_path)).into(pop_bp_img);
            }
        }
        if (requestCode == API.REQUEST_CODE_CONTEXT_MENU) {
            switch (resultCode) {
                case API.RESULT_CODE_COPY: // 复制消息
                    EMMessage copyMsg = mMessageAdapter.getItem(data.getIntExtra("position", -1));
                    clipboard.setText(((TextMessageBody) copyMsg.getBody()).getMessage());
                    PromptUtils.showToast(mActivity , "复制成功");
                    break;
                case API.RESULT_CODE_DELETE: // 删除消息
                    EMMessage deleteMsg = mMessageAdapter.getItem(data.getIntExtra("position", -1));
                    delete_flag = "delete_chat_msg";
                    mPopupWindow.setContentView(delete_baping);
                    mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                    delete_msg.setText("是否删除该条消息？");
                    delMsgId = deleteMsg.getMsgId();
                    delIndex = data.getIntExtra("position", mMessageAdapter.getItemCount()) - 1;
                    break;
                case API.RESULT_CODE_FORWARD: // 转发消息
                    EMMessage forwardMsg = mMessageAdapter.getItem(data.getIntExtra("position", 0));
                    Intent intent = new Intent(this, ChatActivity.class);
                    intent.putExtra("forward_msg_id", forwardMsg.getMsgId());
                    intent.putExtra("is_forward",true);
                    startActivity(intent);
                    break;
                case API.RESULT_CODE_BACK_DEL://消息撤回
                    EMMessage backDelMsg = mMessageAdapter.getItem(data.getIntExtra("position", 0));
                    if (dealTime(backDelMsg.getMsgTime()).equals("已过期")) {
                        PromptUtils.showToast(mActivity, "消息超过2分钟无法撤回");
                    } else {
                        // 显示撤回消息操作的 dialog
                        final ProgressDialog pd = new ProgressDialog(mActivity);
                        pd.setMessage("正在撤回消息");
                        pd.show();
                        EaseCommonUtils.sendRevokeMessage(mActivity, backDelMsg, new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                pd.dismiss();
                                refreshUI();
                            }

                            @Override
                            public void onError(final int i, final String s) {
                                pd.dismiss();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (s.equals("maxtime")) {
                                            Toast.makeText(mActivity, "超时无法撤回消息", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(mActivity, "撤回消息失败" + i + " " + s + "", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onProgress(int i, String s) {

                            }
                        });
                    }

                    break;
                default:
                    break;
            }
        }
        //消息重发
        if (resultCode == RESULT_OK && (requestCode == API.REQUEST_CODE_TEXT || requestCode == API.REQUEST_CODE_VOICE
                || requestCode == API.REQUEST_CODE_PICTURE || requestCode == API.REQUEST_CODE_LOCATION
                || requestCode == API.REQUEST_CODE_VIDEO || requestCode == API.REQUEST_CODE_FILE)) {
            resendMessage();
        }

    }

    /**
     * 重发消息
     */
    private void resendMessage() {
        EMMessage msg = null;
        msg = conversation.getMessage(resendPos);
        msg.status = EMMessage.Status.CREATE;
        mMessageAdapter.refreshSeekTo(resendPos);
    }

    /**
     * 处理撤回时间
     *
     * @param timeStr
     * @return
     */
    public String dealTime(long timeStr) {
        Date date = new Date();
        long between = date.getTime() - timeStr;
        if (between > 120000) {
            return "已过期";
        } else if (between > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("mm分ss秒", Locale.SIMPLIFIED_CHINESE);
            Date rDate = new Date(120000 - between);
            return "剩余" + sdf.format(rDate);
        } else {
            return "已过期";
        }
    }

    /**
     * 适配器刷新
     */
    private void refreshUI() {
        if (mMessageAdapter == null) {
            return;
        }
        runOnUiThread(new Runnable() {
            public void run() {
                if (mMessageAdapter != null) {
                    mMessageAdapter.refresh();
                }
            }
        });
    }

    /**
     * 输入框
     *
     * @param editText
     */
    private void dealEditText(final EditText editText) {
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mRecyclerBaping.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mRecyclerBaping.getAdapter().getItemCount()>1){
                            mRecyclerBaping.smoothScrollToPosition(mRecyclerBaping.getAdapter().getItemCount()-1);
                        }
                    }
                },200);

                baping_more_container.setVisibility(View.GONE);
                baping_face_container.setVisibility(View.GONE);
                return false;
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = editText.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    baping_send.setVisibility(View.GONE);
                    mMoreBaping.setVisibility(View.VISIBLE);
                } else {
                    baping_send.setVisibility(View.VISIBLE);
                    mMoreBaping.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 隐藏键盘
     */
    private void hideSoftKeyBoard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null) {
                mInputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 隐藏该隐藏的
     */
    private void hideAll() {
        hideSoftKeyBoard();
        baping_more_container.setVisibility(View.GONE);
        baping_face_container.setVisibility(View.GONE);
        show_more = true;
        show_emoji = true;
    }

    /**
     * 测量view高度
     *
     * @param view
     * @param flag
     * @return
     */
    private int dealViewWH(View view, int flag) {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
        if (flag == 0) {
            return view.getMeasuredWidth();
        } else {
            return view.getMeasuredHeight();
        }
    }

    /**
     * 获得屏幕宽度
     *
     * @return
     */
    private int getWindowWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * 滚动文字动画
     *
     * @param view
     */
    private void initTextAnmi(View view) {
        objectAnimator = ObjectAnimator.ofFloat(view, "translationX", windowWidth, -dealViewWH(mSysTvBaping, 0));
        objectAnimator.setDuration(5000);
        objectAnimator.setRepeatCount(2);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mSysBaping.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        if (mVDanmaku != null) {
            // dont forget release!
            mVDanmaku.release();
            mVDanmaku = null;
        }
        //停止动画
        if (objectAnimator != null && objectAnimator.isRunning()) {
            objectAnimator.cancel();
        }
        if (catPop != null && catPop.isShowing()) {
            catPop.dismiss();
        }
        activityInstance = null;
        //红包监听置空
        ((AIMIApplication) getApplicationContext()).setRedPacketListener(null);
        //霸屏监听置空
        ((AIMIApplication) getApplicationContext()).setmTyrantscreenlIstener(null);
    }

    /**
     * 处理环信
     */
    private void dealHuanXin() {
        if (toChatUserId == null) {
            toChatUserId = "";
        }
        onGroupViewCreation();
        onConversationInit();
        onListViewCreation();
    }

    /**
     * 群组
     */
    protected void onGroupViewCreation() {
        group = EMGroupManager.getInstance().getGroup(toChatUserId);
        if (group == null) {
            //去获取群组实例
            new Thread_GroupInstance(toChatUserId).start();
            return;
        }
    }

    /**
     * 聊天内容
     */
    protected void onListViewCreation() {
        mMessageAdapter = new MessageAdapter(Activity_BaPing.this, toChatUserId, mRecyclerBaping, mSessionCache.headimgUrl, chatHeadimg);
        // 显示消息
        mRecyclerBaping.setAdapter(mMessageAdapter);
        mMessageAdapter.setOnDeleteBarScreenListener(new MessageAdapter.onDeleteBarScreenListener() {
            @Override
            public void deleteBarScreen(String id, EMMessage message) {
                try {
                    mScreenOrderId = message.getStringAttribute("screenorderid");
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
                delete_flag = "delete_baping";
                mPopupWindow.setContentView(delete_baping);
                mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
            }
        });
        mMessageAdapter.refreshSelectLast();
    }

    /**
     * 会话初始化
     */
    protected void onConversationInit() {

        if (chatType == API.CHATTYPE_SINGLE) {
            Log.i("toChatUserId", "toChatUserId = " + toChatUserId);
            conversation = EMChatManager.getInstance().getConversationByType(toChatUserId, EMConversation.EMConversationType.Chat);
        } else if (chatType == CHATTYPE_GROUP) {
            conversation = EMChatManager.getInstance().getConversationByType(toChatUserId, EMConversation.EMConversationType.GroupChat);
        } else if (chatType == API.CHATTYPE_CHATROOM) {
            conversation = EMChatManager.getInstance().getConversationByType(toChatUserId, EMConversation.EMConversationType.ChatRoom);
        }
        // 把此会话的未读数置为0
        conversation.markAllMessagesAsRead();
        // 初始化db时，每个conversation加载数目是getChatOptions().getNumberOfMessagesLoaded
        // 这个数目如果比用户期望进入会话界面时显示的个数不一样，就多加载一些
        final List<EMMessage> msgs = conversation.getAllMessages();
        int msgCount = msgs != null ? msgs.size() : 0;
        if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
            String msgId = null;
            if (msgs != null && msgs.size() > 0) {
                msgId = msgs.get(0).getMsgId();
            }
            if (chatType == CHATTYPE_SINGLE) {
                conversation.loadMoreMsgFromDB(msgId, pagesize);
            } else {
                conversation.loadMoreGroupMsgFromDB(msgId, pagesize);
            }
        }
    }

    /*
       * 弹幕监听
       * */
    @Override
    public void prepared() {
        if (mVDanmaku != null) {
            mVDanmaku.start();
        }

    }

    @Override
    public void updateTimer(DanmakuTimer timer) {

    }

    @Override
    public void danmakuShown(BaseDanmaku danmaku) {

    }

    @Override
    public void drawingFinished() {

    }

    /**
     * 删除霸屏
     *
     * @param bar_id
     * @param groupId
     */
    @Override
    public void deleteBaPingMsg(String screenOrderId, String bar_id, String groupId) {
        if (mSessionCache.barId.equals(bar_id)) {
            //删除聊天记录
            EMConversation conversation = EMChatManager.getInstance().getConversation(groupId);
            List<EMMessage> allMessages = conversation.getAllMessages();
            for (int i = 0; i < allMessages.size(); i++) {
                EMMessage emMessage = allMessages.get(i);
                try {
                    String screenorderid = emMessage.getStringAttribute("screenorderid");
                    if (screenOrderId.equals(screenorderid)) {
                        conversation.removeMessage(emMessage.getMsgId());
                    }
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
            }
            mMessageAdapter.refresh();
        }
    }

    /**
     * 红包监听 抢红包
     *
     * @param redPacketBean
     */
    @Override
    public void onRedPacketListener(RedPacketBean redPacketBean) {
        if (redPacketBean != null) {
            if (redPacketBean.getStatus().equals("0")){
                mHandlerPlus.sendEmptyMessage(46);
            }else if (redPacketBean.getStatus().equals("1")){
                mHandlerPlus.sendEmptyMessage(48);
            }
        } else {
            mHandlerPlus.sendEmptyMessage(47);
        }
    }

    /**
     * 红包 与 霸屏
     */
    private class HandlerPlus extends WeakHandler {

        public HandlerPlus(Object o) {
            super(o);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Activity_BaPing activity = (Activity_BaPing) mHandlerPlus.getObjct();
            if (activity != null) {
                switch (msg.what) {
                    case API.BP:
                        //霸屏发送成功 将霸屏发送到聊天内
                        Bundle data = msg.getData();
                        String id = data.getString("id");
                        String content = data.getString("content");
                        String imgPath = data.getString("path");
                        Log.e("wtf", "霸屏的ID--------->" + id);
                        Log.e("wtf", "霸屏的图片--------->: " + imgPath);
                        activity.sendBPMessage(imgPath, content, id);//发送消息
                        break;
                    case 48:
                    case 46:
                        //有红包
                        searchRedPackFromDB();
                        /*
                            * 定时器
                         * */
                        if (activity.mTimer==null) {
                            activity.mTimer = new Timer();
                            activity.mTimer.schedule(new AsyncAddTask(), 0, 10000);
                        }
                        break;
                    case 47:
                        Log.e(TAG, "handleMessage: 红包消失");
                        baping_redpacket.setVisibility(View.GONE);
                        mLayoutDanmaku.setVisibility(View.GONE);
                        break;
                    case 49:
                        //有消息 小窗显示
                        unreach_nums++;
                        baping_chat_linear.setVisibility(View.VISIBLE);
                        baping_chat_nums.setText("新消息"+unreach_nums+"条");
                        break;
                }
            }
        }
    }

    /**
     * 获取单个群组实例
     */
    public class Thread_GroupInstance extends Thread {
        private String thirsGroupId;

        public Thread_GroupInstance(String thirsGroupId) {
            this.thirsGroupId = thirsGroupId;
        }

        public void run() {
            // 在这里进行 http request.网络请求相关操作
            try {
                Log.e(TAG, "run: " + thirsGroupId);
                final EMGroup returnGroup = EMGroupManager.getInstance().getGroupFromServer(thirsGroupId);
                // 更新本地数据
                EMGroupManager.getInstance().createOrUpdateLocalGroup(returnGroup);
                EMGroupManager.getInstance().loadAllGroups();
                Log.e("环信", "获取群组实例成功");
            } catch (EaseMobException e) {
                // TODO Auto-generated catch block
                Log.e("环信", "获取群组实例失败！");
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送文字
     *
     * @param content
     */
    private void sendTxtMessage(String content) {
        if (TextUtils.isEmpty(content)) {
            PromptUtils.showToast(Activity_BaPing.this, "请输入内容");
        } else {
            EMMessage sendMessage = EMMessage.createSendMessage(EMMessage.Type.TXT);
            // 设置为群聊
            sendMessage.setChatType(EMMessage.ChatType.GroupChat);
            TextMessageBody txtBody = new TextMessageBody(content);
            // 设置消息body
            sendMessage.addBody(txtBody);
            // 设置要发给谁,用户username或者群聊groupid
            sendMessage.setReceipt(toChatUserId);
            String chatInfo = dealChatInfo();
            //扩展消息
            sendMessage.setAttribute("userInfo", chatInfo);
            sendMessage.setAttribute("transCode", "cc0001");
            sendMessage.setAttribute("em_ignore_notification", false);
            sendMessage.setAttribute("isSystemMsg", false);
            conversation.addMessage(sendMessage);
            mMessageAdapter.refreshSelectLast();
        }
    }

    /**
     * 发送图片
     *
     * @param filePath
     */
    private void sendPicture(final String filePath) {
        if (filePath != null) {
            final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.IMAGE);
            message.setChatType(EMMessage.ChatType.GroupChat);
            message.setReceipt(toChatUserId);
            ImageMessageBody body = new ImageMessageBody(new File(filePath));
            // 默认超过100k的图片会压缩后发给对方，可以设置成发送原图
            body.setSendOriginalImage(true);
            message.addBody(body);
            String chatInfo = dealChatInfo();
            //消息扩展
            message.setAttribute("userInfo", chatInfo);
            message.setAttribute("transCode", "cc0001");
            conversation.addMessage(message);
            mRecyclerBaping.setAdapter(mMessageAdapter);
            mMessageAdapter.refreshSelectLast();
        }
    }

    /**
     * 将霸屏发送至聊天内
     *
     * @param filePath
     * @param content
     * @param id
     */
    private void sendBPMessage(String filePath, String content, String id) {
        final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.IMAGE);
        message.setChatType(EMMessage.ChatType.GroupChat);
        message.setReceipt(toChatUserId);
        ImageMessageBody body = new ImageMessageBody(new File(filePath));
        // 默认超过100k的图片会压缩后发给对方，可以设置成发送原图
        body.setSendOriginalImage(true);
        message.addBody(body);
        String chatInfo = dealChatInfo();
        //message.direct = EMMessage.Direct.RECEIVE;
        //消息扩展
        message.setAttribute("userInfo", chatInfo);
        message.setAttribute("transCode", "cc0001");
        message.setAttribute("content", content);
        message.setAttribute("screenorderid", id);
        mRecyclerBaping.setAdapter(mMessageAdapter);
        conversation.addMessage(message);
        mMessageAdapter.refreshSelectLast();
    }

    /**
     * 向后台发送霸屏
     */
    private void sendBaPing() {
        Runnable bpRun = new Runnable() {
            @Override
            public void run() {
                requestBaPing();
            }
        };
        Thread bpThread = new Thread(bpRun);
        bpThread.start();
    }

    /**
     * 查询数据库中的红包
     */
    private void searchRedPackFromDB() {
        mDatas.clear();
        baping_redpacket.setVisibility(View.GONE);
        mLayoutDanmaku.setVisibility(View.GONE);
        List<HBData> hbDataList = DataSupport.findAll(HBData.class);
        if(hbDataList==null||hbDataList.size()<=0){
            mLayoutDanmaku.setVisibility(View.GONE);
            return;
        }
        for (HBData hbData : hbDataList) {
            if(hbData.getBar_id().equals(barId)){
                mDatas.add(hbData);
            }
        }
        for (int i = 0; i < hbDataList.size(); i++) {
            if (hbDataList.get(i).getBar_id() != null) {
                if (hbDataList.get(i).getBar_id().equals(barId)) {
                    //数据库中有该酒吧的红包
                    Log.e(TAG, "searchRedPackFromDB: -->数据库中有该酒吧的红包");
                    baping_redpacket.setVisibility(View.VISIBLE);//图标显示
                    redpacket_id = hbDataList.get(i).getRed_pack_id();//ID赋值
                    if(mLayoutDanmaku.getVisibility()!=View.VISIBLE){
                        mLayoutDanmaku.setVisibility(View.VISIBLE);
                    }
                    return;
                }

            }
        }
    }

    /**
     * 弹幕计时器
     */
    private class AsyncAddTask extends TimerTask {

        @Override
        public void run() {
           if(mDatas.size()>0){
               for (int i = 0; i < mDatas.size(); i++) {
                   Log.e(TAG, "run: "+i);
                   try {
                       Thread.sleep(500);
                       if(i>=mDatas.size()){
                           mTimer.cancel();
                           mTimer.purge();
                           mTimer=null;
                           return;
                       }
                       addDanmaku(mDatas.get(i));
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }

               }
           }else{
               mTimer.cancel();
               mTimer.purge();
               mTimer=null;
           }

        }
    }

    /**
     * 删除红包回调
     * @param deleteRedPackId
     */
    private void deleteRedPackFromDB(String deleteRedPackId) {
        Log.e(TAG, "deleteRedPackFromDB: ---->删除数据库红包");
        int i = DataSupport.deleteAll(HBData.class, "red_pack_id = ? ", deleteRedPackId);
        Log.e(TAG, "deleteRedPackFromDB: 删除红包数目--->" + i);
    }

    /**
     * 抢红包请求
     */
    private class RedPackTask extends DefaultTask {

        PopupWindow mPopupWindow;

        @Override
        public void preExecute() {
            super.preExecute();
            mPopupWindow = PromptUtils.getProgressDialogPop(Activity_BaPing.this);
            mPopupWindow.showAtLocation(getWindow().getDecorView(),Gravity.CENTER,0,0);
        }

        @Override
        public void onError(DefaultError obj) {
            super.onError(obj);
            PromptUtils.showToast(Activity_BaPing.this, "网络错误");
            if (mPopupWindow != null && mPopupWindow.isShowing()){
                mPopupWindow.dismiss();
            }
        }

        @Override
        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            P10221 p10221 = (P10221) protocol;
            if (mPopupWindow != null && mPopupWindow.isShowing()){
                mPopupWindow.dismiss();
            }
            if (p10221.resp.transcode.equals("9999")) {
                PromptUtils.showToast(Activity_BaPing.this, p10221.resp.msg);
                deleteRedPackFromDB(redpacket_id);
                baping_redpacket.setVisibility(View.GONE);
                mLayoutDanmaku.setVisibility(View.GONE);
            } else {
                if (p10221.resp.state == 1) {
                    PromptUtils.showToast(Activity_BaPing.this, "成功领取" + p10221.resp.score + "猫币");
                    baping_redpacket.setVisibility(View.GONE);
                    mLayoutDanmaku.setVisibility(View.GONE);
                } else if (p10221.resp.state == 0) {
                    PromptUtils.showToast(Activity_BaPing.this, "领取失败");
                } else if (p10221.resp.state == -1) {
                    PromptUtils.showToast(Activity_BaPing.this, "红包已领完");
                } else if (p10221.resp.state == -2) {
                    PromptUtils.showToast(Activity_BaPing.this, "红包已失效");
                } else if (p10221.resp.state == -3) {
                    PromptUtils.showToast(Activity_BaPing.this, "性别不符");
                }
                baping_redpacket.setVisibility(View.GONE);
                mLayoutDanmaku.setVisibility(View.GONE);
                //从数据库中删除该红包
                deleteRedPackFromDB(p10221.resp.red_pack_id);
            }
            //再次从数据库中查询有没有剩余红包
            searchRedPackFromDB();
        }
    }

    /**
     * 请求霸屏信息
     */
    private void requestBaPing() {
        Map<String, String> params = new HashMap<String, String>();
        P13102 p = new P13102();
        //p.req.userId=session.userId;
        p.req.sessionId = mSessionCache.sessionId;
        p.req.screenOptionId = optionId;
        p.req.content = baping_content;
        p.req.type = 0;
        p.req.barId = barId;
        p.req.imgBase = "";
        p.req.transcode = "13102";
        params.put("content", JSON.toJSONString(p.req));
        params.put("transcode", p.req.transcode);
        params.put("sessionId", mSessionCache.sessionId);
        bitmap = decodeFile(baping_img_path);
        //压缩图片
        Utils.compressBmpToFile(bitmap, mTmpFile);
        FormFile formFile;
        try {
            formFile = new FormFile(mTmpFile.getName(), Utils.getBytesFromFile(mTmpFile), "imgBase",
                    null);
            SocketBean bean = SocketHttpRequester.post("", params, formFile);
            if (bean.isSuccess) {
                Log.e("tag", "13102霸屏请求成功  content = " + bean.content);
                P13102.Resp resp = JSON.parseObject(bean.content, P13102.Resp.class);
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("id", resp.screenOrderId);
                bundle.putString("content", baping_content);
                bundle.putString("path", baping_img_path);
                Log.e("wtf", "requestBaPing: " + resp.screenOrderId);
                message.what = API.BP;
                message.setData(bundle);
                mHandlerPlus.sendMessage(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap decodeFile(String filePath) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPurgeable = true;
        options.inSampleSize = 1;//代表容量变为以前容量的1/2
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = false;
        if (filePath != null) {
            bitmap = BitmapFactory.decodeFile(filePath, options);
        }
        return bitmap;
    }

    /**
     * 用户聊天信息
     *
     * @return
     */
    private String dealChatInfo() {
        JsonUserInfo jsonUserInfo = new JsonUserInfo();
        jsonUserInfo.setUserId(mSessionCache.userId);
        jsonUserInfo.setUserName(mSessionCache.nickname);
        jsonUserInfo.setHeadImg(mSessionCache.headimgUrl);
        jsonUserInfo.setUserId2(toChatUserId);
        jsonUserInfo.setUserName2(barName);
        jsonUserInfo.setHeadImg2("");
        jsonUserInfo.setGender(mSessionCache.sex);
        return JSON.toJSONString(jsonUserInfo);
    }

    /**
     * 霸屏价格列表请求
     */
    private void initBPChargeTask() {
        P13101 p13101 = new P13101();
        p13101.req.sessionId = mSessionCache.sessionId;
        p13101.req.barId = barId;
        new BPChargeTask().execute(getApplicationContext(), p13101);
    }

    /**
     * 霸屏价格列表点击事件处理
     * @param position
     */
    public void setChecked(int position) {
        for (int i = 0; i < mScreeOptionBeenArr.size(); i++) {
            if (i == position) {
                mScreeOptionBeenArr.get(i).setChoose(true);
            } else {
                mScreeOptionBeenArr.get(i).setChoose(false);
            }
        }
    }

    /**
     * 弹幕相关
     * @param stream
     * @return
     */
    private BaseDanmakuParser createParser(InputStream stream) {
        if (stream == null) {
            return new BaseDanmakuParser() {

                @Override
                protected Danmakus parse() {
                    return new Danmakus();
                }
            };
        }
        ILoader loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);
        try {
            loader.load(stream);
        } catch (IllegalDataException e) {
            e.printStackTrace();
        }
        BaseDanmakuParser parser = new BiliDanmukuParser();
        IDataSource<?> dataSource = loader.getDataSource();
        parser.load(dataSource);
        return parser;

    }

    public RecyclerView getListView() {
        return mRecyclerBaping;
    }

    /**
     * 霸屏价格列表请求
     */
    private class BPChargeTask extends DefaultTask {

        @Override
        public void onError(DefaultError obj) {
            super.onError(obj);
            PromptUtils.showToast(Activity_BaPing.this, "网络错误");
        }

        @Override
        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            P13101 p13101 = (P13101) protocol;
            if (p13101.resp.transcode.equals("9999")) {
                PromptUtils.showToast(Activity_BaPing.this, "系统异常");
            } else {
                mScreeOptionBeenArr.clear();
                mScreeOptionBeenArr.addAll(p13101.resp.scrOptionList);
                mScreeOptionBeenArr.get(0).setChoose(true);
                mMyMoney = p13101.resp.score;
                pop_bp_money.setText("账户余额: " + p13101.resp.score + " 猫币");
                optionId = mScreeOptionBeenArr.get(0).getScreenOptionId();
                money = mScreeOptionBeenArr.get(0).getMoney() + "";
                time = mScreeOptionBeenArr.get(0).getContinueTime() + "";
                mBPMoneyAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 发红包
     */
    public class SendTask extends DefaultTask {
        @Override
        public void preExecute() {
            super.preExecute();

        }
        public void onError(DefaultError obj) {
            super.onError(obj);
            Toast.makeText(Activity_BaPing.this, "网络错误", Toast.LENGTH_SHORT).show();
        }

        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            P10220 p = (P10220) protocol;

            if (p.resp.transcode.equals("9999")) {
                Toast.makeText(Activity_BaPing.this, " " + p.resp.msg, Toast.LENGTH_SHORT)
                        .show();
            } else {
                if (p.resp.state == 1) {
                    //余额保存
                    SessionCache session = SessionCache.getInstance(mActivity);
                    int ss = session.capNum
                            - Integer.parseInt(mHongbaoMoney.getText().toString().trim());
                    session.capNum = ss;
                    session.save();
                    //重置
                    mHongbaoMoney.setText("");
                    mHongbaoNums.setText("");
                    mHongbaoContent.setText("");
                    mPopupWindow.dismiss();
                } else if (p.resp.state == 2) {
                    Toast.makeText(Activity_BaPing.this, "猫币不足请充值", Toast.LENGTH_SHORT).show();
                } else if (p.resp.state == 3) {
                    Toast.makeText(Activity_BaPing.this, "发送失败", Toast.LENGTH_SHORT).show();
                }
            }
        }

        public void postExecute() {
            super.postExecute();

        }
    }

    class DeleteTask extends DefaultTask {

        PopupWindow popupWindow;

        @Override
        public void preExecute() {
            popupWindow = PromptUtils.getProgressDialogPop(Activity_BaPing.this);
            popupWindow.showAtLocation(Activity_BaPing.this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
            super.preExecute();
        }

        @Override
        public void onError(DefaultError obj) {
            super.onError(obj);
            popupWindow.dismiss();
            PromptUtils.showToast(Activity_BaPing.this, "网络错误");
        }

        @Override
        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            popupWindow.dismiss();
            P23104 p23104 = (P23104) protocol;
            if (p23104.resp.status == 1) {

            } else {
                PromptUtils.showToast(Activity_BaPing.this, "删除失败");
            }
        }
    }


    @Override
    public void onEvent(EMNotifierEvent emNotifierEvent) {
        switch (emNotifierEvent.getEvent()) {
            case EventNewCMDMessage: {// 接收透传新消息
                EMMessage message = (EMMessage) emNotifierEvent.getData();
                CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
                String action = cmdMsgBody.action;//获取自定义action
                if (action.equals(EaseConstant.EASE_ATTR_REVOKE)) {//撤回消息
                    try {
                        String msgId = message.getStringAttribute(EaseConstant.EASE_ATTR_REVOKE_MSG_ID);
                        EMConversation conversation = EMChatManager.getInstance().getConversation(toChatUserId);
                        EMMessage revokeMsg = conversation.getMessage(msgId);
                        TextMessageBody body = new TextMessageBody("对方" + " 撤回了一条消息");
                        revokeMsg.addBody(body);
                        // 这里需要把消息类型改为 TXT 类型
                        revokeMsg.setType(EMMessage.Type.TXT);
                        // 设置扩展为撤回消息类型，是为了区分消息的显示
                        revokeMsg.setAttribute(EaseConstant.EASE_ATTR_REVOKE, true);
                        // 返回修改消息结果
                        EMChatManager.getInstance().updateMessageBody(revokeMsg);
                        refreshUI();
                    } catch (EaseMobException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case EventNewMessage: {//接受新消息
                SimpleMessageCache simpleCache = SimpleMessageCache.getInstance(mActivity);
                //获取到message
                EMMessage message = (EMMessage) emNotifierEvent.getData();
                //刷新ListView 将新发送的消息更新出来
                //mMessageAdapter.refresh();
                String username = null;
                if (message.getChatType() == EMMessage.ChatType.GroupChat || message.getChatType() == EMMessage.ChatType.ChatRoom) {
                    //群组消息
                    username = message.getTo();
                    //群聊消息小弹窗的显示和隐藏
                    if (!isSlideToBottom(mRecyclerBaping)) {
                        //不在最底部 显示弹窗
                        mHandlerPlus.sendEmptyMessage(49);
                    }else {
                        mMessageAdapter.refreshSelectLast();
                    }
                } else {
                    //单聊消息
                    username = message.getFrom();
                    if (!message.getStringAttribute("arrive" , "").equals("end")){//当消息的类型为游戏时，消息的小红点不需要展示
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                baping_chat_flag.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }
                //如果是系统消息的扩展
                boolean isSystemMsg = message.getBooleanAttribute("isSystemMsg", false);
                if (isSystemMsg) {
                    EMConversation commentCon = EMChatManager.getInstance().getConversationByType(username, EMConversation.EMConversationType.Chat);
                    try {
                        String transCode = message.getStringAttribute("transCode", "");

                        if (transCode.equals("send10231")) {
                            //我的动态通知
                            String para = message.getStringAttribute("para");
                            JSONObject paraObject = JSONObject.parseObject(para);
                            String pUserId = paraObject.getString("pUserId");    //评论人
                            String pName = paraObject.getString("pName");    //名称
                            String pImg = paraObject.getString("pImg");        //头像
                            int type = paraObject.getIntValue("type");        //类型
                            String content = paraObject.getString("content");    //内容
                            String id = paraObject.getString("id");            //动态id
                            Date pTime = paraObject.getDate("pTime");        //时间
                            String pic_img = paraObject.getString("pic_img");    //动态图片
                            int pic_type = paraObject.getIntValue("pic_type");    //动态类型

                            ReplyCacheBean bean = new ReplyCacheBean();
                            SessionCache session = SessionCache.getInstance(mActivity);
                            bean.setMyUserId(session.userId);
                            bean.setUserId(pUserId);
                            bean.setName(pName);
                            bean.setImg(pImg);
                            bean.setType(type);
                            bean.setContent(content);
                            bean.setId(id);
                            bean.setTime(pTime);
                            bean.setPic_img(pic_img);
                            bean.setPic_type(pic_type);
                            bean.setMsgId(message.getMsgId());

                            if (bean.getType() == 1 || bean.getType() == 2) {
                                ReplyCache cache = ReplyCache.getCommentCache(mActivity);
                                boolean isHaveData = false;
                                for (ReplyCacheBean rbean : cache.getReplyList()) {
                                    if (rbean.getMsgId() != null && rbean.getMsgId().equals(bean.getMsgId())) {
                                        isHaveData = true;
                                    }
                                }
                                if (!isHaveData) {
                                    cache.getReplyList().add(bean);
                                    cache.save();
                                }

                                Log.i("", "动态有新消息，放入缓存");
                                simpleCache.isNewMy = true;
                                simpleCache.save();
                            }

                            //设为已读
                            commentCon.markMessageAsRead(message.getMsgId());
                            //清除消息
                            commentCon.removeMessage(message.getMsgId());
                        } else if (transCode.equals("cc0001")) {
                            SystemMsgUtil.dealSystemMessage(message, mActivity);
                        } else if (transCode.equals("cc0006")) {
                        } else {
                            simpleCache.isNewMsg = true;
                            simpleCache.save();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i("", "聊天中的系统消息transCode解析失败！");
                    }
                }
                //transCode扩展消息
                try {
                    String transCode = message.getStringAttribute("transCode");
                    if (transCode.equals("cc0003") && username.equals(toChatUserId)) {
                        String content = message.getStringAttribute("para");
                        JsonGiftMsg ext = JSON.parseObject(content, JsonGiftMsg.class);
                        if (ext.getMsg_type() == 1) {
                            conversation.removeMessage(message.getMsgId());
                            return;
                        } else {
                            simpleCache.isNewMsg = true;
                            simpleCache.save();
                        }
                    }

                    boolean isMyFriend = true;//判断是否走招呼   请到nearbyActivity里面的isMyFriend也变成true
                    AttendCache cache = AttendCache.getAttendCache(mActivity);
                    List<FollBean> frlist = cache.getFoll();
                    //遍历关注缓存
                    for (int j = 0; j < frlist.size(); j++) {
                        FollBean abean = frlist.get(j);
                        if (username == null || abean.getUserId() == null) {
                            continue;
                        }
                    }
                    boolean isInMianDaRao = SystemMsgUtil.isInMianDaRao(message.getFrom(), mActivity);
                    if (isInMianDaRao && isMyFriend && transCode.equals("cc0001") && !isSystemMsg && message.getChatType() == EMMessage.ChatType.Chat) {
                        simpleCache.isNewMsg = true;
                        simpleCache.save();
                    }

                } catch (Exception e) {
                }
                //新消息提示
                mEMChatOptions = EMChatManager.getInstance().getChatOptions();
                mEMChatOptions.setNotifyBySoundAndVibrate(true);
                mEMChatOptions.setShowNotificationInBackgroud(true);
                break;
            }
            case EventDeliveryAck: {//接收已发送回执
                break;
            }
            case EventReadAck: {//接收已读回执
                break;
            }
            case EventOfflineMessage: {//接收离线消息
                break;
            }
            default:
                break;
        }
    }

    /**
     * 进入店内夜猫
     */
    private class IntoCatTask extends DefaultTask {
        @Override
        public void preExecute() {
            super.preExecute();
        }

        @Override
        public void onError(DefaultError obj) {
            super.onError(obj);
            if (catPop != null && catPop.isShowing()) {
                catPop.dismiss();
            }
            PromptUtils.showToast(Activity_BaPing.this, "网络错误");
        }

        @Override
        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            P610001 p610001 = (P610001) protocol;
            if (catPop != null && catPop.isShowing()) {
                catPop.dismiss();
            }
            if (p610001.resp.transcode.equals("9999")) {
                PromptUtils.showToast(Activity_BaPing.this, "系统异常");
            } else {
                if (p610001.resp.flag == 0) {
                    //不在酒吧内
                    PromptUtils.showToast(Activity_BaPing.this, "您已经出了该酒吧范围");
                    finish();
                    P10112 p10112 = new P10112();
                    p10112.req.sessionId = mSessionCache.sessionId;
                    p10112.req.userId = mSessionCache.userId;
                    p10112.req.barId = barId;
                    p10112.req.locationX = Double.parseDouble(mSessionCache.locationX);
                    p10112.req.locationY = Double.parseDouble(mSessionCache.locationY);
                    new BardetailTask().execute(getApplicationContext(), p10112);
                } else {
                    Intent intent = new Intent(Activity_BaPing.this, BarMemberActivity.class);
                    intent.putExtra("catList", (Serializable) p610001.resp.YeMaoList);
                    intent.putExtra("headUrl", p610001.resp.imgUrl);
                    intent.putExtra("barName", barName);
                    startActivity(intent);
                }
            }
        }
    }


    class BardetailTask extends DefaultTask {
        PopupWindow mPopupWindow;

        @Override
        public void preExecute() {
            super.preExecute();
            mPopupWindow = PromptUtils.getProgressDialogPop(Activity_BaPing.this);
            mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        }

        @Override
        public void onError(DefaultError obj) {
            super.onError(obj);
            if (mPopupWindow != null && mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
        }

        @Override
        public void cancel() {
            super.cancel();

        }

        @Override
        public void onOk(IProtocol protocol) {
            super.onOk(protocol);
            if (mPopupWindow != null && mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
            P10112 p10112 = (P10112) protocol;
            Intent intent;
            if (p10112.resp.flag == 1) {
                intent = new Intent(Activity_BaPing.this, Activity_BaPing.class);
                intent.putExtra(Activity_BaPing.DATA, p10112.resp);
                intent.putExtra(API.BAR_ID, p10112.resp.barId);
                intent.putExtra(API.BAR_NAME, p10112.resp.barName);
                intent.putExtra(API.TO_CHAT_USERID, p10112.resp.chatGroupId);
                startActivity(intent);
            } else if (p10112.resp.flag == 0) {
                intent = new Intent(Activity_BaPing.this, OutBarActivity.class);
                intent.putExtra(OutBarActivity.OUT_DATA, p10112.resp);
                startActivity(intent);
            }

        }
    }


}
