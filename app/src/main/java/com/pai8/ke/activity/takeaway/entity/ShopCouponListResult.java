package com.pai8.ke.activity.takeaway.entity;

import java.io.Serializable;
import java.util.List;

public class ShopCouponListResult implements Serializable {

    private List<Express_coupon_list> express_coupon_list; //运费券列表
    private List<Order_coupon_list> order_coupon_list;//订单优惠券列表

    public void setExpress_coupon_list(List<Express_coupon_list> express_coupon_list) {
        this.express_coupon_list = express_coupon_list;
    }

    public List<Express_coupon_list> getExpress_coupon_list() {
        return express_coupon_list;
    }

    public void setOrder_coupon_list(List<Order_coupon_list> order_coupon_list) {
        this.order_coupon_list = order_coupon_list;
    }

    public List<Order_coupon_list> getOrder_coupon_list() {
        return order_coupon_list;
    }


    public class Order_coupon_list implements Serializable {

        private int id;
        private int type;
        private int shop_id;
        private String value;
        private int status;
        private long add_time;
        private long start_time;
        private long end_time;
        private int num;
        private String shop_name;
        private String coupon_name;
        private String trig_price;
        private String dis_price;

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public void setShop_id(int shop_id) {
            this.shop_id = shop_id;
        }

        public int getShop_id() {
            return shop_id;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }

        public void setAdd_time(long add_time) {
            this.add_time = add_time;
        }

        public long getAdd_time() {
            return add_time;
        }

        public void setStart_time(long start_time) {
            this.start_time = start_time;
        }

        public long getStart_time() {
            return start_time;
        }

        public void setEnd_time(long end_time) {
            this.end_time = end_time;
        }

        public long getEnd_time() {
            return end_time;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public String getShop_name() {
            return shop_name;
        }

        public void setCoupon_name(String coupon_name) {
            this.coupon_name = coupon_name;
        }

        public String getCoupon_name() {
            return coupon_name;
        }

        public void setTrig_price(String trig_price) {
            this.trig_price = trig_price;
        }

        public String getTrig_price() {
            return trig_price;
        }

        public void setDis_price(String dis_price) {
            this.dis_price = dis_price;
        }

        public String getDis_price() {
            return dis_price;
        }

    }


    public class Express_coupon_list implements Serializable {

        private int id;
        private int type;
        private int shop_id;
        private String value;
        private int status;
        private long add_time;
        private long start_time;
        private long end_time;
        private int num;
        private String shop_name;
        private String coupon_name;
        private String dis_price;

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public void setShop_id(int shop_id) {
            this.shop_id = shop_id;
        }

        public int getShop_id() {
            return shop_id;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }

        public void setAdd_time(long add_time) {
            this.add_time = add_time;
        }

        public long getAdd_time() {
            return add_time;
        }

        public void setStart_time(long start_time) {
            this.start_time = start_time;
        }

        public long getStart_time() {
            return start_time;
        }

        public void setEnd_time(long end_time) {
            this.end_time = end_time;
        }

        public long getEnd_time() {
            return end_time;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public String getShop_name() {
            return shop_name;
        }

        public void setCoupon_name(String coupon_name) {
            this.coupon_name = coupon_name;
        }

        public String getCoupon_name() {
            return coupon_name;
        }

        public void setDis_price(String dis_price) {
            this.dis_price = dis_price;
        }

        public String getDis_price() {
            return dis_price;
        }

    }
}
