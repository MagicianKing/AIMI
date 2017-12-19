package com.yeying.aimi.protocol.impl;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;

/**
 * Created by king .
 * 公司:业英众娱
 * 2017/9/21 下午7:00
 * 足迹隐藏和显示
 */

public class P620007 extends BasicProtocol {
    public Req req;
    public Resp resp;

    public P620007() {
        super();

        req = new Req();
        req.transcode = "620007";
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
        public int isSeefootmark;//1 true/2 false


    }

    public static class Resp extends BaseResp {
        public int status;//1 设置成功
    }
}

