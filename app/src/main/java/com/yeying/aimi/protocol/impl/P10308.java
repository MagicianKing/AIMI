package com.yeying.aimi.protocol.impl;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.bean.FootMarkBean;
import com.yeying.aimi.bean.MineWordsBean;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;

import java.util.List;


public class P10308 extends BasicProtocol {
    public Req req;
    public Resp resp;

    public P10308() {
        super();

        req = new Req();

        req.transcode = "10308";
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
        public String searchUserId;
        public double locationX;
        public double locationY;
    }

    public static class Resp extends BaseResp {
        public String userId;
        public String userName;
        public String imgUrl;//前缀
        public String headUrl;//后缀
        public int followNum;
        public int fansNum;
        public List<MineWordsBean> words;
        public List<FootMarkBean> footmark;
        public int isFollow;
        public int isSeefootmark;//int 1.是 2.否

    }
}
