package com.yeying.aimi.protocol.impl;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;

public class P10129 extends BasicProtocol {
    public Req req;
    public Resp resp;


    public P10129() {
        super();

        req = new Req();

        req.transcode = "10129";
        resp = new Resp();

    }

    @Override
    public Arg[] serialize() {
        // TODO Auto-generated method stub
//		JSONObject packet = null;
//		try {
//			packet = JSONUtil.getJson(req);
////			if (packet != null)
////				packet.put("header", JSONUtil.getJson(header));
//		} catch (JSONException e) {
//
//			e.printStackTrace();
//		}
        return JSONUtil.getReq(JSON.toJSONString(req), req.transcode, req.sessionId);
    }

    @Override
    public void unSerialize(Result result) {
        // TODO Auto-generated method stub
        int index = result.content.indexOf("&");
        String trascodeid = result.content.substring(0, index);
        String content = result.content.substring(index + 1);
        Log.e("我的的慧点慧点都好都好得哈哈", content + "============");

        resp = JSON.parseObject(content, resp.getClass());

        resp.setTranscode(trascodeid);


//		JSONUtil.binding(resp.header, "header", result);
//		JSONUtil.binding(resp, null, result);
    }

    public static class Req extends BaseReq {
        public String userId;
        public String message_id;
    }

    public static class Resp extends BaseResp {
        public String status;
    }
}

