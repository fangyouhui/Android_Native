package com.pai8.ke.activity.takeaway.entity.event;

import com.pai8.ke.activity.takeaway.entity.FoodGoodInfo;

import java.util.List;

/**
 * @author Created by zzf
 * @time 2020/12/16 22:26
 * Description：
 */
public class CartNumEvent {

    private int type;
    private int number;
    private int goods_id;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public CartNumEvent(int type, int number, int goods_id) {
        this.type = type;
        this.number = number;
        this.goods_id = goods_id;
    }
}
