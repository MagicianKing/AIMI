package com.yeying.aimi.protocol.impl;

import com.alibaba.fastjson.JSON;
import com.yeying.aimi.bean.ActiveList;
import com.yeying.aimi.bean.BarPhoneList;
import com.yeying.aimi.bean.BarUserPicList;
import com.yeying.aimi.bean.UserPermissionBean;
import com.yeying.aimi.bean.YeMaoList;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by king .
 * 公司:业英众娱
 * 2017/9/18 上午11:54
 */

public class P10112 extends BasicProtocol {
    public String string;
    public Req req;
    public Resp resp;


    public P10112() {
        super();

        req = new Req();

        req.transcode = "10112";
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
        public double locationY;
        public double locationX;
    }

    public static class Resp extends BaseResp implements Serializable{
        public String area;
        public String address;
        public int flag;
        public String bar_desk;
        public double distance;
        public String city;
        public double location_x;
        public String transcode;
        public double location_y;
        public String barName;
        public String imgUrl;

        public String chatGroupId;
        public String picUrl;
        /**
         * appVer :
         * ip :
         * transcode : 10112
         * source :
         * terminalid :
         * time : 2017-09-18 15:27:43
         * userId :
         * token :
         */

        public String barId;
        public int status;
        /**
         * bar_id : 20151019142927209
         * create_time : 2016-01-26 10:43:35
         * phone : 119
         * bar_phone_id : 3
         */

        public List<BarPhoneList> phoneList;
        /**
         * gender : 1
         * headImg : /headimg/66/170829163608527906620170829163632.png
         * rankingScore : 0
         * isStaff : 0
         * nickName : hh有的
         * visitNumber : 0
         * userHistoryId :
         * userId : 1708291636085279066
         * top : 0
         * createTime : 2017-08-29 16:36:08
         * time : 0
         * barId :
         */

        public List<YeMaoList> yeMaoList;
        /**
         * picUrl : /mytrace/1707131643562222112/20170815164827.png
         * createTime : 2017-08-15 16:48:27
         * picType : 0
         * width : 375
         * messageId : 170815164827972110
         * photoId : 170815164827974111
         * height : 900
         */

        public List<BarUserPicList> barUserPicList;
        public List<ActiveList> activityList;
        public Date startTime;
        public Date endTime;
        public List<UserPermissionBean> userPermissionList;
    }


}
