package com.yeying.aimi.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by king .
 * 公司:业英众娱
 * 2017/9/26 下午6:02
 */

public class UserPermissionBean implements Serializable {
    public String id;     //id

    public String userId;     //用户id

    public String barId;      //酒吧id

    public String roleId;     //角色id

    public Date createTime; //创建

    public Date modifiedTime; //修改时间

    public String permissionId; //权限id

    public String permissionName; //权限名称


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBarId() {
        return barId;
    }

    public void setBarId(String barId) {
        this.barId = barId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }
}
