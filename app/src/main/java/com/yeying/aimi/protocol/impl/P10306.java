package com.yeying.aimi.protocol.impl;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;


/**
 * 视频上传
 */
public class P10306 extends BasicProtocol {
    public Req req;
    public Resp resp;

    public P10306() {
        super();
        req = new Req();
        req.transcode = "10306";
        resp = new Resp();
    }

    @Override
    public Arg[] serialize() {
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
        public String userId;//	用户id
        public String type;//	上传类型	0是上传图片 1 表示用户上传视频
        public String videoBase;//	字节码	<String>BASE64加密
        public String imgBase;//	字节码	<String>BASE64加密
        public int imgWidth;//	图片宽
        public int imgHeight;//	图片高
        public String message;//	留言内容
        public String barId;
        public Double locationX;
        public Double locationY;

    }

    public static class Resp extends BaseResp {
        public String userId;//	用户id
        public String video_url;//	视频url
        public String image_url;//	图片url
        public String message_id;

    }
}
