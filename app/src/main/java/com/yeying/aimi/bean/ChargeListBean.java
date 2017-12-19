package com.yeying.aimi.bean;

/**
 * 充值兑换列表实体类
 */
public class ChargeListBean {
    private String name;
    private String sId;
    private String sMoney;//	金额
    private String sNumber;//	猫币
    private boolean isChecked;// 是否选中

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getsMoney() {
        return sMoney;
    }

    public void setsMoney(String sMoney) {
        this.sMoney = sMoney;
    }

    public String getsNumber() {
        return sNumber;
    }

    public void setsNumber(String sNumber) {
        this.sNumber = sNumber;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
