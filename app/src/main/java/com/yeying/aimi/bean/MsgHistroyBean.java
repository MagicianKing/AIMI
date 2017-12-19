package com.yeying.aimi.bean;

/**
 * 历史信息实体类
 */
public class MsgHistroyBean {
    private String myUserId;//我的userid，用于区分不同登录
    private boolean isRead;//是否已读
    private long theMsgTime;//环信消息时间
    private String para;

    private String from;//群聊信息的发送者昵称
    private String userId;
    private String userName;
    private String content;
    private int chatType; //0单聊、1群聊
    private int type; //0文字、1图片、2语音
    private String headImg;
    private int unreadCount;
    private String msgTime;
    private boolean isSystemMsg;
    private String msgId;//消息id
    private String recordId;//招呼id
    private String chat;//招呼消息文字内容
    private int score;//赠送的瓶盖数
    private String ratio;//回复消息比例
    private int zhaohuType;
    private MsgGiftBean giftbean;//礼物信息

    public int getZhaohuType() {
        return zhaohuType;
    }

    public void setZhaohuType(int zhaohuType) {
        this.zhaohuType = zhaohuType;
    }


    public MsgGiftBean getGiftbean() {
        return giftbean;
    }

    public void setGiftbean(MsgGiftBean giftbean) {
        this.giftbean = giftbean;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public boolean isSystemMsg() {
        return isSystemMsg;
    }

    public void setSystemMsg(boolean isSystemMsg) {
        this.isSystemMsg = isSystemMsg;
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

    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
    }

    public String getMyUserId() {
        return myUserId;
    }

    public void setMyUserId(String myUserId) {
        this.myUserId = myUserId;
    }
}
