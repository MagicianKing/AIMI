package com.yeying.aimi.protocol.impl;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.bean.TopUserBean;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;

import java.util.List;

/**
 * Created by tanchengkeji on 2017/10/23.
 */

public class P640000 extends BasicProtocol{

    public Req req;
    public Resp resp;

    public P640000() {
        super();

        req = new Req();
        req.transcode = "640000";
        resp = new Resp();
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
        public String barId;
    }

    public static class Resp extends BaseResp{
        public String season;
        public List<TopUserBean> topUserList;
    }
}
