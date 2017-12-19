package com.yeying.aimi.protocol.impl;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;


/**
 * 领红包
 */
public class P10221 extends BasicProtocol {
    public Req req;
    public Resp resp;

    public P10221() {
        super();
        req = new Req();
        req.transcode = "10221";
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
    //请求参数
    public static class Req extends BaseReq {
        public String userId;//	用户id
        public String red_pack_id;//	红包id
        public String message_id;
    }
    //回传结果
    public static class Resp extends BaseResp {
        public int state;//	状态	1成功 0失败 -1红包领完了 -2 已经失效 -3 性别不符
        public String red_pack_id;//	红包id
        public String score;//	积分	Int
    }
}

