package com.pai8.ke.entity;

import java.io.Serializable;

public class GroupBuyTypeResult implements Serializable {

    private int id;
    private String type_name;
    private String img_url;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getType_name() {
        return type_name;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getImg_url() {
        return img_url;
    }


}
