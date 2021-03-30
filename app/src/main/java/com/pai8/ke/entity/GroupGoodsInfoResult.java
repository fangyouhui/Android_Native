package com.pai8.ke.entity;

import java.io.Serializable;
import java.util.List;

public class GroupGoodsInfoResult implements Serializable {
    private int id;
    private int shop_id;
    private int cate_id;
    private int type;
    private String title;
    private String desc;
    private List<String> cover;
    private String video;
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
    private Term term;
    private String matter;
    private String details;
    private List<String> details_img;
    private int food_sort;
    private int food_type;
    private String is_weekend;
    private Shop shop;
    private String video_url;
    private List<String> cover_key;
    private List<String> details_key;

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

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideo() {
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

    public void setTerm(Term term) {
        this.term = term;
    }

    public Term getTerm() {
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

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Shop getShop() {
        return shop;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setCover_key(List<String> cover_key) {
        this.cover_key = cover_key;
    }

    public List<String> getCover_key() {
        return cover_key;
    }

    public void setDetails_key(List<String> details_key) {
        this.details_key = details_key;
    }

    public List<String> getDetails_key() {
        return details_key;
    }


    public class Term {

        private String start_time;
        private String end_time;

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getEnd_time() {
            return end_time;
        }

    }


    public class Shop {

        private String shop_name;
        private String address;
        private String shop_img;
        private String mobile;

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public String getShop_name() {
            return shop_name;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAddress() {
            return address;
        }

        public void setShop_img(String shop_img) {
            this.shop_img = shop_img;
        }

        public String getShop_img() {
            return shop_img;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getMobile() {
            return mobile;
        }

    }
}
