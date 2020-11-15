package com.pai8.ke.entity.resp;

import java.io.Serializable;

public class ShopList implements Serializable {

    /**
     * id : 1
     * shop_name : 刚刚
     * shop_desc :
     * begin_time : 0
     * end_time : 0
     * shop_img : https://jianshen.fyh5p8.com/FgJS3Dn8UWHpaczk9v6aaYvlARab
     * score : 0
     * send_cost : 0
     * floor_send_cost : 0
     */

    private int id;
    private String shop_name;
    private String shop_desc;
    private int begin_time;
    private int end_time;
    private String shop_img;
    private int score;
    private String send_cost;
    private String floor_send_cost;

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

    public int getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(int begin_time) {
        this.begin_time = begin_time;
    }

    public int getEnd_time() {
        return end_time;
    }

    public void setEnd_time(int end_time) {
        this.end_time = end_time;
    }

    public String getShop_img() {
        return shop_img;
    }

    public void setShop_img(String shop_img) {
        this.shop_img = shop_img;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getSend_cost() {
        return send_cost;
    }

    public void setSend_cost(String send_cost) {
        this.send_cost = send_cost;
    }

    public String getFloor_send_cost() {
        return floor_send_cost;
    }

    public void setFloor_send_cost(String floor_send_cost) {
        this.floor_send_cost = floor_send_cost;
    }
}
