package com.pai8.ke.entity;

import com.google.gson.annotations.SerializedName;

public class OrderPrepayResult {

    private String appid;
    private String noncestr;
    @SerializedName("package")
    private String package2;
    private String partnerid;
    private String prepayid;
    private long timestamp;
    private String sign;

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppid() {
        return appid;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setPackage2(String package2) {
        this.package2 = package2;
    }

    public String getPackage2() {
        return package2;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign() {
        return sign;
    }





}
