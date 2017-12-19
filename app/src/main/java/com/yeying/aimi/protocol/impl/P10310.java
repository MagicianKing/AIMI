package com.yeying.aimi.protocol.impl;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;


/**
 * 意见反馈
 */
public class P10310 extends BasicProtocol {
    public Req req;
    public Resp resp;

    public P10310() {
        super();
        req = new Req();
        req.transcode = "10310";
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
        public String feedback_title;//	反馈标题
        public String feedback_content;//	内容
        public String email;//	邮箱


    }

    public static class Resp extends BaseResp {
        public String userId;//	用户Id

    }
}
