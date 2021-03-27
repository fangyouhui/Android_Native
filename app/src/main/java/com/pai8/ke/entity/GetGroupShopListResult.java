package com.pai8.ke.entity;

import java.util.List;

public class GetGroupShopListResult {

    private List<Banner> banner;
    private List<ShopList> list;

    public void setBanner(List<Banner> banner) {
        this.banner = banner;
    }

    public List<Banner> getBanner() {
        return banner;
    }

    public void setList(List<ShopList> list) {
        this.list = list;
    }

    public List<ShopList> getList() {
        return list;
    }


    public class Banner {

        private String imgurl;
        private int type;
        private int shop_id;
        private String href_url;

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getImgurl() {
            return imgurl;
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

        public void setHref_url(String href_url) {
            this.href_url = href_url;
        }

        public String getHref_url() {
            return href_url;
        }

    }


    public class ShopList {

        private int id;
        private String shop_name;
        private int cate_id;
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

        public void setCate_id(int cate_id) {
            this.cate_id = cate_id;
        }

        public int getCate_id() {
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
