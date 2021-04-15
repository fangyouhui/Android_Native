package com.pai8.ke.activity.takeaway.entity.resq;

import com.pai8.ke.activity.takeaway.entity.req.AddFoodReq;

import java.io.Serializable;
import java.util.List;

public class ShopInfo implements Serializable {

    public String name;
    public int shop_id;
    public int id;
    public String add_time;
    public String desc;
    public List<AddFoodReq> goods;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
