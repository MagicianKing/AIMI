package com.yeying.aimi.protocol.impl;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.bean.TotalBean;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;

import java.util.List;

public class P20116 extends BasicProtocol {


    public Req req;
    public Resp resp;

    public P20116() {
        super();

        req = new Req();

        req.transcode = "20116";
        resp = new Resp();

    }

    @Override
    public Arg[] serialize() {
        // TODO Auto-generated method stub
        //Log.e("sessionid", req.sessionId);
        return JSONUtil.getReq(JSON.toJSONString(req), req.transcode, req.sessionId);
    }

    @Override
    public void unSerialize(Result result) {
        // TODO Auto-generated method stub
        int index = result.content.indexOf("&");
        String trascodeid = result.content.substring(0, index);
        String content = result.content.substring(index + 1);
        Log.e("这是json", content);
        resp = JSON.parseObject(content, resp.getClass());
        resp.setTranscode(trascodeid);


    }

    public static class Req extends BaseReq {

        public String userId;
        public String locationY;
        public String locationX;
        public int sex;
        public int area;
        public int pageNum;
        public int pageSize;

    }

    public static class Resp extends BaseResp {

        public String imgUrl;
        //public List<E>
        public List<TotalBean> words;


    }

}
