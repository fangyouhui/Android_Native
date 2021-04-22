package com.pai8.ke.activity.message.entity;

import java.io.Serializable;

public class UserFollowResult implements Serializable {

    private int id;
    private int user_id;
    private int shop_id;
    private String re_code;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public int getShop_id() {
        return shop_id;
    }

    public String getRe_code() {
        return re_code;
    }

    public void setRe_code(String re_code) {
        this.re_code = re_code;
    }
}
