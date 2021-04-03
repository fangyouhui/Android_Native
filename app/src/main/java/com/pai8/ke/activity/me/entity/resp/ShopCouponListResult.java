package com.pai8.ke.activity.me.entity.resp;

import java.io.Serializable;
import java.util.List;

/**
 * @author Created by zzf
 * @time 2020/12/21 22:18
 * Description：
 */
public class ShopCouponListResult {


    private List<CouponListBean> express_coupon_list;//运费券列表
    private List<CouponListBean> order_coupon_list;//订单优惠券列表

    public List<CouponListBean> getExpress_coupon_list() {
        return express_coupon_list;
    }

    public void setExpress_coupon_list(List<CouponListBean> express_coupon_list) {
        this.express_coupon_list = express_coupon_list;
    }

    public List<CouponListBean> getOrder_coupon_list() {
        return order_coupon_list;
    }

    public void setOrder_coupon_list(List<CouponListBean> order_coupon_list) {
        this.order_coupon_list = order_coupon_list;
    }

    public static class CouponListBean implements Serializable {
        /**
         * id : 32
         * type : 1
         * shop_id : 22
         * value : 38/38
         * status : 1
         * add_time : 1606923060
         * start_time : 1606923060
         * end_time : 1609169460
         * num : 0
         * shop_name : 张氏奶茶店
         * coupon_name : 商品满减券
         * trig_price : 38
         * dis_price : 38
         */

        private int id;
        private int type;
        private int shop_id;
        private String value;
        private int status;
        private int add_time;
        private int start_time;
        private int end_time;
        private int num;
        private String shop_name;
        private String coupon_name;
        private String trig_price;
        private String dis_price;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getShop_id() {
            return shop_id;
        }

        public void setShop_id(int shop_id) {
            this.shop_id = shop_id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getAdd_time() {
            return add_time;
        }

        public void setAdd_time(int add_time) {
            this.add_time = add_time;
        }

        public int getStart_time() {
            return start_time;
        }

        public void setStart_time(int start_time) {
            this.start_time = start_time;
        }

        public int getEnd_time() {
            return end_time;
        }

        public void setEnd_time(int end_time) {
            this.end_time = end_time;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public String getCoupon_name() {
            return coupon_name;
        }

        public void setCoupon_name(String coupon_name) {
            this.coupon_name = coupon_name;
        }

        public String getTrig_price() {
            return trig_price;
        }

        public void setTrig_price(String trig_price) {
            this.trig_price = trig_price;
        }

        public String getDis_price() {
            return dis_price;
        }

        public void setDis_price(String dis_price) {
            this.dis_price = dis_price;
        }
    }
}
