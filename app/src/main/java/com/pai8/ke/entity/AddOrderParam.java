package com.pai8.ke.entity;

import java.util.List;

public class AddOrderParam {

    private List<GoodsInfo> goods_info;//商品信息json字符串
    private int buyer_id;//买家id值
    private int shop_id;//店铺id值
    private int order_type;//订单类型 1为邮寄订单 2为外卖订单 3为核销订单
    private int address_id;//收货地址id
    private String express_price = "0";//配送费用
    private String box_price = "0";//包装费用
    private int order_discount_coupon_id;//满减券id值
    private int express_discount_coupon_id;//运费券id值
    private String remark;//订单备注
    private String buyer_name;//仅在团购订单需填写 用户名称
    private String buyer_phone;//仅在团购订单需填写 用户联系方式

    public List<GoodsInfo> getGoods_info() {
        return goods_info;
    }

    public void setGoods_info(List<GoodsInfo> goods_info) {
        this.goods_info = goods_info;
    }

    public int getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(int buyer_id) {
        this.buyer_id = buyer_id;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public int getOrder_type() {
        return order_type;
    }

    public void setOrder_type(int order_type) {
        this.order_type = order_type;
    }

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    public String getExpress_price() {
        return express_price;
    }

    public void setExpress_price(String express_price) {
        this.express_price = express_price;
    }

    public String getBox_price() {
        return box_price;
    }

    public void setBox_price(String box_price) {
        this.box_price = box_price;
    }

    public int getOrder_discount_coupon_id() {
        return order_discount_coupon_id;
    }

    public void setOrder_discount_coupon_id(int order_discount_coupon_id) {
        this.order_discount_coupon_id = order_discount_coupon_id;
    }

    public int getExpress_discount_coupon_id() {
        return express_discount_coupon_id;
    }

    public void setExpress_discount_coupon_id(int express_discount_coupon_id) {
        this.express_discount_coupon_id = express_discount_coupon_id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBuyer_name() {
        return buyer_name;
    }

    public void setBuyer_name(String buyer_name) {
        this.buyer_name = buyer_name;
    }

    public String getBuyer_phone() {
        return buyer_phone;
    }

    public void setBuyer_phone(String buyer_phone) {
        this.buyer_phone = buyer_phone;
    }


    public static class GoodsInfo {
        private int goods_id;
        private int goods_num;
        private double goods_price;

        public int getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(int goods_id) {
            this.goods_id = goods_id;
        }

        public int getGoods_num() {
            return goods_num;
        }

        public void setGoods_num(int goods_num) {
            this.goods_num = goods_num;
        }

        public double getGoods_price() {
            return goods_price;
        }

        public void setGoods_price(double goods_price) {
            this.goods_price = goods_price;
        }
    }
}
