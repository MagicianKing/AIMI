package com.yeying.aimi.bean;

import java.util.Date;
import java.util.List;

/**
 * 系统消息实体类
 */
public class NewSystemMsgBean {
    private String myUserId;//我的userid，用于区分不同登录
    private boolean isRead;//是否已读
    private long theMsgTime;//环信消息时间
    private String msgId;
    private String para;
    private String userId;
    private String userName;
    private String headImg;
    private String transCode;
    private String fUid;//	接受礼物用户id
    private int score;//	积分,竞猜猫币
    private String recordId;//	打招呼记录id
    private String giftName;//	礼物名称
    private String fName;//	接受礼物用户昵称
    private String ratio;//	回复消息比例
    private int type;//	类型	0 打招呼   1 送礼物
    private int replyType;//	回复消息类型	 0是文字1是图片2语音3小视频 4 系统退回
    private String periodId;//期id

    private String content;
    private String msgTime;
    private String barId;//	酒吧id
    private double money;//	总金额、	预付金
    private String barName;//	酒吧名称
    private String orderId;//	订单id,兑奖订单id
    private int status;//	支付结果
    private Date pTime;//	支付时间,订单状态改变时间

    private String title;
    private String picUrl;
    private String url;
    private String description;
    private String guessId;//	竞猜主题id
    private String shopName;//	奖品名称
    private int multi;//	倍数

    private String guessName;//	主题名称
    private String prizeOption;//	中奖选项
    private String remark;//	备注

    private String bookId;//	预定记录id
    private Date bookTime;//	预定时间
    private String name;//	预定人名称
    private String phone;//	     预定人电话
    private List<String> barPhones;//	     酒吧电话
    private Date createTime;//	 变更时间

    private String targetUserId;
    private Date prizeTime;
    private String prizeCode;
    private String prizeName;
    private String activityId;
    private boolean isWinner;


    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public Date getPrizeTime() {
        return prizeTime;
    }

    public void setPrizeTime(Date prizeTime) {
        this.prizeTime = prizeTime;
    }

    public String getPrizeCode() {
        return prizeCode;
    }

    public void setPrizeCode(String prizeCode) {
        this.prizeCode = prizeCode;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getMyUserId() {
        return myUserId;
    }

    public void setMyUserId(String myUserId) {
        this.myUserId = myUserId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    public String getfUid() {
        return fUid;
    }

    public void setfUid(String fUid) {
        this.fUid = fUid;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGuessId() {
        return guessId;
    }

    public void setGuessId(String guessId) {
        this.guessId = guessId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getMulti() {
        return multi;
    }

    public void setMulti(int multi) {
        this.multi = multi;
    }

    public String getGuessName() {
        return guessName;
    }

    public void setGuessName(String guessName) {
        this.guessName = guessName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getBarId() {
        return barId;
    }

    public void setBarId(String barId) {
        this.barId = barId;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getBarName() {
        return barName;
    }

    public void setBarName(String barName) {
        this.barName = barName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getpTime() {
        return pTime;
    }

    public void setpTime(Date pTime) {
        this.pTime = pTime;
    }

    public String getPrizeOption() {
        return prizeOption;
    }

    public void setPrizeOption(String prizeOption) {
        this.prizeOption = prizeOption;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getReplyType() {
        return replyType;
    }

    public void setReplyType(int replyType) {
        this.replyType = replyType;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public long getTheMsgTime() {
        return theMsgTime;
    }

    public void setTheMsgTime(long theMsgTime) {
        this.theMsgTime = theMsgTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public Date getBookTime() {
        return bookTime;
    }

    public void setBookTime(Date bookTime) {
        this.bookTime = bookTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getBarPhones() {
        return barPhones;
    }

    public void setBarPhones(List<String> barPhones) {
        this.barPhones = barPhones;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public void setWinner(boolean isWinner) {
        this.isWinner = isWinner;
    }

    public String getPeriodId() {
        return periodId;
    }

    public void setPeriodId(String periodId) {
        this.periodId = periodId;
    }


}
