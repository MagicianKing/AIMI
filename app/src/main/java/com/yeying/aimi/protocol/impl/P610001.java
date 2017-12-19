package com.yeying.aimi.protocol.impl;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.bean.YeMaoBean;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;

import java.util.List;

/**
 * Created by tanchengkeji on 2017/9/19.
 */

public class P610001 extends BasicProtocol {
    public static class Req extends BaseReq {
        public String userId;//	用户id
        public String barId;//	酒吧id
        public String locationY;
        public String locationX;
    }

    public static class Resp extends BaseResp {
        public String barId;
        public String imgUrl;
        public List<YeMaoBean> YeMaoList;
        public int flag;//int 1在酒吧内 0不在就把范围内D
    }

    public Req req;
    public Resp resp;

    public P610001(){
        super();
        req=new Req();
        req.transcode="610001";
        resp=new  Resp();
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
}
