package com.yeying.aimi.protocol.impl;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;

/**
 * 发红包
 */
public class P10220 extends BasicProtocol {
    public Req req;
    public Resp resp;

    public P10220() {
        super();
        req = new Req();
        req.transcode = "10220";
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
        public String groupId;//	群组id	可以为空
        public String red_pack_type;//	类型	0群 1个人
        public String total_count;//	总个数
        public int per_count;//	单个积分	适用于普通红包
        public String score_count;//	总积分	适用于拼手气&个人红包
        public String type;//	红包类型	0拼手气 1普通红包
        public int limit;//	限制	0 全部 1男  2女
        public String remark;//	红包备注
        public String send_userId;//	用户ID	个人红包适用
        public String barId;
        public int redpack_choose;
    }

    public static class Resp extends BaseResp {
        public int state;//	状态	1成功 2.余额不足 3.失败
        public String red_pack_id;//	红包id	String

    }
}

