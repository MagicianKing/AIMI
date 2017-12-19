package com.yeying.aimi.bean;

/**
 * 礼物信息实体类
 */
public class MsgGiftBean {
    private String id;//	礼物id
    private String name;//	名称
    private int type;//	类型
    private String ssUrl;//	Smal图片地址
    private String url;//	图片

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSsUrl() {
        return ssUrl;
    }

    public void setSsUrl(String ssUrl) {
        this.ssUrl = ssUrl;
    }

}
