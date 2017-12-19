package com.yeying.aimi.protocol.impl;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;

/**
 * 点赞接口
 */
public class P10141 extends BasicProtocol {
    public Req req;
    public Resp resp;

    public P10141() {
        super();

        req = new Req();

        req.transcode = "10141";
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
        public String userId;//	点赞人id	String
        public int type;//	类型	1.对相册点赞 2.对人点赞
        public String messageId;//	相册id	若type为2，则为空
        public String toUserId;//	受赞人id	若type为1则为空
        public String barId;
        public int likeType;//	点赞/取消赞	Int 1 点赞 0 取消赞
        public String transcode;


    }

    public static class Resp extends BaseResp {
        public int state;//	状态	Int   1.成功 其他失败
        public String userId;//	用户id	String

    }
}
