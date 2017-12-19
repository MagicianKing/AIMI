package com.yeying.aimi.bean;

import android.media.MediaPlayer;
import android.view.Surface;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户动态实体类
 */
@SuppressWarnings("serial")
public class DongTaiBean implements Serializable {
    private String wordUserId;//	用户id
    private String wordUserName;//	用户名
    private int wordUserAge; //用户年龄
    private String wordUserConstellation; //用户星座
    private int wordUserGender; //用户性别
    private String headImg;//	用户头像
    private String message_id;//	相册id
    private String message;//	内容
    private Date createTime;//	创建时间	date
    private int replyNumber;//	回复数	Int
    private int likeNum;//	点赞数	Int
    private int userLikeNum;//	用户对相册赞数	0表示未点赞，1表示已点过赞
    private String barId;//	酒吧id
    private String barName;//	酒吧名
    private List<PiculBean> pic_urls;//	图片地址列表
    private List<ReplyBean> replys;//	回复列表
    private boolean isclick = false;
    private List<String> likeNames;//	点赞人昵称
    private int wordsType;    //动态类型	0图片1是视频
    private boolean isVideoPlay;//是否正在播放视频
    private String videoPath;//视频网络地址
    private MediaPlayer mediaPlayer;
    private Surface surface;
    private int index;
    private String messageId;
    private boolean isReadNewMsg;
    private int likeStatus;
    private String userId;
    private int isAttention;
    private String distance;

    public void setIsAttention(int isAttention){
        this.isAttention = isAttention;
    }

    public int getIsAttention(){
        return this.isAttention;
    }
    private List<LikeUsersBean> likeUsers;

    public int getWordUserGender(){
        return wordUserGender;
    }

    public void setWordUserGender(int wordUserGender){
        this.wordUserGender = wordUserGender;
    }

    public String getWordUserConstellation(){
        return wordUserConstellation;
    }

    public void setWordUserConstellation(String wordUserConstellation){
        this.wordUserConstellation = wordUserConstellation;
    }

    public void setWordUserAge (int wordUserAge){
        this.wordUserAge = wordUserAge;
    }

    public int getWordUserAge(){
        return wordUserAge;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(int likeStatus) {
        this.likeStatus = likeStatus;
    }

    public List<LikeUsersBean> getLikeUsers() {
        return likeUsers;
    }

    public void setLikeUsers(List<LikeUsersBean> likeUsers) {
        this.likeUsers = likeUsers;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Surface getSurface() {
        return surface;
    }

    public void setSurface(Surface surface) {
        this.surface = surface;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public boolean isVideoPlay() {
        return isVideoPlay;
    }

    public void setVideoPlay(boolean isVideoPlay) {
        this.isVideoPlay = isVideoPlay;
    }

    public int getWordsType() {
        return wordsType;
    }

    public void setWordsType(int wordsType) {
        this.wordsType = wordsType;
    }

    public List<String> getLikeNames() {
        return likeNames;
    }

    public void setLikeNames(List<String> likeNames) {
        this.likeNames = likeNames;
    }

    public String getWordUserId() {
        return wordUserId;
    }

    public void setWordUserId(String wordUserId) {
        this.wordUserId = wordUserId;
    }

    public String getWordUserName() {
        return wordUserName;
    }

    public void setWordUserName(String wordUserName) {
        this.wordUserName = wordUserName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getReplyNumber() {
        return replyNumber;
    }

    public void setReplyNumber(int replyNumber) {
        this.replyNumber = replyNumber;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public int getUserLikeNum() {
        return userLikeNum;
    }

    public void setUserLikeNum(int userLikeNum) {
        this.userLikeNum = userLikeNum;
    }

    public boolean isReadNewMsg() {
        return isReadNewMsg;
    }

    public void setReadNewMsg(boolean isReadNewMsg) {
        this.isReadNewMsg = isReadNewMsg;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<PiculBean> getPic_urls() {
        return pic_urls;
    }

    public void setPic_urls(List<PiculBean> pic_urls) {
        this.pic_urls = pic_urls;
    }

    public List<ReplyBean> getReplys() {
        return replys;
    }

    public void setReplys(List<ReplyBean> replys) {
        this.replys = replys;
    }

    public String getBarId() {
        return barId;
    }

    public void setBarId(String barId) {
        this.barId = barId;
    }

    public String getBarName() {
        return barName;
    }

    public void setBarName(String barName) {
        this.barName = barName;
    }

    public boolean isIsclick() {
        return isclick;
    }

    public void setIsclick(boolean isclick) {
        this.isclick = isclick;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
