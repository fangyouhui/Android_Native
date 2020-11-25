package com.pai8.ke.entity.resp;

import com.pai8.ke.entity.CouponInfoEntity;


public class CouponListResp {

    private String id;
    private String user_id;
    private String shop_id;
    private String coupon_id;
    private int status;
    private CouponInfoEntity coupon_info;
    private ShopList shop_info;

    private boolean isChecked = false;
    private boolean isGuize = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public CouponInfoEntity getCoupon_info() {
        return coupon_info;
    }

    public void setCoupon_info(CouponInfoEntity coupon_info) {
        this.coupon_info = coupon_info;
    }

    public ShopList getShop_info() {
        return shop_info;
    }

    public void setShop_info(ShopList shop_info) {
        this.shop_info = shop_info;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isGuize() {
        return isGuize;
    }

    public void setGuize(boolean guize) {
        isGuize = guize;
    }
}
