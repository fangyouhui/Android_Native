package com.pai8.ke.entity;

import java.io.Serializable;

public class Address implements Serializable {
    private String distance;
    private double lat;
    private double lon;
    private String title;
    private String address;

    /**  本地用的数据 **/
    private boolean isLocalAddress;//是否是用户的收货地址

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isLocalAddress() {
        return isLocalAddress;
    }

    public void setLocalAddress(boolean localAddress) {
        isLocalAddress = localAddress;
    }
}
