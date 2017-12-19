


package com.yeying.aimi.protocol.impl;


import com.alibaba.fastjson.JSON;
import com.yeying.aimi.protoco.BasicProtocol;
import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.protocol.request.Arg;
import com.yeying.aimi.utils.JSONUtil;

/**
 * 霸屏下单接口
 * */
public class P13102 extends BasicProtocol {
	public static class Req extends BaseReq {
		public String userId;//	用户id
		public String screenOptionId;//	霸屏选项id	
		public int type;//	支付方式	int 0:虚拟货币，1第三方
		public String content;//	霸屏语	
		public String imgBase;//	图片字节码
		public  String barId;

	}
	
	public static class Resp extends BaseResp {
		public String userId;//	下单用户id	
		public int status;//	支付结果	状态默认为0 1是待支付 2是成功
		public String payOrderId;//	如果是异步支付，返回异步支付的id	
		public String payUrl;//	异步支付使用的url地址	
		public String screenOrderId;//单个霸屏唯一id

	}
	
	public Req req;
	public Resp resp;
	
    public P13102(){
    	super();
    	req=new Req();
    	req.transcode="13102"; 
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


