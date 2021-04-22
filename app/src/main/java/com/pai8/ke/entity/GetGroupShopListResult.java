package com.pai8.ke.entity;

import java.io.Serializable;
import java.util.List;

public class GetGroupShopListResult {

    private List<BannerResult> banner;
    private List<ShopList> list;

    public void setBanner(List<BannerResult> banner) {
        this.banner = banner;
    }

    public List<BannerResult> getBanner() {
        return banner;
    }

    public void setList(List<ShopList> list) {
        this.list = list;
    }

    public List<ShopList> getList() {
        return list;
    }




    public class ShopList implements Serializable {

        private int id;
        private String shop_name;
        private String cate_id;
        private double score;
        private String shop_desc;
        private String province;
        private String city;
        private String district;
        private String address;
        private String longitude;
        private String latitude;
        private int month_sale_count;
        private String shop_img;
        private String shop_video;
        private String logo_img;
        private String cate_name;

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

        public void setCate_id(String cate_id) {
            this.cate_id = cate_id;
        }

        public String getCate_id() {
            return cate_id;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public void setShop_desc(String shop_desc) {
            this.shop_desc = shop_desc;
        }

        public String getShop_desc() {
            return shop_desc;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getProvince() {
            return province;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCity() {
            return city;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getDistrict() {
            return district;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAddress() {
            return address;
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

        public void setMonth_sale_count(int month_sale_count) {
            this.month_sale_count = month_sale_count;
        }

        public int getMonth_sale_count() {
            return month_sale_count;
        }

        public void setShop_img(String shop_img) {
            this.shop_img = shop_img;
        }

        public String getShop_img() {
            return shop_img;
        }

        public void setShop_video(String shop_video) {
            this.shop_video = shop_video;
        }

        public String getShop_video() {
            return shop_video;
        }

        public void setLogo_img(String logo_img) {
            this.logo_img = logo_img;
        }

        public String getLogo_img() {
            return logo_img;
        }

        public void setCate_name(String cate_name) {
            this.cate_name = cate_name;
        }

        public String getCate_name() {
            return cate_name;
        }

    }

}
