package com.yeying.aimi.protocol.impl;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;

/**
 * Created by king .
 * 公司:业英众娱
 * 2017/11/8 下午5:23
 */

public class P630101 extends BasicProtocol {

    public P630101.Req req;
    public P630101.Resp resp;

    public P630101() {
        super();

        req = new P630101.Req();
        req.transcode = "630101";
        resp = new P630101.Resp();
    }

    @Override
    public Arg[] serialize() {
        return JSONUtil.getReq(JSON.toJSONString(req), req.transcode, req.sessionId);
    }

    @Override
    public void unSerialize(Result result) {
        int index = result.content.indexOf("&");
        String trascodeid = result.content.substring(0, index);
        String content = result.content.substring(index + 1);
        resp = JSON.parseObject(content, resp.getClass());
        resp.setTranscode(trascodeid);
    }


    public static class Req extends BaseReq{
            public  String appType;
            public String version;
    }

    public static class Resp extends BaseResp{

        /**
         * appType : ios
         * version : 1.0.1
         * forced : 0
         * url : http://miw.com/apk
         * appSize : 20M
         * desc : 更新内容描述
         */

        public String appType;
        public String version;
        public String forced;
        public String url;
        public String appSize;
        public String desc;

    }
}
