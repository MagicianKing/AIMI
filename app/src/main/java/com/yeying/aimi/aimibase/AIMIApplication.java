package com.yeying.aimi.aimibase;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.exceptions.EaseMobException;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.yeying.aimi.API;
import com.yeying.aimi.R;
import com.yeying.aimi.bean.RedPacketBean;
import com.yeying.aimi.bean.ReplyCacheBean;
import com.yeying.aimi.bean.TopRankBean;
import com.yeying.aimi.database.HBData;
import com.yeying.aimi.huanxin.HXNotifier;
import com.yeying.aimi.mode.HomeActivity;
import com.yeying.aimi.protocol.impl.P10112;
import com.yeying.aimi.storage.ReplyCache;
import com.yeying.aimi.storage.SessionCache;
import com.yeying.aimi.utils.MyLifecycleHandler;

import org.json.JSONException;
import org.litepal.LitePalApplication;
import org.litepal.crud.DataSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.easemob.EMNotifierEvent.Event.EventNewCMDMessage;
import static com.easemob.EMNotifierEvent.Event.EventNewMessage;

/**
 * Created by king .
 * 公司:业英众娱
 * 2017/9/6 下午1:52
 */

//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                            O\ = /O
//                        ____/`---'\____
//                      .   ' \\| |// `.
//                       / \\||| : |||// \
//                     / _||||| -:- |||||- \
//                       | | \\\ - /// | |
//                     | \_| ''\---/'' | |
//                      \ .-\__ `-` ___/-. /
//                   ___`. .' /--.--\ `. . __
//                ."" '< `.___\_<|>_/___.' >'"".
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |
//                 \ \ `-. \_ __\ /__ _/ .-` / /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//
//         .............................................
//                  佛祖镇楼                 BUG辟易

public class AIMIApplication extends LitePalApplication implements EMEventListener, HXNotifier.HXNotificationInfoProvider {

    public int unReachNums=0;

    private static AIMIApplication context;

    private EMChatOptions chatOptions;

    private static ArrayList<Activity> mActivities;
    public final String TAG = AIMIApplication.class.getSimpleName();
    private static ArrayList<Activity> someActivities;
    public static Context mContext;
    private P10112 p10112;

    public HXNotifier mNotifier = new HXNotifier();

    private RedPacketListener mRedPacketListener;
    private GameRankListener mGameRankListener;

    public static boolean isMineFragmentShow = false;
    private static SessionCache mSessionCache;

