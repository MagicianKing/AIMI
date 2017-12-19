package com.yeying.aimi.bean;

import java.io.Serializable;

public class ScreenOptionBean implements Serializable{

	private boolean isChoose;
	private String screenOptionId;//	霸屏选项id
	private int continueTime;//	持续时间
	private String desc;
	private double money;//	价格
	public String getScreenOptionId() {
		return screenOptionId;
	}
	public void setScreenOptionId(String screenOptionId) {
		this.screenOptionId = screenOptionId;
	}
	public int getContinueTime() {
		return continueTime;
	}
	public void setContinueTime(int continueTime) {
		this.continueTime = continueTime;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public boolean isChoose() {
		return isChoose;
	}
	public void setChoose(boolean isChoose) {
		this.isChoose = isChoose;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
