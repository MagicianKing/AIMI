package com.yeying.aimi.protocol.impl;


import com.alibaba.fastjson.JSON;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;

/**
 * Created by king .
 * 公司:业英众娱
 * 2017/9/20 下午4:20
 */

public class P10103 extends BasicProtocol {

    public String string;
    public Req req;
    public Resp resp;


    public P10103() {
        super();

        req = new Req();

        req.transcode = "10103";
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
        int index = result.content.indexOf("&");
        String trascodeid = result.content.substring(0, index);
        String content = result.content.substring(index + 1);
        resp = JSON.parseObject(content, resp.getClass());
        resp.setTranscode(trascodeid);


//		JSONUtil.binding(resp.header, "header", result);
//		JSONUtil.binding(resp, null, result);
    }

    public static class Req extends BaseReq {
       public String  userId;
        public String headImg;
    }

    public static class Resp extends BaseResp {
        //public RespHeader content;
        public String headImg;
        public String headUrl;
        public String imgUrl;

    }


}

