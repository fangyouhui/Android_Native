package com.pai8.ke.entity;

import java.io.Serializable;

public class ShopTypeResult implements Serializable {

    private int id;
    private int shop_id;
    private String title;
    private String desc;
    private String cover;
    private String video;
    private String origin_price;
    private String sell_price;
    private int food_type;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCover() {
        return cover;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideo() {
        return video;
    }

    public void setOrigin_price(String origin_price) {
        this.origin_price = origin_price;
    }

    public String getOrigin_price() {
        return origin_price;
    }

    public void setSell_price(String sell_price) {
        this.sell_price = sell_price;
    }

    public String getSell_price() {
        return sell_price;
    }

    public void setFood_type(int food_type) {
        this.food_type = food_type;
    }

    public int getFood_type() {
        return food_type;
    }


}

