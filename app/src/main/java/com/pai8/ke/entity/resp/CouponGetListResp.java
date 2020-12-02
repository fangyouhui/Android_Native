package com.pai8.ke.entity.resp;

import java.io.Serializable;

public class CouponGetListResp implements Serializable {

    /**
     * id : 30
     * type : 1
     * shop_id : 22
     * value : 100/200
     * status : 1
     * add_time : 1606829346
     * start_time : 1606829346
     * end_time : 2147483647
     * num : 10
     * shop_name : 张氏奶茶店
     * coupon_name : 商品满减券
     * trig_price : 200
     * dis_price : 100
     */

    private String id;
    private int type; // 1为满减券 2为运费券
    private String shop_id;
    private String value;
    private int status;
    private long add_time;
    private long start_time;
    private long end_time;
    private String num;
    private String shop_name;
    private String coupon_name;
    private String trig_price;
    private String dis_price;

    private boolean isGuize = false;

    public boolean isGuize() {
        return isGuize;
    }

    public void setGuize(boolean guize) {
        isGuize = guize;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
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

    public long getAdd_time() {
        return add_time;
    }

    public void setAdd_time(long add_time) {
        this.add_time = add_time;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
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
