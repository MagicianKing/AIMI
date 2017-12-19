package com.yeying.aimi.protocol.impl;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;

/**
 * Created by tanchengkeji on 2017/11/7.
 */

public class P11001 extends BasicProtocol {

    public Req req;
    public Resp resp;

    public P11001() {
        req = new Req();
        req.transcode = "11001";
        resp = new Resp();
    }

    @Override
    public Arg[] serialize() {
        return JSONUtil.getReq(JSON.toJSONString(req),req.transcode,req.sessionId);
    }

    @Override
    public void unSerialize(Result result) {
        // TODO Auto-generated method stub
        int index=result.content.indexOf("&");
        String trascodeid = result.content.substring(0, index);
        String content=result.content.substring(index+1);
        resp = JSON.parseObject(content, resp.getClass());
        resp.setTranscode(trascodeid);
    }

    public static class Req extends BaseReq{
        public String thirdId;//三方登录openID
        public int thirdType;//0 微信 1 qq 2 微博
    }

    public static class Resp extends BaseResp{
        public int register;//0 未绑定 1 绑定
        public int type;//0 微信 1 qq 2 微博
        public String userId;//用户ID
        public String phone;
        public String nickname;
        public String headImg;
        public int gender;
        public String sessionId;
        public String scretKey;
        public int dataNeedComplete;//0 否 1 是
        public String imgUrl;//图片前缀

    }
}
