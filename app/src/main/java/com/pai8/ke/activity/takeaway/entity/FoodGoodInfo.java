package com.pai8.ke.activity.takeaway.entity;


import java.io.Serializable;

public class FoodGoodInfo implements Serializable {
    public int viewType;
    public int id;
    public int shop_id;
    public int cate_id;
    public String title;
    public String name;
    public String desc;
    public String cover;
    public String goods_price;
    public String origin_price;
    public String sell_price;
    public String discount = "0";
    public String month_sale_count;
    public String like_count;
    public String option;
    public String video;
    public int status;
    public String packing_price;
    private String tag;
    public int goods_num;
    public int position = -1;
    public int type = 2;
    public String goods_origin_price;

    public boolean isTitle = false;

    public FoodGoodInfo() {
    }


    public FoodGoodInfo(String title) {
        this.title = title;
    }

    public boolean isTitle() {
        return isTitle;
    }

    public void setTitle(boolean title) {
        isTitle = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
