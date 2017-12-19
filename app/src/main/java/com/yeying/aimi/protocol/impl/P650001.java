package com.yeying.aimi.protocol.impl;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;

/**
 * Created by tanchengkeji on 2017/10/25.
 */

public class P650001 extends BasicProtocol {

    public Req req;
    public Resp resp;

    public P650001() {
        super();
        req = new Req();
        resp = new Resp();
        req.transcode = "650001";
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

    public static class Req extends BaseResp{
        public String userId;
        public String barId;
    }

    public static class Resp extends BaseResp{
        public int status;//0 失败 1 成功
        public String userRole;//游戏角色名称
        public String uuid;//游戏ID
    }
}
