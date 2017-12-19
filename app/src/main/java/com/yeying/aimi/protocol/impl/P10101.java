package com.yeying.aimi.protocol.impl;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.bean.BarInfoBean;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;


public class P10101 extends BasicProtocol {
    public String string;
    public Req req;
    public Resp resp;


    public P10101() {
        super();

        req = new Req();

        req.transcode = "10101";
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
        public String phone;
        public String smsCode;
        public String pwd;
        public String city;
        public String area;
        public double locationY;
        public double locationX;
    }

    public static class Resp extends BaseResp {
        public String userId;
        public String phone;
        public String nickname;
        public String headImg;
        public String sessionId;
        public String type;
        public int gender;
        public String scretKey;
        public BarInfoBean barInfo;
        public int dataNeedComplete;
        public String card_no;//	支付宝账号
        public String real_name;//	真实姓名
    }

    public static class ReqData {
        public String phone;
    }
}
