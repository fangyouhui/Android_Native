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
    public int order_status;  //订单状态 0为待支付 1为已支付 2为商家已接单 3为配送中 4为订单已完成 5为订单已申请退款 6订单被拒绝退款 8为订单已退款 9为订单已取消 -1为支付超时 -2订单拒绝接单
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