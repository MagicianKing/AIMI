package com.yeying.aimi.protocol.impl;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;


/**
 * 酒友关系
 */
public class P10150 extends BasicProtocol {
    public Req req;
    public Resp resp;


    public P10150() {
        super();

        req = new Req();

        req.transcode = "10150";
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
        Log.e("这个是加关注", content);

        resp = JSON.parseObject(content, resp.getClass());

        resp.setTranscode(trascodeid);


//		JSONUtil.binding(resp.header, "header", result);
//		JSONUtil.binding(resp, null, result);
    }

    public static class Req extends BaseReq {
        public String userId;
        public String friendId;
        public int type;    //	1添加酒友2删除酒友
        public String barId;
        //新加
        public String sessionId;
        public String transcode;


    }

    public static class Resp extends BaseResp {
        public String userId;
        public int state;    //	添加状态  1成功
        //新加
        public int isAttention;

    }


}
