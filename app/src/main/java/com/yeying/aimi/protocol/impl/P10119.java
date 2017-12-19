package com.yeying.aimi.protocol.impl;


import com.alibaba.fastjson.JSON;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;


/**
 * 充值接口
 */
public class P10119 extends BasicProtocol {
    public Req req;
    public Resp resp;

    public P10119() {
        super();
        req = new Req();
        req.transcode = "10119";
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
        public String userId;
        public String shoppingId;
        public int payType;
    }

    public static class Resp extends BaseResp {
        public String userId;//	用户Id
        public int status;
        public String desc;
        public String payOrderId;
        public String payUrl;
    }
}
