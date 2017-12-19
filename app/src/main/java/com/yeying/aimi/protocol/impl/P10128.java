package com.yeying.aimi.protocol.impl;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;


/**
 * 用户相册留言
 */
public class P10128 extends BasicProtocol {
    public Req req;
    public Resp resp;

    public P10128() {
        super();

        req = new Req();

        req.transcode = "10128";
        resp = new Resp();

    }

    @Override
    public Arg[] serialize() {
        // TODO Auto-generated method stub
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
        public String message_id;//	相册id
        public String user_name;//	留言用户名
        public String reply_user_id;//	回复用户id
        public String reply_user_name;//	回复用户名
        public String content;//	留言内容
        public String barId;
        public String messageId;

    }

    public static class Resp extends BaseResp {
        public String comment_id;//	留言id	若失败返回 ” ”。

    }
}
