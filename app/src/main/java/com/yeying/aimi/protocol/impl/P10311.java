package com.yeying.aimi.protocol.impl;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;


/**
 * 关于我们
 */
public class P10311 extends BasicProtocol {
    public Req req;
    public Resp resp;

    public P10311() {
        super();
        req = new Req();
        req.transcode = "10311";
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
        resp.setTranscode(trascodeid);
    }

    public static class Req extends BaseReq {
        public String userId;//	用户id


    }

    public static class Resp extends BaseResp {
        public String content;//	用户Id
        public String phone;//	手机
        public String email;//	邮箱
        public String qq;
    }
}
