package com.yeying.aimi.bean;

public class ChatBean {
    private int superType;//0普通聊天消息  1系统消息  2招呼消息  3 访客消息  4动态消息
    private long theMsgTime;//环信消息时间
    private String name;
    private String image;
    private String text;
    private String time;
    private String userId;//昵称
    private int chatType;
    private int type;
    private int num;
    private String letters; //分组用
    private boolean isSystemMsg;//是否是系统消息
    private boolean isMyGroup;//是否是我的群组
    private String myGroupId;//我的群组id
    private String myGroupName;//我的群组名字
    private int isType;// 当前用户与群的关系	0是群成员1是群主2是群管理员3不是群成员
    private String msgId;//消息id
    private String recordId;//招呼id
    private String chat;//招呼消息文字内容
    private int score;//赠送的瓶盖数
    private String ratio;//回复消息比例
    private int zhaohuType;//
    private MsgGiftBean giftbean;//礼物信息
    private boolean isCheck;//是否选中
    private boolean isTop;//是否置顶
    private boolean isMianDaRao;//是否消息免打扰
    private String from;//群聊消息发送者的昵称

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

    public String getLetters() {
        return letters;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean isTop) {
        this.isTop = isTop;
    }

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

    public boolean isSystemMsg() {
        return isSystemMsg;
    }

    public void setSystemMsg(boolean isSystemMsg) {
        this.isSystemMsg = isSystemMsg;
    }

    public boolean isMyGroup() {
        return isMyGroup;
    }

    public void setMyGroup(boolean isMyGroup) {
        this.isMyGroup = isMyGroup;
    }

    public String getMyGroupId() {
        return myGroupId;
    }

    public void setMyGroupId(String myGroupId) {
        this.myGroupId = myGroupId;
    }

    public String getMyGroupName() {
        return myGroupName;
    }

    public void setMyGroupName(String myGroupName) {
        this.myGroupName = myGroupName;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public int getIsType() {
        return isType;
    }

    public void setIsType(int isType) {
        this.isType = isType;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public boolean isMianDaRao() {
        return isMianDaRao;
    }

    public void setMianDaRao(boolean isMianDaRao) {
        this.isMianDaRao = isMianDaRao;
    }

    public int getSuperType() {
        return superType;
    }

    public void setSuperType(int superType) {
        this.superType = superType;
    }

    public long getTheMsgTime() {
        return theMsgTime;
    }

    public void setTheMsgTime(long theMsgTime) {
        this.theMsgTime = theMsgTime;
    }
}
