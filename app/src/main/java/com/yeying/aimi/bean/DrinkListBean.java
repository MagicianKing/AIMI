package com.yeying.aimi.bean;

import java.util.List;

public class DrinkListBean {
    private String shoping_id;
    private String user_shoping_id;
    private String shop_name;
    private int left_number;
    private double price;
    private int buy_number;
    private double favour_price;
    private int shop_type;
    private String shop_descrption;
    private String shop_url;
    private int picture_height;
    private int picture_width;
    private boolean isclick;
    private int teg = 100;
    private int go_number;
    private List<ChildBean> shopChildList;

    public int getTeg() {
        return teg;
    }

    public void setTeg(int teg) {
        this.teg = teg;
    }

    public String getShoping_id() {
        return shoping_id;
    }

    public void setShoping_id(String shoping_id) {
        this.shoping_id = shoping_id;
    }

    public int getGo_number() {
        return go_number;
    }

    public void setGo_number(int go_number) {
        this.go_number = go_number;
    }

    public String getUser_shoping_id() {
        return user_shoping_id;
    }

    public void setUser_shoping_id(String user_shoping_id) {
        this.user_shoping_id = user_shoping_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public int getLeft_number() {
        return left_number;
    }

    public void setLeft_number(int left_number) {
        this.left_number = left_number;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getBuy_number() {
        return buy_number;
    }

    public void setBuy_number(int buy_number) {
        this.buy_number = buy_number;
    }

    public double getFavour_price() {
        return favour_price;
    }

    public void setFavour_price(double favour_price) {
        this.favour_price = favour_price;
    }

    public int getShop_type() {
        return shop_type;
    }

    public void setShop_type(int shop_type) {
        this.shop_type = shop_type;
    }

    public String getShop_descrption() {
        return shop_descrption;
    }

    public void setShop_descrption(String shop_descrption) {
        this.shop_descrption = shop_descrption;
    }

    public String getShop_url() {
        return shop_url;
    }

    public void setShop_url(String shop_url) {
        this.shop_url = shop_url;
    }

    public int getPicture_height() {
        return picture_height;
    }

    public void setPicture_height(int picture_height) {
        this.picture_height = picture_height;
    }

    public int getPicture_width() {
        return picture_width;
    }

    public void setPicture_width(int picture_width) {
        this.picture_width = picture_width;
    }

    public List<ChildBean> getShopChildList() {
        return shopChildList;
    }

    public void setShopChildList(List<ChildBean> shopChildList) {
        this.shopChildList = shopChildList;
    }

    public boolean isIsclick() {
        return isclick;
    }

    public void setIsclick(boolean isclick) {
        this.isclick = isclick;
    }


}
