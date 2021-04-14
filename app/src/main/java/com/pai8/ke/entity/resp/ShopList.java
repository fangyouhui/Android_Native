package com.pai8.ke.entity.resp;

import java.io.Serializable;

public class ShopList implements Serializable {

    /**
     * id : 1
     * shop_name : 刚刚
     * shop_desc :
     * shop_img : https://jianshen.fyh5p8.com/FgJS3Dn8UWHpaczk9v6aaYvlARab
     * score : 0
     * address :
     */

    private int id;
    private String shop_name;
    private String shop_desc;
    private String shop_img;
    private float score;
    private String address;
    private String longitude;
    private String latitude;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_desc() {
        return shop_desc;
    }

    public void setShop_desc(String shop_desc) {
        this.shop_desc = shop_desc;
    }

    public String getShop_img() {
        return shop_img;
    }

    public void setShop_img(String shop_img) {
        this.shop_img = shop_img;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
