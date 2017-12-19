package com.yeying.aimi.bean;

/**
 * Created by tanchengkeji on 2017/8/2.
 */

public class FateAgreeBean {


    /**
     * agreeStatus : 0
     * createTime : 2017-08-03 15:12:12
     * fateCardId : 1708031510109529004
     * fateUser : {"birthday":1498233600000,"gender":2,"headImg":"/headimg/41/170724135456101984120170731180324.png","nickName":"GG","location_x":"116.49645","location_y":"39.980004","privacy":0,"session_id":"2274a9fb-f07c-4f77-8b96-05e8fd38b2f8","autograph":"","userId":"1707241354561019841","secret_key":"BUkFdVEpNsSa1R4s","constellation":"巨蟹座","createTime":1500875696000,"phone":"18716025155","registehx":1,"state":0,"pwd":"e807f1fcf82d132f9bb018ca6738a19f","region":"","is_friend":0,"age":0,"location_time":1501231077000}
     * barId : 1603251601511000001
     */

    private int agreeStatus;
    private String createTime;
    private String fateCardId;
    private FateUser fateUser;
    private String barId;

    public int getAgreeStatus() {
        return agreeStatus;
    }

    public void setAgreeStatus(int agreeStatus) {
        this.agreeStatus = agreeStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFateCardId() {
        return fateCardId;
    }

    public void setFateCardId(String fateCardId) {
        this.fateCardId = fateCardId;
    }

    public FateUser getFateUser() {
        return fateUser;
    }

    public void setFateUser(FateUser fateUser) {
        this.fateUser = fateUser;
    }

    public String getBarId() {
        return barId;
    }

    public void setBarId(String barId) {
        this.barId = barId;
    }

}
