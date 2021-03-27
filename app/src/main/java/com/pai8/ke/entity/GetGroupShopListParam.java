package com.pai8.ke.entity;

public class GetGroupShopListParam {

    private String page;//页码，不填默认为0
    private String size;//页数，不填默认为10
    private String shop_name;//商户名称检索
    private String cate_id;//商户分类检索

    public GetGroupShopListParam(String page, String size, String shop_name, String cate_id) {
        this.page = page;
        this.size = size;
        this.shop_name = shop_name;
        this.cate_id = cate_id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getCate_id() {
        return cate_id;
    }

    public void setCate_id(String cate_id) {
        this.cate_id = cate_id;
    }
}
