package com.yeying.aimi.protocol.impl;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.bean.ChargeListBean;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;

import java.util.List;

/**
 * 充值兑换列表
 */
public class P10323 extends BasicProtocol {
    public Req req;
    public Resp resp;

    public P10323() {
        super();
        req = new Req();
        req.transcode = "10323";
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
        resp.content = content;
    }

    public static class Req extends BaseReq {
        public String userId;
    }

    public static class Resp extends BaseResp {
        public List<ChargeListBean> chargeList;//	充值列表
        public String content;
    }
}

