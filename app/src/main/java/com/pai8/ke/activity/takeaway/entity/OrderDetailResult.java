package com.pai8.ke.activity.takeaway.entity;

import com.google.gson.annotations.SerializedName;
import com.pai8.ke.entity.GroupGoodsInfoResult;

import java.io.Serializable;
import java.util.List;

public class OrderDetailResult implements Serializable {
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
    private Comment comment;
    private String remark;
    private int pay_type;
    private String refund_reason;
    private String order_qrcode;
    private String buyer_name;
    private String buyer_phone;
    private List<GoodsInfo> goods_info;
    private Shop_info shop_info;
    private Rider_info rider_info;
    private AddressInfo address_info;
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

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Comment getComment() {
        return comment;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public int getPay_type() {
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

    public void setGoods_info(List<GoodsInfo> goods_info) {
        this.goods_info = goods_info;
    }

    public List<GoodsInfo> getGoods_info() {
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

    public AddressInfo getAddress_info() {
        return address_info;
    }

    public void setAddress_info(AddressInfo address_info) {
        this.address_info = address_info;
    }

    public void setRemain_pay_time(int remain_pay_time) {
        this.remain_pay_time = remain_pay_time;
    }

    public int getRemain_pay_time() {
        return remain_pay_time;
    }


    public class Rider_info implements Serializable {
        private String logistic;
        private String rider_name;
        private String rider_phone;
        private String map_type;
        private String rider_latitude;
        private String rider_longitude;
        private String status_desc;

        public String getLogistic() {
            return logistic;
        }

        public void setLogistic(String logistic) {
            this.logistic = logistic;
        }

        public String getRider_name() {
            return rider_name;
        }

        public void setRider_name(String rider_name) {
            this.rider_name = rider_name;
        }

        public String getRider_phone() {
            return rider_phone;
        }

        public void setRider_phone(String rider_phone) {
            this.rider_phone = rider_phone;
        }

        public String getMap_type() {
            return map_type;
        }

        public void setMap_type(String map_type) {
            this.map_type = map_type;
        }

        public String getRider_latitude() {
            return rider_latitude;
        }

        public void setRider_latitude(String rider_latitude) {
            this.rider_latitude = rider_latitude;
        }

        public String getRider_longitude() {
            return rider_longitude;
        }

        public void setRider_longitude(String rider_longitude) {
            this.rider_longitude = rider_longitude;
        }

        public String getStatus_desc() {
            return status_desc;
        }

        public void setStatus_desc(String status_desc) {
            this.status_desc = status_desc;
        }
    }

    public class Shop_info implements Serializable {

        private int id;
        private String shop_name;
        private String mobile;
        private String shop_img;
        private String address;
        private double score;
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

        public void setScore(double score) {
            this.score = score;
        }

        public double getScore() {
            return score;
        }

        public void setSale_count(int sale_count) {
            this.sale_count = sale_count;
        }

        public int getSale_count() {
            return sale_count;
        }

    }


    public class Comment implements Serializable {

        private int id;
        private String image;
        private int score;
        private String content;
        private int user_id;
        private long create_time;
        private String order_no;
        private String reply;
        private String video;
        private int shop_id;

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getImage() {
            return image;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getScore() {
            return score;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }

        public long getCreate_time() {
            return create_time;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

        public String getOrder_no() {
            return order_no;
        }

        public void setReply(String reply) {
            this.reply = reply;
        }

        public String getReply() {
            return reply;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public String getVideo() {
            return video;
        }

        public void setShop_id(int shop_id) {
            this.shop_id = shop_id;
        }

        public int getShop_id() {
            return shop_id;
        }

    }


    public class AddressInfo implements Serializable {

        private int id;
        private int user_id;
        private String address;
        private String linkman;
        private String phone;
        private String longitude;
        private String latitude;
        private String house_number;
        private long update_time;

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAddress() {
            return address;
        }

        public void setLinkman(String linkman) {
            this.linkman = linkman;
        }

        public String getLinkman() {
            return linkman;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPhone() {
            return phone;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setHouse_number(String house_number) {
            this.house_number = house_number;
        }

        public String getHouse_number() {
            return house_number;
        }

        public void setUpdate_time(long update_time) {
            this.update_time = update_time;
        }

        public long getUpdate_time() {
            return update_time;
        }

    }
}
