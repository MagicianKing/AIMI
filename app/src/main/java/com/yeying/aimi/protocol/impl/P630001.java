package com.yeying.aimi.protocol.impl;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.bean.BarScreenBean;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;

import java.util.List;

/**
 * Created by king .
 * 公司:业英众娱
 * 2017/9/27 下午1:38
 * 管理员霸屏预览
 */

public class P630001 extends BasicProtocol {
    public Req req;
    public Resp resp;

    public P630001() {
        super();

        req = new Req();
        req.transcode = "630001";
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
        public String userId;
        public String barId;



    }

    public static class Resp extends BaseResp {

        /**
         * imgUrl : http://47.94.164.180:10032/hi8_files/
         * soList : [{"barId":"1604222030151000013","content":"","continueTime":40,"createTime":"2017-09-27 14:17:40","headImg":"/headimg/96/170927120229463909620170927120644.png","money":0,"nickName":"哦哦哦","picUrl":"/screen/96/170927120229463909620170927141740.png","playStatus":0,"screenOptionId":"1612212053151000002","screenOrderId":"1709271417408849006","status":3,"userId":"1709271202294639096"},{"barId":"1604222030151000013","content":"","continueTime":40,"createTime":"2017-09-27 14:18:27","headImg":"/headimg/96/170927120229463909620170927120644.png","money":0,"nickName":"哦哦哦","picUrl":"/screen/96/170927120229463909620170927141827.png","playStatus":0,"screenOptionId":"1612212053151000002","screenOrderId":"1709271418270309009","status":3,"userId":"1709271202294639096"}]
         */

        public String imgUrl;
        /**
         * barId : 1604222030151000013
         * content :
         * continueTime : 40
         * createTime : 2017-09-27 14:17:40
         * headImg : /headimg/96/170927120229463909620170927120644.png
         * money : 0
         * nickName : 哦哦哦
         * picUrl : /screen/96/170927120229463909620170927141740.png
         * playStatus : 0
         * screenOptionId : 1612212053151000002
         * screenOrderId : 1709271417408849006
         * status : 3
         * userId : 1709271202294639096
         */

        public List<BarScreenBean> soList;


    }
}
