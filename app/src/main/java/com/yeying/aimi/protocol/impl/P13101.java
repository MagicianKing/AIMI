


package com.yeying.aimi.protocol.impl;


import com.alibaba.fastjson.JSON;
import com.yeying.aimi.bean.ScreenOptionBean;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;

import java.util.List;

/**
 * 查询霸屏价格列表
 * */
public class P13101 extends BasicProtocol {
	public static class Req extends BaseReq {
		public String userId;//	用户id
		public String barId;//	酒吧id	

	}
	
	public static class Resp extends BaseResp {
		public List<ScreenOptionBean> scrOptionList;//	价格列表	List
		public String userId;//	用户id	String
		public int score;//	剩余猫币	int
		public double account;//	余额（money）	double
		public int rechargeCeiling;//	最大充值限额	int
	}
	
	public Req req;
	public Resp resp;
	
    public P13101(){
    	super();
    	req=new Req();
    	req.transcode="13101"; 
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


