package com.pai8.ke.activity.takeaway.entity.req;

import java.io.Serializable;

public class AddFoodReq implements Serializable {

    public int goods_id;
    public String shop_id;
    public String cate_id;  //类别
    public String cate_name;  //类别

    public String title;  //名称
    public String desc;  //描述
    public String cover;  //图片
    public String origin_price;  //原价
    public String sell_price;  //售价
    public String discount;  // 折扣
    public String packing_price;
    public String cateName;
    public boolean isTitle;
    private String tag;
    public int id;
    public int type;//type 1-图片 2-视频
    public String cover_qiniu_key;//七牛key
    public String key;  // 编辑时上传的七牛key字段名

    public AddFoodReq() {
    }

    public AddFoodReq(String title) {
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
