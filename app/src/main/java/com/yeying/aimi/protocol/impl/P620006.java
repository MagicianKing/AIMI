package com.yeying.aimi.protocol.impl;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.bean.FansBean;
import com.yeying.aimi.bean.FollowsBean;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;

import java.util.List;

/**
 * Created by tanchengkeji on 2017/9/22.
 */

public class P620006 extends BasicProtocol {

    public static class Req extends BaseReq{
        public String searchUserId;
    }

    public static class Resp extends BaseResp{
        public String imgUrl;//前缀
        public List<FollowsBean> followList;
        public List<FansBean> fansList;
    }

    public Req req;
    public Resp resp;

    public P620006() {
        req = new Req();
        req.transcode="620006";
        resp = new Resp();
    }

    @Override
    public Arg[] serialize() {
        return JSONUtil.getReq(JSON.toJSONString(req),req.transcode,req.sessionId);
    }

    @Override
    public void unSerialize(Result result) {
        int index=result.content.indexOf("&");
        String trascodeid = result.content.substring(0, index);
        String content=result.content.substring(index+1);

        resp = JSON.parseObject(content, resp.getClass());
        resp.setTranscode(trascodeid);
    }
}
