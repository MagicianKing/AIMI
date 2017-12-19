package com.yeying.aimi.protocol.impl;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;

/**
 * 群组上传图片
 **/
public class P10208 extends BasicProtocol {
    public Req req;
    public Resp resp;

    public P10208() {
        super();

        req = new Req();

        req.transcode = "10208";
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
        public String userId;//	用户id	<String>
        public String imgCode;//	图片字节码	<String>BASE64加密
        public String groupId;//	群组id	可以为空
        public String type;//	类型	1是群组缩略图2是群的墙图3是相册图
    }

    public static class Resp extends BaseResp {
        public String pic_url;
        public int pic_width;
        public int pic_height;
    }
}
