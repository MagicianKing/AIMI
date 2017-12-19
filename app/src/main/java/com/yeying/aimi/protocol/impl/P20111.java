package com.yeying.aimi.protocol.impl;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.bean.NewBarListBean;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;

import java.util.List;

public class P20111 extends BasicProtocol {
    public Req req;
    public Resp resp;


    public P20111() {
        super();

        req = new Req();

        req.transcode = "20111";
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
        /*int i=0,j=0;
		for(i =0 ;i<content.length();i+=100){
			System.out.println(content.substring(i,i+100));
			j=i;
		}
		System.out.println(content.substring(j));
		 Log.e("你爹我的请求结果",content);
		*/

        resp = JSON.parseObject(content, resp.getClass());

        resp.setTranscode(trascodeid);


//		JSONUtil.binding(resp.header, "header", result);
//		JSONUtil.binding(resp, null, result);
    }

    public static class Req extends BaseReq {
        public String locationX;
        public String locationY;


    }

    public static class Resp extends BaseResp {
        public List<NewBarListBean> barList;
        public String imgUrl;

    }
}
