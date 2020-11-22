package com.pai8.ke.activity.takeaway.entity;

import java.util.List;

public class OrderInfo {

    public int id;
    public String order_no;
    public int shop_id;
    public int order_type;
    public int buyer_id;
    public String order_price;
    public String express_price;
    public String order_discount_coupon_id;
    public String order_discount_price;
    public String express_discount_coupon_id;
    public String express_discount_price;
    public int order_status;
    public String order_express_no;
    public long add_time;
    public long update_time;
    public int address_id;
    public String star_num;
    public String comment;
    public String shop_name;
    public String shop_img;
    public String shop_phone;
    public List<OrderGoodInfo> goods_info;
    public int count;
    public String remain_pay_time;


}
