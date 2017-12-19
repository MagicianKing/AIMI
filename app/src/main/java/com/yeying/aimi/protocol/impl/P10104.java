package com.yeying.aimi.protocol.impl;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;


public class P10104 extends BasicProtocol {

    public String string;
    public Req req;
    public Resp resp;


    public P10104() {
        super();

        req = new Req();

        req.transcode = "10104";
        resp = new Resp();

    }

    @Override
    public Arg[] serialize() {
        // TODO Auto-generated method stub
        return JSONUtil.getReq(JSON.toJSONString(req), req.transcode, req.sessionId);
        //加密
//		try {
//			return JSONUtil.getRe(JSON.toJSONString(req),req.transcode,req.sessionId);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}

    }

    @Override
    public void unSerialize(Result result) {
        // TODO Auto-generated method stub
        resp.result = result.content;
        int index = result.content.indexOf("&");
        String trascodeid = result.content.substring(0, index);
        String content = result.content.substring(index + 1);
        resp = JSON.parseObject(content, resp.getClass());
        resp.setTranscode(trascodeid);


//		JSONUtil.binding(resp.header, "header", result);
//		JSONUtil.binding(resp, null, result);
    }

    public static class Req extends BaseReq {
        public String phone;

    }

    public static class Resp extends BaseResp {
        //public RespHeader content;
        public String smsCode;
        public String result;
    }

    public static class ReqData {
        public String phone;
    }
}
