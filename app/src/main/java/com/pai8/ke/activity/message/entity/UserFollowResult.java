package com.pai8.ke.activity.message.entity;

import java.io.Serializable;

public class UserFollowResult implements Serializable {

    private int id;
    private int user_id;
    private int shop_id;

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

}