    public int rank = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        mSessionCache = SessionCache.getInstance(this);
        mActivities = new ArrayList<>();
        someActivities = new ArrayList<>();
        mContext = this;
        registerActivityLifecycleCallbacks(new MyLifecycleHandler());
        initHX();
        mNotifier.init(this);
        mNotifier.setNotificationInfoProvider(this);
        Log.e(TAG, "onCreate: " + getPhoneName());
        //多渠道平台
        //QQ/QQ 空间
        PlatformConfig.setQQZone(API.APP_QQ_ID,API.APP_QQ_KEY);
        //微博
        PlatformConfig.setSinaWeibo(API.APP_WEIBO_ID,API.APP_WEIBO_SECRET,API.APP_WEIBO_URL);
        UMShareAPI.get(this);
    }

    private void initHX() {
        int pid = Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的app会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回
        if (processAppName == null ||!processAppName.equalsIgnoreCase("com.yeying.aimi")) {
            Log.e(TAG, "enter the service process!");
            //"com.easemob.chatuidemo"为demo的包名，换到自己项目中要改成自己包名
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        EMChat.getInstance().init(this);

        /**
        * debugMode == true 时为打开，SDK会在log里输入调试信息
        * @param debugMode
        * 在做代码混淆的时候需要设置成false
        */
        EMChat.getInstance().setDebugMode(true);//在做打包混淆时，要关闭debug模式，避免消耗不必要的资源
        //监听事件
        EMChatManager.getInstance().registerEventListener(this, new EMNotifierEvent.Event[]{EventNewCMDMessage, EventNewMessage});
    }

    /*
	* 获取processAppName
	* */
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this
                .getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
                    .next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm
                            .getApplicationInfo(info.processName,
                                    PackageManager.GET_META_DATA));
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {

            }
        }
        return processName;
    }

    public static void addActivity(Activity activity) {
        if (activity != null) {
            mActivities.add(activity);

        }
    }
    public static void exitActivity(Activity activity){
        if(activity!=null){
            activity.finish();
            mActivities.remove(activity);
        }
    }
    public static void exitAll() {
        for (Activity activity : mActivities) {
            if (activity != null && (!activity.isFinishing())) {
                activity.finish();
            }
        }
    }
    public static List<Activity> getActivityCollector(){
        if(mActivities!=null){
            return mActivities;
        }
       return null;
    }
    public static Activity getTopActivity(){
        if(mActivities!=null){
            return mActivities.get(mActivities.size()-1);
        }
        return null;
    }


    @Override
    public void onEvent(EMNotifierEvent emNotifierEvent) {
        EMMessage message = (EMMessage) emNotifierEvent.getData();
        String tempPara = message.getStringAttribute("para", "");
        Log.i("Palmapplication", "收到环信推送 消息" + message.toString() + "tempPara==" + tempPara);
        String transCode = message.getStringAttribute("transCode", "");
        String type=message.getStringAttribute("type","");
        Log.e("Palmapplication", "onEvent: "+tempPara);
        switch (emNotifierEvent.getEvent()) {
            case EventNewMessage://新消息

            case EventNewCMDMessage://透传消息
                if (transCode.equals("send20001")){//红包
                   Log.e(TAG, "onEvent: ---->收到红包消息------>"+tempPara);
                    RedPacketBean redPacketBean = JSON.parseObject(tempPara,RedPacketBean.class);
                    if (redPacketBean.getStatus().equals("0")){// 为0的时候有红包 存入数据库
                        HBData hbData = new HBData();
                        hbData.setBar_id(redPacketBean.getBar_id());
                        hbData.setImgUrl(redPacketBean.getImgUrl());
                        hbData.setRed_pack_id(redPacketBean.getRed_pack_id());
                        hbData.setRedpack_choose(redPacketBean.getRedpack_choose());
                        hbData.setCreate_time(redPacketBean.getCreate_time());
                        hbData.setUser_id(redPacketBean.getUser_id());
                        hbData.setUser_name(redPacketBean.getUser_name());
                        hbData.setInvalid_time(redPacketBean.getInvalid_time());
                        hbData.setRemark(redPacketBean.getRemark());
                        hbData.setType(redPacketBean.getType());
                        hbData.setLimit_sex(redPacketBean.getLimit_sex());
                        hbData.setStatus(redPacketBean.getStatus());
                        if (hbData.save()){
                            Log.e(TAG, "onEvent: 红包成功存入数据库-------->hbData.getRed_pack_id()--->"+hbData.getRed_pack_id());
                        }else {
                            Log.e(TAG, "onEvent: 红包存入数据库失败");
                        }
                        if (mRedPacketListener != null){
                            mRedPacketListener.onRedPacketListener(redPacketBean);
                        }
                    }else if (redPacketBean.getStatus().equals("1")){//红包已经抢完，删除数据库中的红包
                        deleteRedPackFromDB(redPacketBean.getRed_pack_id());
                        if (mRedPacketListener != null){
                            mRedPacketListener.onRedPacketListener(redPacketBean);
                        }
                    }
                    return;
                }
                if (transCode.equals("send20013")){//游戏排行榜
                    if (mGameRankListener != null){
                        mGameRankListener.onGameRankListener(tempPara);
                    }
                    return;
                }
                if (transCode.equals("send20012")){//霸屏排行
                    TopRankBean topRankBean = JSON.parseObject(tempPara, TopRankBean.class);
                    rank = topRankBean.getRanking();
                    ReplyCacheBean messageBean = new ReplyCacheBean();
                    SessionCache session = SessionCache.getInstance(this);
                    messageBean.setMyUserId(session.userId);
                    messageBean.setTypeInt(7);
                    messageBean.setTheMsgTime(message.getMsgTime());
                    messageBean.setTempParaInfo(tempPara);
                    messageBean.setMsgId(message.getMsgId());
                    messageBean.setRank(rank);
                    ReplyCache cache = ReplyCache.getCommentCache(this);
                    boolean isHaveData = false;
                    for (ReplyCacheBean rbean : cache.getReplyList()) {//循环整个缓存集合 如果msgid相等说明缓存里面有该数据 则不需要再次保存
                        if (rbean.getMsgId() != null && rbean.getMsgId().equals(messageBean.getMsgId())) {
                            isHaveData = true;
                            break;
                        }
                    }
                    if (!isHaveData) {//如果没有该条数据 将该数据放到缓存中保存
                        cache.getReplyList().add(messageBean);
                        cache.save();
                    }
                    HomeActivity.activityInstance.dealUnReachNums();
                    HomeActivity.activityInstance.toRefresh();
                    HomeActivity.activityInstance.redPointVisbile();
                }
                /*if (transCode.equals("send20004")) {//抢到缘分牌后有异性抢到相同的缘分牌
                    ReplyCacheBean messageBean = new ReplyCacheBean();
                    SessionCache session = SessionCache.getInstance(this);
                    messageBean.setMyUserId(session.userId);
                    messageBean.setTypeInt(4);
                    messageBean.setTheMsgTime(message.getMsgTime());
                    messageBean.setFateUserInfo(tempPara);
                    messageBean.setMsgId(message.getMsgId());
                    if (!GreetCardActivity.isVisible) {
                        mNotifier.onNewMsg(message);
                    } else {
                        onLuckCarddListener.luckCardListener(tempPara, 0);
                    }
                    ReplyCache cache = ReplyCache.getCommentCache(this);
                    boolean isHaveData = false;
                    for (ReplyCacheBean rbean : cache.getReplyList()) {
                        if (rbean.getMsgId() != null && rbean.getMsgId().equals(messageBean.getMsgId())) {
                            isHaveData = true;
                            break;
                        }
                    }
                    if (!isHaveData) {
                        cache.getReplyList().add(messageBean);
                        cache.save();
                    }
                }else if (transCode.equals("send20007")){
                    //缘分牌每轮结束匹配失败消息
                    ReplyCacheBean messageBean = new ReplyCacheBean();
                    SessionCache session = SessionCache.getInstance(this);
                    messageBean.setMyUserId(session.userId);
                    messageBean.setMsgId(message.getMsgId());
                    messageBean.setTypeInt(6);
                    messageBean.setTheMsgTime(message.getMsgTime());
                    messageBean.setFateUserInfo(tempPara);
                    ReplyCache cache = ReplyCache.getCommentCache(this);
                    boolean isHaveData = false;
                    for (ReplyCacheBean rbean : cache.getReplyList()) {
                        if (rbean.getMsgId() != null && rbean.getMsgId().equals(messageBean.getMsgId())) {
                            isHaveData = true;
                            break;
                        }
                    }
                    if (!isHaveData) {
                        cache.getReplyList().add(messageBean);
                        cache.save();
                    }

                }else if (transCode.equals("send20008")) {
                    //缘分牌是否同意消息  两方都会接收到通知
                    onMatchedListener.matchedListener(tempPara);
                    Gson gson=new Gson();
                    FateAgreeBean fateAgreeBean = gson.fromJson(tempPara, FateAgreeBean.class);
                    ReplyCacheBean messageBean = new ReplyCacheBean();
                    SessionCache session = SessionCache.getInstance(this);
                    messageBean.setMyUserId(session.userId);
                    messageBean.setTheMsgTime(message.getMsgTime());
                    messageBean.setTypeInt(5);
                    messageBean.setMsgId(message.getMsgId());
                    messageBean.setFateUserInfo(tempPara);
                    ReplyCache cache = ReplyCache.getCommentCache(this);
                    boolean isHaveData = false;
                    for (ReplyCacheBean rbean : cache.getReplyList()) {
                        if (rbean.getMsgId() != null && rbean.getMsgId().equals(messageBean.getMsgId())) {
                            isHaveData = true;
                            break;
                        }
                    }
                    if (!isHaveData) {
                        if (fateAgreeBean.getAgreeStatus()==1){
                            //配对成功，添加通知添加到通知栏
                            cache.getReplyList().add(messageBean);
                            cache.save();
                        }
                    }
                } else */
                if (transCode.equals("send20009")) {
                    try {
                        org.json.JSONObject jsonObject = new org.json.JSONObject(tempPara);
                        String messageId = jsonObject.optString("messageId");
                        String bar_id = jsonObject.optString("bar_id");
                        String groupId = jsonObject.optString("groupId");
                        if (mTyrantscreenlIstener != null){
                            mTyrantscreenlIstener.deleteBaPingMsg(messageId,bar_id,groupId);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                break;
        }
        //应用在后台，不需要刷新UI,通知栏提示新消息
        if (!BaseActivityWithoutSwipeBack.isForeground) {
            Log.e(TAG, "onEvent: "+tempPara);
            if (!transCode.equals("12201") &&
                    !transCode.equals("send10320") &&
                    !transCode.equals("send20004")&&
                    !transCode.equals("send20005")&&
                    !transCode.equals("cc00011")&&
                    !message.getStringAttribute("arrive" , "").equals("end")&&
                    message.getChatType()!= EMMessage.ChatType.GroupChat&&
                    message.getChatType()!=EMMessage.ChatType.ChatRoom) {
                //酒友关系变更、好友设置通知、系统发送的礼物消息不推送
                if (message.getStringAttribute("type","").equals("1")){
                    //当应用在后台时发送 吧台见 三个字的消息不推送并删除 其他关于吧台见的进行消息推送
                    EMConversation conversation = EMChatManager.getInstance().getConversation(message.getFrom());
                    conversation.removeMessage(message.getMsgId());
                }else{
                    if (transCode.equals("send20012")){//霸屏排行榜
                        createNotifier(rank);
                    }else {
                        mNotifier.onNewMsg(message);
                    }
                }
            }
        }
    }

    /**
     * 霸屏接口监听
     */
    private TyrantscreenlIstener mTyrantscreenlIstener;

    public void setmTyrantscreenlIstener(TyrantscreenlIstener mTyrantscreenlIstener) {
        this.mTyrantscreenlIstener = mTyrantscreenlIstener;
    }

    public interface TyrantscreenlIstener {
        void deleteBaPingMsg(String messageId,String bar_id,String groupId);
    }

    public static Context getContext(){
        return mContext;
    }

    /**
     * 头像前缀处理
     * @param head_url
     * @return
     */
    public static String dealHeadImg(String head_url){
        String head_path="";
        if (head_url != null){
            if (head_url.startsWith("http:")) {
                head_path = head_url;
            } else {
                head_path = mSessionCache.imgUrl + head_url;
            }
            return head_path;
        }else {
            return head_path;
        }

    }

    /**
     * 加工timeStr
     */
    public static String getDateStr(Date date5) {
        String str = "";
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM月dd日 HH:mm");
        SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        Date date = null;
        Date date1 = date5;
        String str1 = sdf.format(date1);
        try {
            date = sdf.parse(str1);
            now = sdf.parse(sdf.format(now));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        long sl = date.getTime();
        long el = now.getTime();
        long ei = sl - el;
        int value = (int) (ei / (1000 * 60 * 60 * 24));
        if (value == 0) {
            //一天内的显示时间
            str = sdf3.format(date1);
        } else if (value >= -365) {
            str = sdf2.format(date1);
        } else{
            str = sdf1.format(date1);
        }
        return str;
    }

    /**
     * 关于消息的推送通知
     * @param message 接收到的消息
     * @return
     */
    @Override
    public String getDisplayedText(EMMessage message) {
        try {
            String transCode = message.getStringAttribute("transCode", "");
            Log.e(TAG, "getDisplayedText: " + transCode);
            String para = message.getStringAttribute("para");
            Log.e(TAG, "getDisplayedText: " + para);
            com.alibaba.fastjson.JSONObject paraObject = com.alibaba.fastjson.JSONObject.parseObject(para);
            String userId = paraObject.getString("userId");
            String username = paraObject.getString("userName");
            String pName = paraObject.getString("pName");
            if (transCode.equals("send10231")) {//评论

                Log.e(TAG, "getDisplayedText: " + pName);
                if (paraObject.getInteger("type") == 2) {
                    return pName + "评论了你";
                } else {
                    return pName + "赞了你";
                }
            } else if (transCode.equals("send10141")) {//赞
                return username + "赞了你";
            } else if (transCode.equals("send10150")) {//关注
                return username + "关注了你";
            }else if (transCode.equals("cc0001")) {
                return null;
            }else if (transCode.equals("send20003")){
                return "缘分牌活动已开始";
            }else if (transCode.equals("send20008")){
                return "缘分牌配对成功";
            }else if (transCode.equals("send20004")){
                return "缘分牌配对成功";
            }
        } catch (EaseMobException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
        return null;
    }

    @Override
    public String getTitle(EMMessage message) {
        return null;
    }

    @Override
    public int getSmallIcon(EMMessage message) {
        return 0;
    }

    @Override
    public Intent getLaunchIntent(EMMessage message) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("notify_flag",true);

        return intent;
    }

    /**
     * 获取手机名字
     * @return
     */
    public String getPhoneName(){
        return Build.MODEL+"\t"
                +Build.DEVICE+"\t"
                +Build.PRODUCT+"\t"
                +Build.BOARD+"\t"
                +Build.BOOTLOADER+"\t"
                +Build.BRAND+"\t"
                +Build.DISPLAY+"\t"
                +Build.HARDWARE+"\t"
                +Build.MANUFACTURER;
    }

    /**
     * 从数据库中删除红包
     * @param deleteRedPackId
     */
    private void deleteRedPackFromDB(String deleteRedPackId) {
        Log.e(TAG, "deleteRedPackFromDB: ---->删除数据库红包");
        int i = DataSupport.deleteAll(HBData.class, "red_pack_id = ? ", deleteRedPackId);
        Log.e(TAG, "deleteRedPackFromDB: 删除红包数目--->" + i);
    }

    /**
     * 红包监听回调
     */
    public interface RedPacketListener{
        void onRedPacketListener(RedPacketBean redPacketBean);
    }

    public void setRedPacketListener(RedPacketListener redPacketListener){
        mRedPacketListener = redPacketListener;
    }

    /**
     * 游戏排行榜监听回调
     */
    public interface GameRankListener{
        void onGameRankListener(String tempInfo);
    }

    /**
     * 设置游戏排行榜监听
     * @param gameRankListener
     */
    public void setGameRankListener(GameRankListener gameRankListener){
        mGameRankListener = gameRankListener;
    }

    /**
     * 接触监听
     */
    public void unRegisteGameRankListener(){
        mGameRankListener = null;
    }

    public static String dealTime(Date dateValue){
        String timeStr = "";
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM月dd日 HH:mm");
        SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sdf4 = new SimpleDateFormat("yyyyMMdd");
        Date before = dateValue;
        Date today = new Date();
        long beforeLong = before.getTime();
        long todayLong = today.getTime();
        int value = (int) ((todayLong - beforeLong) / (1000 * 60 * 60 * 24));
        String beforeTime = sdf4.format(before);
        String todayTime = sdf4.format(today);
        int durTime = Integer.valueOf(todayTime)-Integer.valueOf(beforeTime);
        if (durTime == 0){//当天的时间
            return sdf3.format(before);
        }else if (durTime == 1){//昨天的时间
            return sdf2.format(before);
        }else {
            if (value > 365){//一年以外的
                return sdf1.format(before);
            }else {//一年以内的
                return sdf2.format(before);
            }
        }
    }

    public static String dealTime(long dateValue){
        String timeStr = "";
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM月dd日 HH:mm");
        SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sdf4 = new SimpleDateFormat("yyyyMMdd");
        Date before = new Date(dateValue);
        Date today = new Date();

        long beforeLong = before.getTime();
        long todayLong = today.getTime();
        int value = (int) ((todayLong - beforeLong) / (1000 * 60 * 60 * 24));
        String beforeTime = sdf4.format(before);
        String todayTime = sdf4.format(today);
        int durTime = Integer.valueOf(todayTime)-Integer.valueOf(beforeTime);
        if (durTime == 0){//当天的时间
            return sdf3.format(before);
        }else if (durTime == 1){//昨天的时间
            return sdf2.format(before);
        }else {
            if (value > 365){//一年以外的
                return sdf1.format(before);
            }else {//一年以内的
                return sdf2.format(before);
            }
        }
    }

    /**
     * 霸屏排行榜的通知 和消息的通知有区分
     * @param rank
     */
    private void createNotifier(int rank){
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("AIMI");
        builder.setContentText("今晚霸屏玩的嗨，您夺得今夜霸屏榜第"+rank+"名");
        builder.setSmallIcon(R.drawable.logo);
        builder.setTicker("哎米通知");
        builder.setAutoCancel(true);
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("notify_flag",true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this , 1 , intent , PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(pendingIntent);
        Notification notificationCompat = builder.build();
        notificationManager.notify(1 , notificationCompat);
    }

}
