package com.pai8.ke.activity.takeaway.entity;

import java.io.Serializable;
import java.util.List;

public class OrderListResult implements Serializable {

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
    private int order_status; //订单状态 0为待支付 1为已支付 2为商家已接单 7为订单制作完成 3为配送中 4为订单已完成 5为订单已申请退款 6订单被拒绝退款 8为订单已退款 9为订单已取消 -1为支付超时 -2订单拒绝接单 10为订单已评价
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
    private String shop_name;
    private String shop_img;
    private String shop_phone;
    private int count;
    private int remain_pay_time;

    private List<Goods_info> goods_info;

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

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_img(String shop_img) {
        this.shop_img = shop_img;
    }

    public String getShop_img() {
        return shop_img;
    }

    public void setShop_phone(String shop_phone) {
        this.shop_phone = shop_phone;
    }

    public String getShop_phone() {
        return shop_phone;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setRemain_pay_time(int remain_pay_time) {
        this.remain_pay_time = remain_pay_time;
    }

    public int getRemain_pay_time() {
        return remain_pay_time;
    }

    public void setGoods_info(List<Goods_info> goods_info) {
        this.goods_info = goods_info;
    }

    public List<Goods_info> getGoods_info() {
        return goods_info;
    }

    public class Goods_info {

        private int id;
        private int shop_id;
        private int cate_id;
        private int type;
        private String title;
        private String desc;
        private List<String> cover;
        private List<String> video;
        private String origin_price;
        private String sell_price;
        private String discount;
        private String option;
        private int status;
        private long add_time;
        private String up_time;
        private int verify_status;
        private String packing_price;
        private String verify_desc;
        private int sale_count;
        private int month_sale_count;
        private String month_sale_price;
        private String recent_month_time;
        private int like_count;
        private String update_time;
        private String score;
        private int stock;
        private String term;
        private String matter;
        private String details;
        private List<String> details_img;
        private int food_sort;
        private int food_type;
        private String is_weekend;

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setShop_id(int shop_id) {
            this.shop_id = shop_id;
        }

        public int getShop_id() {
            return shop_id;
        }

        public void setCate_id(int cate_id) {
            this.cate_id = cate_id;
        }

        public int getCate_id() {
            return cate_id;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

        public void setCover(List<String> cover) {
            this.cover = cover;
        }

        public List<String> getCover() {
            return cover;
        }

        public void setVideo(List<String> video) {
            this.video = video;
        }

        public List<String> getVideo() {
            return video;
        }

        public void setOrigin_price(String origin_price) {
            this.origin_price = origin_price;
        }

        public String getOrigin_price() {
            return origin_price;
        }

        public void setSell_price(String sell_price) {
            this.sell_price = sell_price;
        }

        public String getSell_price() {
            return sell_price;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getDiscount() {
            return discount;
        }

        public void setOption(String option) {
            this.option = option;
        }

        public String getOption() {
            return option;
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

        public void setUp_time(String up_time) {
            this.up_time = up_time;
        }

        public String getUp_time() {
            return up_time;
        }

        public void setVerify_status(int verify_status) {
            this.verify_status = verify_status;
        }

        public int getVerify_status() {
            return verify_status;
        }

        public void setPacking_price(String packing_price) {
            this.packing_price = packing_price;
        }

        public String getPacking_price() {
            return packing_price;
        }

        public void setVerify_desc(String verify_desc) {
            this.verify_desc = verify_desc;
        }

        public String getVerify_desc() {
            return verify_desc;
        }

        public void setSale_count(int sale_count) {
            this.sale_count = sale_count;
        }

        public int getSale_count() {
            return sale_count;
        }

        public void setMonth_sale_count(int month_sale_count) {
            this.month_sale_count = month_sale_count;
        }

        public int getMonth_sale_count() {
            return month_sale_count;
        }

        public void setMonth_sale_price(String month_sale_price) {
            this.month_sale_price = month_sale_price;
        }

        public String getMonth_sale_price() {
            return month_sale_price;
        }

        public void setRecent_month_time(String recent_month_time) {
            this.recent_month_time = recent_month_time;
        }

        public String getRecent_month_time() {
            return recent_month_time;
        }

        public void setLike_count(int like_count) {
            this.like_count = like_count;
        }

        public int getLike_count() {
            return like_count;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getScore() {
            return score;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public int getStock() {
            return stock;
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

        public void setFood_type(int food_type) {
            this.food_type = food_type;
        }

        public int getFood_type() {
            return food_type;
        }

        public void setIs_weekend(String is_weekend) {
            this.is_weekend = is_weekend;
        }

        public String getIs_weekend() {
            return is_weekend;
        }

    }


}
