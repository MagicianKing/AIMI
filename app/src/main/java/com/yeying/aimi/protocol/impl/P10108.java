package com.yeying.aimi.protocol.impl;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;

/**
 * Created by tanchengkeji on 2017/11/8.
 */

public class P10108 extends BasicProtocol {

    public Req req;
    public Resp resp;

    public P10108() {
        req = new Req();
        req.transcode = "10108";
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

    public static class Req extends BaseReq{
        /**
         * 第三方登录类型(0:微信、1:QQ、2:微博)
         */
        public int thirdType;
        /**
         * 手机号
         */
        public String phone;
        /**
         * 验证码
         */
        public String smsCode;
        /**
         * 密码
         */
        public String pwd;

        /**
         * 第三方授权的openid
         */
        public String openid;
        /**
         * 昵称
         */
        public String nickName;
        /**
         * 性别
         */
        public String sex;

        /**
         * 头像
         */
        public String headImgUrl;
    }

    public static class Resp extends BaseResp{
        public int register;//0 未绑定 1 绑定
        public int type;//0 微信 1 qq 2 微博
        public String userId;//用户ID
        public String phone;
        public String nickname;
        public String headImg;
        public int gender;
        public String sessionId;
        public String scretKey;
        public int dataNeedComplete;//0 否 1 是
        public String imgUrl;//图片前缀
    }
}
