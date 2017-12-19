package com.yeying.aimi.protocol.impl;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.bean.DongTaiBean;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;


/**
 * 动态详情
 */
public class P10325 extends BasicProtocol {
    public Req req;
    public Resp resp;

    public P10325() {
        super();
        req = new Req();
        req.transcode = "10325";
        resp = new Resp();
    }

    @Override
    public Arg[] serialize() {
        return JSONUtil.getReq(JSON.toJSONString(req), req.transcode, req.sessionId);
    }

    @Override
    public void unSerialize(Result result) {
        // TODO Auto-generated method stub
        int index = result.content.indexOf("&");
        String trascodeid = result.content.substring(0, index);
        String content = result.content.substring(index + 1);
        resp = JSON.parseObject(content, resp.getClass());
        Log.e("kankan", content);
        resp.setTranscode(trascodeid);
    }

    public static class Req extends BaseReq {


        public String userId;//	接收用户id
        public String wordId;//	动态id
        public String targetUserId;//看谁的动态
        public String transcode;
        public String messageId;
        public double locationX;
        public double locationY;
        public int pageNum;
        public int pageSize;

    }

    public static class Resp extends BaseResp {
        public String imgUrl;//	图片的前缀
        public DongTaiBean word;//	动态数据
        public int isAttention;//	0是未关注1关注2 被关注3互相关注


    }
}

