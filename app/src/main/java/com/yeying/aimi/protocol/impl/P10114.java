package com.yeying.aimi.protocol.impl;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.bean.MyBean;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;

import java.util.Date;
import java.util.List;

public class P10114 extends BasicProtocol {
    public String string;
    public Req req;
    public Resp resp;
    public P10114() {
        super();

        req = new Req();

        req.transcode = "10114";
        resp = new Resp();

    }

    @Override
    public Arg[] serialize() {
        // TODO Auto-generated method stub
//		JSONObject packet = null;
//		try {
//			packet = JSONUtil.getJson(req);
////			if (packet != null)
////				packet.put("header", JSONUtil.getJson(header));
//		} catch (JSONException e) {
//
//			e.printStackTrace();
//		}

        return JSONUtil.getReq(JSON.toJSONString(req), req.transcode, req.sessionId);
    }

    @Override
    public void unSerialize(Result result) {
        // TODO Auto-generated method stub
        int index = result.content.indexOf("&");
        String trascodeid = result.content.substring(0, index);
        String content = result.content.substring(index + 1);


        resp = JSON.parseObject(content, resp.getClass());

        resp.setTranscode(trascodeid);
        Log.e("我的解析", content);

//		JSONUtil.binding(resp.header, "header", result);
//		JSONUtil.binding(resp, null, result);
    }

    public static class Req extends BaseReq {
        public String searchUserId;
        public String userId;
        //xinjia
        public String sessionId;
        public String locationX;
        public String locationY;
        public int pageNum;
        public int pageSize;
        public String transcode;


    }

    public static class Resp extends BaseResp {
        //public RespHeader content;
        public String userId;
        public String phone;
        public String nickName;
        public String headUrl;
        public String score;
        public String money;
        public String gender;
        public String autograph;//	签名//
        public String region;//	地区
        public Date birthday;//	生日//
        public int privacy;    //隐私 0所有人 1相互关注
        public int isAttention;//	0是未关注1关注2 被关注3互相关注
        public String replyRatio;//	回复率
        public int calledNumber;//	打招呼数
        public int gift_num;//	收礼物数
        public int like_num;//	点赞数
        public String location_x;//	经度
        public String location_y;//	纬度
        public String locationTime;
        public int followNum;//	关注数
        public int fansNum;//	粉丝数
        public int sendGiftnum;//	送礼物数
        public List<MyBean> words;
        public String imgUrl;
        public String distance;
        public int age;
        public String constellation;

        //xinjia


    }
}
