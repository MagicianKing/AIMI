package com.yeying.aimi.protocol.impl;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;

import java.util.Date;

/**
 * Created by king .
 * 公司:业英众娱
 * 2017/9/25 上午11:33
 * 编辑资料
 */

public class P620005 extends BasicProtocol {
    public Req req;
    public Resp resp;

    public P620005() {
        super();

        req = new Req();
        req.transcode = "620005";
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
    }

    public static class Resp extends BaseResp {
        public String headImg;    //头像地址后缀
        public String imgUrl;    //图片地址前缀
        public String userName;    //用户名
        public   Date birthday;    //生日
        public String constellation;    //星座
        public   int gender;    //性别	 1.男 2.女
        public   String autograph;    //签名
    }
}
