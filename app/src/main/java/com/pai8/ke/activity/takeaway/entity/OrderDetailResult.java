package com.pai8.ke.activity.takeaway.entity;

import com.pai8.ke.entity.GroupGoodsInfoResult;

import java.util.List;

public class OrderDetailResult {
    private int id;
    private String order_no;
    private int shop_id;
    private int order_type;
    private int buyer_id;
    private String order_price;
    private String express_price;
    private String box_price;
    private String order_discount_coupon_id;
    private String order_discount_price;
    private String express_discount_coupon_id;
    private String express_discount_price;
    private int order_status;
    private String order_express_no;
    private long add_time;
    private long update_time;
    private int address_id;
    private String comment;
    private String remark;
    private String pay_type;
    private String refund_reason;
    private String order_qrcode;
    private String buyer_name;
    private String buyer_phone;
    private List<Goods_info> goods_info;
    private Shop_info shop_info;
    private Rider_info rider_info;
    private String address_info;
    private int remain_pay_time;
    private GroupGoodsInfoResult.Term term;
    private String matter;

    public String getMatter() {
        return matter;
    }

    public void setMatter(String matter) {
        this.matter = matter;
    }

    public GroupGoodsInfoResult.Term getTerm() {
        return term;
    }

    public void setTerm(GroupGoodsInfoResult.Term term) {
        this.term = term;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setOrder_type(int order_type) {
        this.order_type = order_type;
    }

    public int getOrder_type() {
        return order_type;
    }

    public void setBuyer_id(int buyer_id) {
        this.buyer_id = buyer_id;
    }

    public int getBuyer_id() {
        return buyer_id;
    }

    public void setOrder_price(String order_price) {
        this.order_price = order_price;
    }

    public String getOrder_price() {
        return order_price;
    }

    public void setExpress_price(String express_price) {
        this.express_price = express_price;
    }

    public String getExpress_price() {
        return express_price;
    }

    public void setBox_price(String box_price) {
        this.box_price = box_price;
    }

    public String getBox_price() {
        return box_price;
    }

    public void setOrder_discount_coupon_id(String order_discount_coupon_id) {
        this.order_discount_coupon_id = order_discount_coupon_id;
    }

    public String getOrder_discount_coupon_id() {
        return order_discount_coupon_id;
    }

    public void setOrder_discount_price(String order_discount_price) {
        this.order_discount_price = order_discount_price;
    }

    public String getOrder_discount_price() {
        return order_discount_price;
    }

    public void setExpress_discount_coupon_id(String express_discount_coupon_id) {
        this.express_discount_coupon_id = express_discount_coupon_id;
    }

    public String getExpress_discount_coupon_id() {
        return express_discount_coupon_id;
    }

    public void setExpress_discount_price(String express_discount_price) {
        this.express_discount_price = express_discount_price;
    }

    public String getExpress_discount_price() {
        return express_discount_price;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_express_no(String order_express_no) {
        this.order_express_no = order_express_no;
    }

    public String getOrder_express_no() {
        return order_express_no;
    }

    public void setAdd_time(long add_time) {
        this.add_time = add_time;
    }

    public long getAdd_time() {
        return add_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    public int getAddress_id() {
        return address_id;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setRefund_reason(String refund_reason) {
        this.refund_reason = refund_reason;
    }

    public String getRefund_reason() {
        return refund_reason;
    }

    public void setOrder_qrcode(String order_qrcode) {
        this.order_qrcode = order_qrcode;
    }

    public String getOrder_qrcode() {
        return order_qrcode;
    }

    public void setBuyer_name(String buyer_name) {
        this.buyer_name = buyer_name;
    }

    public String getBuyer_name() {
        return buyer_name;
    }

    public void setBuyer_phone(String buyer_phone) {
        this.buyer_phone = buyer_phone;
    }

    public String getBuyer_phone() {
        return buyer_phone;
    }

    public void setGoods_info(List<Goods_info> goods_info) {
        this.goods_info = goods_info;
    }

    public List<Goods_info> getGoods_info() {
        return goods_info;
    }

    public void setShop_info(Shop_info shop_info) {
        this.shop_info = shop_info;
    }

    public Shop_info getShop_info() {
        return shop_info;
    }

    public void setRider_info(Rider_info rider_info) {
        this.rider_info = rider_info;
    }

    public Rider_info getRider_info() {
        return rider_info;
    }

    public void setAddress_info(String address_info) {
        this.address_info = address_info;
    }

    public String getAddress_info() {
        return address_info;
    }

    public void setRemain_pay_time(int remain_pay_time) {
        this.remain_pay_time = remain_pay_time;
    }

    public int getRemain_pay_time() {
        return remain_pay_time;
    }


    public class Rider_info {

    }


    public class Goods_info {
        private int id;
        private int order_id;
        private int goods_id;
        private int goods_num;
        private String goods_price;
        private String goods_title;
        private List<String> goods_img;
        private String goods_origin_price;
        private String goods_sell_price;
        private String goods_discount;
        private String term;
        private String matter;
        private String details;
        private List<String> details_img;
        private int food_sort;
        private String is_weekend;
        private String desc;

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }

        public int getOrder_id() {
            return order_id;
        }

        public void setGoods_id(int goods_id) {
            this.goods_id = goods_id;
        }

        public int getGoods_id() {
            return goods_id;
        }

        public void setGoods_num(int goods_num) {
            this.goods_num = goods_num;
        }

        public int getGoods_num() {
            return goods_num;
        }

        public void setGoods_price(String goods_price) {
            this.goods_price = goods_price;
        }

        public String getGoods_price() {
            return goods_price;
        }

        public void setGoods_title(String goods_title) {
            this.goods_title = goods_title;
        }

        public String getGoods_title() {
            return goods_title;
        }

        public void setGoods_img(List<String> goods_img) {
            this.goods_img = goods_img;
        }

        public List<String> getGoods_img() {
            return goods_img;
        }

        public void setGoods_origin_price(String goods_origin_price) {
            this.goods_origin_price = goods_origin_price;
        }

        public String getGoods_origin_price() {
            return goods_origin_price;
        }

        public void setGoods_sell_price(String goods_sell_price) {
            this.goods_sell_price = goods_sell_price;
        }

        public String getGoods_sell_price() {
            return goods_sell_price;
        }

        public void setGoods_discount(String goods_discount) {
            this.goods_discount = goods_discount;
        }

        public String getGoods_discount() {
            return goods_discount;
        }

        public void setTerm(String term) {
            this.term = term;
        }

        public String getTerm() {
            return term;
        }

        public void setMatter(String matter) {
            this.matter = matter;
        }

        public String getMatter() {
            return matter;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails_img(List<String> details_img) {
            this.details_img = details_img;
        }

        public List<String> getDetails_img() {
            return details_img;
        }

        public void setFood_sort(int food_sort) {
            this.food_sort = food_sort;
        }

        public int getFood_sort() {
            return food_sort;
        }

        public void setIs_weekend(String is_weekend) {
            this.is_weekend = is_weekend;
        }

        public String getIs_weekend() {
            return is_weekend;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

    }

    /**
     * Auto-generated: 2021-03-31 17:25:28
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */
    public class Shop_info {

        private int id;
        private String shop_name;
        private String mobile;
        private String shop_img;
        private String address;
        private int score;
        private int sale_count;

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public String getShop_name() {
            return shop_name;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getMobile() {
            return mobile;
        }

        public void setShop_img(String shop_img) {
            this.shop_img = shop_img;
        }

        public String getShop_img() {
            return shop_img;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAddress() {
            return address;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getScore() {
            return score;
        }

        public void setSale_count(int sale_count) {
            this.sale_count = sale_count;
        }

        public int getSale_count() {
            return sale_count;
        }

    }
}
