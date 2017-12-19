package com.yeying.aimi.protocol.impl;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;

/**
 * Created by tanchengkeji on 2017/10/25.
 */

public class P650000 extends BasicProtocol {

    public Req req;
    public Resp resp;

    public P650000() {
        super();
        req = new Req();
        resp = new Resp();
        req.transcode = "650000";
    }

    @Override
    public Arg[] serialize() {
        return JSONUtil.getReq(JSON.toJSONString(req), req.transcode, req.sessionId);
    }

    @Override
    public void unSerialize(Result result) {
        int index = result.content.indexOf("&");
        String trascodeid = result.content.substring(0, index);
        String content = result.content.substring(index + 1);
        resp = JSON.parseObject(content, resp.getClass());
        resp.setTranscode(trascodeid);
    }

    public static class Req extends BaseReq{
        public String userId;
        public String barId;
    }

    public static class Resp extends BaseResp{
        public int status;//0 未开始 1 报名中 2游戏中
        public int userScore;//用户余额
        public int gameScore;
    }
}
